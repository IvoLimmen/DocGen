package org.limmen.docgen.model.helper;

import java.io.IOException;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.limmen.docgen.model.Config;

public class Json {
  
    private static ObjectMapper mapper;

    public static ObjectMapper mapper() {
        if (mapper == null) {
            mapper = ObjectMapperFactory.createJson();
        }
        return mapper;
    }

    public static Config load(Path fileName) throws IOException {
        return mapper().readValue(fileName.toFile(), Config.class);
    }
}  

