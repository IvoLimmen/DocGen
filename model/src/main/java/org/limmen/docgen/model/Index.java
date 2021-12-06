package org.limmen.docgen.model;

public class Index {
  
  private boolean includeNavigation;  

  public boolean isIncludeNavigation() {
    return this.includeNavigation;
  }

  public void setIncludeNavigation(boolean includeNavigation) {
    this.includeNavigation = includeNavigation;
  }

  @Override
  public String toString() {
    return "Index [includeNavigation=" + includeNavigation + "]";
  }
}
