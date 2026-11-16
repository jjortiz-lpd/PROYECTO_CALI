package torneojav.vista;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import torneojav.Contexto;
import torneojav.excepcion.PersistenciaException;
import torneojav.modelo.Rol;
import torneojav.modelo.Usuario;
import torneojav.persistencia.GestorConexionJDBC;

public class MainApp extends Application {

    private Stage stage;
    private Contexto ctx;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        try {
            GestorConexionJDBC.probarConexion();
        } catch (PersistenciaException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            Platform.exit();
            return;
        }
        ctx = new Contexto();

        stage.setTitle("TorneoJAV");
        mostrarLogin();
        stage.show();
    }

    private void mostrarLogin() {
        VistaLogin login = new VistaLogin(ctx, this::mostrarPanel);
        stage.setScene(new Scene(login.getRaiz(), 480, 360));
    }

    private void mostrarPanel(Usuario u) {
        Label saludo = new Label("Bienvenido " + u.getNombreUsuario() + " (" + u.getRol() + ")");
        Button btnSalir = new Button("Cerrar sesión");
        btnSalir.setOnAction(e -> {
            ctx.auth.cerrarSesion();
            mostrarLogin();
        });

        HBox cabecera = new HBox(20, saludo, btnSalir);
        cabecera.setAlignment(Pos.CENTER_LEFT);
        cabecera.setPadding(new Insets(15, 15, 0, 15));

        VistaConsultas consultas = new VistaConsultas(ctx);
        Tab tabConsultas = new Tab("Consultas", consultas.getRaiz());
        tabConsultas.setOnSelectionChanged(e -> {
            if (tabConsultas.isSelected()) {
                consultas.actualizar();
            }
        });

        TabPane pestanas = new TabPane();
        pestanas.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        if (u.getRol() == Rol.ARBITRO) {
            pestanas.getTabs().add(new Tab("Actas", new VistaArbitro(ctx).getRaiz()));
        }
        if (u.getRol() == Rol.ADMINISTRADOR) {
            pestanas.getTabs().add(new Tab("Administración", new VistaAdmin(ctx).getRaiz()));
        }
        if (u.getRol() == Rol.CAPITAN) {
            pestanas.getTabs().add(new Tab("Mis equipos", new VistaCapitan(ctx).getRaiz()));
        }
        pestanas.getTabs().add(tabConsultas);

        BorderPane panel = new BorderPane();
        panel.setTop(cabecera);
        panel.setCenter(pestanas);
        stage.setScene(new Scene(panel, 980, 620));
    }

    public static void main(String[] args) {
        launch(args);
    }
}