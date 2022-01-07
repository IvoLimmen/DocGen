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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsciiDocProjectOverviewGenerator implements ProjectOverviewGenerator {

  private final static Logger log = LoggerFactory.getLogger(AsciiDocProjectOverviewGenerator.class);
  
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
    log.info("Generating project overview document...");
    var filename = Path.of(config.getSourceDirectory().toString(), PROJECT_OVERVIEW_FILENAME);
    var projects = files.stream().map(this::loadProject).toList();
    try {
      Files.writeString(filename, createDocument(projects));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private CharSequence createDocument(List<Project> projects) {

    var document = """
        # Project overview documentation

        ## Architecture overview

        The following diagram is generated based on found dependencies focused on internal groupId's.

        """;

    var header = """
        .Architecture diagram
        [graphviz, target="architecture", format="png"]
        ----
        digraph flow {
          size ="8.5, 11";
        """;

    var footer = """
        }
        ----
        """;


    var labels = projects.stream().flatMap(this::formatLabels).collect(Collectors.joining("\n"));

    var dags = projects.stream().flatMap(this::formatDependencies).collect(Collectors.joining("\n"));

    return String.join("\n", document, header, labels, dags, footer);
  }

  private Stream<String> formatLabels(Project project) {
    String projectName = sanitizeName(project.getArtifactId());
    return Stream.of(String.format("%s [shape=record fontname=Arial label=\"%s\"];", projectName, project.getArtifactId()));
  }

  private Stream<String> formatDependencies(Project project) {
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
