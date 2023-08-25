package org.limmen.docgen.asciidoc;

import java.util.HashMap;

public class MarkdownConverter {

  /**
   * Does nothing if the string is empty or null; if it contains markdown links it
   * will convert them to AsciiDoc links.
   * 
   * @param markdown markdown text
   * @return markdown text but with asciidoc links
   */
  public static String convertLinks(String markdown) {
    if (markdown == null || markdown.length() == 0) {
      return markdown;
    }

    if (markdown.contains("[") && markdown.contains("(")) {
      
      var links = new HashMap<String, String>();

      var index = markdown.indexOf("[");
    
      while (index < markdown.length() && index != -1) {

        var startIndex = markdown.indexOf("[", index);
        var endIndex = markdown.indexOf("]", startIndex);
        var label = markdown.substring(1 + startIndex, endIndex);

        if (markdown.indexOf("(", endIndex) == endIndex + 1) {
          startIndex = endIndex + 1;
          endIndex = markdown.indexOf(")", startIndex);

          var link = markdown.substring(1 + startIndex, endIndex);

          var originalLink = String.format("[%s](%s)", label, link);
          links.put(originalLink, AsciiDoc.linkMacro(link, label));

          index = markdown.indexOf("[", endIndex);
        }      
      }

      // replace all links
      for (var entry : links.entrySet()) {
        markdown = markdown.replace(entry.getKey(), entry.getValue());
      }
    }

    return markdown;
  }
}
