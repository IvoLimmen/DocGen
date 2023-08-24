package org.limmen.docgen.app;

import java.nio.charset.Charset;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.spi.ContextAwareBase;

public class ProgrammaticLoggingConfigurator extends ContextAwareBase implements Configurator {
  
  @Override
  public ExecutionStatus configure(LoggerContext loggerContext) {
    addInfo("Setting up default configuration.");

    PatternLayout pattern = new PatternLayout();
    pattern.setContext(loggerContext);
    pattern.setPattern("[%highlight(%level)] %msg%n");
    pattern.start();

    LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<ILoggingEvent>();
    encoder.setContext(loggerContext);
    encoder.setCharset(Charset.forName("utf-8"));
    encoder.setLayout(pattern);

    ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
    consoleAppender.setContext(loggerContext);
    consoleAppender.setName("console");
    consoleAppender.setEncoder(encoder);
    consoleAppender.setWithJansi(true);
    consoleAppender.start();

    Logger log = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
    log.setAdditive(false);
    log.setLevel(Level.DEBUG);
    log.addAppender(consoleAppender);

    return ExecutionStatus.NEUTRAL;
  }
}
