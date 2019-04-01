package sample;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class ClientSSL extends Client {

    public ClientSSL(String host, int port) throws UnknownHostException, IOException {
        try {
            SocketFactory factory = SSLSocketFactory.getDefault();
            socket = factory.createSocket();
            socket.connect(new InetSocketAddress(host, port));
            inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            response = inStream.readLine();
            System.out.println("S: " + response);
            System.out.println("Connection setup...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        int port = 8043; //8045
        String host = "localhost";
        String user = "john";
        String password = "passjohn";
        Client pop3Client = new ClientSSL(host, port);

        pop3Client.login(user, password);
        pop3Client.retrieveMails();
        pop3Client.logout();
    }


}
