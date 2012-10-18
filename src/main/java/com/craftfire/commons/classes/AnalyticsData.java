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
package com.craftfire.commons.classes;

import java.util.HashMap;
import java.util.Map;

public class AnalyticsData {
    private String name, version;
    private Map<String, String> customData = new HashMap<String, String>();

    public AnalyticsData(String name, String version) {
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

    public void setVersion(String version) {
        this.version = version;
    }

    public void add(String key, String value) {
        this.customData.put(key, value);
    }

    public Map<String, String> getCustomData() {
        return this.customData;
    }

    public String getCustom(String key) {
        if (getCustomData().containsKey(key)) {
            return getCustomData().get(key);
        }
        return null;
    }

    public String getOSName() {
        return System.getProperty("os.name");
    }

    public String getOSVersion() {
        return System.getProperty("os.version");
    }

    public String getOSArchitecture() {
        return System.getProperty("os.arch");
    }

    public long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    public long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    public String getJavaVersion() {
        return System.getProperty("java.version");
    }
}
