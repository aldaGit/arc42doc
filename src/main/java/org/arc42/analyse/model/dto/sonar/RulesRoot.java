package org.arc42.analyse.model.dto.sonar;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RulesRoot {

  private static RulesRoot instance;

  public static RulesRoot getInstance() {
    if (instance == null) {
      instance = new RulesRoot();
    }
    return instance;
  }

  public RulesRoot() {}

  Rule rule;

  @SerializedName("actives")
  List<Active> actives;

  public Rule getRule() {
    return rule;
  }

  public void setRule(Rule rule) {
    this.rule = rule;
  }

  public RulesRoot(Rule rule, List<Active> actives) {
    this.rule = rule;
    this.actives = actives;
  }

  public List<Active> getActives() {
    return actives;
  }

  public void setActives(List<Active> actives) {
    this.actives = actives;
  }
}
