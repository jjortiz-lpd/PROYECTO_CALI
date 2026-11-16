package torneojav.vista;

import java.util.List;
import java.util.function.Function;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import torneojav.Contexto;
import torneojav.modelo.Equipo;
import torneojav.modelo.FormatoTorneo;
import torneojav.modelo.Partido;
import torneojav.modelo.Torneo;

/** Vista del administrador: inscripciones (HU-06), calendario (HU-07), formato (HU-03) y torneos (HU-02). */
public class VistaAdmin {

    private final Contexto ctx;
    private final BorderPane raiz = new BorderPane();

    // torneo de trabajo (lo usan las pestañas Inscripciones y Calendario)
    private final ComboBox<Torneo> cmbTorneo = new ComboBox<>();
    private final Label lblInfoTorneo = new Label();

    // Inscripciones
    private final TableView<Equipo> tblPendientes = new TableView<>();
    private final TableView<Equipo> tblAprobados = new TableView<>();

    // Calendario
    private final TableView<Partido> tblCalendario = new TableView<>();
    private final Label lblCalendario = new Label();
    private final Button btnGenerar = new Button("Generar calendario");
    private final Button btnSiguienteRonda = new Button("Generar siguiente ronda");

    // Torneos
    private final TableView<Torneo> tblTorneos = new TableView<>();

    public VistaAdmin(Contexto ctx) {
        this.ctx = ctx;
        configurarTablas();

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
        cmbTorneo.valueProperty().addListener((obs, anterior, nuevo) -> alCambiarTorneo(nuevo));

        HBox barra = new HBox(10, new Label("Torneo de trabajo:"), cmbTorneo, lblInfoTorneo);
        barra.setAlignment(Pos.CENTER_LEFT);
        barra.setPadding(new Insets(0, 0, 10, 0));

        TabPane pestanas = new TabPane(
                new Tab("Inscripciones", construirInscripciones()),
                new Tab("Calendario", construirCalendario()),
                new Tab("Torneos", construirTorneos()));
        pestanas.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        raiz.setTop(barra);
        raiz.setCenter(pestanas);
        raiz.setPadding(new Insets(15));

        recargarTorneos();
    }

    public Parent getRaiz() {
        return raiz;
    }

    // ---------- construcción de las pestañas ----------

    private VBox construirInscripciones() {
        Button btnAprobar = new Button("Aprobar");
        btnAprobar.setOnAction(e -> aprobar());
        Button btnRechazar = new Button("Rechazar");
        btnRechazar.setOnAction(e -> rechazar());

        VBox.setVgrow(tblPendientes, Priority.ALWAYS);
        VBox.setVgrow(tblAprobados, Priority.ALWAYS);
        VBox caja = new VBox(10,
                new Label("Pendientes de aprobación"), tblPendientes,
                new HBox(10, btnAprobar, btnRechazar),
                new Label("Equipos aprobados"), tblAprobados);
        caja.setPadding(new Insets(10));
        return caja;
    }

    private VBox construirCalendario() {
        btnGenerar.setOnAction(e -> generarCalendario());
        btnSiguienteRonda.setOnAction(e -> generarSiguienteRonda());
        Button btnActualizar = new Button("Actualizar");
        btnActualizar.setOnAction(e -> cargarCalendario());

        VBox.setVgrow(tblCalendario, Priority.ALWAYS);
        VBox caja = new VBox(10, new HBox(10, btnGenerar, btnSiguienteRonda, btnActualizar),
                lblCalendario, tblCalendario);
        caja.setPadding(new Insets(10));
        return caja;
    }

    private VBox construirTorneos() {
        Button btnCrear = new Button("Crear torneo");
        btnCrear.setOnAction(e -> dialogoTorneo(null));
        Button btnModificar = new Button("Modificar nombre y fecha");
        btnModificar.setOnAction(e -> modificarTorneo());
        Button btnFormato = new Button("Cambiar formato");
        btnFormato.setOnAction(e -> cambiarFormato());
        Button btnEliminar = new Button("Eliminar");
        btnEliminar.setOnAction(e -> eliminarTorneo());

        VBox.setVgrow(tblTorneos, Priority.ALWAYS);
        VBox caja = new VBox(10, new HBox(10, btnCrear, btnModificar, btnFormato, btnEliminar), tblTorneos);
        caja.setPadding(new Insets(10));
        return caja;
    }

    private void configurarTablas() {
        for (TableView<Equipo> tabla : List.of(tblPendientes, tblAprobados)) {
            tabla.getColumns().add(col("Id", Equipo::getId));
            tabla.getColumns().add(col("Equipo", Equipo::getNombre));
            tabla.getColumns().add(col("Estado", Equipo::getEstado));
            tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        }
        tblPendientes.setPlaceholder(new Label("No hay inscripciones pendientes."));
        tblAprobados.setPlaceholder(new Label("No hay equipos aprobados."));

        tblCalendario.getColumns().add(col("Id", Partido::getId));
        tblCalendario.getColumns().add(col("Ronda", Partido::getRonda));
        tblCalendario.getColumns().add(col("Fecha", Partido::getFecha));
        tblCalendario.getColumns().add(col("Local", p -> p.getLocal().getNombre()));
        tblCalendario.getColumns().add(col("Marcador", p -> p.getGolesLocal() + " - " + p.getGolesVisitante()));
        tblCalendario.getColumns().add(col("Visitante", p -> p.getVisitante().getNombre()));
        tblCalendario.getColumns().add(col("Estado", Partido::getEstado));
        tblCalendario.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tblCalendario.setPlaceholder(new Label("Sin partidos."));

        tblTorneos.getColumns().add(col("Id", Torneo::getId));
        tblTorneos.getColumns().add(col("Nombre", Torneo::getNombre));
        tblTorneos.getColumns().add(col("Inicio", Torneo::getFechaInicio));
        tblTorneos.getColumns().add(col("Formato", VistaAdmin::textoFormato));
        tblTorneos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tblTorneos.setPlaceholder(new Label("No hay torneos. Cree uno con el botón de arriba."));
    }

    // ---------- carga de datos ----------

    /** Recarga la lista de torneos y conserva el torneo de trabajo si todavía existe. */
    private void recargarTorneos() {
        int idSeleccionado = cmbTorneo.getValue() == null ? -1 : cmbTorneo.getValue().getId();
        try {
            List<Torneo> lista = ctx.torneoService.listar();
            cmbTorneo.getSelectionModel().clearSelection();
            tblTorneos.setItems(FXCollections.observableArrayList(lista));
            cmbTorneo.setItems(FXCollections.observableArrayList(lista));

            Torneo elegido = null;
            for (Torneo t : lista) {
                if (t.getId() == idSeleccionado) {
                    elegido = t;
                }
            }
            if (elegido == null && !lista.isEmpty()) {
                elegido = lista.get(0);
            }
            if (elegido != null) {
                cmbTorneo.getSelectionModel().select(elegido); // dispara alCambiarTorneo
            }
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void alCambiarTorneo(Torneo t) {
        if (t == null) {
            lblInfoTorneo.setText("");
            lblCalendario.setText("");
            tblPendientes.getItems().clear();
            tblAprobados.getItems().clear();
            tblCalendario.getItems().clear();
            btnGenerar.setDisable(true);
            btnSiguienteRonda.setDisable(true);
            return;
        }
        lblInfoTorneo.setText("Formato: " + textoFormato(t) + "   |   Inicio: " + t.getFechaInicio());
        cargarInscripciones();
        cargarCalendario();
    }

    private void cargarInscripciones() {
        Torneo t = cmbTorneo.getValue();
        if (t == null) {
            return;
        }
        try {
            tblPendientes.setItems(FXCollections.observableArrayList(ctx.inscripcionService.listarPendientes(t.getId())));
            tblAprobados.setItems(FXCollections.observableArrayList(ctx.inscripcionService.equiposAprobados(t.getId())));
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void cargarCalendario() {
        Torneo t = cmbTorneo.getValue();
        if (t == null) {
            return;
        }
        btnGenerar.setDisable(false);
        btnSiguienteRonda.setDisable(t.getFormato() != FormatoTorneo.ELIMINACION_DIRECTA);
        try {
            List<Partido> lista = ctx.calendarioService.consultarCalendario(t.getId());
            tblCalendario.setItems(FXCollections.observableArrayList(lista));
            lblCalendario.setText(lista.isEmpty()
                    ? "El calendario aún no se ha generado."
                    : "Partidos en el calendario: " + lista.size());
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    // ---------- inscripciones ----------

    private void aprobar() {
        Equipo equipo = tblPendientes.getSelectionModel().getSelectedItem();
        if (equipo == null) {
            mostrarAviso("Seleccione un equipo pendiente.");
            return;
        }
        try {
            ctx.inscripcionService.aprobar(equipo.getId());
            cargarInscripciones();
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void rechazar() {
        Equipo equipo = tblPendientes.getSelectionModel().getSelectedItem();
        if (equipo == null) {
            mostrarAviso("Seleccione un equipo pendiente.");
            return;
        }
        if (!confirmar("Rechazar a " + equipo.getNombre(), "La decisión no se puede deshacer. ¿Continuar?")) {
            return;
        }
        try {
            ctx.inscripcionService.rechazar(equipo.getId());
            cargarInscripciones();
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    // ---------- calendario ----------

    private void generarCalendario() {
        Torneo t = torneoDeTrabajo();
        if (t == null) {
            return;
        }
        try {
            List<Partido> generados = ctx.calendarioService.generarCalendario(t.getId());
            cargarCalendario();
            mostrarInfo("Calendario generado con " + generados.size() + " partidos.");
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void generarSiguienteRonda() {
        Torneo t = torneoDeTrabajo();
        if (t == null) {
            return;
        }
        try {
            List<Partido> generados = ctx.calendarioService.generarSiguienteRonda(t.getId());
            cargarCalendario();
            mostrarInfo("Siguiente ronda generada con " + generados.size() + " partidos.");
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    // ---------- torneos ----------

    /** Con existente == null crea un torneo; si no, modifica su nombre y fecha. */
    private void dialogoTorneo(Torneo existente) {
        TextField txtNombre = new TextField();
        DatePicker dpFecha = new DatePicker();
        dpFecha.setEditable(false); // la fecha se escoge desde el calendario desplegable
        ComboBox<FormatoTorneo> cmbFormato = new ComboBox<>(
                FXCollections.observableArrayList(ctx.formatoService.opcionesDisponibles()));
        if (existente != null) {
            txtNombre.setText(existente.getNombre());
            dpFecha.setValue(existente.getFechaInicio());
        }

        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(10);
        formulario.setPadding(new Insets(15));
        formulario.add(new Label("Nombre:"), 0, 0);
        formulario.add(txtNombre, 1, 0);
        formulario.add(new Label("Fecha de inicio:"), 0, 1);
        formulario.add(dpFecha, 1, 1);
        if (existente == null) { // el formato de un torneo existente se cambia con su propio botón
            formulario.add(new Label("Formato:"), 0, 2);
            formulario.add(cmbFormato, 1, 2);
        }

        Dialog<ButtonType> dialogo = new Dialog<>();
        dialogo.setTitle(existente == null ? "Crear torneo" : "Modificar torneo");
        dialogo.getDialogPane().setContent(formulario);
        dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Si el servicio rechaza los datos, se muestra el error y el diálogo no se cierra.
        Button ok = (Button) dialogo.getDialogPane().lookupButton(ButtonType.OK);
        ok.addEventFilter(ActionEvent.ACTION, ev -> {
            try {
                if (existente == null) {
                    ctx.torneoService.crear(txtNombre.getText(), dpFecha.getValue(), cmbFormato.getValue());
                } else {
                    ctx.torneoService.actualizar(existente.getId(), txtNombre.getText(), dpFecha.getValue());
                }
            } catch (RuntimeException ex) {
                mostrarError(ex);
                ev.consume();
            }
        });

        if (dialogo.showAndWait().filter(b -> b == ButtonType.OK).isPresent()) {
            recargarTorneos();
        }
    }

    private void modificarTorneo() {
        Torneo t = tblTorneos.getSelectionModel().getSelectedItem();
        if (t == null) {
            mostrarAviso("Seleccione un torneo de la tabla.");
            return;
        }
        dialogoTorneo(t);
    }

    private void cambiarFormato() {
        Torneo t = tblTorneos.getSelectionModel().getSelectedItem();
        if (t == null) {
            mostrarAviso("Seleccione un torneo de la tabla.");
            return;
        }
        ChoiceDialog<FormatoTorneo> dialogo = new ChoiceDialog<FormatoTorneo>(
                t.getFormato(), List.of(ctx.formatoService.opcionesDisponibles()));
        dialogo.setTitle("Cambiar formato");
        dialogo.setHeaderText("Formato de " + t.getNombre());
        dialogo.setContentText("Formato:");
        dialogo.showAndWait().ifPresent(formato -> {
            try {
                ctx.formatoService.asignarFormato(t.getId(), formato);
                recargarTorneos();
            } catch (RuntimeException ex) {
                mostrarError(ex);
            }
        });
    }

    private void eliminarTorneo() {
        Torneo t = tblTorneos.getSelectionModel().getSelectedItem();
        if (t == null) {
            mostrarAviso("Seleccione un torneo de la tabla.");
            return;
        }
        if (!confirmar("Eliminar " + t.getNombre(), "Esta acción no se puede deshacer. ¿Continuar?")) {
            return;
        }
        try {
            ctx.torneoService.eliminar(t.getId());
            recargarTorneos();
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    // ---------- utilidades ----------

    private Torneo torneoDeTrabajo() {
        Torneo t = cmbTorneo.getValue();
        if (t == null) {
            mostrarAviso("Seleccione un torneo de trabajo.");
        }
        return t;
    }

    private static String textoFormato(Torneo t) {
        return t.getFormato() == null ? "Sin definir" : t.getFormato().getNombreVisible();
    }

    private static <T, V> TableColumn<T, V> col(String titulo, Function<T, V> valor) {
        TableColumn<T, V> c = new TableColumn<>(titulo);
        c.setCellValueFactory(fila -> new ReadOnlyObjectWrapper<>(valor.apply(fila.getValue())));
        return c;
    }

    private boolean confirmar(String encabezado, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, mensaje, ButtonType.YES, ButtonType.NO);
        alerta.setHeaderText(encabezado);
        return alerta.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }

    private void mostrarError(RuntimeException ex) {
        new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
    }

    private void mostrarAviso(String mensaje) {
        new Alert(Alert.AlertType.WARNING, mensaje).showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        new Alert(Alert.AlertType.INFORMATION, mensaje).showAndWait();
    }
}