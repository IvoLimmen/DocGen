package org.limmen.docgen.domain;

import java.io.IOException;
import java.nio.file.Path;

public interface AsciiDocConverter {
  
  void convertToHtml(Path sourceFile, Path targetFile) throws IOException;
}
