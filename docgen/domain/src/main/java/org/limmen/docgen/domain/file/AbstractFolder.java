package org.limmen.docgen.domain.file;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractFolder {
  
  private String name;

  private Set<Path> files = new HashSet<>();

  public AbstractFolder(String name) {
    this.name = name;
  }

  public Set<Path> getFiles() {
    return files;
  }

  public void setFiles(Set<Path> files) {
    this.files = files;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Path> getFilteredFiles(Predicate<Path> predicate) {
    return this.files.stream()
        .filter(predicate)
        .collect(Collectors.toSet());
  }
}
