package org.limmen.docgen.domain.file;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProjectFolder extends AbstractFolder {
  
  private Set<SupportFolder> supportFolders = new HashSet<>();

  public ProjectFolder(String name) {
    super(name);
  }

  public Set<SupportFolder> getSupportFolders() {
    return supportFolders;
  }

  public void setSupportFolders(Set<SupportFolder> supportFolders) {
    this.supportFolders = supportFolders;
  }

  @Override
  public Set<Path> getFilteredFiles(Predicate<Path> predicate) {
    Set<Path> files = new HashSet<>();
    files.addAll(getFiles().stream()
        .filter(predicate)
        .collect(Collectors.toSet()));

    this.getSupportFolders().forEach(folder -> {
      files.addAll(folder.getFilteredFiles(predicate));
    });    
    
    return files;
  }
}
