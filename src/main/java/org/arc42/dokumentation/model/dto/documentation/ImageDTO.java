package org.arc42.dokumentation.model.dto.documentation;

public class ImageDTO {
  private String id;
  private String bildName;
  private String bildMimeType;
  private byte[] bildPath;
  private String uxfName;
  private String uxfMimeType;
  private byte[] uxfPath;
  private String description;

  public ImageDTO(String description) {
    this.description = description;
  }

  public ImageDTO(
      String id,
      String bildName,
      String bildMimeType,
      byte[] bildPath,
      String uxfName,
      String uxfMimeType,
      byte[] uxfPath) {

    this.id = id;
    this.bildName = bildName;
    this.bildMimeType = bildMimeType;
    this.bildPath = bildPath;
    this.uxfName = uxfName;
    this.uxfMimeType = uxfMimeType;
    this.uxfPath = uxfPath;
  }

  public ImageDTO(
      String id,
      String bildName,
      String bildMimeType,
      byte[] bildPath,
      String uxfName,
      String uxfMimeType,
      byte[] uxfPath,
      String description) {
    this.id = id;
    this.bildName = bildName;
    this.bildMimeType = bildMimeType;
    this.bildPath = bildPath;
    this.uxfName = uxfName;
    this.uxfMimeType = uxfMimeType;
    this.uxfPath = uxfPath;
    this.description = description;
  }

  public ImageDTO() {}

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getBildName() {
    return bildName;
  }

  public void setBildName(String bildName) {
    this.bildName = bildName;
  }

  public String getBildMimeType() {
    return bildMimeType;
  }

  public void setBildMimeType(String bildMimeType) {
    this.bildMimeType = bildMimeType;
  }

  public byte[] getBildStream() {
    return bildPath;
  }

  public void setBildPath(byte[] bildPath) {
    this.bildPath = bildPath;
  }

  public String getUxfName() {

    return uxfName;
  }

  public void setUxfName(String uxfName) {
    this.uxfName = uxfName;
  }

  public String getUxfMimeType() {
    return uxfMimeType;
  }

  public void setUxfMimeType(String uxfMimeType) {
    this.uxfMimeType = uxfMimeType;
  }

  public byte[] getUxfStream() {
    return uxfPath;
  }

  public void setUxfPath(byte[] uxfPath) {
    this.uxfPath = uxfPath;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean showImage() {
    return this.getBildStream() != null;
  }
}
