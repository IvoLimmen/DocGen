package org.limmen.docgen.plugin.projectoverview;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.limmen.docgen.asciidoc.AsciiDoc;
import org.limmen.docgen.domain.file.RootFolder;
import org.limmen.docgen.model.Config;
import org.limmen.docgen.plugins.api.AsciiDocGenerator;
import org.limmen.zenodotus.project.model.Project;
import org.limmen.zenodotus.project.model.helper.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsciiDocProjectOverviewGenerator implements AsciiDocGenerator {

  private final static Logger log = LoggerFactory.getLogger(AsciiDocProjectOverviewGenerator.class);
  
  private Config config;

  private RootFolder rootFolder;

  private String PROJECT_DEPENDENCIES_FILENAME = "dependencies.adoc";

  private String PROJECT_OVERVIEW_FILENAME = "project";

  public AsciiDocProjectOverviewGenerator() {
  }

  @Override
  public void initialise(Config config) {
    this.config = config;
  }

  @Override
  public void generate(RootFolder rootFolder) {
    this.rootFolder = rootFolder;
    generate(rootFolder.getFilteredFiles(file -> file.getFileName().toString().equals(PROJECT_OVERVIEW_FILENAME + ".json")));
  }

  private void createdFile(String fileName) {
    rootFolder.getFiles().add(Path.of(config.getSourceDirectory().toString(), fileName));
  }

  /**
   * @param files project JSON files
   */
  private void generate(Set<Path> files) {
    var projects = files.stream()
        .map(this::loadProject)
        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

    generateOverviewDocument(projects);
    generateDependencyOverviewPerProject(projects);
  }

  private void generateDependencyOverviewPerProject(Map<Path, Project> files) {
    files.forEach((path, project) -> {
      var filename = path.resolveSibling(PROJECT_DEPENDENCIES_FILENAME);
      createProjectDependenciesDocument(filename, project, files.values());
    });
  }

  private void generateOverviewDocument(Map<Path, Project> files) {
    log.info("Generating project overview document...");
    var filename = Path.of(config.getSourceDirectory().toString(), PROJECT_OVERVIEW_FILENAME + ".adoc");
    try {
      Files.writeString(filename, createProjectOverviewDocument(files.values()));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    createdFile(PROJECT_OVERVIEW_FILENAME + ".adoc");
  }

  private void createProjectDependenciesDocument(Path file, Project project, Collection<Project> others) {
    try (var printStream = new PrintStream(file.toFile())) {

      var adoc = new AsciiDoc(printStream);

      adoc.section1("Project dependencies overview");
      
      adoc.section2(project.getName() + " depends on");
      adoc.tableHeader("2,2,1", "|GroupId|ArtifactId|Version");
      project.getDependencies().forEach(dep -> {
        adoc.tableCell(dep.getGroupId());
        adoc.tableCell(dep.getArtifactId());
        adoc.tableCell(dep.getVersion());
        adoc.tableEndRow();
      });
      adoc.tableEnd();

      var projectsThatDepend = others.stream().filter(prj -> prj.hasDependencyOn(project.getGroupId(), project.getArtifactId())).toList();
      if (projectsThatDepend.size() > 0) {

        adoc.section2(project.getName() + " other projects depend on");

        adoc.tableHeader("2,2,1,1", "|GroupId|ArtifactId|Version|Uses version");
        projectsThatDepend.stream().forEach(prj -> {
          adoc.tableCell(prj.getGroupId());
          adoc.tableCell(prj.getArtifactId());
          adoc.tableCell(prj.getVersion());
          adoc.tableCell(prj.getDependencyVersion(project.getGroupId(), project.getArtifactId()).orElse("?"));
          adoc.tableEndRow();
        });
        adoc.tableEnd();
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    createdFile(PROJECT_DEPENDENCIES_FILENAME);
  } 

  private String createProjectOverviewDocument(Collection<Project> projects) {

    var document = """
        # Project overview documentation

        ## Architecture overview

        The following diagram is generated based on found dependencies focused on internal groupId's.

        """;

    return String.join("\n", document, createGraph(projects));
  }

  private String createGraph(Collection<Project> projects) {

    var graphHeader = """
        .Architecture diagram
        [graphviz, target="architecture", format="png"]
        ----
        digraph flow {
          size ="8.5, 11";
        """;

    var graphFooter = """
        }
        ----
        """;


    var graphLabels = projects.stream().flatMap(this::formatLabels).collect(Collectors.joining("\n"));

    var graphDags = projects.stream().flatMap(this::formatDependencies).collect(Collectors.joining("\n"));

    return String.join("\n", graphHeader, graphLabels, graphDags, graphFooter);
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

  private Map.Entry<Path, Project> loadProject(Path file) {
    try {
      return Map.entry(file, Json.loadProject(file));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public void close() throws Exception {
  }
}
