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
package com.craftfire.commons.analytics;

import java.io.UTFDataFormatException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AnalyticsItem {
    private String key, value;

    public AnalyticsItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return this.key;
    }

    public String getKeyUTF8() {
        try {
            return URLEncoder.encode(this.key, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
            return this.key;
        }
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public String getValueUTF8() {
        try {
            return URLEncoder.encode(this.value, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
            return this.value;
        }
    }

    public void setValue(String value) {
        this.value = value;
    }
}
