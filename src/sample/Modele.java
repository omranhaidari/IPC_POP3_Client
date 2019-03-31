package sample;

import java.io.IOException;
import java.util.List;
import java.util.Observable;

public class Modele extends Observable {

    private Client client;
    private String UserNameLogedIn;

    public Modele() throws IOException {
        this.client = new Client("localhost", 8025);
    }

    public String getUserNameLogedIn() {
        return UserNameLogedIn;
    }

    public void setUserNameLogedIn(String userNameLogedIn) {
        UserNameLogedIn = userNameLogedIn;
        notif();
    }

    public void connexion(String user, String password) throws Exception {
        client.login(user, password);
    }

    public void notif() {
        this.setChanged();
        this.notifyObservers();
    }

    public List<String> getMails() {
        return null;
        //return client.getMails();
    }
}
