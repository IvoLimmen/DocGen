package org.limmen.docgen.indexer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.limmen.docgen.domain.FileSystemHelper;
import org.limmen.docgen.domain.Indexer;
import org.limmen.docgen.model.Config;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModelException;

public class IndexerImpl implements Indexer {

  private FileSystemHelper fileSystemHelper;
  private Config config;
  private Set<Path> files = new TreeSet<>();

  public IndexerImpl(Config config, FileSystemHelper fileSystemHelper) {
    this.fileSystemHelper = fileSystemHelper;
    this.config = config;
  }

  @Override
  public void addNewLink(Path targetFile) {
    this.files.add(fileSystemHelper.toTargetPathFromRoot(targetFile));
  }

  public Set<Path> getFiles() {
    return this.files;
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
  }
}
