package org.limmen.docgen.domain;

import java.io.IOException;
import java.nio.file.Path;

public interface IndexGenerator {
  void addNewLink(Path targetFile);
  void generate() throws IOException;  
}
