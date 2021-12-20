package org.limmen.docgen.domain;

import java.io.IOException;

public interface SearchIndexGenerator {
  void addIndexNode(IndexNode indexNode);
  void generate() throws IOException;    
}
