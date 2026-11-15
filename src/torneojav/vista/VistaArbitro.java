package torneojav.vista;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import torneojav.Contexto;
import torneojav.modelo.EventoPartido;
import torneojav.modelo.Jugador;
import torneojav.modelo.Partido;
import torneojav.modelo.TipoEvento;
import torneojav.modelo.Torneo;

/** Vista del árbitro (HU-08): registrar goles, tarjetas y cambios, y cerrar el acta. */
public class VistaArbitro {

    private final Contexto ctx;
    private final BorderPane raiz = new BorderPane();

    private final ComboBox<Torneo> cmbTorneo = new ComboBox<>();
    private final TableView<Partido> tblPartidos = new TableView<>();

    private final Label lblPartido = new Label();
    private final Label lblMarcador = new Label();
    private final ComboBox<TipoEvento> cmbTipo = new ComboBox<>();
    private final TextField txtMinuto = new TextField();
    private final ComboBox<Jugador> cmbJugador = new ComboBox<>();
    private final Button btnRegistrar = new Button("Registrar evento");
    private final Button btnCerrar = new Button("Cerrar acta");
    private final TableView<EventoPartido> tblEventos = new TableView<>();

    private Partido partidoActual;
    private final Map<Integer, Jugador> jugadoresPorId = new HashMap<>();

    public VistaArbitro(Contexto ctx) {
        this.ctx = ctx;

        configurarPartidos();
        configurarEventos();

        cmbTipo.setItems(FXCollections.observableArrayList(TipoEvento.values()));
        cmbTipo.setConverter(new StringConverter<TipoEvento>() {
            @Override
            public String toString(TipoEvento t) {
                return etiquetaTipo(t);
            }

            @Override
            public TipoEvento fromString(String s) {
                return null;
            }
        });
        cmbTipo.getSelectionModel().selectFirst();

        cmbJugador.setConverter(new StringConverter<Jugador>() {
            @Override
            public String toString(Jugador j) {
                if (j == null) {
                    return "";
                }
                return nombreEquipo(j.getEquipoId()) + " - #" + j.getNumeroCamiseta() + " " + j.getNombreCompleto();
            }

            @Override
            public Jugador fromString(String s) {
                return null;
            }
        });

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
        cmbTorneo.valueProperty().addListener((obs, anterior, nuevo) -> {
            if (nuevo != null) {
                cargarPartidos();
            }
        });

        tblPartidos.getSelectionModel().selectedItemProperty()
                .addListener((obs, anterior, nuevo) -> seleccionar(nuevo));

        btnRegistrar.setOnAction(e -> registrarEvento());
        btnCerrar.setOnAction(e -> cerrarActa());

        Button btnActualizar = new Button("Actualizar");
        btnActualizar.setOnAction(e -> cargarPartidos());
        HBox barra = new HBox(10, new Label("Torneo:"), cmbTorneo, btnActualizar);
        barra.setPadding(new Insets(0, 0, 10, 0));

        SplitPane division = new SplitPane(tblPartidos, construirDetalle());
        division.setDividerPositions(0.45);

        raiz.setTop(barra);
        raiz.setCenter(division);
        raiz.setPadding(new Insets(15));

        seleccionar(null);
        cargarTorneos();
    }

    public Parent getRaiz() {
        return raiz;
    }

    // ---------- construcción de la interfaz ----------

    private VBox construirDetalle() {
        lblPartido.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        lblMarcador.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        txtMinuto.setPromptText("0 a 130");
        cmbTipo.setMaxWidth(Double.MAX_VALUE);
        cmbJugador.setMaxWidth(Double.MAX_VALUE);

        GridPane formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(8);
        ColumnConstraints etiquetas = new ColumnConstraints();
        ColumnConstraints campos = new ColumnConstraints();
        campos.setHgrow(Priority.ALWAYS);
        formulario.getColumnConstraints().addAll(etiquetas, campos);
        formulario.add(new Label("Tipo:"), 0, 0);
        formulario.add(cmbTipo, 1, 0);
        formulario.add(new Label("Minuto:"), 0, 1);
        formulario.add(txtMinuto, 1, 1);
        formulario.add(new Label("Jugador:"), 0, 2);
        formulario.add(cmbJugador, 1, 2);

        HBox botones = new HBox(10, btnRegistrar, btnCerrar);
        VBox.setVgrow(tblEventos, Priority.ALWAYS);

        VBox detalle = new VBox(10, lblPartido, lblMarcador, formulario, botones,
                new Label("Eventos del partido"), tblEventos);
        detalle.setPadding(new Insets(10));
        return detalle;
    }

    private void configurarPartidos() {
        tblPartidos.getColumns().add(col("Ronda", Partido::getRonda));
        tblPartidos.getColumns().add(col("Local", p -> p.getLocal().getNombre()));
        tblPartidos.getColumns().add(col("Marcador", p -> p.getGolesLocal() + " - " + p.getGolesVisitante()));
        tblPartidos.getColumns().add(col("Visitante", p -> p.getVisitante().getNombre()));
        tblPartidos.getColumns().add(col("Estado", Partido::getEstado));
        tblPartidos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tblPartidos.setPlaceholder(new Label("No hay partidos pendientes. El administrador debe generar el calendario."));
    }

    private void configurarEventos() {
        tblEventos.getColumns().add(col("Min", EventoPartido::getMinuto));
        tblEventos.getColumns().add(col("Tipo", e -> etiquetaTipo(e.getTipo())));
        tblEventos.getColumns().add(col("Jugador", e -> {
            Jugador j = jugadoresPorId.get(e.getJugadorId());
            return j == null ? "#" + e.getJugadorId() : "#" + j.getNumeroCamiseta() + " " + j.getNombreCompleto();
        }));
        tblEventos.getColumns().add(col("Equipo", e -> nombreEquipo(e.getEquipoId())));
        tblEventos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tblEventos.setPlaceholder(new Label("Aún no hay eventos registrados."));
    }

    // ---------- carga de datos ----------

    private void cargarTorneos() {
        try {
            List<Torneo> lista = ctx.consultaService.listarTorneos();
            cmbTorneo.setItems(FXCollections.observableArrayList(lista));
            if (!lista.isEmpty()) {
                cmbTorneo.getSelectionModel().selectFirst(); // dispara cargarPartidos()
            }
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    /** Recarga la lista y, si el partido seleccionado sigue pendiente, lo vuelve a seleccionar. */
    private void cargarPartidos() {
        Torneo torneo = cmbTorneo.getValue();
        if (torneo == null) {
            return;
        }
        int idSeleccionado = partidoActual == null ? -1 : partidoActual.getId();
        try {
            List<Partido> lista = ctx.resultadoService.partidosProgramados(torneo.getId());
            tblPartidos.setItems(FXCollections.observableArrayList(lista));
            for (Partido p : lista) {
                if (p.getId() == idSeleccionado) {
                    tblPartidos.getSelectionModel().select(p); // dispara seleccionar(p)
                    break;
                }
            }
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void seleccionar(Partido partido) {
        partidoActual = partido;
        if (partido == null) {
            lblPartido.setText("Seleccione un partido de la lista");
            lblMarcador.setText("");
            cmbJugador.getItems().clear();
            tblEventos.getItems().clear();
            jugadoresPorId.clear();
            habilitarPanel(false);
            return;
        }
        try {
            List<Jugador> jugadores = ctx.resultadoService.jugadoresDelPartido(partido.getId());
            jugadoresPorId.clear();
            for (Jugador j : jugadores) {
                jugadoresPorId.put(j.getId(), j);
            }
            cmbJugador.setItems(FXCollections.observableArrayList(jugadores));

            lblPartido.setText(partido.getLocal().getNombre() + " vs " + partido.getVisitante().getNombre());
            Partido actualizado = ctx.resultadoService.calcularResultado(partido.getId());
            lblMarcador.setText(actualizado.getGolesLocal() + " - " + actualizado.getGolesVisitante());

            List<EventoPartido> eventos = new ArrayList<>(ctx.resultadoService.eventosDelPartido(partido.getId()));
            eventos.sort(Comparator.comparingInt(EventoPartido::getMinuto));
            tblEventos.setItems(FXCollections.observableArrayList(eventos));
            habilitarPanel(true);
        } catch (RuntimeException ex) {
            mostrarError(ex);
            habilitarPanel(false);
        }
    }

    // ---------- acciones ----------

    private void registrarEvento() {
        if (partidoActual == null) {
            return;
        }
        Jugador jugador = cmbJugador.getValue();
        if (jugador == null) {
            mostrarAviso("Seleccione un jugador.");
            return;
        }
        int minuto;
        try {
            minuto = Integer.parseInt(txtMinuto.getText().trim());
        } catch (NumberFormatException ex) {
            mostrarAviso("El minuto debe ser un número entero entre 0 y 130.");
            return;
        }
        try {
            ctx.resultadoService.registrarEvento(partidoActual.getId(), cmbTipo.getValue(), minuto, jugador.getId());
            txtMinuto.clear();
            cargarPartidos(); // refresca la lista y el detalle del partido
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void cerrarActa() {
        if (partidoActual == null) {
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                "Después de cerrar el acta no se puede modificar. ¿Continuar?", ButtonType.YES, ButtonType.NO);
        confirmacion.setHeaderText("Cerrar acta: " + lblPartido.getText() + " (" + lblMarcador.getText() + ")");
        if (confirmacion.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
            return;
        }
        try {
            Partido cerrado = ctx.resultadoService.cerrarActa(partidoActual.getId());
            new Alert(Alert.AlertType.INFORMATION, "Marcador final: " + cerrado.getGolesLocal() + " - "
                    + cerrado.getGolesVisitante() + "\nLa tabla de posiciones fue actualizada.").showAndWait();
            cargarPartidos(); // el partido cerrado desaparece de la lista
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    // ---------- utilidades ----------

    private void habilitarPanel(boolean habilitado) {
        cmbTipo.setDisable(!habilitado);
        txtMinuto.setDisable(!habilitado);
        cmbJugador.setDisable(!habilitado);
        btnRegistrar.setDisable(!habilitado);
        btnCerrar.setDisable(!habilitado);
    }

    private String nombreEquipo(int equipoId) {
        if (partidoActual == null) {
            return "";
        }
        return partidoActual.getLocal().getId() == equipoId
                ? partidoActual.getLocal().getNombre()
                : partidoActual.getVisitante().getNombre();
    }

    private static String etiquetaTipo(TipoEvento tipo) {
        return tipo == null ? "" : tipo.name().replace('_', ' ');
    }

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