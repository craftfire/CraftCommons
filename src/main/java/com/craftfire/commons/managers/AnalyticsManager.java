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

import com.craftfire.commons.Util;
import com.craftfire.commons.classes.AnalyticsData;
import com.craftfire.commons.exceptions.AnalyticsException;

import java.net.URL;

public class AnalyticsManager {
    private URL url;
    private AnalyticsData data;
    private LoggingManager loggingManager = new LoggingManager("CraftFire.AnalyticsManager", "[AnalyticsManager]");

    public AnalyticsManager(URL url, String name, String version) {
        this.url = url;
        this.data = new AnalyticsData(name, version);
    }

    public void submit() throws AnalyticsException {
        if (Util.isURLOnline(getURL())) {
            //TODO: Make it submit.
        }
        String error = getURL().toString() + " did not return HTTP Status 200, status returned was: " +
                       Util.getResponseCode(getURL()) + ".";
        getLogging().error(error);
        throw new AnalyticsException(this, error);
    }

    public LoggingManager getLogging() {
        return this.loggingManager;
    }

    public void setLoggingManager(LoggingManager loggingManager) {
        this.loggingManager = loggingManager;
    }

    public AnalyticsData getData() {
        return this.data;
    }

    public void addData(String key, String value) {
        getData().add(key, value);
    }

    public URL getURL() {
        return this.url;
    }

    public void setURL(URL url) {
        this.url = url;
    }
}
