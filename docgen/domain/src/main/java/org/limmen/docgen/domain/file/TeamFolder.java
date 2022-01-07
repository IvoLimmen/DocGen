package org.limmen.docgen.domain.file;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class TeamFolder extends AbstractFolder {

  public TeamFolder(String name) {
    super(name);
  }

  private Set<ProjectFolder> projectFolders = new HashSet<>();

  public Set<ProjectFolder> getProjectFolders() {
    return projectFolders;
  }

  public void setProjectFolders(Set<ProjectFolder> projectFolders) {
    this.projectFolders = projectFolders;
  }

  @Override
  public Set<Path> getFilteredFiles(Predicate<Path> predicate) {
    Set<Path> files = new HashSet<>();
    files.addAll(getFiles().stream()
        .filter(predicate)
        .toList());

    this.getProjectFolders().forEach(folder -> {
      files.addAll(folder.getFilteredFiles(predicate));
    });    
    
    return files;
  }  
}
