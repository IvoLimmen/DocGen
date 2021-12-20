package org.limmen.analyser.model.helper;

import java.io.IOException;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.limmen.analyser.model.MavenProject;

public class Json {
  
    private static ObjectMapper mapper;

    public static ObjectMapper mapper() {
        if (mapper == null) {
            mapper = ObjectMapperFactory.createJson();
        }
        return mapper;
    }

    public static MavenProject load(Path fileName) throws IOException {
        return mapper().readValue(fileName.toFile(), MavenProject.class);
    }
}