package org.limmen.docgen.converter;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.limmen.docgen.domain.ProjectOverviewGenerator;
import org.limmen.docgen.model.Config;
import org.limmen.zenodotus.project.model.Project;
import org.limmen.zenodotus.project.model.helper.Json;

public class AsciiDocProjectOverviewGenerator implements ProjectOverviewGenerator {

  private Config config;

  public AsciiDocProjectOverviewGenerator(Config config) {
    this.config = config;
  }

  /**
   * 
   * @param files project JSON files
   */
  @Override
  public void generate(List<Path> files) {
    var filename = Path.of(config.getTargetDirectory().toString(), PROJECT_OVERVIEW_FILENAME);
    var dags = files.stream().map(this::loadProject).flatMap(this::loadDependencies).collect(Collectors.joining("\n"));
    try {
      Files.writeString(filename, createDocument(dags));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private CharSequence createDocument(String dags) {

    var header = """
        [graphviz, target="flow", format="png"]
        ----
        digraph flow {
          size ="8.5, 11";
        """;

    var footer = """
        }
        ----
        """;
    return String.join("\n", header, dags, footer);
  }

  private Stream<String> loadDependencies(Project project) {
    String projectName = sanitizeName(project.getArtifactId());
    return project.getDependencies().stream()
        .map(dependency -> String.format("%s -> %s", projectName, sanitizeName(dependency.getArtifactId())));
  }

  private String sanitizeName(String projectName) {
    return projectName.replace("-", "_");
  }

  private Project loadProject(Path file) {
    try {
      return Json.loadProject(file);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
