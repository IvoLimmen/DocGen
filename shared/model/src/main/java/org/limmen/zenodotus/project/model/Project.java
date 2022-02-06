package org.limmen.zenodotus.project.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = Project.Builder.class)
public class Project {

  private String artifactId;
  private String groupId;
  private String version;
  private String name;
  private String description;
  private String email;
  private List<Dependency> dependencies;

  public static Builder builder() {
    return new Builder();
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private String artifactId;
    private String groupId;
    private String version;
    private String name;
    private String description;
    private String email;
    private List<Dependency> dependencies = new ArrayList<>();

    public Builder() {
    }

    Builder(String artifactId, String groupId, String version, String name, String description, String email,
        List<Dependency> dependencies) {
      this.artifactId = artifactId;
      this.groupId = groupId;
      this.version = version;
      this.name = name;
      this.description = description;
      this.email = email;
      this.dependencies.clear();
      this.dependencies.addAll(dependencies);
    }

    public Builder from(Project project) {
      this.artifactId = project.artifactId;
      this.groupId = project.groupId;
      this.version = project.version;
      this.name = project.name;
      this.description = project.description;
      this.email = project.email;
      this.dependencies.clear();
      this.dependencies.addAll(project.dependencies);
      return Builder.this;
    }

    public Builder dependencies(List<Dependency> dependencies) {
      this.dependencies.clear();
      this.dependencies.addAll(dependencies);
      return Builder.this;
    }

    public Builder artifactId(String artifactId) {
      if (this.artifactId == null) {
        this.artifactId = artifactId;
      }
      return Builder.this;
    }

    public Builder groupId(String groupId) {
      if (this.groupId == null) {
        this.groupId = groupId;
      }
      return Builder.this;
    }

    public Builder version(String version) {
      if (this.version == null) {
        this.version = version;
      }
      return Builder.this;
    }

    public Builder name(String name) {
      if (this.name == null) {
        this.name = name;
      }
      return Builder.this;
    }

    public Builder description(String description) {
      if (this.description == null) {
        this.description = description;
      }
      return Builder.this;
    }

    public Builder email(String email) {
      if (this.email == null) {
        this.email = email;
      }
      return Builder.this;
    }

    public Project build() {
      return new Project(this);
    }
  }

  private Project(Builder builder) {
    this.artifactId = builder.artifactId;
    this.groupId = builder.groupId;
    this.version = builder.version;
    this.name = builder.name;
    this.description = builder.description;
    this.email = builder.email;
    this.dependencies = builder.dependencies;
  }

  public String getArtifactId() {
    return artifactId;
  }

  public String getDescription() {
    return description;
  }

  public String getEmail() {
    return email;
  }

  public String getGroupId() {
    return groupId;
  }

  public String getName() {
    return name;
  }

  public String getVersion() {
    return version;
  }

  public List<Dependency> getDependencies() {
    return dependencies;
  }

  public boolean hasDependencyOn(String groupId, String artifactId) {
    return this.dependencies.stream().anyMatch(dep -> 
      groupId.equalsIgnoreCase(dep.getGroupId()) 
      && artifactId.equalsIgnoreCase(dep.getArtifactId()));
  }

  public Optional<String> getDependencyVersion(String groupId, String artifactId) {
    return this.dependencies.stream()
        .filter(dep -> groupId.equalsIgnoreCase(dep.getGroupId()) && artifactId.equalsIgnoreCase(dep.getArtifactId()))
        .findFirst()
        .map(Dependency::getVersion);
  }
}