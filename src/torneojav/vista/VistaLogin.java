package torneojav.vista;

import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import torneojav.Contexto;
import torneojav.excepcion.CredencialesInvalidasException;
import torneojav.excepcion.PersistenciaException;
import torneojav.modelo.Usuario;

public class VistaLogin {

    private final VBox raiz = new VBox(12);

    public VistaLogin(Contexto ctx, Consumer<Usuario> alIniciarSesion) {
        Label titulo = new Label("TorneoJAV");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Usuario");

        PasswordField txtClave = new PasswordField();
        txtClave.setPromptText("Contraseña");

        Label lblError = new Label();
        lblError.setStyle("-fx-text-fill: #c0392b;");

        Button btnEntrar = new Button("Iniciar sesión");
        btnEntrar.setDefaultButton(true); // Enter también dispara el botón
        btnEntrar.setOnAction(e -> {
            try {
                Usuario u = ctx.auth.iniciarSesion(txtUsuario.getText().trim(), txtClave.getText());
                txtClave.clear();
                lblError.setText("");
                alIniciarSesion.accept(u);
            } catch (CredencialesInvalidasException | PersistenciaException ex) {
                lblError.setText(ex.getMessage());
            }
        });

        raiz.getChildren().addAll(titulo, txtUsuario, txtClave, btnEntrar, lblError);
        raiz.setAlignment(Pos.CENTER);
        raiz.setPadding(new Insets(40));
        raiz.setMaxWidth(320);
    }

    public Parent getRaiz() {
        VBox centro = new VBox(raiz);
        centro.setAlignment(Pos.CENTER);
        return centro;
    }
}