package org.limmen.docgen.domain;

import java.nio.file.Path;
import java.util.List;

public interface ProjectOverviewGenerator {

  String PROJECT_FILENAME = "project.json";
  String PROJECT_OVERVIEW_FILENAME = "project.adoc";

  void generate(List<Path> files);

}
