package org.limmen.docgen.asciidoc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MarkdownTest {
  
  @Test
  void testConversionOfLink() {

    var result = MarkdownConverter.convertLinks("please click [here](https://example.com)");

    assertEquals("please click link:https://example.com[here]", result);
  }

  @Test
  void testConversionOfMultipleLinks() {

    var result = MarkdownConverter.convertLinks("please click [here](https://example.com)... or [here](http://www.example.com)");

    assertEquals("please click link:https://example.com[here]... or link:http://www.example.com[here]", result);
  }
}
