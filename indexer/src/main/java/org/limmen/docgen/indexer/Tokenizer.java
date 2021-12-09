package org.limmen.docgen.indexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.text.StringTokenizer;

public class Tokenizer {
  
  private List<String> stopWords;
  
  public Tokenizer() {    
    init();
  }

  private void init() {
    try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/stop-word-list.txt")))) {
      stopWords = reader.lines().toList();
    }    
    catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  public List<String> tokenize(String text) {
    StringTokenizer stringTokenizer = new StringTokenizer(text);    
    
    return stringTokenizer.getTokenList().stream()
        .map(String::toLowerCase)
        .map(this::stripSpecialCharacters)
        .filter(word -> !stopWords.contains(word))
        .toList();
  }
  
  private String stripSpecialCharacters(String raw) {
    return raw.replaceAll("[^\\w\\s]", "").replace("_", "");
  }
}
