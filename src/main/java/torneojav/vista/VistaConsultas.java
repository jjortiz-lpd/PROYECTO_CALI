package torneojav.vista;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import torneojav.Contexto;
import torneojav.modelo.EstadisticasTorneo;
import torneojav.modelo.Partido;
import torneojav.modelo.PosicionTabla;
import torneojav.modelo.Torneo;

/** Consultas de HU-10: posiciones, partidos, resultados y estadísticas. Sirve para los tres roles. */
public class VistaConsultas {

    private final Contexto ctx;
    private final BorderPane raiz = new BorderPane();
    private final ComboBox<Torneo> cmbTorneo = new ComboBox<>();

    private final TableView<PosicionTabla> tblPosiciones = new TableView<>();
    private final TableView<Partido> tblProgramados = new TableView<>();
    private final TableView<Partido> tblResultados = new TableView<>();
    private final TableView<Map.Entry<String, int[]>> tblTarjetas = new TableView<>();

    private final Label lblResumen = new Label();
    private final Label lblGoleador = new Label();
    private final Label lblValla = new Label();
    private final Label lblTarjetas = new Label();

    public VistaConsultas(Contexto ctx) {
        this.ctx = ctx;

        configurarPosiciones();
        configurarProgramados();
        configurarResultados();
        configurarTarjetas();

        TabPane pestanas = new TabPane(
                new Tab("Posiciones", tblPosiciones),
                new Tab("Programados", tblProgramados),
                new Tab("Resultados", tblResultados),
                new Tab("Estadísticas", construirEstadisticas()));
        pestanas.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

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
                actualizar();
            }
        });

        Button btnActualizar = new Button("Actualizar");
        btnActualizar.setOnAction(e -> actualizar());

        HBox barra = new HBox(10, new Label("Torneo:"), cmbTorneo, btnActualizar);
        barra.setPadding(new Insets(0, 0, 10, 0));

        raiz.setTop(barra);
        raiz.setCenter(pestanas);
        raiz.setPadding(new Insets(15));

        cargarTorneos();
    }

    public Parent getRaiz() {
        return raiz;
    }

    // ---------- carga de datos ----------

    private void cargarTorneos() {
        try {
            List<Torneo> lista = ctx.consultaService.listarTorneos();
            cmbTorneo.setItems(FXCollections.observableArrayList(lista));
            if (!lista.isEmpty()) {
                cmbTorneo.getSelectionModel().selectFirst(); // dispara actualizar()
            }
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    public void actualizar() {
        Torneo t = cmbTorneo.getValue();
        if (t == null) {
            return;
        }
        try {
            int id = t.getId();
            tblPosiciones.setItems(FXCollections.observableArrayList(ctx.consultaService.tablaPosiciones(id)));
            tblProgramados.setItems(FXCollections.observableArrayList(ctx.consultaService.partidosProgramados(id)));
            tblResultados.setItems(FXCollections.observableArrayList(ctx.consultaService.partidosFinalizados(id)));
            mostrarEstadisticas(ctx.consultaService.estadisticas(id));
        } catch (RuntimeException ex) {
            mostrarError(ex);
        }
    }

    private void mostrarEstadisticas(EstadisticasTorneo e) {
        lblResumen.setText(String.format("Partidos jugados: %d   |   Programados: %d   |   Goles: %d   |   Promedio: %.2f",
                e.getPartidosJugados(), e.getPartidosProgramados(), e.getTotalGoles(), e.getPromedioGoles()));

        if (e.getMasGoleadores().isEmpty()) {
            lblGoleador.setText("Equipo más goleador: aún sin datos");
            lblValla.setText("Valla menos vencida: aún sin datos");
        } else {
            lblGoleador.setText("Equipo más goleador: " + nombres(e.getMasGoleadores())
                    + " (" + e.getMasGoleadores().get(0).getGolesFavor() + " goles)");
            lblValla.setText("Valla menos vencida: " + nombres(e.getVallasMenosVencidas())
                    + " (" + e.getVallasMenosVencidas().get(0).getGolesContra() + " goles en contra)");
        }

        lblTarjetas.setText("Tarjetas amarillas: " + e.getTarjetasAmarillas()
                + "   |   Tarjetas rojas: " + e.getTarjetasRojas());
        tblTarjetas.setItems(FXCollections.observableArrayList(e.getTarjetasPorEquipo().entrySet()));
    }

    private String nombres(List<PosicionTabla> filas) {
        StringBuilder sb = new StringBuilder();
        for (PosicionTabla p : filas) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(p.getEquipo().getNombre());
        }
        return sb.toString();
    }

    // ---------- tablas ----------

    private void configurarPosiciones() {
        tblPosiciones.getColumns().add(col("#", p -> tblPosiciones.getItems().indexOf(p) + 1));
        tblPosiciones.getColumns().add(col("Equipo", p -> p.getEquipo().getNombre()));
        tblPosiciones.getColumns().add(col("PJ", PosicionTabla::getPartidosJugados));
        tblPosiciones.getColumns().add(col("PG", PosicionTabla::getPartidosGanados));
        tblPosiciones.getColumns().add(col("PE", PosicionTabla::getPartidosEmpatados));
        tblPosiciones.getColumns().add(col("PP", PosicionTabla::getPartidosPerdidos));
        tblPosiciones.getColumns().add(col("GF", PosicionTabla::getGolesFavor));
        tblPosiciones.getColumns().add(col("GC", PosicionTabla::getGolesContra));
        tblPosiciones.getColumns().add(col("DG", PosicionTabla::calcularDiferenciaGoles));
        tblPosiciones.getColumns().add(col("PTS", PosicionTabla::getPuntos));
        tblPosiciones.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tblPosiciones.setPlaceholder(new Label("No hay equipos aprobados en este torneo."));
    }

    private void configurarProgramados() {
        tblProgramados.getColumns().add(col("Ronda", Partido::getRonda));
        tblProgramados.getColumns().add(col("Fecha", Partido::getFecha));
        tblProgramados.getColumns().add(col("Local", p -> p.getLocal().getNombre()));
        tblProgramados.getColumns().add(col("Visitante", p -> p.getVisitante().getNombre()));
        tblProgramados.getColumns().add(col("Estado", Partido::getEstado));
        tblProgramados.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tblProgramados.setPlaceholder(new Label("No hay partidos programados."));
    }

    private void configurarResultados() {
        tblResultados.getColumns().add(col("Ronda", Partido::getRonda));
        tblResultados.getColumns().add(col("Fecha", Partido::getFecha));
        tblResultados.getColumns().add(col("Local", p -> p.getLocal().getNombre()));
        tblResultados.getColumns().add(col("Marcador", p -> p.getGolesLocal() + " - " + p.getGolesVisitante()));
        tblResultados.getColumns().add(col("Visitante", p -> p.getVisitante().getNombre()));
        tblResultados.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tblResultados.setPlaceholder(new Label("Aún no hay partidos finalizados."));
    }

    private void configurarTarjetas() {
        TableColumn<Map.Entry<String, int[]>, String> equipo = new TableColumn<>("Equipo");
        equipo.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getKey()));
        TableColumn<Map.Entry<String, int[]>, Integer> amarillas = new TableColumn<>("Amarillas");
        amarillas.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getValue()[0]));
        TableColumn<Map.Entry<String, int[]>, Integer> rojas = new TableColumn<>("Rojas");
        rojas.setCellValueFactory(c -> new ReadOnlyObjectWrapper<>(c.getValue().getValue()[1]));

        tblTarjetas.getColumns().add(equipo);
        tblTarjetas.getColumns().add(amarillas);
        tblTarjetas.getColumns().add(rojas);
        tblTarjetas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tblTarjetas.setPlaceholder(new Label("Sin tarjetas registradas."));
    }

    private VBox construirEstadisticas() {
        VBox caja = new VBox(10, lblResumen, lblGoleador, lblValla, lblTarjetas, tblTarjetas);
        caja.setPadding(new Insets(15));
        return caja;
    }

    /** Crea una columna de solo lectura a partir de una función sobre la fila. */
    private static <T, V> TableColumn<T, V> col(String titulo, Function<T, V> valor) {
        TableColumn<T, V> c = new TableColumn<>(titulo);
        c.setCellValueFactory(fila -> new ReadOnlyObjectWrapper<>(valor.apply(fila.getValue())));
        return c;
    }

    private void mostrarError(RuntimeException ex) {
        new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
    }
}