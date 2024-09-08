package org.arc42.dokumentation.model.user;

public class Profil {

    private String username;
    private String mail;
    private String firstName;
    private String lastName;
    private String phone;

    public Profil(String username, String mail, String firstName, String lastName, String phone) {
        this.username = username;
        this.mail = mail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public String getMail() {
        return mail;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone(){
        return phone;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastname){
        this.lastName = lastname;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
}
