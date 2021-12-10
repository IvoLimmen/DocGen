package org.limmen.docgen.indexer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.codec.language.Soundex;
import org.limmen.docgen.domain.FileSystemHelper;
import org.limmen.docgen.domain.IndexGenerator;
import org.limmen.docgen.domain.IndexItem;
import org.limmen.docgen.domain.IndexLink;
import org.limmen.docgen.domain.IndexNode;
import org.limmen.docgen.model.Config;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModelException;

public class IndexGeneratorImpl implements IndexGenerator {

  private Soundex soundex = new Soundex();
  private Map<String, List<IndexItem>> indexes = new HashMap<>();
  private Tokenizer tokenizer;
  private FileSystemHelper fileSystemHelper;
  private Config config;
  private Set<Path> files = new TreeSet<>();

  public IndexGeneratorImpl(Config config, FileSystemHelper fileSystemHelper, Tokenizer tokenizer) {
    this.fileSystemHelper = fileSystemHelper;
    this.config = config;
    this.tokenizer = tokenizer;
  }

  @Override
  public void addIndexNode(IndexNode indexNode) {
    if (!this.config.getIndexGenerator().isIncludeSearch()) {
      return;
    } 

    var keywords = tokenizer.tokenize(indexNode.getRawText());
    
    List<IndexItem> indexItems = keywords.stream()
        .distinct()
        .filter(k -> k != null && k.length() > 0)
        .map(k -> createIndexItem(k, indexNode.getLinkPart(), indexNode.getSectionName(), indexNode.getTargetFile()))
        .toList();
        
    indexItems.forEach(indexItem -> {
      String key = indexItem.getKeyword().substring(0, 1);
      if (indexes.containsKey(key)) {
        List<IndexItem> list = indexes.get(key);
        
        if (list.contains(indexItem)) {
          list.get(list.indexOf(indexItem)).getLinks().addAll(indexItem.getLinks());
        } else {
          list.add(indexItem);
        }
      } else {
        List<IndexItem> list = new ArrayList<>();
        list.add(indexItem);
        indexes.put(key, list);
      }
    });    
  }

  private IndexItem createIndexItem(String keyword, String link, String section, Path targetFile) {
    return IndexItem.builder()
        .addLinks(IndexLink.builder()
            .link(link)
            .sectionName(section)
            .targetFile(fileSystemHelper.toTargetPathFromRoot(targetFile).toString())
            .build())
        .keyword(keyword)
        .soundex(soundex.encode(keyword))
        .build();
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

    if (this.config.getIndexGenerator().isIncludeSearch()) {
      ObjectMapper objectMapper = new ObjectMapper();
      for (var entry : getIndexes().entrySet()) {
        var file = Path.of(this.config.getTargetDirectory().toString(), "data", entry.getKey() + ".json");
        Files.createDirectories(file.getParent());
        Files.writeString(file, objectMapper.writeValueAsString(entry.getValue()));
      }
    } 
  }

  public Map<String, List<IndexItem>> getIndexes() {
    return indexes;
  }
}
