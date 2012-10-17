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

import java.util.ArrayList;
import java.util.List;

public class AnalyticsManager {
    private String name, version;
    private LoggingManager loggingManager = new LoggingManager("CraftFire.AnalyticsManager", "[AnalyticsManager]");

    public AnalyticsManager(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
       return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return this.version;
    }

    public String setVersion(String version) {
        this.version = version;
    }

    public LoggingManager getLogging() {
        return this.loggingManager;
    }

    public void setLoggingManager(LoggingManager loggingManager) {
        this.loggingManager = loggingManager;
    }
}
