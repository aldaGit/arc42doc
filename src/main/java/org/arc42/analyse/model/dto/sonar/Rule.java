package org.arc42.analyse.model.dto.sonar;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Rule {
  String key;
  String htmlDesc;

  @SerializedName("params")
  List<Param> params;

  @SerializedName("name")
  String descr;

  String severity;

  String type;

  List<Active> actives;

  public String getFormattedName() {
    return key.substring(key.lastIndexOf(":") + 1);
  }

  public void completeRuleDetails(Rule r) {
    setDescr(r.getDescr());
    setParams(r.getParams());
    setSeverity(r.getSeverity());
    setType(r.getType());
  }

  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

  public String getSeverity() {
    return severity;
  }

  public void setSeverity(String severity) {
    this.severity = severity;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<Active> getActives() {
    return actives;
  }

  public void setActives(List<Active> activesNew) {
    this.actives = activesNew;
  }

  public List<Param> getParams() {
    return params;
  }

  public void setParams(List<Param> params) {
    this.params = params;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getHtmlDesc() {
    return htmlDesc;
  }

  @Override
  public String toString() {
    return "Rule{"
        + "key='"
        + key
        + '\''
        + ", htmlDesc='"
        + htmlDesc
        + '\''
        + ", params="
        + params
        + ", descr='"
        + descr
        + '\''
        + ", severity='"
        + severity
        + '\''
        + ", type='"
        + type
        + '\''
        + ", actives="
        + actives
        + '}';
  }
}
