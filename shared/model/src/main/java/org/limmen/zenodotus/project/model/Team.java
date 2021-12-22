package org.limmen.zenodotus.project.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = Team.Builder.class)
public class Team {

  private String name;

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private String name;

    public Builder() {
    }

    Builder(String name) {
      this.name = name;
    }

    public Builder name(String name) {
      this.name = name;
      return Builder.this;
    }

    public Team build() {

      return new Team(this);
    }
  }

  private Team(Builder builder) {
    this.name = builder.name;
  }

  public String getName() {
    return this.name;
  }
}
