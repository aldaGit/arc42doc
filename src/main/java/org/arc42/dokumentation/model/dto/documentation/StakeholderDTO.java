package org.arc42.dokumentation.model.dto.documentation;

public class StakeholderDTO {

  private String id;
  private String roleORName;
  private String contact;
  private String expectation;

  public StakeholderDTO(String roleORName, String contact, String expectation) {
    this.roleORName = roleORName;
    this.contact = contact;
    this.expectation = expectation;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getRoleORName() {
    return roleORName;
  }

  public void setRoleORName(String roleORName) {
    this.roleORName = roleORName;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public String getExpectation() {
    return expectation;
  }

  public void setExpectation(String expectation) {
    this.expectation = expectation;
  }
}
