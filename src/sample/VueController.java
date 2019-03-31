package sample;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class VueController extends Application {

    private Modele modele;

    private BorderPane root;
    private GridPane connexion;
    private ComboBox comboBox;
    ;

    private Text userNameLogedIn;
    private TextField userTextField;
    private PasswordField passwordField;

    private void conexion(){
        try {
            modele.connexion(userTextField.getText(), passwordField.getText());
            userNameLogedIn.setText(modele.getUserNameLogedIn());
            comboBox.getItems().setAll("mails", modele.getMails());
        } catch (Exception e) {

        }

    }
    @Override
    public void start(Stage primaryStage) throws IOException {

        this.modele = new Modele();
        this.modele.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                conexion();

            }
        });

        root = new BorderPane();
        connexion = new GridPane();
        userNameLogedIn = new Text("");

        connexion.setAlignment(Pos.TOP_LEFT);
        connexion.setHgap(20);
        connexion.setPadding(new Insets(5, 0, 0, 25));

        Label userName = new Label("Utilisateur :");
        connexion.add(userName, 1, 0);

        userTextField = new TextField();
        connexion.add(userTextField, 2, 0);

        Label pw = new Label("Mot de passe :");
        connexion.add(pw, 4, 0);

        passwordField = new PasswordField();
        connexion.add(passwordField, 5, 0);

        Button btn = new Button("Connexion");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        connexion.add(hbBtn, 6, 0);

        btn.setOnAction(event -> {
            modele.setUserNameLogedIn(userTextField.getCharacters().toString());
        });

        GridPane.setHalignment(userNameLogedIn, HPos.RIGHT);
        connexion.add(userNameLogedIn, 7, 0, 20, 1);

        comboBox = new ComboBox();


        root.setTop(connexion);
        root.setCenter(comboBox);
        Scene scene = new Scene(root);

        primaryStage.setResizable(false);
        primaryStage.setTitle("Mailbox");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
