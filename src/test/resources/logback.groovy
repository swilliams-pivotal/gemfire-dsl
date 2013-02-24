import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

import java.util.logging.LogManager
import java.util.logging.Logger

import static ch.qos.logback.classic.Level.*

appender("stdout", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d [%thread] [%level] %logger{5} - %msg%n"
    }
}

//logger("rx", ERROR)
root(INFO, ["stdout"])