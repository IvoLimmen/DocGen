package org.limmen.docgen.plugins.api;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import org.limmen.docgen.domain.file.RootFolder;
import org.limmen.docgen.model.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginRunner implements AutoCloseable {

  private final static Logger log = LoggerFactory.getLogger(PluginRunner.class);

  private final List<AsciiDocGenerator> generator = new ArrayList<>();

  public PluginRunner(Config config) {
    log.info("Searching for asciidoc converters...");
    ServiceLoader<AsciiDocGenerator> serviceLoader = ServiceLoader.load(AsciiDocGenerator.class);
    for (AsciiDocGenerator p : serviceLoader) {
      log.info("Found: {}", p.getClass().getSimpleName());
      p.initialise(config);
      generator.add(p);
    }
  }

  public void run(RootFolder rootFolder) {
    this.generator.forEach(gen -> {
      gen.generate(rootFolder);
    });
  }

  @Override
  public void close() throws Exception {
    this.generator.forEach(gen -> {
      try {
        gen.close();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }
}
