package org.arc42.analyse.model.dto.sonar;

import java.util.HashMap;
import java.util.List;

public class Issue {

  private String status;
  private String message;
  private String type;

  private String severity;

  private String component;

  private int line;

  private String rule;

  private String ruleshort;

  private String key;

  private String resolution;

  private String ruleHtml;

  private List<String> tags;

  public String getTag() {
    return tags.get(0);
  }

  public void setTags(String tag) {
    this.tags.set(0, tag);
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getRuleHtml() {
    return ruleHtml;
  }

  public void setRuleHtml(String ruleHtml) {
    this.ruleHtml = ruleHtml;
  }

  public String getResolution() {
    return resolution;
  }

  public void setResolution(String resolution) {
    this.resolution = resolution;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getRuleshort() {
    return ruleshort;
  }

  public void setRuleshort(String ruleshort) {
    this.ruleshort = ruleshort;
  }

  public String getRule() {
    return rule;
  }

  public void setRule(String rule) {
    this.rule = rule;
  }

  public String getSeverity() {
    return severity;
  }

  public String getComponent() {
    return component;
  }

  public void setComponent(String component) {
    this.component = component;
  }

  public int getLine() {
    return line;
  }

  public void setLine(int line) {
    this.line = line;
  }

  public void formatTag() {
    HashMap<String, String> names = new HashMap<>();
    names.put("codesmell", "Code Smell");
    names.put("collection", "Collection");
    this.setTags(names.get(this.getTag()));
  }
}
