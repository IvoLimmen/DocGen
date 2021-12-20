package org.limmen.docgen.model;

public class IndexGenerator {
  
  private boolean includeNavigation;

  private boolean includeSearch;

  private Footer footer;

  public Footer getFooter() {
    return footer;
  }

  public void setFooter(Footer footer) {
    this.footer = footer;
  }

  public boolean hasFooter() {
    return this.footer != null;
  }

  public boolean isIncludeNavigation() {
    return this.includeNavigation;
  }

  public void setIncludeNavigation(boolean includeNavigation) {
    this.includeNavigation = includeNavigation;
  }

  public boolean isIncludeSearch() {
    return includeSearch;
  }
  
  public void setIncludeSearch(boolean includeSearch) {
    this.includeSearch = includeSearch;
  }
}
