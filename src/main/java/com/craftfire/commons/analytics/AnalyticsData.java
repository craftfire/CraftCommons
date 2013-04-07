/*
 * This file is part of CraftCommons.
 *
 * Copyright (c) 2011 CraftFire <http://www.craftfire.com/>
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
package com.craftfire.commons.analytics;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains all the reporting data for the {@link AnalyticsManager}.
 *
 * @see AnalyticsManager
 * @see AnalyticsItem
 */
public class AnalyticsData {
    private AnalyticsItem name, version;

    /**
     * Contains all the custom data for the analytics.
     */
    private Map<String, AnalyticsItem> customData = new HashMap<String, AnalyticsItem>();

    /**
     * Default constructor for the class.
     *
     * @param name     the name of the item that is being reported
     * @param version  the version of the item that is being reported
     */
    public AnalyticsData(String name, String version) {
        this.name = new AnalyticsItem("project_name", name);
        this.version = new AnalyticsItem("project_version",version);
    }

    /**
     * Returns the name of the data item.
     *
     * @return name of the data item
     */
    public AnalyticsItem getName() {
        return this.name;
    }

    /**
     * Sets the name of the data item.
     *
     * @param name  name of the data item
     */
    public void setName(String name) {
        getName().setValue(name);
    }

    /**
     * Returns the version of the data item.
     *
     * @return version of the data item
     */
    public AnalyticsItem getVersion() {
        return this.version;
    }

    /**
     * Sets the version of the data item.
     *
     * @param version  version of the data item
     */
    public void setVersion(String version) {
        getVersion().setValue(version);
    }

    /**
     * Adds a custom data item.
     * <p>
     * Use this method if you wish to add custom values to report.
     *
     * @param key    the key of the custom item
     * @param value  the value of the custom item
     */
    public void add(String key, String value) {
        this.customData.put(key.toLowerCase(), new AnalyticsItem(key.toLowerCase(), value));
    }

    /**
     * Returns the custom data items.
     *
     * @return custom data items
     */
    public Map<String, AnalyticsItem> getCustomData() {
        return this.customData;
    }

    /**
     * Returns a specific data item if {@code key} exists, returns {@code null} if the {@code key} was not found.
     *
     * @param  key  key identifier of the custom data item
     * @return      custom data item if found, null if {@code key} was not found
     */
    public AnalyticsItem getCustom(String key) {
        if (getCustomData().containsKey(key.toLowerCase())) {
            return getCustomData().get(key.toLowerCase());
        }
        return null;
    }

    /**
     * Returns the operating system name.
     * <pre><b>Example:</b> Linux</pre>
     *
     * @return operating system name
     */
    public AnalyticsItem getOSName() {
        return new AnalyticsItem("os_name", System.getProperty("os.name"));
    }

    /**
     * Returns the operating system version.
     * <pre><b>Example:</b> 3.2.0-3-amd64</pre>
     *
     * @return operating system version
     */
    public AnalyticsItem getOSVersion() {
        return new AnalyticsItem("os_version", System.getProperty("os.version"));
    }

    /**
     * Returns the operating system architecture.
     * <pre><b>Example:</b> amd64</pre>
     *
     * @return operating system architecture
     */
    public AnalyticsItem getOSArch() {
        return new AnalyticsItem("os_arch", System.getProperty("os.arch"));
    }

    /**
     * Returns the amount of memory used.
     * <pre><b>Example:</b> 2813722624</pre>
     *
     * @return operating system name
     */
    public AnalyticsItem getTotalMemory() {
        return new AnalyticsItem("ram_total", Long.toString(Runtime.getRuntime().totalMemory()));
    }

    /**
     * Returns the total amount of memory.
     * <pre><b>Example:</b> 2813722624</pre>
     *
     * @return operating system name
     */
    public AnalyticsItem getMaxMemory() {
        return new AnalyticsItem("ram_max", Long.toString(Runtime.getRuntime().maxMemory()));
    }

    /**
     * Returns the Java version.
     * <pre><b>Example:</b> 1.6.0_24</pre>
     *
     * @return java version
     */
    public AnalyticsItem getJavaVersion() {
        return new AnalyticsItem("java_version", System.getProperty("java.version"));
    }
}
