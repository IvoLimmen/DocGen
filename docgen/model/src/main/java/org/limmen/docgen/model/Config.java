package org.limmen.docgen.model;

import java.io.File;
import java.nio.file.Path;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Config {

  @JsonIgnore
  private String templateDirectory;

  private String sourceDirectory;

  private String targetDirectory;

  private Toggle tableOfContent;

  private Toggle numberedSections;

  private IndexGenerator indexGenerator;

  public Toggle getNumberedSections() {
    return numberedSections;
  }

  public Toggle getTableOfContent() {
    return tableOfContent;
  }

  public void setNumberedSections(Toggle numberedSections) {
    this.numberedSections = numberedSections;
  }

  public void setTableOfContent(Toggle tableOfContent) {
    this.tableOfContent = tableOfContent;
  }
  
  public Path getSourceDirectory() {
    return Path.of(sourceDirectory);
  }

  public Path getTargetDirectory() {
    return Path.of(targetDirectory);
  }

  public File getTemplateDirectory() {
    return new File(templateDirectory);
  }

  public void setSourceDirectory(String sourceDirectory) {
    this.sourceDirectory = sourceDirectory;
  }

  public void setTargetDirectory(String targetDirectory) {
    this.targetDirectory = targetDirectory;
  }

  public void setTemplateDirectory(String templateDirectory) {
    this.templateDirectory = templateDirectory;
  }

  public IndexGenerator getIndexGenerator() {
    return indexGenerator;
  }

  public void setIndexGenerator(IndexGenerator indexGenerator) {
    this.indexGenerator = indexGenerator;
  }
}
