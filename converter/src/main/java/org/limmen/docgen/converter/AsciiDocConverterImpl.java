package org.limmen.docgen.converter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.SafeMode;
import org.limmen.docgen.domain.AsciiDocConverter;
import org.limmen.docgen.domain.FileSystemHelper;

public class AsciiDocConverterImpl implements AsciiDocConverter {

  private Asciidoctor asciidoctor = Asciidoctor.Factory.create();

  private FileSystemHelper fileSystemHelper;

  public AsciiDocConverterImpl(FileSystemHelper fileSystemHelper) {
    this.fileSystemHelper =fileSystemHelper;
  }

  @Override
  public void convertToHtml(Path sourceFile, Path targetFile) throws IOException {
    if (sourceFile.toString().endsWith("json") || sourceFile.toString().endsWith("yml") || sourceFile.toString().endsWith("yaml")) {
      convertSwaggerFileToHtml(sourceFile);
      convertAsciiDocToHtml(this.fileSystemHelper.changeExtention(sourceFile, ".adoc"), targetFile);
    }
    if (sourceFile.toString().endsWith("adoc")) {
      convertAsciiDocToHtml(sourceFile, targetFile);
    }
  }

  private void convertSwaggerFileToHtml(Path sourceFile) throws IOException {

    Generator generator = new Generator();
    generator.generate(sourceFile, sourceFile.getParent());
  }

  private void convertAsciiDocToHtml(Path sourceFile, Path targetFile) throws IOException {
    asciidoctor.convertFile(sourceFile.toFile(), 
        Options.builder()
            .toFile(true)
            .backend("html5")
            .safe(SafeMode.UNSAFE)
            .build());
    Files.move(this.fileSystemHelper.changeExtention(sourceFile, ".html"), targetFile, StandardCopyOption.REPLACE_EXISTING);
  }
}
