package org.limmen.docgen.domain.index;

import java.io.IOException;

public interface SearchIndexGenerator {
  void addIndexNode(IndexNode indexNode);
  void generate() throws IOException;    
}
