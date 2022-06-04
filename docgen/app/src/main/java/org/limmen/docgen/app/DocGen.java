package org.limmen.docgen.app;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.limmen.docgen.Constants;
import org.limmen.docgen.converter.AsciiDocConverterImpl;
import org.limmen.docgen.converter.SwaggerAsciiDocGeneratorImpl;
import org.limmen.docgen.domain.AsciiDocConverter;
import org.limmen.docgen.domain.FileSystemHelper;
import org.limmen.docgen.domain.IndexGenerator;
import org.limmen.docgen.domain.SwaggerAsciiDocGenerator;
import org.limmen.docgen.domain.file.RootFolder;
import org.limmen.docgen.domain.index.SearchIndexGenerator;
import org.limmen.docgen.indexer.IndexGeneratorImpl;
import org.limmen.docgen.indexer.SearchIndexGeneratorImpl;
import org.limmen.docgen.indexer.Tokenizer;
import org.limmen.docgen.model.Config;
import org.limmen.docgen.plugins.api.PluginRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocGen {

  private final static Logger log = LoggerFactory.getLogger(DocGen.class);

  private FileSystemHelper fileSystemHelper;
  private IndexGenerator indexGenerator;
  private SearchIndexGenerator searchIndexGenerator;
  private SwaggerAsciiDocGenerator swaggerAsciiDocGenerator;
  private Tokenizer tokenizer;
  private AsciiDocConverter asciiDocConverter;
  private PluginRunner pluginRunner;

  public void run(Config config) throws Exception {
    initialize(config);
    mainProcess(config);
  }

  private void initialize(Config config) {
    this.fileSystemHelper = new FileSystemHelper(config);
    this.tokenizer = new Tokenizer();
    this.searchIndexGenerator = new SearchIndexGeneratorImpl(config, fileSystemHelper, tokenizer);
    this.indexGenerator = new IndexGeneratorImpl(config, fileSystemHelper, searchIndexGenerator);
    this.asciiDocConverter = new AsciiDocConverterImpl(config, fileSystemHelper, indexGenerator);
    this.swaggerAsciiDocGenerator = new SwaggerAsciiDocGeneratorImpl(fileSystemHelper);
    this.pluginRunner = new PluginRunner(config);
  }

  private void logPhase(String phase) {
    String text = " < " + phase + " > ";
    int addition = (Constants.CONSOLE_WITH - text.length()) / 2;
    int plus = Constants.CONSOLE_WITH - ((addition * 2) + text.length());
    String line = "-".repeat(addition) + text + "-".repeat(addition) + "-".repeat(plus);

    log.info(line);
  }

  private void mainProcess(Config config) throws Exception {
    var fileScanner = new FileScanner(config);

    phaseGenerateFiles(fileScanner.getRootFolder());
    phaseGenerateAsciiDoc(fileScanner.getRootFolder());
    phaseCopySupportFiles(config);

    this.indexGenerator.generate();
    this.pluginRunner.close();
  }

  private void phaseGenerateFiles(RootFolder rootFolder) {
    logPhase("Generate files");
    this.pluginRunner.run(rootFolder);
    rootFolder.getFilteredFiles(swaggerAsciiDocGenerator::canConvertFile)
        .forEach(this::convertSwaggerToAsciiDoc);
  }

  private void phaseGenerateAsciiDoc(RootFolder rootFolder) {
    logPhase("Generate AsciiDoc");

    List<Path> includedFiles = new ArrayList<>();
    rootFolder.getAsciiDocFiles().forEach(file -> {
      findIncludes(file).forEach(i -> {
        includedFiles.add(Path.of(file.getParent().toString(), i));
      });
    });

    rootFolder.getAsciiDocFiles().stream()
        .filter(file -> !includedFiles.contains(file))
        .forEach(this::convertAsciiDocToHtml);
  }

  private void phaseCopySupportFiles(Config config) throws IOException {
    logPhase("Copy supporting files");

    var supportFileVisitor = new SupportFileVisitor(fileSystemHelper);
    Files.walkFileTree(config.getSourceDirectory(), supportFileVisitor);
  }

  private void convertSwaggerToAsciiDoc(Path file) {
    try {
      var targetFile = this.fileSystemHelper.changeExtention(file, ".adoc");
      swaggerAsciiDocGenerator.generate(file, targetFile);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void convertAsciiDocToHtml(Path file) {
    Path targetFile = this.fileSystemHelper.changeExtention(this.fileSystemHelper.toTargetPath(file), ".html");
    try {
      Files.createDirectories(targetFile.getParent());
      asciiDocConverter.convertToHtml(file, targetFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.indexGenerator.addNewLink(targetFile);
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
