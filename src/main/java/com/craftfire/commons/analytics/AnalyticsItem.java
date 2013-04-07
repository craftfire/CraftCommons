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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Data item of the analytics.
 */
public class AnalyticsItem {
    private String key, value;

    /**
     * Default constructor.
     *
     * @param key    the key of the data item
     * @param value  the value of the data item
     */
    public AnalyticsItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Returns the key of the data item.
     *
     * @return key of the data item
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Returns the key of the data item encoded in UTF-8.
     *
     * @return key of the data item in UTF-8
     */
    public String getKeyUTF8() {
        try {
            return URLEncoder.encode(this.key, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
            return this.key;
        }
    }

    /**
     * Sets the key of the data item.
     *
     * @param key  key of the data item
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Returns the value of the data item.
     *
     * @return value of the data item
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Returns the value of the data item encoded in UTF-8.
     *
     * @return value of the data item in UTF-8
     */
    public String getValueUTF8() {
        try {
            return URLEncoder.encode(this.value, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
            return this.value;
        }
    }

    /**
     * Sets the value of the data item.
     *
     * @param value  value of the data item
     */
    public void setValue(String value) {
        this.value = value;
    }
}
