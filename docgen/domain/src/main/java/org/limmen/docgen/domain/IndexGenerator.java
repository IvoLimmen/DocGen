package org.limmen.docgen.domain;

import java.io.IOException;
import java.nio.file.Path;

import org.limmen.docgen.domain.index.SearchIndexGenerator;

public interface IndexGenerator {
  SearchIndexGenerator getSearchIndexGenerator();
  void addNewLink(Path targetFile);
  void generate() throws IOException;  
}
