package org.limmen.analyser.model;

import java.util.ArrayList;
import java.util.List;

public class MavenProject {
  
  private String artifactId;

  private String groupId;

  private String version;
  
  private String name;

  private String description;

  private List<MavenDependency> dependencies = new ArrayList<>();

  public String getArtifactId() {
    return artifactId;
  }

  public void setArtifactId(String artifactId) {
    this.artifactId = artifactId;
  }

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<MavenDependency> getDependencies() {
    return dependencies;
  }

  public void setDependencies(List<MavenDependency> dependencies) {
    this.dependencies.clear();    
    this.dependencies.addAll(dependencies);
  }
}
