package com.challenge.loadbalancer.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerWrapper implements ILogger {

    private final Logger logger;

    public LoggerWrapper() {
        this.logger = Logger.getLogger("Load-Balancer");
    }

    @Override
    public void error(String message, String classname) {
        logger.log(Level.SEVERE, message);
    }

    @Override
    public void information(String message, String className) {
        logger.log(Level.INFO, message);
    }

    @Override
    public void warning(String message, String className) {
        logger.log(Level.WARNING, message);
    }
}
