package org.limmen.docgen.indexer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.limmen.docgen.domain.FileSystemHelper;
import org.limmen.docgen.domain.IndexNode;
import org.limmen.docgen.model.Config;
import org.limmen.docgen.model.IndexGenerator;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SearchIndexGeneratorImplTest {

  private Config config = new Config();

  private FileSystemHelper fileSystemHelper = new FileSystemHelper(config);
  
  private Tokenizer tokenizer = new Tokenizer();

  private SearchIndexGeneratorImpl subject;

  @BeforeEach
  void init() {
    subject = new SearchIndexGeneratorImpl(config, fileSystemHelper, tokenizer);
  }

  @Test
  void shouldCreateOneIndex() {
    enableIndexing();
    
    subject.addIndexNode(IndexNode.builder()
        .rawText("bla bla")
        .sectionName("Section One")
        .linkPart("section_one")
        .targetFile(Path.of("/home/build/output/Team1/test.html"))
        .build());

    subject.addIndexNode(IndexNode.builder()
        .rawText("bla blaa")
        .sectionName("Section Two")
        .linkPart("section_two")
        .targetFile(Path.of("/home/build/output/Team2/test.html"))
        .build());

    // should be one index on B
    assertEquals(1, subject.getIndexes().size());   
    
    assertNotNull(subject.getIndexes().get("b")); 

    var result = subject.getIndexes().get("b");

    assertEquals(2, result.size());

    assertEquals("bla", result.get(0).getKeyword());
    assertEquals(2, result.get(0).getLinks().size());

    assertEquals("blaa", result.get(1).getKeyword());
    assertEquals(1, result.get(1).getLinks().size());
  }

  @Test
  void shouldCreateOneIndexEvenIfSectionsAreTheSame() {
    enableIndexing();
    
    subject.addIndexNode(IndexNode.builder()
        .rawText("bla bla")
        .sectionName("Section One")
        .linkPart("section_one")
        .targetFile(Path.of("/home/build/output/Team1/test.html"))
        .build());

    subject.addIndexNode(IndexNode.builder()
        .rawText("bla blaa")
        .sectionName("Section One")
        .linkPart("section_one")
        .targetFile(Path.of("/home/build/output/Team2/test.html"))
        .build());

    // should be one index on B
    assertEquals(1, subject.getIndexes().size());   
    
    assertNotNull(subject.getIndexes().get("b")); 

    var result = subject.getIndexes().get("b");

    assertEquals(2, result.size());

    assertEquals("bla", result.get(0).getKeyword());
    assertEquals(2, result.get(0).getLinks().size());

    assertEquals("blaa", result.get(1).getKeyword());
    assertEquals(1, result.get(1).getLinks().size());
  }

  private void enableIndexing() {
    this.config.setTargetDirectory("/home/build/output/");
    this.config.setSourceDirectory("/home/build/target/");
    this.config.setIndexGenerator(new IndexGenerator());
    this.config.getIndexGenerator().setIncludeSearch(true);
  }}
