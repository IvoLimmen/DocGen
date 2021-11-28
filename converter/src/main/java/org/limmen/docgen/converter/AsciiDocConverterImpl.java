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
    asciidoctor.convertFile(sourceFile.toFile(), 
        Options.builder()
            .toFile(true)
            .backend("html5")
            .safe(SafeMode.UNSAFE)
            .build());
    Files.move(this.fileSystemHelper.changeExtention(sourceFile, ".adoc", ".html"), targetFile, StandardCopyOption.REPLACE_EXISTING);
  }
}
