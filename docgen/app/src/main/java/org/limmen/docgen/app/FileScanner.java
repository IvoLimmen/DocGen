package org.limmen.docgen.app;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.limmen.docgen.domain.file.ProjectFolder;
import org.limmen.docgen.domain.file.RootFolder;
import org.limmen.docgen.domain.file.SupportFolder;
import org.limmen.docgen.domain.file.TeamFolder;
import org.limmen.docgen.model.Config;

public class FileScanner {

  private Config config;

  private RootFolder rootFolder = new RootFolder("ROOT");

  public FileScanner(Config config) {
    this.config = config;
  }

  public RootFolder getRootFolder() {

    try {
      var files = Files.list(this.config.getSourceDirectory()).toList();

      for (Path path : files) {
        if (!path.toFile().isHidden()) {
          if (path.toFile().isDirectory()) {
            rootFolder.getTeamFolders().add(getTeamFolder(path));
          } else {
            rootFolder.getFiles().add(path);
          }
        }
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    return rootFolder;
  }

  private TeamFolder getTeamFolder(Path folder) throws IOException {
    TeamFolder teamFolder = new TeamFolder(folder.getFileName().toString());

    var files = Files.list(folder).toList();

    for (Path path : files) {
      if (!path.toFile().isHidden()) {
        if (path.toFile().isDirectory()) {
          teamFolder.getProjectFolders().add(getProjectFolder(path));
        } else {
          teamFolder.getFiles().add(path);
        }
      }
    }

    return teamFolder;
  }

  private ProjectFolder getProjectFolder(Path folder) throws IOException {
    ProjectFolder projectFolder = new ProjectFolder(folder.getFileName().toString());

    var files = Files.list(folder).toList();

    for (Path path : files) {
      if (!path.toFile().isHidden()) {
        if (path.toFile().isDirectory()) {
          projectFolder.getSupportFolders().add(getSupportFolder(path));
        } else {
          projectFolder.getFiles().add(path);
        }
      }
    }

    return projectFolder;
  }

  private SupportFolder getSupportFolder(Path folder) throws IOException {
    SupportFolder supportFolder = new SupportFolder(folder.getFileName().toString());

    var files = Files.list(folder).toList();

    for (Path path : files) {
      if (!path.toFile().isHidden()) {
        if (path.toFile().isFile()) {
          supportFolder.getFiles().add(path);
        }
      }
    }

    return supportFolder;
  }
}
