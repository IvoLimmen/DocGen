package org.limmen.docgen.domain;

import java.io.IOException;
import java.nio.file.Path;

public interface AsciiDocConverter {
  
  boolean canConvertFile(String extention);
  
  void convertToHtml(Path sourceFile, Path targetFile) throws IOException;

  void close();
}
