package org.limmen.analyser.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.junit.jupiter.api.Test;
import org.limmen.zenodotus.project.model.Project;

public class ProjectAnalyserTest {
  
  private ProjectAnalyser subject = new ProjectAnalyser();

  @Test
  void shouldOnlyExamineCompanyRelatedDependencies() {

    var model = new Model();
    model.setDependencyManagement(createDependencyManagement(List.of(
      createDependency("org.company.service1", "service1-model", "1.0.0"),
      createDependency("org.company.service2", "service2-persistance", "1.2.0"),
      createDependency("org.springframework.boot", "springboot-dummy-starter", "2.6.4")
    )));

    var project = Project.builder().build();

    var newProject = subject.analyse(model, project, "company");

    assertEquals(2, newProject.getDependencies().size());    
  }

  private Dependency createDependency(String groupId, String artifactId, String version) {
    Dependency dependency = new Dependency();
    dependency.setArtifactId(artifactId);
    dependency.setGroupId(groupId);
    dependency.setVersion(version);
    return dependency;
  }

  private DependencyManagement createDependencyManagement(List<Dependency> dependencies) {
    DependencyManagement dependencyManagement = new DependencyManagement();
    dependencyManagement.setDependencies(dependencies);
    return dependencyManagement;
  } 
}
