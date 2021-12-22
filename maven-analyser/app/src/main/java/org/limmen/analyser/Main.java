package org.limmen.analyser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.limmen.analyser.domain.ProjectAnalyser;
import org.limmen.zenodotus.project.model.Project;
import org.limmen.zenodotus.project.model.helper.Json;

import picocli.CommandLine;
import picocli.CommandLine.Option;

public class Main implements Callable<Integer> {

  @Option(names = { "-d",
      "--directory" }, required = true, description = "Directory that contains the pom.xml file to analyse and the project.json to fill")
  private Path file;

  @Option(names = { "-c",
      "--companyName" }, required = true, description = "The company name to search the dependencies of")
  private String companyName;

  @Option(names = { "-h", "--help", "-?", "-help" }, usageHelp = true, description = "Display this help and exit")
  private boolean help;

  @Override
  public Integer call() throws Exception {

    var projectAnalyser = new ProjectAnalyser();

    if (file == null) {
      file = Path.of(System.getProperty("user.dir"));
    }

    var project = loadProjectFile(Path.of(file.toString(), "project.json"));

    var updatedProject = projectAnalyser.analyse(
        Path.of(file.toString(), "pom.xml"),
        project,
        companyName);

    saveProject(updatedProject);

    return 0;
  }

  private Project loadProjectFile(Path projectfile) throws IOException {
    try {
      return Json.loadProject(projectfile);
    } catch (FileNotFoundException fnfe) {
      return Project.builder().build();
    }
  }

  private void saveProject(Project project) throws JsonProcessingException, IOException {
    Json.saveProject(project, Path.of(file.toString(), "project.json"));
  }

  public static void main(String[] args) throws Exception {
    System.exit(new CommandLine(new Main()).execute(args));
  }
}
