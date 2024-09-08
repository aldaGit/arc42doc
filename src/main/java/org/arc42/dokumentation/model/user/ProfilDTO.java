package org.arc42.dokumentation.model.user;

public class ProfilDTO {

  private String id;
  private String username;
  private String email;
  private String lastName;
  private String firstName;
  private String phone; // Can be Any string but represents the phone number

  public ProfilDTO(String u) {
    this.id = "";
    this.username = u;
    this.email = "";
    this.lastName = "";
    this.firstName = "";
    this.phone = "";
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String fName) {
    this.firstName = fName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lN) {
    this.lastName = lN;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getId() {
    return id;
  }

  public void setId(String iD) {
    this.id = iD;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public ProfilDTO makeSaveableCopy() {
    // WILL NOT INCLUDE NULL AS VALUE
    ProfilDTO copy = new ProfilDTO(this.username);
    copy.setId(this.id == null ? "" : this.id);
    copy.setEmail(this.email == null ? "" : this.email);
    copy.setFirstName(this.firstName == null ? "" : this.firstName);
    copy.setLastName(this.lastName == null ? "" : this.lastName);
    copy.setPhone(this.phone == null ? "" : this.phone);
    return copy;
  }

  // for detailed debugging
  @Override
  public String toString() {
    return "ProfilDTO: \nid={"
        + id
        + "}\nusername={"
        + username
        + "}\nemail={"
        + email
        + "}\nlastName={"
        + lastName
        + "}\nfirstName={"
        + firstName
        + "}\nphone={"
        + phone
        + "}";
  }
}
