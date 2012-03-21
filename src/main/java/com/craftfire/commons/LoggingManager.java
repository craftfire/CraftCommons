/*
 * This file is part of CraftCommons <http://www.craftfire.com/>.
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
package com.craftfire.commons;

import java.util.logging.Logger;

public class LoggingManager {
    private final Logger logger;
    private String prefix, directory;
    private boolean debug = false;
    
    public LoggingManager(String logger, String directory, String prefix) {
        this.logger = Logger.getLogger(logger);
        this.directory = directory;
        this.prefix = prefix;
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
    }

    public boolean isDebug() {
        return this.debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    
    public void debug(String line) {
        if (this.debug) {
            this.logger.info(this.prefix + " " + line);
        }
    }
    
    public void info(String line) {
        this.logger.info(this.prefix + " " + line);    
    }

    public void severe(String line) {
        this.logger.severe(this.prefix + " " + line);
    }

    public void warning(String line) {
        this.logger.warning(this.prefix + " " + line);
    }
    
    public void advancedWarning(String line) {
        warning(System.getProperty("line.separator") + 
                "|-----------------------------------------------------------------------------|" + 
                System.getProperty("line.separator") + 
                "|---------------------------------- WARNING ----------------------------------|" + 
                System.getProperty("line.separator") + 
                "|-----------------------------------------------------------------------------|" + 
                System.getProperty("line.separator") + 
                "| " + line.toUpperCase() + System.getProperty("line.separator") + 
                "|-----------------------------------------------------------------------------|");   
    }

}
