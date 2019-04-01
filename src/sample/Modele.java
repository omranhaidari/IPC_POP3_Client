package sample;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Modele extends Observable {

    private Client client;
    private String userNameLogedIn;

    public Modele() throws IOException {
        this.client = new Client("localhost", 8025);
    }

    public String getUserNameLogedIn() {
        return userNameLogedIn;
    }

    public void setUserNameLogedIn(String userNameLogedIn) {
        this.userNameLogedIn = userNameLogedIn;
        notif();
    }

    public void connexion(String user, String password) throws Exception {
        client.login(user, password);
        setUserNameLogedIn(user);
    }

    public void notif() {
        this.setChanged();
        this.notifyObservers();
    }

    public List<String> getMails() {
        List<String> mailsName = new ArrayList<>();
        try {
            client.retrieveMails();
            for (int i = 1; i <= client.getNumberOfMail(); i++) {
                InputStream flux = new FileInputStream(new File("receiver", i + ".txt"));
                InputStreamReader lecture = new InputStreamReader(flux);
                BufferedReader buff = new BufferedReader(lecture);
                final StringBuilder mail = new StringBuilder();
                buff.lines().forEach(line -> mail.append(line).append("\n"));
                mailsName.add(mail.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mailsName;
    }

    public void deconnexion() throws IOException {
        client.logout();
    }
}
