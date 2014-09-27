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

    public Logging(Configuration config) {
        this.config = config;
    }

    private void addFileHandler(Logger logger) {
        try {
            Handler file = new FileHandler(config.logFilename, MAX_LOG_SIZE, MAX_LOG_COUNT);
            file.setFormatter(new SimpleFormatter());
            file.setLevel(config.logLevel);
            logger.addHandler(file);
        } catch (IOException | SecurityException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    private void readLoggingProperties() {
        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream(config.logProperties));
        } catch (IOException | SecurityException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    public void reset() {
        LogManager.getLogManager().reset();

        if (config.logProperties != null) {
            readLoggingProperties();
        } else {

            // Default logger is not "global" for some reason
            Logger logger = Logger.getLogger("");
            logger.setLevel(config.logLevel);

            // Setup console logging
            if (config.logConsole) {
                Handler console = new ConsoleHandler();
                console.setFormatter(new SimpleFormatter());
                console.setLevel(config.logLevel);
                logger.addHandler(console);
            }

            // Setup a log file
            if (config.logFile) {
                addFileHandler(logger);
            }
        }
    }

}
