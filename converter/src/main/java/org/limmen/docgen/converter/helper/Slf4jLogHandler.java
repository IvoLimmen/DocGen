package org.limmen.docgen.converter.helper;

import org.asciidoctor.log.LogHandler;
import org.asciidoctor.log.LogRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jLogHandler implements LogHandler {

  private final static Logger log = LoggerFactory.getLogger(Slf4jLogHandler.class);
  
  @Override
  public void log(LogRecord logRecord) {
    switch (logRecord.getSeverity()) {
      case DEBUG:
        log.debug(logRecord.getMessage());
        break;
      case INFO:
        log.info(logRecord.getMessage());
        break;
      case WARN:
        log.warn(logRecord.getMessage());
        break;
      case ERROR:
      case FATAL:
        log.error(logRecord.getMessage());
        break;
      default:
        log.debug(logRecord.getMessage());
        break;
  }
  }  
}
