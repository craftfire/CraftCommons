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
package com.craftfire.commons.yaml;

import java.security.PublicKey;

public class YamlNode {
    private String node;
    private Object data;

    public YamlNode(String node, Object data) {
        this.node = node;
        this.data = data;
    }

    public String getNode() {
        return this.node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public void set(Object data) {
        this.data = data;
    }

    public Object getObject() {
        return this.data;
    }

    public String getString() {
        if (this.data instanceof String) {
            return (String) this.data;
        }
        return null;
    }

    public boolean getBoolean() {
        if (this.data instanceof Boolean) {
            return (Boolean) this.data;
        }
        return false;
    }

    public int getInt() {
        if (this.data instanceof Integer) {
            return (Integer) this.data;
        }
        return 0;
    }

    public double getDouble() {
        if (this.data instanceof Double) {
            return (Double) this.data;
        }
        return 0;
    }

    public long getLong() {
        if (this.data instanceof Long) {
            return (Long) this.data;
        }
        return 0;
    }
}
