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
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class VueController extends Application {

    private Modele modele;

    private BorderPane root;
    private GridPane connexion;
    private GridPane listMails;


    private Text userNameLogedIn;
    private TextField userTextField;
    private PasswordField passwordField;

    @Override
    public void start(Stage primaryStage) throws IOException {

        this.modele = new Modele();
        this.modele.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                userNameLogedIn.setText("");
                userTextField.clear();
                passwordField.clear();
            }
        });

        root = new BorderPane();

        // espace mails (au centre)
        listMails = new GridPane();
        listMails.setGridLinesVisible(true);
        listMails.setAlignment(Pos.CENTER);
        root.setCenter(listMails);

        //espace bouton retrieve (à gauche)
        Button retrieve = new Button("Retrieve mails");
        HBox hbBtnRetrieve = new HBox(10);
        hbBtnRetrieve.setAlignment(Pos.CENTER_LEFT);
        hbBtnRetrieve.getChildren().add(retrieve);
        retrieve.setOnAction(event -> retrieveMails());
        root.setLeft(hbBtnRetrieve);

        // espace connexion (barre du haut)
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

        Button connexionbtn = new Button("Connexion");
        HBox hbBtnConnexion = new HBox(10);
        hbBtnConnexion.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnConnexion.getChildren().add(connexion);
        connexionbtn.setOnAction(event -> this.connexion());
        connexion.add(hbBtnConnexion, 6, 0);

        GridPane.setHalignment(userNameLogedIn, HPos.RIGHT);
        connexion.add(userNameLogedIn, 8, 0, 20, 1);

        Button deconnexion = new Button("Deconnexion");
        HBox hbBtnDeconnexion = new HBox(10);
        hbBtnDeconnexion.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnDeconnexion.getChildren().add(deconnexion);
        deconnexion.setOnAction(event -> deconnexion());
        this.connexion.add(deconnexion, 7, 0);
        root.setTop(this.connexion);


        Scene scene = new Scene(root, 1500, 500);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Mailbox");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void retrieveMails() {
        List<String> mailsBody = modele.getMails();
        for (int i = 0; i < mailsBody.size(); i++)
            listMails.add(new Label(mailsBody.get(i)), 0, i);
    }

    private void connexion(){
        try {
            modele.connexion(userTextField.getText(), passwordField.getText());
            userNameLogedIn.setText(modele.getUserNameLogedIn());
            passwordField.clear();
            userTextField.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void deconnexion() {
        try {
            modele.deconnexion();
            userNameLogedIn.setText("");
            passwordField.clear();
            userTextField.clear();
            List<String> mailsBody = new ArrayList<>();
            listMails = new GridPane();
            modele.setUserNameLogedIn("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
