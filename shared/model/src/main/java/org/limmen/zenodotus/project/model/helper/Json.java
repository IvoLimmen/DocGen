package org.limmen.zenodotus.project.model.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.limmen.zenodotus.project.model.Project;
import org.limmen.zenodotus.project.model.Team;

public class Json {
  
    private static ObjectMapper mapper;

    public static ObjectMapper mapper() {
        if (mapper == null) {
            mapper = ObjectMapperFactory.createJson();
        }
        return mapper;
    }

    public static Project loadProject(Path fileName) throws IOException {
        return mapper().readValue(fileName.toFile(), Project.class);
    }

    public static void saveProject(Project project, Path fileName) throws JsonProcessingException, IOException {      
      Files.writeString(fileName, mapper().writerWithDefaultPrettyPrinter().writeValueAsString(project));
    }

    public static Team loadTeam(Path fileName) throws IOException {
        return mapper().readValue(fileName.toFile(), Team.class);
    }

    public static void saveTeam(Team team, Path fileName) throws JsonProcessingException, IOException {      
      Files.writeString(fileName, mapper().writerWithDefaultPrettyPrinter().writeValueAsString(team));
    }    
}