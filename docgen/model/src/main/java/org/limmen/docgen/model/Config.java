package org.limmen.docgen.model;

import java.io.File;
import java.nio.file.Path;

public class Config {

  private String templateDirectory;

  private String sourceDirectory;

  private String targetDirectory;

  private IndexGenerator indexGenerator;

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

  @Override
  public String toString() {
    return "Config [indexGenerator=" + indexGenerator + ", sourceDirectory=" + sourceDirectory + ", targetDirectory="
        + targetDirectory + ", templateDirectory=" + templateDirectory + "]";
  }
}
