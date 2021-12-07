package org.limmen.docgen.app;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import org.limmen.docgen.domain.AsciiDocConverter;
import org.limmen.docgen.domain.FileSystemHelper;
import org.limmen.docgen.domain.IndexGenerator;

public class AsciiDocFileVisitor implements FileVisitor<Path> {

  private AsciiDocConverter asciiDocConverter;
  private FileSystemHelper fileSystemHelper;
  private IndexGenerator indexer;

  public AsciiDocFileVisitor(AsciiDocConverter asciiDocConverter, IndexGenerator indexer, FileSystemHelper fileSystemHelper) {
    this.asciiDocConverter = asciiDocConverter;
    this.fileSystemHelper = fileSystemHelper;
    this.indexer = indexer;
  }

  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    String extention = this.fileSystemHelper.getExtention(file);
    if (extention.equals("adoc")) {
      Path targetFile = this.fileSystemHelper.changeExtention(this.fileSystemHelper.toTargetPath(file), ".html");
      Files.createDirectories(targetFile.getParent());
      this.asciiDocConverter.convertToHtml(file, targetFile);
      this.indexer.addNewLink(targetFile);  
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
}
