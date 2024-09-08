package org.arc42.analyse.model.dto.sonar;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RuleList {

  private static RuleList instance;

  public static RuleList getInstance() {
    if (instance == null) {
      instance = new RuleList();
    }
    return instance;
  }

  public List<Rule> getRules() {
    return rules;
  }

  public void setRules(List<Rule> rules) {
    this.rules = rules;
  }

  public Map<String, String> toMap() {
    return this.rules.stream().collect(Collectors.toMap(Rule::getKey, Rule::getHtmlDesc));
  }

  List<Rule> rules;

  @Override
  public String toString() {
    return "RuleList{" + "rules=" + rules + '}';
  }
}
