package org.limmen.docgen.app;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.limmen.docgen.domain.AsciiDocConverter;

public class FileFinderVisitor implements FileVisitor<Path> {

  private List<Path> files = new ArrayList<>();
  
  private AsciiDocConverter asciiDocConverter;

  public FileFinderVisitor(AsciiDocConverter asciiDocConverter) {
    this.asciiDocConverter = asciiDocConverter;
  }

  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    if (asciiDocConverter.canConvertFile(file)) {
      this.files.add(file);
    }
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  public List<Path> getFiles() {
    return files;
  }
}
