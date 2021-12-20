package org.limmen.docgen.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class IndexItem {

  private String soundex;
  private String keyword;
  private Set<IndexLink> links;

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private String soundex;
    private String keyword;
    private Set<IndexLink> links = new HashSet<>();

    public Builder() {
    }

    Builder(String soundex, String keyword, Set<IndexLink> links) {
      this.soundex = soundex;
      this.keyword = keyword;
      this.links = links;
    }

    public Builder soundex(String soundex) {
      this.soundex = soundex;
      return Builder.this;
    }

    public Builder keyword(String keyword) {
      this.keyword = keyword;
      return Builder.this;
    }

    public Builder links(Set<IndexLink> links) {
      this.links.clear();
      this.links.addAll(links);
      return Builder.this;
    }

    public Builder addLinks(IndexLink links) {
      this.links.add(links);
      return Builder.this;
    }

    public IndexItem build() {

      return new IndexItem(this);
    }
  }

  private IndexItem(Builder builder) {
    this.soundex = builder.soundex;
    this.keyword = builder.keyword;
    this.links = builder.links;
  }

  public String getKeyword() {
    return keyword;
  }

  public Set<IndexLink> getLinks() {
    return links;
  }

  public String getSoundex() {
    return soundex;
  }

  @Override
  public int hashCode() {
    return Objects.hash(keyword, soundex);
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
    IndexItem other = (IndexItem) obj;
    return Objects.equals(keyword, other.keyword) && Objects.equals(soundex, other.soundex);
  }
}
