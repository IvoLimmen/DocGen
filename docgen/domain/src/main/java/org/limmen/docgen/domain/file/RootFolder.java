package org.limmen.docgen.domain.file;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RootFolder extends AbstractFolder {
  
  private Set<TeamFolder> teamFolders = new HashSet<>();

  public RootFolder(String name) {
    super(name);
  }

  public Set<TeamFolder> getTeamFolders() {
    return teamFolders;
  }

  public void setTeamFolders(Set<TeamFolder> teamFolders) {
    this.teamFolders = teamFolders;
  }

  public Set<Path> getProjectFiles() {
    return getFilteredFiles(file -> file.getFileName().toString().equals("project.json"));
  }

  public Set<Path> getAsciiDocFiles() {
    return getFilteredFiles(file -> file.getFileName().toString().endsWith(".adoc"));
  }

  @Override
  public Set<Path> getFilteredFiles(Predicate<Path> predicate) {
    Set<Path> files = new HashSet<>();
    files.addAll(getFiles().stream()
        .filter(predicate)
        .collect(Collectors.toSet()));

    this.getTeamFolders().forEach(folder -> {
      files.addAll(folder.getFilteredFiles(predicate));
    });    
    
    return files;
  }
}
