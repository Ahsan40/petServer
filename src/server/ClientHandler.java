package server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private int id;
    public Socket sc;
    public ObjectInputStream receiveObj;
    public ObjectOutputStream sendObj;

    public ClientHandler(Socket sc) {
        try {
            this.sc = sc;
            OutputStream oo = sc.getOutputStream();
            sendObj = new ObjectOutputStream(oo);

            InputStream inputStream = sc.getInputStream();
            receiveObj = new ObjectInputStream(inputStream);

            id = ++Server.clientCount;
            Server.clients.put(this, Server.clientCount + "");
            System.out.println(" - Connected with client " + id + "!");
        } catch (IOException e) {
            close(sc, receiveObj, sendObj);
        }
    }

    @Override
    public void run() {
        String request;
        try {
            while (sc.isConnected()) {
                // Receiving Request
                request = (String) receiveObj.readObject();
                task(request);
            }
        } catch (IOException | ClassNotFoundException e) {
            close(sc, receiveObj, sendObj);
            System.out.println(" - Disconnected client " + id + "!\n\n");
        }
    }

    void task(String request) {
        try {
            System.out.println(" - Received request for " + request);

            // Handling Request
            switch (request) {
                case "signup" -> Operations.signup(sendObj, receiveObj);
                case "login" -> Operations.login(sendObj, receiveObj);
                case "logout" -> Operations.logout(sendObj);
                case "addAnimal" -> Operations.addAnimal(sendObj, receiveObj);
                case "addToFavourite" -> Operations.addToFavourite(sendObj, receiveObj);
                case "requestToAdoptPet" -> Operations.reqToAdoptPet(sendObj, receiveObj);
                case "getUploadedCat" -> Operations.getUploadedCat(sendObj, receiveObj);
                case "getUploadedDog" -> Operations.getUploadedDog(sendObj, receiveObj);
                case "getUserFromUserName" -> Operations.getUserFromUserName(sendObj, receiveObj);
                case "updatePetStatus" -> Operations.updatePetStatus(receiveObj);
            }
        } catch (Exception e) {
            close(sc, receiveObj, sendObj);
            e.printStackTrace();
        }
    }

    void removeClientHandler() {
        System.out.println(" - Finish Serving client " + Server.clients.get(this));
        Server.clients.remove(this);
    }

    void close(Socket sc, ObjectInputStream ois, ObjectOutputStream oos) {
        removeClientHandler();
        try {
            if (ois != null) ois.close();
            if (oos != null) oos.close();
            if (sc != null) sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
