package org.arc42.analyse.model.dto;

import java.util.List;

public class ComponentList {

  public ComponentList(List<Component> components) {
    this.components = components;
  }

  List<Component> components;

  public List<Component> getComponents() {
    return components;
  }

  public void setComponents(List<Component> components) {
    this.components = components;
  }

  public class Component {
    String key;
    String name;
    String qualifier;
    String project;

    public Component(String key, String name, String qualifier, String project) {
      this.key = key;
      this.name = name;
      this.qualifier = qualifier;
      this.project = project;
    }

    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getQualifier() {
      return qualifier;
    }

    public void setQualifier(String qualifier) {
      this.qualifier = qualifier;
    }

    public String getProject() {
      return project;
    }

    public void setProject(String project) {
      this.project = project;
    }
  }
}
