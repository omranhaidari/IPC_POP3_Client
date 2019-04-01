package sample;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Queue;

public class Client {

    protected Socket socket;
    protected BufferedReader inStream = null;
    protected BufferedWriter outStream = null;
    protected String response;
    protected int numberOfMail;
    protected String directory = "receiver";
    protected String timeStamp;

    public Client(String host, int port) throws UnknownHostException, IOException {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port));
            inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            response = inStream.readLine();
            System.out.println("S: " + response);
            timeStamp = "<" + response.split("<")[1].split(">")[0] + ">";
            System.out.println("Connection setup...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Client() {
    }

    public void login(String user, String password) throws Exception {
        String checksum;
        try {
            checksum = new String(MessageDigest.getInstance("MD5").digest((this.timeStamp + password).getBytes()));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Algorithm MD5 not found");
            checksum = password;
        }
        sendRequest("APOP " + user + " " + checksum);
        try {
            response = inStream.readLine();
            if (!response.startsWith("+OK")) {
                throw new Exception("Error server responded: Username Denied!");
            } else {
                System.out.println("S: " + response);
                File dir = new File(directory);
                dir.mkdir();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout() throws IOException {
        sendRequest("QUIT");
        response = inStream.readLine();
        System.out.println("S: " + response);
        System.out.println("pop3Client logged out!");

        socket.close();
        System.out.println("Connection closed with host");
        inStream.close();
        outStream.close();
    }

    public void retrieveMails() throws IOException {
        sendRequest("STAT");
        String response;
        try {
            response = inStream.readLine();
            System.out.println("S: " + response);
            numberOfMail = Integer.parseInt(response.split(" ")[1]);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        for (int i = 1; i <= numberOfMail; i++) {
            sendRequest("RETR " + i);
            String path = i + ".txt";

            File f = new File(directory, path);

            f.createNewFile();

            FileWriter fw = new FileWriter(f);
            BufferedWriter writer = new BufferedWriter(fw);

            String line;
            while (!(line = inStream.readLine()).equals(".")) {
                if(line.startsWith("+OK")) {
                    continue;
                }

                System.out.println(line);
                writer.write(line);
                writer.newLine();
                writer.flush();
            }

            writer.close();
            fw.close();
        }
    }

    private void sendRequest(String string) {
        try {
            outStream.write(string + "\r\n");
            outStream.flush();
            System.out.println("C: " + string.trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        int port = 8025;
        String host = "localhost";
        String user = "john";
        String password = "passjohn";
        Client pop3Client = new Client(host, port);

        pop3Client.login(user, password);
        pop3Client.retrieveMails();
        pop3Client.logout();
    }

    public int getNumberOfMail() {
        return numberOfMail;
    }
}