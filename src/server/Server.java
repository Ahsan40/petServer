package server;

import Classes.Animal;
import Classes.User;
import utils.FileIO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static main.Configs.*;

public class Server {
    public static int clientCount = 0;
    public static HashMap<ClientHandler, String> clients = new HashMap<>();
    public static HashMap<String, User> users = new HashMap<>();
    public static ArrayList<Animal> cat = new ArrayList<>();
    public static ArrayList<Animal> dog = new ArrayList<>();
    public static HashMap<String, ArrayList<Animal>> favourite = new HashMap<>();
    public static HashMap<String, String> usersName = new HashMap<>(); // for checking userName
    public ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {

        try {
            FileIO.checkDB(userData, users);
            FileIO.checkDB(catData, cat);
            FileIO.checkDB(dogData, dog);

            // Read User & Pet Data
            users = FileIO.readObjFromFile(userData);
            cat = FileIO.readObjFromFile(catData);
            dog = FileIO.readObjFromFile(dogData);

            // Duplicate UserName Checking Map
            for (Map.Entry<String, User> u : users.entrySet()) {
                usersName.put(u.getValue().getUsername(), u.getValue().getEmail());
            }

            System.out.println("Server is waiting for client.");

            while (!serverSocket.isClosed()) {
                Socket sc = serverSocket.accept();
                ClientHandler ch = new ClientHandler(sc);
                Thread t = new Thread(ch);
                t.start();
            }
        } catch (Exception e) {
            closeServer();
            e.printStackTrace();
        }
    }

    public void closeServer() {
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
