package Classes;

import utils.Utils;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String username;
    private String password;
    private String email;
    private String location;
    private String contact;
    private Img profilePic;

    public User(String name, String username, String password, String email, String location, String contact) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.location = location;
        this.contact = contact;
    }

    public User(String name, String username, String password, String email, String location, String contact, Img profilePic) {
        this(name, username, password, email, location, contact);
        this.profilePic = profilePic;
    }

    public User(String str, String password) {
        boolean isEmail = Utils.validateEmail(str);
        if (isEmail)
            this.email = str;
        else
            this.username = str;
        this.password = password;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Img getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Img profilePic) {
        this.profilePic = profilePic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

}
