package org.limmen.docgen.app;

import java.nio.file.Path;

import org.limmen.docgen.model.Config;
import org.limmen.docgen.model.helper.Json;

public class Main {

  private Main() throws Exception {
    String configDir = System.getenv("CONFIG_DIR");

    if (configDir == null) {
      configDir = Path.of(System.getProperty("user.dir"), "config").toString();
    }
    
    Config config = Json.load(Path.of(configDir, "docgen.json"));
    config.setTemplateDirectory(configDir);

    DocGen docGen = new DocGen();
    docGen.run(config);
  }

  public static void main(String[] args) throws Exception {
    new Main();
  }
}
