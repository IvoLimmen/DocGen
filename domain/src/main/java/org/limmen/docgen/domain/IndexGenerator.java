package org.limmen.docgen.domain;

import java.io.IOException;
import java.nio.file.Path;

public interface IndexGenerator {
  void addIndexNode(IndexNode indexNode);
  void addNewLink(Path targetFile);
  void generate() throws IOException;  
}
