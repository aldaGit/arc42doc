package org.arc42.analyse.model.dto;

public class Arc42DocumentationDTO {

  private final String arcId;
  private String decisionId;
  private String decisionTitle;
  private Arc42AlternativeDT0 referencedAlternative;
  private Arc42AlternativeDT0 acceptedAlternative;
  private String rationaleId;
  private String rationaleReferenz;
  private String rationaleBeschreibung;
  private String affectedArtifact;

  public Arc42DocumentationDTO(String arcId) {
    this.arcId = arcId;
  }

  public Boolean acceptedAlternativChange() {
    if (referencedAlternative.getId() != null
        && !referencedAlternative.getId().trim().isEmpty()
        && acceptedAlternative.getId() != null
        && !acceptedAlternative.getId().trim().isEmpty()) {
      return !referencedAlternative
          .getId()
          .trim()
          .equalsIgnoreCase(acceptedAlternative.getId().trim());
    } else if (referencedAlternative.getTitle() != null
        && !referencedAlternative.getTitle().trim().isEmpty()
        && acceptedAlternative.getTitle() != null
        && !acceptedAlternative.getTitle().trim().isEmpty()) {
      return !referencedAlternative
          .getTitle()
          .trim()
          .equalsIgnoreCase(acceptedAlternative.getTitle().trim());
    }
    return true;
  }

  public Boolean checkRationale() {
    if (rationaleId == null) {
      return false;
    } else if (rationaleBeschreibung == null || rationaleBeschreibung.isEmpty()) {
      return false;
    } else return rationaleBeschreibung.length() >= 10;
  }

  public String getDecisionId() {

    return decisionId;
  }

  public void setDecisionId(String decisionId) {
    this.decisionId = decisionId;
  }

  public void setReferencedAlternative(Arc42AlternativeDT0 referencedAlternative) {
    this.referencedAlternative = referencedAlternative;
  }

  public void setAcceptedAlternative(Arc42AlternativeDT0 acceptedAlternative) {
    this.acceptedAlternative = acceptedAlternative;
  }

  public void setRationaleId(String rationaleId) {
    this.rationaleId = rationaleId;
  }

  public String getRationaleReferenz() {
    return rationaleReferenz;
  }

  public void setRationaleReferenz(String rationaleReferenz) {
    this.rationaleReferenz = rationaleReferenz;
  }

  public void setRationaleBeschreibung(String rationaleBeschreibung) {
    this.rationaleBeschreibung = rationaleBeschreibung;
  }

  public String getAffectedArtifact() {
    return affectedArtifact;
  }

  public void setAffectedArtifact(String affectedArtifact) {
    this.affectedArtifact = affectedArtifact;
  }

  public String getDecisionTitle() {
    return decisionTitle;
  }

  public void setDecisionTitle(String decisionTitle) {
    this.decisionTitle = decisionTitle;
  }

  @Override
  public String toString() {
    return "Arc42DocumentationDTO{"
        + "arcId='"
        + arcId
        + '\''
        + ", decisionId='"
        + decisionId
        + '\''
        + ", referencedAlternative="
        + referencedAlternative
        + ", acceptedAlternative="
        + acceptedAlternative
        + ", rationaleId='"
        + rationaleId
        + '\''
        + ", rationaleReferenz='"
        + rationaleReferenz
        + '\''
        + ", rationaleBeschreibung='"
        + rationaleBeschreibung
        + '\''
        + ", decisionTitle='"
        + decisionTitle
        + '\''
        + '}';
  }
}
