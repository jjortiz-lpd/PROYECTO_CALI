package torneojav.vista;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import torneojav.Contexto;
import torneojav.modelo.Equipo;
import torneojav.modelo.EstadoInscripcion;
import torneojav.modelo.Jugador;
import torneojav.modelo.Torneo;
import torneojav.servicio.JugadorService;

/** Vista del capitán: equipos (HU-04), inscripción (HU-06) y plantilla de jugadores (HU-05). */
public class VistaCapitan {

    private final Contexto ctx;
    private final BorderPane raiz = new BorderPane();

    private final TableView<Equipo> tblEquipos = new TableView<>();
    private final Button btnCambiarNombre = new Button("Cambiar nombre");

    private final Label lblEquipo = new Label();
    private final Label lblEstado = new Label();
    private final Label lblConteo = new Label();
    private final TableView<Jugador> tblJugadores = new TableView<>();
    private final Button btnAgregar = new Button("Agregar jugador");
    private final Button btnEditar = new Button("Editar");
    private final Button btnEliminar = new Button("Eliminar");

    private final Map<Integer, String> nombresTorneo = new HashMap<>();
    private Equipo equipoActual;

    public VistaCapitan(Contexto ctx) {
        this.ctx = ctx;
        configurarTablas();

        tblEquipos.getSelectionModel().selectedItemProperty()
                .addListener((obs, anterior, nuevo) -> seleccionar(nuevo));

        btnCambiarNombre.setOnAction(e -> cambiarNombre());
        btnAgregar.setOnAction(e -> dialogoJugador(null));
        btnEditar.setOnAction(e -> editarJugador());
        btnEliminar.setOnAction(e -> eliminarJugador());

        SplitPane division = new SplitPane(construirEquipos(), construirPlantilla());
        division.setDividerPositions(0.45);

        raiz.setCenter(division);
        raiz.setPadding(new Insets(15));

        seleccionar(null);
        cargarEquipos();
    }

    public Parent getRaiz() {
        return raiz;
    }

    // ---------- construcción de la interfaz ----------

    private VBox construirEquipos() {
        Button btnRegistrar = new Button("Registrar equipo");
        btnRegistrar.setOnAction(e -> registrarEquipo());

        VBox.setVgrow(tblEquipos, Priority.ALWAYS);
        VBox caja = new VBox(10, new Label("Mis equipos"), tblEquipos,
                new HBox(10, btnRegistrar, btnCambiarNombre));
        caja.setPadding(new Insets(10));
        return caja;
    }

    private VBox construirPlantilla() {
        lblEquipo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox.setVgrow(tblJugadores, Priority.ALWAYS);
        VBox caja = new VBox(10, lblEquipo, lblEstado, lblConteo, tblJugadores,
                new HBox(10, btnAgregar, btnEditar, btnEliminar));
        caja.setPadding(new Insets(10));
        return caja;
    }

    private void configurarTablas() {
        tblEquipos.getColumns().add(col("Id", Equipo::getId));
        tblEquipos.getColumns().add(col("Equipo", Equipo::getNombre));
        tblEquipos.getColumns().add(col("Torneo",
                e -> nombresTorneo.getOrDefault(e.getTorneoId(), "Torneo " + e.getTorneoId())));
        tblEquipos.getColumns().add(col("Estado", Equipo::getEstado));
        tblEquipos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tblEquipos.setPlaceholder(new Label("Aún no tiene equipos. Registre uno."));

        tblJugadores.getColumns().add(col("Id", Jugador::getId));
        tblJugadores.getColumns().add(col("#", Jugador::getNumeroCamiseta));
        tblJugadores.getColumns().add(col("Nombre", Jugador::getNombreCompleto));
        tblJugadores.getColumns().add(col("Documento", Jugador::getDocumento));
        tblJugadores.getColumns().add(col("Nacimiento", Jugador::getFechaNacimiento));
        tblJugadores.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tblJugadores.setPlaceholder(new Label("Este equipo todavía no tiene jugadores."));
    }

    // ---------- carga de datos ----------

    private void cargarEquipos() {
        int idSeleccionado = equipoActual == null ? -1 : equipoActual.getId();
        try {
            nombresTorneo.clear();
            for (Torneo t : ctx.consultaService.listarTorneos()) {
                nombresTorneo.put(t.getId(), t.getNombre());
            }
            List<Equipo> lista = ctx.equipoService.consultarMisEquipos();
            tblEquipos.setItems(FXCollections.observableArrayList(lista));

            boolean encontrado = false;
            for (Equipo e : lista) {
                if (e.getId() == idSeleccionado) {
                    tblEquipos.getSelectionModel().select(e); // dispara seleccionar(e)
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado && !lista.isEmpty()) {
                tblEquipos.getSelectionModel().selectFirst();
            }
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void seleccionar(Equipo equipo) {
        equipoActual = equipo;
        if (equipo == null) {
            lblEquipo.setText("Seleccione un equipo de la lista");
            lblEstado.setText("");
            lblConteo.setText("");
            tblJugadores.getItems().clear();
            habilitar(false);
            return;
        }
        lblEquipo.setText("Plantilla de " + equipo.getNombre());
        try {
            EstadoInscripcion estado = ctx.inscripcionService.consultarEstado(equipo.getId());
            lblEstado.setText("Estado de la inscripción: " + estado);
            cargarJugadores();
            habilitar(true);
        } catch (RuntimeException ex) {
            mostrarError(ex);
            habilitar(false);
        }
    }

    private void cargarJugadores() {
        if (equipoActual == null) {
            return;
        }
        try {
            List<Jugador> lista = ctx.jugadorService.listarPorEquipo(equipoActual.getId());
            tblJugadores.setItems(FXCollections.observableArrayList(lista));
            lblConteo.setText("Jugadores: " + lista.size() + "   (el equipo debe tener entre "
                    + JugadorService.MIN_JUGADORES + " y " + JugadorService.MAX_JUGADORES + ")");
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void habilitar(boolean habilitado) {
        btnCambiarNombre.setDisable(!habilitado);
        btnAgregar.setDisable(!habilitado);
        btnEditar.setDisable(!habilitado);
        btnEliminar.setDisable(!habilitado);
    }

    // ---------- equipos ----------

    private void registrarEquipo() {
        List<Torneo> torneos;
        try {
            torneos = ctx.consultaService.listarTorneos();
        } catch (RuntimeException ex) {
            mostrarError(ex);
            return;
        }
        if (torneos.isEmpty()) {
            mostrarAviso("No hay torneos disponibles para inscribir un equipo.");
            return;
        }

        TextField txtNombre = new TextField();
        ComboBox<Torneo> cmbTorneo = new ComboBox<>(FXCollections.observableArrayList(torneos));
        cmbTorneo.setConverter(new StringConverter<Torneo>() {
            @Override
            public String toString(Torneo t) {
                return t == null ? "" : t.getNombre();
            }

            @Override
            public Torneo fromString(String s) {
                return null;
            }
        });
        cmbTorneo.getSelectionModel().selectFirst();

        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(10);
        formulario.setPadding(new Insets(15));
        formulario.add(new Label("Nombre del equipo:"), 0, 0);
        formulario.add(txtNombre, 1, 0);
        formulario.add(new Label("Torneo:"), 0, 1);
        formulario.add(cmbTorneo, 1, 1);

        Dialog<ButtonType> dialogo = new Dialog<>();
        dialogo.setTitle("Registrar equipo");
        dialogo.getDialogPane().setContent(formulario);
        dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Button ok = (Button) dialogo.getDialogPane().lookupButton(ButtonType.OK);
        ok.addEventFilter(ActionEvent.ACTION, ev -> {
            try {
                ctx.equipoService.registrar(txtNombre.getText(), cmbTorneo.getValue().getId());
            } catch (RuntimeException ex) {
                mostrarError(ex);
                ev.consume(); // el diálogo no se cierra
            }
        });

        if (dialogo.showAndWait().filter(b -> b == ButtonType.OK).isPresent()) {
            cargarEquipos();
        }
    }

    private void cambiarNombre() {
        if (equipoActual == null) {
            return;
        }
        Equipo equipo = equipoActual;
        TextInputDialog dialogo = new TextInputDialog(equipo.getNombre());
        dialogo.setTitle("Cambiar nombre");
        dialogo.setHeaderText("Equipo: " + equipo.getNombre());
        dialogo.setContentText("Nuevo nombre:");

        Button ok = (Button) dialogo.getDialogPane().lookupButton(ButtonType.OK);
        ok.addEventFilter(ActionEvent.ACTION, ev -> {
            try {
                ctx.equipoService.modificarNombre(equipo.getId(), dialogo.getEditor().getText());
            } catch (RuntimeException ex) {
                mostrarError(ex);
                ev.consume();
            }
        });

        if (dialogo.showAndWait().isPresent()) {
            cargarEquipos();
        }
    }

    // ---------- jugadores ----------

    /** Con existente == null agrega un jugador al equipo actual; si no, edita ese jugador. */
    private void dialogoJugador(Jugador existente) {
        if (equipoActual == null) {
            return;
        }
        Equipo equipo = equipoActual;

        TextField txtNombre = new TextField();
        TextField txtDocumento = new TextField();
        TextField txtNacimiento = new TextField();
        txtNacimiento.setPromptText("AAAA-MM-DD");
        TextField txtCamiseta = new TextField();
        if (existente != null) {
            txtNombre.setText(existente.getNombreCompleto());
            txtDocumento.setText(existente.getDocumento());
            txtNacimiento.setText(existente.getFechaNacimiento().toString());
            txtCamiseta.setText(String.valueOf(existente.getNumeroCamiseta()));
        }

        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(10);
        formulario.setPadding(new Insets(15));
        formulario.add(new Label("Nombre completo:"), 0, 0);
        formulario.add(txtNombre, 1, 0);
        formulario.add(new Label("Documento:"), 0, 1);
        formulario.add(txtDocumento, 1, 1);
        formulario.add(new Label("Fecha de nacimiento:"), 0, 2);
        formulario.add(txtNacimiento, 1, 2);
        formulario.add(new Label("Número de camiseta:"), 0, 3);
        formulario.add(txtCamiseta, 1, 3);

        Dialog<ButtonType> dialogo = new Dialog<>();
        dialogo.setTitle(existente == null ? "Agregar jugador a " + equipo.getNombre() : "Editar jugador");
        dialogo.getDialogPane().setContent(formulario);
        dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Button ok = (Button) dialogo.getDialogPane().lookupButton(ButtonType.OK);
        ok.addEventFilter(ActionEvent.ACTION, ev -> {
            LocalDate nacimiento;
            try {
                nacimiento = LocalDate.parse(txtNacimiento.getText().trim());
            } catch (DateTimeParseException ex) {
                mostrarAviso("La fecha debe tener el formato AAAA-MM-DD, por ejemplo 2004-05-21.");
                ev.consume();
                return;
            }
            int camiseta;
            try {
                camiseta = Integer.parseInt(txtCamiseta.getText().trim());
            } catch (NumberFormatException ex) {
                mostrarAviso("El número de camiseta debe ser un número entero.");
                ev.consume();
                return;
            }
            try {
                if (existente == null) {
                    ctx.jugadorService.agregar(equipo.getId(), txtNombre.getText(), txtDocumento.getText(),
                            nacimiento, camiseta);
                } else {
                    ctx.jugadorService.editar(existente.getId(), txtNombre.getText(), txtDocumento.getText(),
                            nacimiento, camiseta);
                }
            } catch (RuntimeException ex) {
                mostrarError(ex);
                ev.consume();
            }
        });

        if (dialogo.showAndWait().filter(b -> b == ButtonType.OK).isPresent()) {
            cargarJugadores();
        }
    }

    private void editarJugador() {
        Jugador j = tblJugadores.getSelectionModel().getSelectedItem();
        if (j == null) {
            mostrarAviso("Seleccione un jugador de la tabla.");
            return;
        }
        dialogoJugador(j);
    }

    private void eliminarJugador() {
        Jugador j = tblJugadores.getSelectionModel().getSelectedItem();
        if (j == null) {
            mostrarAviso("Seleccione un jugador de la tabla.");
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Seguro que desea eliminarlo?", ButtonType.YES, ButtonType.NO);
        confirmacion.setHeaderText("Eliminar a " + j.getNombreCompleto());
        if (confirmacion.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
            return;
        }
        try {
            ctx.jugadorService.eliminar(j.getId());
            cargarJugadores();
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    // ---------- utilidades ----------

    private static <T, V> TableColumn<T, V> col(String titulo, Function<T, V> valor) {
        TableColumn<T, V> c = new TableColumn<>(titulo);
        c.setCellValueFactory(fila -> new ReadOnlyObjectWrapper<>(valor.apply(fila.getValue())));
        return c;
    }

    private void mostrarError(RuntimeException ex) {
        new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
    }

    private void mostrarAviso(String mensaje) {
        new Alert(Alert.AlertType.WARNING, mensaje).showAndWait();
    }
}