package org.limmen.docgen.domain;

import java.nio.file.Path;
import java.util.Objects;

public class IndexNode {

  private String sectionName;
  private String linkPart;
  private Path targetFile;
  private String rawText;

  public static Builder builder() {
    return new Builder();      
  }

  public static class Builder {

    private String sectionName;
    private String linkPart;
    private Path targetFile;
    private String rawText;
    
    public Builder() {
    }

    Builder(String sectionName, String linkPart, Path targetFile, String rawText) {
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

  @Override
  public int hashCode() {
    return Objects.hash(linkPart, rawText, sectionName, targetFile);
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
    return Objects.equals(linkPart, other.linkPart) && Objects.equals(rawText, other.rawText)
        && Objects.equals(sectionName, other.sectionName) && Objects.equals(targetFile, other.targetFile);
  }
}
