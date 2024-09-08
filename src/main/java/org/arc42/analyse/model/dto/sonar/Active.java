package org.arc42.analyse.model.dto.sonar;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Active {

  @SerializedName("params")
  List<Param> params;

  String qProfile;

  @Override
  public String toString() {
    return "Active{"
        + "params="
        + params
        + ", qProfile='"
        + qProfile
        + '\''
        + ", severity='"
        + severity
        + '\''
        + '}';
  }

  String severity;

  public Active(List<Param> params, String qProfile, String severity) {
    this.params = params;
    this.qProfile = qProfile;
    this.severity = severity;
  }

  public List<Param> getParams() {
    return params;
  }

  public void setParams(List<Param> params) {
    this.params = params;
  }

  public Active(List<Param> params) {
    this.params = params;
  }
}
