package org.limmen.docgen.plugins.api;

import org.limmen.docgen.domain.file.RootFolder;
import org.limmen.docgen.model.Config;

public interface AsciiDocGenerator extends AutoCloseable {
  
  void initialise(Config config);
  
  void generate(RootFolder rootFolder);
}
