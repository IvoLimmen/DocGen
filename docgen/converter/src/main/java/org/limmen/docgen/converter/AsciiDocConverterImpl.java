package org.limmen.docgen.converter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Attributes;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.Options;
import org.asciidoctor.SafeMode;
import org.asciidoctor.ast.Block;
import org.asciidoctor.ast.Section;
import org.limmen.docgen.converter.helper.Slf4jLogHandler;
import org.limmen.docgen.domain.AsciiDocConverter;
import org.limmen.docgen.domain.FileSystemHelper;
import org.limmen.docgen.domain.IndexGenerator;
import org.limmen.docgen.domain.IndexNode;
import org.limmen.docgen.model.Config;
import org.limmen.docgen.model.value.TocValue;
import org.limmen.docgen.model.value.ToggleValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsciiDocConverterImpl implements AsciiDocConverter {

  private final static Logger log = LoggerFactory.getLogger(AsciiDocConverter.class);
  
  private Asciidoctor asciidoctor = Asciidoctor.Factory.create();

  private Options options;

  private FileSystemHelper fileSystemHelper;

  private IndexGenerator indexGenerator;

  private final List<String> supportedFiles = List.of("json", "yml", "yaml", "adoc");

  public AsciiDocConverterImpl(Config config, FileSystemHelper fileSystemHelper, IndexGenerator indexGenerator) {
    this.fileSystemHelper =fileSystemHelper;
    this.indexGenerator = indexGenerator;

    asciidoctor.requireLibrary("asciidoctor-diagram");
    asciidoctor.registerLogHandler(new Slf4jLogHandler());

    this.options = Options.builder()
        .toFile(true)
        .backend("html5")
        .safe(SafeMode.UNSAFE)
        .attributes(attributes(config))
        .build();
  }

  private Attributes attributes(Config config) {
    AttributesBuilder builder = Attributes.builder();

    if (config.getNumberedSections() == ToggleValue.ON) {
      builder.arguments("sectnums");
    } else if (config.getNumberedSections() == ToggleValue.OFF) {
      builder.arguments("!sectnums");
    }

    if (config.getTableOfContent() != TocValue.AS_DEFINED) {
      if (config.getTableOfContent() == TocValue.OFF) {
        builder.arguments("!toc");
      } else {
        builder.attribute("toc", config.getTableOfContent().name().toLowerCase());
      }
    }

    builder.docType("book");
    
    return builder.build();
  }

  @Override
  public boolean canConvertFile(String extention) {
    return supportedFiles.contains(extention);
  }

  @Override
  public void convertToHtml(Path sourceFile, Path targetFile) throws IOException {
    if (sourceFile.toString().endsWith("json") || sourceFile.toString().endsWith("yml") || sourceFile.toString().endsWith("yaml")) {      
      convertSwagger3FileToAsciiDoc(sourceFile, targetFile.getParent());
      var targetAdoc = this.fileSystemHelper.changeExtention(targetFile, ".adoc");
      analyzerAsciiDocForSearchIndex(targetAdoc, targetAdoc);
      convertAsciiDocToHtml(targetAdoc, targetFile);
    }
    if (sourceFile.toString().endsWith("adoc")) {
      analyzerAsciiDocForSearchIndex(sourceFile, targetFile);
      convertAsciiDocToHtml(sourceFile, targetFile);
    }
  }

  private void convertSwagger3FileToAsciiDoc(Path sourceFile, Path targetPath) throws IOException {
    log.info("Convert {} to AsciiDoc (assuming Swagger file)", sourceFile);
    AsciiDocGenerator generator = new AsciiDocGenerator();    
    generator.generate(sourceFile, targetPath);
  }

  private void analyzerAsciiDocForSearchIndex(Path sourceFile, Path targetFile) throws IOException {
    log.info("Analyze {} for indexing", sourceFile);

    var document = asciidoctor.loadFile(
      sourceFile.toFile(), 
      Options.builder()
          .safe(SafeMode.UNSAFE)          
          .build()); 
      
    document.getBlocks().forEach(item -> {
      if (item instanceof Section section) {
        analyzeSection(section, targetFile);
      }
    });
  }

  private void analyzeSection(Section section, Path targetFile) {
    section.getBlocks().forEach(item -> {
      if (item instanceof Section subSection) {
        analyzeSection(subSection, targetFile);
      } else if (item instanceof Block block) {
        analyzeBlock(section, block, targetFile);
      } 
    });
    section.findBy(Map.of("context", ":paragraph")).forEach(sectionItem -> {
      if (sectionItem instanceof Block block) {
        analyzeBlock(section, block, targetFile);
      }
    });        
  }

  private void analyzeBlock(Section section, Block block, Path targetFile) {
    var text = block.getLines().stream().collect(Collectors.joining(" "));

    this.indexGenerator.getSearchIndexGenerator().addIndexNode(IndexNode.builder()
        .linkPart(section.getId())
        .sectionName(section.getTitle())
        .rawText(section.getTitle() + " " + text)
        .targetFile(targetFile)
        .build());
  }

  private void convertAsciiDocToHtml(Path sourceFile, Path targetFile) throws IOException {
    log.info("Convert {} to HTML", sourceFile);

    asciidoctor.convertFile(sourceFile.toFile(), this.options);
    Files.move(this.fileSystemHelper.changeExtention(sourceFile, ".html"), targetFile, StandardCopyOption.REPLACE_EXISTING);
  }

  @Override
  public void close() {
    asciidoctor.close();
  }
}
