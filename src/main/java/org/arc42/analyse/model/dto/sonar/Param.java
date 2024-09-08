package org.arc42.analyse.model.dto.sonar;

import com.google.gson.annotations.SerializedName;

public class Param {
  private String key;
  private String value;

  @SerializedName("htmlDesc")
  public String paramDescr;

  private int defaultValue;

  public String getParamDescr() {
    return paramDescr;
  }

  public int getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(int defaultValue) {
    this.defaultValue = defaultValue;
  }

  public Param(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "Param{"
        + "key='"
        + key
        + '\''
        + ", value='"
        + value
        + '\''
        + ", paramDescr='"
        + paramDescr
        + '\''
        + ", defaultValue="
        + defaultValue
        + '}';
  }
}