package org.limmen.analyser.domain;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.limmen.analyser.model.MavenDependency;
import org.limmen.analyser.model.MavenProject;

public class ProjectAnalyser {

  public MavenProject analyse(Path pomFile, String companyName) {
    MavenXpp3Reader xpp3Reader = new MavenXpp3Reader();
    MavenProject project = new MavenProject();

    try {
      try (var reader = new FileReader(pomFile.toFile())) {

        var model = xpp3Reader.read(reader);

        project.setGroupId(model.getGroupId());
        project.setArtifactId(model.getArtifactId());
        project.setVersion(model.getVersion());
        project.setDescription(model.getDescription());
        project.setName(model.getName());

        var dependencies = model.getDependencyManagement().getDependencies();
        var properties = model.getProperties();

        dependencies.stream()
            .filter(dep -> dep.getGroupId().contains(companyName))
            .forEach(dep -> {

              MavenDependency dependency = new MavenDependency();
              dependency.setGroupId(dep.getGroupId());
              dependency.setArtifactId(dep.getArtifactId());
              dependency.setVersion(resolveProperties(dep.getVersion(), properties));

              project.getDependencies().add(dependency);
            });
      } catch (XmlPullParserException e) {
        // xml issue
      }
    } catch (IOException ioe) {
      // io issue
    }

    return project;
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
