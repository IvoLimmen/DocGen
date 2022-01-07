package org.limmen.docgen.domain;

import java.io.IOException;
import java.nio.file.Path;

public interface SwaggerAsciiDocGenerator {
  
  boolean canConvertFile(Path file);
  
  void generate(Path inputFile, Path outputDir) throws IOException;
}
