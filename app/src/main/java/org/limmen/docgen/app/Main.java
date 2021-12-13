package org.limmen.docgen.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.limmen.docgen.converter.AsciiDocConverterImpl;
import org.limmen.docgen.domain.FileSystemHelper;
import org.limmen.docgen.domain.IndexGenerator;
import org.limmen.docgen.domain.SearchIndexGenerator;
import org.limmen.docgen.indexer.IndexGeneratorImpl;
import org.limmen.docgen.indexer.SearchIndexGeneratorImpl;
import org.limmen.docgen.indexer.Tokenizer;
import org.limmen.docgen.model.Config;
import org.limmen.docgen.model.helper.Json;

import ch.qos.logback.classic.ClassicConstants;

public class Main {
  
  private FileSystemHelper fileSystemHelper;
  private IndexGenerator indexGenerator;
  private SearchIndexGenerator searchIndexGenerator;
  private Tokenizer tokenizer;
  private Config config;

  private Main() throws IOException {
    System.setProperty(ClassicConstants.CONFIG_FILE_PROPERTY, Path.of(System.getProperty("user.dir"), "config", "logback.xml").toString());

    this.config = Json.load(Path.of(System.getProperty("user.dir"), "config", "docgen.json"));
    this.fileSystemHelper = new FileSystemHelper(config);
    this.tokenizer = new Tokenizer();

    this.searchIndexGenerator = new SearchIndexGeneratorImpl(config, fileSystemHelper, tokenizer);
    this.indexGenerator = new IndexGeneratorImpl(config, fileSystemHelper, searchIndexGenerator);

    this.walkThroughFiles();
  }

  public static void main(String[] args) throws IOException {
    new Main();
  }

  private void walkThroughFiles() throws IOException {
    var converter = new AsciiDocConverterImpl(fileSystemHelper, indexGenerator);
    var supportFileVisitor = new SupportFileVisitor(fileSystemHelper);
    var asciiDocfileVisitor = new AsciiDocFileVisitor(converter, indexGenerator, fileSystemHelper);
    
    Files.walkFileTree(config.getSourceDirectory(), asciiDocfileVisitor);

    Files.walkFileTree(config.getSourceDirectory(), supportFileVisitor);

    indexGenerator.generate();
  }
}
