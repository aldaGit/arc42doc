package org.arc42.analyse.model.dto;

import java.util.Date;

public class GitHubSecurityAdvisoryDTO {
  private String id;
  private Date published;
  private Date updated;
  private String title;
  private String category;
  private String content;

  public GitHubSecurityAdvisoryDTO(
      String id, Date published, Date updated, String title, String category, String content) {
    this.id = id;
    this.published = published;
    this.updated = updated;
    this.title = title;
    this.category = category;
    this.content = content;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Date getPublished() {
    return published;
  }

  public void setPublished(Date published) {
    this.published = published;
  }

  public Date getUpdated() {
    return updated;
  }

  public void setUpdated(Date updated) {
    this.updated = updated;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return "GitHubSecurityAdvisoryDTO{"
        + "id='"
        + id
        + '\''
        + ", published="
        + published
        + ", updated="
        + updated
        + ", title='"
        + title
        + '\''
        + ", category='"
        + category
        + '\''
        + ", content='"
        + content
        + '\''
        + '}';
  }
}
