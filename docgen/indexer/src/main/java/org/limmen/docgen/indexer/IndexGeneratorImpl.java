package org.limmen.docgen.indexer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;


import org.limmen.docgen.domain.FileSystemHelper;
import org.limmen.docgen.domain.IndexGenerator;
import org.limmen.docgen.domain.index.SearchIndexGenerator;
import org.limmen.docgen.model.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModelException;

public class IndexGeneratorImpl implements IndexGenerator {

  private final static Logger log = LoggerFactory.getLogger(IndexGeneratorImpl.class);
  
  private SearchIndexGenerator searchIndexGenerator;
  private FileSystemHelper fileSystemHelper;
  private Config config;
  private Set<Path> files = new TreeSet<>();

  public IndexGeneratorImpl(Config config, FileSystemHelper fileSystemHelper, SearchIndexGenerator searchIndexGenerator) {
    this.fileSystemHelper = fileSystemHelper;
    this.config = config;
    this.searchIndexGenerator = searchIndexGenerator;
  }

  @Override
  public void addNewLink(Path targetFile) {
    var file = fileSystemHelper.toTargetPathFromRoot(targetFile);
    this.files.add(file);
  }

  public Set<Path> getFiles() {
    return this.files;
  }

  public String toLink(String name) {
    return name.replace(' ', '_');
  }

  public List<String> getFoldersIndex(int index) {
    return this.files.stream()
        .map(file -> this.fileSystemHelper.getDirectory(file, index))
        .filter(Objects::nonNull)
        .distinct()
        .sorted()        
        .toList();
  }

  @Override
  public void generate() throws IOException {
    log.info("Generating indexfile for documentation site");

    Configuration cfg;
    try {
      cfg = new Configuration(Configuration.VERSION_2_3_29);
      cfg.setDirectoryForTemplateLoading(config.getTemplateDirectory());
      cfg.setDefaultEncoding("UTF-8");
      cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
      cfg.setLogTemplateExceptions(false);
      cfg.setWrapUncheckedExceptions(true);
      cfg.setFallbackOnNullLoopVariable(false);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    
    try {
      cfg.setSharedVariable("helper", this.fileSystemHelper);
      cfg.setSharedVariable("config", this.config.getIndexGenerator());
    } catch (TemplateModelException e) {
      e.printStackTrace();
    }
    var index = cfg.getTemplate("index.ftl");    
    var indexFile = Path.of(this.config.getTargetDirectory().toString(), "index.html");
    try (var writer = new FileWriter(indexFile.toFile())) {
      index.process(this, writer);
    }
    catch (TemplateException te) {
      te.printStackTrace();
    }

    searchIndexGenerator.generate();
  }

  @Override
  public SearchIndexGenerator getSearchIndexGenerator() {
    return this.searchIndexGenerator;
  }
}
