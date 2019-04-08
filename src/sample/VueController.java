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
    private GridPane connexionGrille;
    private GridPane listMails;


    private Text userNameLogedIn;
    private TextField userTextField;
    private PasswordField passwordField;

    @Override
    public void start(Stage primaryStage) throws IOException {

        this.modele = new Modele();

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

        // espace connexionGrille (barre du haut)
        connexionGrille = new GridPane();
        userNameLogedIn = new Text("");

        connexionGrille.setAlignment(Pos.TOP_LEFT);
        connexionGrille.setHgap(20);
        connexionGrille.setPadding(new Insets(5, 0, 0, 25));

        Label userName = new Label("Utilisateur :");
        connexionGrille.add(userName, 1, 0);

        userTextField = new TextField();
        connexionGrille.add(userTextField, 2, 0);

        Label pw = new Label("Mot de passe :");
        connexionGrille.add(pw, 4, 0);

        passwordField = new PasswordField();
        connexionGrille.add(passwordField, 5, 0);

        Button connexionbtn = new Button("Connexion");
        HBox hbBtnConnexion = new HBox(10);
        hbBtnConnexion.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnConnexion.getChildren().add(connexionbtn);
        connexionbtn.setOnAction(event -> this.connexion());
        connexionGrille.add(hbBtnConnexion, 6, 0);

        GridPane.setHalignment(userNameLogedIn, HPos.RIGHT);
        connexionGrille.add(userNameLogedIn, 8, 0, 20, 1);

        Button deconnexion = new Button("Deconnexion");
        HBox hbBtnDeconnexion = new HBox(10);
        hbBtnDeconnexion.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnDeconnexion.getChildren().add(deconnexion);
        deconnexion.setOnAction(event -> deconnexion());
        connexionGrille.add(deconnexion, 7, 0);
        root.setTop(this.connexionGrille);


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
            modele.setUserNameLogedIn("");
            passwordField.clear();
            userTextField.clear();
            listMails = new GridPane();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
