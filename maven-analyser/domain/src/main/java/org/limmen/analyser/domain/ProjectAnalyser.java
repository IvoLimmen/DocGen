package org.limmen.analyser.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Model;
import org.limmen.zenodotus.project.model.Dependency;
import org.limmen.zenodotus.project.model.Project;

public class ProjectAnalyser {

  public Project analyse(Model model, Project project, String companyName) {
    Project.Builder builder = Project.builder();

    builder.groupId(model.getGroupId());
    builder.artifactId(model.getArtifactId());
    builder.version(model.getVersion());
    builder.description(model.getDescription());
    builder.name(model.getName());

    List<Dependency> newDependencies = new ArrayList<>();

    if (model.getDependencyManagement() != null) {
      var dependencies = model.getDependencyManagement().getDependencies();

      analyseDependencies(companyName, model, newDependencies, dependencies);
    }
    if (model.getDependencies() != null) {
      var dependencies = model.getDependencies();

      analyseDependencies(companyName, model, newDependencies, dependencies);
    }

    builder.dependencies(newDependencies);

    return builder.build();
  }

  private void analyseDependencies(String companyName, Model model, List<Dependency> newDependencies,
      List<org.apache.maven.model.Dependency> dependencies) {
    dependencies.stream()
        .filter(dep -> dep.getGroupId().contains(companyName))
        .forEach(dep -> {

          Dependency dependency = new Dependency();
          dependency.setGroupId(dep.getGroupId());
          dependency.setArtifactId(dep.getArtifactId());
          if (dep.getVersion() != null) {
            dependency.setVersion(resolveProperties(dep.getVersion(), model));
          }
          newDependencies.add(dependency);
        });
  }

  private String resolveProperties(String version, Model model) {
    if (version.contains("${")) {
      var index = version.indexOf("${");
      var ref = version.substring(index + 2, version.indexOf("}"));

      if ("project.version".equals(ref)) {
        return model.getVersion();
      } else {
        return model.getProperties().get(ref).toString();
      }
    } else {
      return version;
    }
  }
}
