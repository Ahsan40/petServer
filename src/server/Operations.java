package server;

import Classes.Animal;
import Classes.User;
import main.Configs;
import utils.FileIO;
import utils.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import static main.Configs.userData;

public class Operations {
    synchronized public static void signup(ObjectOutputStream sendObj, ObjectInputStream receiveObj) throws IOException, ClassNotFoundException {
        User user = (User) receiveObj.readObject();
        System.out.println(" - Attempt to registration");
        if (!Server.users.containsKey(user.getEmail()) && !Server.usersName.containsKey(user.getUsername())) {

            // Upload Profile Picture First (Important)
            String imgPath = Utils.base64ToImg(user.getProfilePic(), Configs.uploadLocation);
            user.getProfilePic().setPath(imgPath);
            System.out.println(" - Updated User IMG to -> " + imgPath);

            Server.users.put(user.getEmail(), user);
            Server.usersName.put(user.getUsername(), user.getEmail());
            FileIO.writeObjToFile(Server.users, userData);
            sendObj.writeObject("SUCCESS");
            System.out.println(" - Registration Successful");
        } else {
            sendObj.writeObject("FAILED!");
            System.out.println(" - Registration failed! Duplicate email found!");
        }
    }

    public static void login(ObjectOutputStream sendObj, ObjectInputStream receiveObj) throws IOException, ClassNotFoundException {
        User user = (User) receiveObj.readObject();

        if(user == null) {
            sendObj.writeObject("FAILED");
            return;
        }

        System.out.println(" - Attempt to Login");
        System.out.println(" - Passwords Hash: " + user.getPassword());

//        boolean isEmail = !user.getEmail().isEmpty();
        String key;
        String match;

//        if (isEmail)
            key = user.getEmail();
//        else
//            key = Server.usersName.get(user.getUsername());

        if(key != null) {
            if (Server.users.containsKey(key)) {
                match = Server.users.get(key).getPassword();
                if (match.equals(user.getPassword())) {
                    System.out.println(" - login success");
                    sendObj.writeObject("SUCCESS");
                    System.out.println(" - sending user info");
                    user = Server.users.get(key);
                    sendObj.writeObject(user);
                    System.out.println(" - info send successfully!");
                    return;
                }
            }
        }

        System.out.println(" - Invalid credentials, login failed!!");
        sendObj.writeObject("FAILED!");
    }

    public static void logout(ObjectOutputStream sendObj) throws IOException {
        System.out.println(" - Client Logged Out from Account!");
        sendObj.writeObject("Success");
    }

//    synchronized public static void updateInfo(ObjectInputStream receiveObj) throws IOException, ClassNotFoundException {
//        User user = (User) receiveObj.readObject();
//        System.out.println(" - Attempt to update user info");
//        Server.data.put(user.getEmail(), user);
//        System.out.println(" - New Pass Hash: " + user.getPasswords());
//        Utils.writeHashMapToFile(Server.data, "database.ser");
//        System.out.println(" - Update Info Successful");
//    }

//    synchronized public static void addBus(ObjectInputStream receiveObj, String cmd) throws IOException, ClassNotFoundException {
//        Bus bus = (Bus) receiveObj.readObject();
//        Server.busData.put(bus, new HashMap<>());
//        Utils.writeBusDataToFile(Server.busData, "busData.ser");
//        refresh(cmd);
//    }

//    synchronized public static void overWrite(ObjectInputStream receiveObj, String cmd) throws IOException, ClassNotFoundException {
//        Server.busData = (HashMap<Bus, HashMap<String, ArrayList<Ticket>>>) receiveObj.readObject();
//        Utils.writeBusDataToFile(Server.busData, "busData.ser");
//        refresh(cmd);
//    }
//
//    public static void getBusList(ObjectOutputStream sendObj) throws IOException {
//        HashSet<Bus> buses = new HashSet<>();
//        for (Map.Entry<Bus, HashMap<String, ArrayList<Ticket>>> entry: Server.busData.entrySet()) {
//            buses.add(entry.getKey());
//        }
//        sendObj.writeObject(buses);
//    }
//
//    public static void getBusData(ObjectOutputStream sendObj) throws IOException {
//        sendObj.writeObject(Server.busData);
//    }
//
//    synchronized public static void refresh(String cmd) {
//        System.out.println(" - Start Refreshing...");
//        try {
//            for (Map.Entry<ClientHandler, String> entry: Server.clients.entrySet()) {
//                System.out.println(" - Refresh Client " + entry.getValue());
//                entry.getKey().sendObj.writeObject(cmd);
//                entry.getKey().sendObj.writeObject(Server.busData);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void addAnimal(ObjectOutputStream sendObj, ObjectInputStream receiveObj) throws IOException, ClassNotFoundException {
        Animal animal = (Animal) receiveObj.readObject();
        System.out.println(" - Animal Name: " + animal.getBreedName());

        // Upload Profile Picture First (Important)
        String imgPath = Utils.base64ToImg(animal.getAnimalPic(), Configs.uploadLocation);
        animal.getAnimalPic().setPath(imgPath);

        if(animal.getType().equalsIgnoreCase("cat")) {
            Server.cat.add(animal);
            FileIO.writeObjToFile(Server.cat, Configs.catData);
        }
        else {
            Server.dog.add(animal);
            FileIO.writeObjToFile(Server.dog, Configs.dogData);
        }
    }

    public static void addToFavourite(ObjectOutputStream sendObj, ObjectInputStream receiveObj) {
    }

    public static void reqToAdoptPet(ObjectOutputStream sendObj, ObjectInputStream receiveObj) {
    }

    public static void getUploadedAnimal(String animal, ObjectOutputStream sendObj, ObjectInputStream receiveObj) throws IOException, ClassNotFoundException {
        User user = (User) receiveObj.readObject();

        System.out.println(" - Filtering Animal");
        ArrayList<Animal> list = new ArrayList<>();

        if(animal.equalsIgnoreCase("cat")) {
            for (Animal a : Server.cat) {
                if(user == null) {
                    list.add(a);
                    continue;
                }
                if (a.getOwner().equalsIgnoreCase(user.getUsername()))
                    list.add(a);
            }
        } else if (animal.equalsIgnoreCase("dog")) {
            for (Animal a : Server.dog) {
                if(user == null) {
                    list.add(a);
                    continue;
                }
                if (a.getOwner().equalsIgnoreCase(user.getUsername()))
                    list.add(a);
            }
        }

        System.out.println(" - Sending Animal List to Client");
        sendObj.writeObject(list);
    }

    public static void getUploadedCat(ObjectOutputStream sendObj, ObjectInputStream receiveObj) throws IOException, ClassNotFoundException {
        getUploadedAnimal("cat", sendObj, receiveObj);
    }

    public static void getUploadedDog(ObjectOutputStream sendObj, ObjectInputStream receiveObj) throws IOException, ClassNotFoundException {
        getUploadedAnimal("dog", sendObj, receiveObj);
    }

    public static void getUserFromUserName(ObjectOutputStream sendObj, ObjectInputStream receiveObj) throws IOException, ClassNotFoundException {
        String userName = (String) receiveObj.readObject();
        User u = Server.users.get(Server.usersName.get(userName));
        sendObj.writeObject(u);
        sendObj.flush();
    }

    private static boolean matchAnimal(Animal a, Animal b) {
        if(!a.getPetname().equalsIgnoreCase(b.getPetname()))
            return false;
        if(!a.getType().equalsIgnoreCase(b.getType()))
            return false;
        if(!a.getAge().equalsIgnoreCase(b.getAge()))
            return false;
        if(!a.getOwner().equalsIgnoreCase(b.getOwner()))
            return false;
        if(!a.getBreedName().equalsIgnoreCase(b.getBreedName()))
            return false;
        if(!a.getFoodhabit().equalsIgnoreCase(b.getFoodhabit()))
            return false;
        return true;
    }
    public static void updatePetStatus(ObjectOutputStream sendObj, ObjectInputStream receiveObj) throws IOException, ClassNotFoundException {
        Animal animal = (Animal) receiveObj.readObject();
        System.out.println(" - (updatePetStatus) Animal Name: " + animal.getBreedName());
        int i = 0;

        if(animal.getType().equalsIgnoreCase("cat")) {
            for (Animal a: Server.cat) {
                if (matchAnimal(a, animal)) {
                    Server.cat.get(i).setStatus("Not Available");
                    FileIO.writeObjToFile(Server.cat, Configs.catData);
                    System.out.println(a.getStatus());
                    break;
                }
                i++;
            }
        } else {
            for (Animal a: Server.dog) {
                if (matchAnimal(a, animal)) {
                    Server.dog.get(i).setStatus("Not Available");
                    FileIO.writeObjToFile(Server.dog, Configs.dogData);
                    System.out.println(a.getStatus());
                    break;
                }
                i++;
            }
        }
    }
}
