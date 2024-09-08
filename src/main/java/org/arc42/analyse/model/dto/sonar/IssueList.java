package org.arc42.analyse.model.dto.sonar;

import java.util.List;

public class IssueList {

  private List<Issue> issues;

  public List<Issue> getIssues() {
    return issues;
  }

  public void setIssues(List<Issue> issues) {
    this.issues = issues;
  }

  public IssueList(List<Issue> issues) {
    this.issues = issues;
  }

  public List<String> getKeys() {
    return issues.stream().map(Issue::getKey).toList();
  }
}
