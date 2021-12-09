package org.limmen.docgen.domain;

import java.util.Objects;

public class IndexLink {

  private String sectionName;
  private String link;
  private String targetFile;

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private String sectionName;
    private String link;
    private String targetFile;

    public Builder() {
    }

    Builder(String sectionName, String link, String targetFile) {
      this.sectionName = sectionName;
      this.link = link;
      this.targetFile = targetFile;
    }

    public Builder targetFile(String targetFile) {
      this.targetFile = targetFile;
      return Builder.this;
    }

    public Builder sectionName(String sectionName) {
      this.sectionName = sectionName;
      return Builder.this;
    }

    public Builder link(String link) {
      this.link = link;
      return Builder.this;
    }

    public IndexLink build() {
      return new IndexLink(this);
    }
  }

  private IndexLink(Builder builder) {
    this.sectionName = builder.sectionName;
    this.link = builder.link;
    this.targetFile = builder.targetFile;
  }

  public String getLink() {
    return link;
  }

  public String getSectionName() {
    return sectionName;
  }

  public String getTargetFile() {
    return targetFile;
  }

  @Override
  public int hashCode() {
    return Objects.hash(link, sectionName, targetFile);
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
    IndexLink other = (IndexLink) obj;
    return Objects.equals(link, other.link)
        && Objects.equals(sectionName, other.sectionName) 
        && Objects.equals(targetFile, other.targetFile);
  }
}
