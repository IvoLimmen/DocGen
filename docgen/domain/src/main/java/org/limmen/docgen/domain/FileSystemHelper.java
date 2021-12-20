package org.limmen.docgen.domain;

import java.nio.file.Path;

import org.apache.commons.text.WordUtils;
import org.limmen.docgen.model.Config;

public class FileSystemHelper {

  private Config config;

  public FileSystemHelper(Config config) {
    this.config = config;
  }

  public String toRelativeLink(Path file) {
    return file.toString().substring(1);
  }

  public String getExtention(Path file) {
    var name = file.getFileName().toString();       
    return name.substring(name.indexOf(".") + 1);
  }

  public String getFileName(Path file) {    
    var name = file.getFileName().toString();       
    return WordUtils.capitalize(name.substring(0, name.indexOf(".")));
  }

  public String getDirectory(Path file, int index) {
    if (index >= file.getNameCount()) {
      return "";
    }
    var part = file.getName(index);
    if (part.toString().contains(".")) {
      return "";
    }
    return file.getName(index).toString();
  }

  public String subPath(Path file, String source) {
    return file.toString().substring(source.length());
  }

  public Path toTargetPathFromRoot(Path file) {
    return Path.of(subPath(file, config.getTargetDirectory().toString()));
  }

  public Path toTargetPath(Path file) {
    return Path.of(
        config.getTargetDirectory().toString(),
        subPath(file, config.getSourceDirectory().toString()));
  }

  public Path changeExtention(Path file, String newExtention) {
    var path = file.getParent().toString();
    var name = file.getFileName().toString();       
    return Path.of(path, name.substring(0, name.indexOf(".")) + newExtention);
  }
}
