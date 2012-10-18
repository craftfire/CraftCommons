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
    private AnalyticsItem name, version;
    private Map<String, AnalyticsItem> customData = new HashMap<String, AnalyticsItem>();

    public AnalyticsData(String name, String version) {
        this.name = new AnalyticsItem("project_name", name);
        this.version = new AnalyticsItem("project_version",version);
    }

    public AnalyticsItem getName() {
        return this.name;
    }

    public void setName(String name) {
        getName().setValue(name);
    }

    public AnalyticsItem getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        getVersion().setValue(version);
    }

    public void add(String key, String value) {
        this.customData.put(key.toLowerCase(), new AnalyticsItem(key.toLowerCase(), value));
    }

    public Map<String, AnalyticsItem> getCustomData() {
        return this.customData;
    }

    public AnalyticsItem getCustom(String key) {
        if (getCustomData().containsKey(key.toLowerCase())) {
            return getCustomData().get(key.toLowerCase());
        }
        return null;
    }

    public AnalyticsItem getOSName() {
        return new AnalyticsItem("os_name", System.getProperty("os.name"));
    }

    public AnalyticsItem getOSVersion() {
        return new AnalyticsItem("os_version", System.getProperty("os.version"));
    }

    public AnalyticsItem getOSArch() {
        return new AnalyticsItem("os_arch", System.getProperty("os.arch"));
    }

    public AnalyticsItem getTotalMemory() {
        return new AnalyticsItem("ram_total", Long.toString(Runtime.getRuntime().totalMemory()));
    }

    public AnalyticsItem getMaxMemory() {
        return new AnalyticsItem("ram_max", Long.toString(Runtime.getRuntime().maxMemory()));
    }

    public AnalyticsItem getJavaVersion() {
        return new AnalyticsItem("java_version", System.getProperty("java.version"));
    }
}
