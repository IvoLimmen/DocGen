package org.limmen.docgen.domain;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IndexNode {

  private String sectionName;
  private String linkPart;
  private Path targetFile;
  private String rawText;
  private List<String> keywords = new ArrayList<>();

  public static Builder builder() {
    return new Builder();      
  }

  public static class Builder {

    private String sectionName;
    private String linkPart;
    private Path targetFile;
    private String rawText;
    private List<String> keywords = new ArrayList<>();
    
    public Builder() {
    }

    Builder(String sectionName, String linkPart, Path targetFile, String rawText, List<String> keyworkds) {
      this.sectionName = sectionName;
      this.linkPart = linkPart;
      this.targetFile = targetFile;
      this.rawText = rawText;
    }

    public Builder from(IndexNode source) {
      this.linkPart = source.linkPart;
      this.rawText = source.rawText;
      this.sectionName = source.sectionName;
      this.targetFile = source.targetFile;      
      return Builder.this;
    }

    public Builder keywords(List<String> keywords) {
      this.keywords.clear();
      this.keywords.addAll(keywords);
      return Builder.this;
    }

    public Builder sectionName(String sectionName) {
      this.sectionName = sectionName;
      return Builder.this;
    }

    public Builder linkPart(String linkPart) {
      this.linkPart = linkPart;
      return Builder.this;
    }

    public Builder targetFile(Path targetFile) {
      this.targetFile = targetFile;
      return Builder.this;
    }

    public Builder rawText(String rawText) {
      this.rawText = rawText;
      return Builder.this;
    }

    public IndexNode build() {
      return new IndexNode(this);
    }
  }

  private IndexNode(Builder builder) {
    this.sectionName = builder.sectionName;
    this.linkPart = builder.linkPart;
    this.targetFile = builder.targetFile;
    this.rawText = builder.rawText;
    this.keywords = builder.keywords;
  }

  public String getSectionName() {
    return sectionName;
  }
  
  public String getLinkPart() {
    return linkPart;
  }

  public Path getTargetFile() {
    return targetFile;
  }

  public String getRawText() {
    return rawText;
  }

  public List<String> getKeywords() {
    return keywords;
  }

  @Override
  public String toString() {
    return "IndexNode [keywords=" + keywords + ", linkPart=" + linkPart + ", rawText=" + rawText + ", sectionName="
        + sectionName + ", targetFile=" + targetFile + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(keywords, linkPart, rawText, sectionName, targetFile);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    IndexNode other = (IndexNode) obj;
    return Objects.equals(keywords, other.keywords) && Objects.equals(linkPart, other.linkPart)
        && Objects.equals(rawText, other.rawText) && Objects.equals(sectionName, other.sectionName)
        && Objects.equals(targetFile, other.targetFile);
  }
}
