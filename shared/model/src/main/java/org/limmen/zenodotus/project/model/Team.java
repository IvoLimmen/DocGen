package org.limmen.zenodotus.project.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = Team.Builder.class)
public class Team {

  private String name;

  private String email;

  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private String name;

    private String email;

    public Builder() {
    }

    Builder(String name, String email) {
      this.name = name;
      this.email = email;
    }

    public Builder name(String name) {
      this.name = name;
      return Builder.this;
    }

    public Builder email(String email) {
      this.email = email;
      return Builder.this;
    }

    public Team build() {
      return new Team(this);
    }
  }

  private Team(Builder builder) {
    this.name = builder.name;
    this.email = builder.email;
  }

  public String getName() {
    return this.name;
  }

  public String getEmail() {
    return email;
  }
}
