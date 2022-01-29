package org.limmen.docgen.app;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

import org.limmen.docgen.domain.Constants;
import org.limmen.docgen.domain.FileSystemHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SupportFileVisitor implements FileVisitor<Path> {

  private final static Logger log = LoggerFactory.getLogger(SupportFileVisitor.class);
  
  private FileSystemHelper fileSystemHelper;

  public SupportFileVisitor(FileSystemHelper fileSystemHelper) {
    this.fileSystemHelper = fileSystemHelper;
  }

  @Override
  public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    String extention = this.fileSystemHelper.getExtention(file);
    if (Constants.SUPPORTED_EXTENTIONS.contains(extention) || Constants.SPECIAL_FILENAMES.contains(file.getFileName().toString())) {
      log.info("Copy support file {} to target directory", file);
      Path targetFile = this.fileSystemHelper.toTargetPath(file);
      Files.createDirectories(targetFile.getParent());
      Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
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
