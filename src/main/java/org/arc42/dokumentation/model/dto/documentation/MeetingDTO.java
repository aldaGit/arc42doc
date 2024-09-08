package org.arc42.dokumentation.model.dto.documentation;

import java.util.List;

public class MeetingDTO {
  private String id;
  private String meetingName;
  private int frequency;
  private String repetition;
  private String meetingType;

  public enum Repetition {
    EINMALIG("Einmalig"),
    TAG("Tag"),
    WOCHE("Woche"),
    MONAT("Monat"),
    JAHR("Jahr");
    public final String label;

    Repetition(String label) {
      this.label = label;
    }
  }

  public enum MeetingType {
    REMOTE("Remote"),
    HYBRID("Hybrid"),
    PRAESENZ("Präsenz");
    public final String label;

    MeetingType(String label) {
      this.label = label;
    }
  }

  public MeetingDTO(String meetingName, int frequency, String repetition, String meetingType) {
    this.meetingName = meetingName;
    this.frequency = frequency;
    this.repetition = repetition;
    this.meetingType = meetingType;
  }

  public String getMeetingName() {
    return meetingName;
  }

  public void setMeetingName(String meetingName) {
    this.meetingName = meetingName;
  }

  public void setFrequency(int frequency) {
    this.frequency = frequency;
  }

  public void setRepetition(String repetition) {
    this.repetition = repetition;
  }

  public int getFrequency() {
    return frequency;
  }

  public String getRepetition() {
    return repetition;
  }

  public String getRegularity() {
    return frequency + " x " + repetition;
  }

  public String getMeetingType() {
    return meetingType;
  }

  public void setMeetingType(String meetingType) {
    this.meetingType = meetingType;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public static List<String> getRepetitions() {
    return List.of(
        Repetition.EINMALIG.label,
        Repetition.TAG.label,
        Repetition.WOCHE.label,
        Repetition.MONAT.label,
        Repetition.JAHR.label);
  }

  public static List<String> getMeetingTypes() {
    return List.of(MeetingType.REMOTE.label, MeetingType.HYBRID.label, MeetingType.PRAESENZ.label);
  }
}
