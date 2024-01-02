package com.challenge.loadbalancer.util;

public interface ILogger {

    void error(String message, String className);

    void information(String message, String className);

    void warning(String message, String className);

}
