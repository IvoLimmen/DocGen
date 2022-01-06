package org.limmen.docgen.domain;

import java.io.IOException;
import java.nio.file.Path;

public interface AsciiDocConverter {
  
  boolean canConvertFile(Path file);
  
  void convertToHtml(Path sourceFile, Path targetFile) throws IOException;

  void close();
}
