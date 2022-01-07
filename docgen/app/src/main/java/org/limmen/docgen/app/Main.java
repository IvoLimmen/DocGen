package org.limmen.docgen.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.limmen.docgen.converter.AsciiDocConverterImpl;
import org.limmen.docgen.converter.AsciiDocProjectOverviewGenerator;
import org.limmen.docgen.domain.AsciiDocConverter;
import org.limmen.docgen.domain.FileSystemHelper;
import org.limmen.docgen.domain.IndexGenerator;
import org.limmen.docgen.domain.ProjectOverviewGenerator;
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
  private AsciiDocConverter asciiDocConverter;
  private Tokenizer tokenizer;
  private Config config;
  private ProjectOverviewGenerator projectOverviewGenerator;


  private Main() throws IOException {
    String configDir = System.getenv("CONFIG_DIR");

    if (configDir == null) {
      configDir = Path.of(System.getProperty("user.dir"), "config").toString();
    }

    System.setProperty(ClassicConstants.CONFIG_FILE_PROPERTY,
        Path.of(configDir, "logback.xml").toString());

    this.config = Json.load(Path.of(configDir, "docgen.json"));
    this.config.setTemplateDirectory(configDir);

    this.fileSystemHelper = new FileSystemHelper(config);
    this.tokenizer = new Tokenizer();
    this.searchIndexGenerator = new SearchIndexGeneratorImpl(config, fileSystemHelper, tokenizer);
    this.indexGenerator = new IndexGeneratorImpl(config, fileSystemHelper, searchIndexGenerator);
    this.asciiDocConverter = new AsciiDocConverterImpl(config, fileSystemHelper, indexGenerator);
    this.projectOverviewGenerator = new AsciiDocProjectOverviewGenerator(config);

    this.walkThroughFiles();

    this.asciiDocConverter.close();
  }

  public static void main(String[] args) throws IOException {
    new Main();
  }

  private List<Path> findFilesToConvert() throws IOException {
    var fileFinderVisitor = new FileFinderVisitor(this.asciiDocConverter);
    Files.walkFileTree(config.getSourceDirectory(), fileFinderVisitor);

    List<Path> includedFiles = new ArrayList<>();
    fileFinderVisitor.getAsciidocFiles().forEach(file -> {
      findIncludes(file).forEach(i -> {
        includedFiles.add(Path.of(file.getParent().toString(), i));
      });
    });

    projectOverviewGenerator.generate(fileFinderVisitor.getProjectFiles());
    var sourceFile = Path.of(config.getSourceDirectory().toString(), ProjectOverviewGenerator.PROJECT_OVERVIEW_FILENAME);
    convertFile(sourceFile);
    Files.delete(sourceFile);
    
    return fileFinderVisitor.getAsciidocFiles().stream()
        .filter(file -> !includedFiles.contains(file))
        .toList();
  }

  private void walkThroughFiles() throws IOException {
    var supportFileVisitor = new SupportFileVisitor(fileSystemHelper);

    findFilesToConvert().forEach(this::convertFile);

    Files.walkFileTree(config.getSourceDirectory(), supportFileVisitor);

    indexGenerator.generate();
  }

  private void convertFile(Path file) {
    if (asciiDocConverter.canConvertFile(file)) {
      Path targetFile = this.fileSystemHelper.changeExtention(this.fileSystemHelper.toTargetPath(file), ".html");
      try {
        Files.createDirectories(targetFile.getParent());
        asciiDocConverter.convertToHtml(file, targetFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
      this.indexGenerator.addNewLink(targetFile);
    } 
  }

  private List<String> findIncludes(Path file) {

    try {
      return Files.readAllLines(file).stream()
          .filter(line -> line.contains("include::"))
          .map(line -> {
            var filePart = line.trim().substring(9);
            return filePart.substring(0, filePart.indexOf("["));
          })
          .toList();
    } catch (IOException e) {
      return Collections.emptyList();
    }
  }
}
