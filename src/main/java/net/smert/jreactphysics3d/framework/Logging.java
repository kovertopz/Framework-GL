/**
 * Copyright 2012 Jason Sorensen (sorensenj@smert.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package net.smert.jreactphysics3d.framework;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Jason Sorensen <sorensenj@smert.net>
 */
public class Logging {

    private final static int MAX_LOG_COUNT = 4;
    private final static int MAX_LOG_SIZE = 131072;

    private final Configuration config;
    private final SimpleFormatter simpleFormatter;

    public Logging(Configuration config, SimpleFormatter simpleFormatter) {
        this.config = config;
        this.simpleFormatter = simpleFormatter;
    }

    private void addFileHandler(Logger logger) throws IOException, SecurityException {
        Handler file = new FileHandler(config.logFilename, MAX_LOG_SIZE, MAX_LOG_COUNT);
        file.setFormatter(simpleFormatter);
        file.setLevel(config.logLevel);
        logger.addHandler(file);
    }

    private void readLoggingProperties() throws IOException, SecurityException {
        try (FileInputStream fis = new FileInputStream(config.logProperties)) {
            LogManager.getLogManager().readConfiguration(fis);
        }
    }

    public void reset() throws IOException, SecurityException {
        LogManager.getLogManager().reset();

        if (config.logProperties != null) {
            readLoggingProperties();
            return;
        }

        // Default logger is not "global" for some reason
        Logger logger = Logger.getLogger("");
        logger.setLevel(config.logLevel);

        // Setup console logging
        if (config.logConsole) {
            Handler console = new ConsoleHandler();
            console.setFormatter(simpleFormatter);
            console.setLevel(config.logLevel);
            logger.addHandler(console);
        }

        // Setup a log file
        if (config.logFile) {
            addFileHandler(logger);
        }
    }

}
