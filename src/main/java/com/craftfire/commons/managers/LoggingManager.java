/*
 * This file is part of CraftCommons.
 *
 * Copyright (c) 2011-2012, CraftFire <http://www.craftfire.com/>
 * CraftCommons is licensed under the GNU Lesser General Public License.
 *
 * CraftCommons is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CraftCommons is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.craftfire.commons.managers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

public class LoggingManager {
    private final Logger logger;
    private String prefix, directory, format = "HH:mm:ss";
    private boolean debug = false, logging = false;

    public LoggingManager(String logger, String prefix) {
        this.logger = Logger.getLogger(logger);
        this.prefix = prefix;
    }

    public static enum Type {
        error, debug
    }

    public Logger getLogger() {
        return this.logger;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getDirectory() {
        return this.directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
        if (directory == null || directory.isEmpty()) {
            this.logging = false;
        }
    }

    public boolean isDebug() {
        return this.debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isLogging() {
        return this.logging;
    }

    public void setLogging(boolean logging) {
        this.logging = (this.directory != null) && (!this.directory.isEmpty()) && logging;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void info(String line) {
        this.logger.info(this.prefix + " " + line);
    }

    public void warning(String line) {
        this.logger.warning(this.prefix + " " + line);
    }

    public void severe(String line) {
        this.logger.severe(this.prefix + " " + line);
    }

    public void debug(String line) {
        if (this.debug) {
            this.logger.info(this.prefix + " [Debug] " + line);
            toFile(Type.debug, line);
        }
    }

    public void error(String error) {
        warning(error);
        toFile(Type.error, error);
    }

    public void advancedWarning(String line) {
        warning(System.getProperty("line.separator")
                + "|-----------------------------------------------------------------------------|"
                + System.getProperty("line.separator")
                + "|---------------------------------- WARNING ----------------------------------|"
                + System.getProperty("line.separator")
                + "|-----------------------------------------------------------------------------|"
                + System.getProperty("line.separator")
                + "| "
                + line.toUpperCase()
                + System.getProperty("line.separator")
                + "|-----------------------------------------------------------------------------|");
    }

    public void stackTrace(final Exception e) {
        stackTrace(e, null);
    }

    public void stackTrace(final Exception e, HashMap<Integer, String> extra) {
        advancedWarning("Stacktrace Error");
        warning("Class name: " + e.getStackTrace()[1].getClassName());
        warning("Error message: " + e.getMessage());
        warning("Error cause: " + e.getCause());
        warning("File name: " + e.getStackTrace()[1].getFileName());
        warning("Function name: " + e.getStackTrace()[1].getMethodName());
        warning("Error line: " + e.getStackTrace()[1].getLineNumber());
        if (this.logging) {
            DateFormat LogFormat = new SimpleDateFormat(this.format);
            Date date = new Date();
            warning("Check log file: " + this.directory + "error\\"
                    + LogFormat.format(date) + "-error.log");
        } else {
            warning("Enable logging in the config to get more information about the error.");
        }

        logError("--------------------------- STACKTRACE ERROR ---------------------------");
        logError("Class name: " + e.getStackTrace()[1].getClassName());
        logError("Error message: " + e.getMessage());
        logError("Error cause: " + e.getCause());
        logError("File name: " + e.getStackTrace()[1].getFileName());
        logError("Function name: " + e.getStackTrace()[1].getMethodName());
        logError("Error line: " + e.getStackTrace()[1].getLineNumber());
        if (extra != null) {
            for (int id : extra.keySet()) {
                logError(extra.get(id));
            }
        }
        logError("--------------------------- STACKTRACE START ---------------------------");
        for (int i = 0; i < e.getStackTrace().length; i++) {
            logError(e.getStackTrace()[i].toString());
        }
        logError("---------------------------- STACKTRACE END ----------------------------");
    }

    public void logError(String error) {
        toFile(Type.error, error);
    }

    private void toFile(Type type, String line) {
        if (this.logging) {
            File data = new File(this.directory, "");
            if (!data.exists()) {
                if (data.mkdir()) {
                    debug("Created missing directory: " + this.directory);
                }
            }
            data = new File(this.directory + type.toString() + "/", "");
            if (!data.exists()) {
                if (data.mkdir()) {
                    debug("Created missing directory: " + this.directory
                            + type.toString());
                }
            }
            DateFormat logFormat = new SimpleDateFormat(this.format);
            Date date = new Date();
            data = new File(this.directory + type.toString() + "/"
                    + logFormat.format(date) + "-" + type.toString() + ".log");
            if (!data.exists()) {
                try {
                    data.createNewFile();
                } catch (IOException e) {
                    stackTrace(e);
                }
            }
            try {
                DateFormat stringFormat = new SimpleDateFormat(
                        "yyyy/MM/dd HH:mm:ss");
                FileWriter writer = new FileWriter(this.directory
                        + type.toString() + "/" + logFormat.format(date) + "-"
                        + type.toString() + ".log", true);
                BufferedWriter buffer = new BufferedWriter(writer);
                buffer.write(stringFormat.format(date) + " - " + line
                        + System.getProperty("line.separator"));
                buffer.close();
                writer.close();
            } catch (IOException e) {
                stackTrace(e);
            }
        }
    }
}
