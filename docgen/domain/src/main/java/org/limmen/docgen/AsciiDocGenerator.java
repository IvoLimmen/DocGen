package org.limmen.docgen;

import org.limmen.docgen.domain.file.RootFolder;
import org.limmen.docgen.model.Config;

public interface AsciiDocGenerator {
  
  void initialise(Config config);
  
  void generate(RootFolder rootFolder);
}
