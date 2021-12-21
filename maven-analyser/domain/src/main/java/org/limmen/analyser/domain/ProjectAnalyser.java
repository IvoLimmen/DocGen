package org.limmen.analyser.domain;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.limmen.zenodotus.project.model.Dependency;
import org.limmen.zenodotus.project.model.Project;

public class ProjectAnalyser {

  public Project analyse(Path pomFile, Project project, String companyName) {
    MavenXpp3Reader xpp3Reader = new MavenXpp3Reader();
    Project.Builder builder = Project.builder();

    try {
      try (var reader = new FileReader(pomFile.toFile())) {

        var model = xpp3Reader.read(reader);

        builder.groupId(model.getGroupId());
        builder.artifactId(model.getArtifactId());
        builder.version(model.getVersion());
        builder.description(model.getDescription());
        builder.name(model.getName());

        if (model.getDependencyManagement() != null) {
          List<Dependency> newDependencies = new ArrayList<>();
          var dependencies = model.getDependencyManagement().getDependencies();
          var properties = model.getProperties();

          dependencies.stream()
              .filter(dep -> dep.getGroupId().contains(companyName))
              .forEach(dep -> {

                Dependency dependency = new Dependency();
                dependency.setGroupId(dep.getGroupId());
                dependency.setArtifactId(dep.getArtifactId());
                dependency.setVersion(resolveProperties(dep.getVersion(), properties));
                newDependencies.add(dependency);
              });
          builder.dependencies(newDependencies);
        }

      } catch (XmlPullParserException e) {
        // xml issue
      }
    } catch (IOException ioe) {
      // io issue
    }

    return builder.build();
  }

  private String resolveProperties(String version, Properties properties) {
    if (version.contains("${")) {
      var index = version.indexOf("${");
      var ref = version.substring(index + 2, version.indexOf("}"));

      return properties.get(ref).toString();
    } else {
      return version;
    }
  }
}
