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
package com.craftfire.commons;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * PhpSerializer - util class to serialize java objects with php serialization
 *
 * @see SerializedPhpParser
 * @see Util
 */
public class PhpSerializer {
    /**
     * Serialize integer.
     *
     * @param i     integer to serialize
     * @return      serialized i
     */
    public String serialize(int i) {
        return "i:" + String.valueOf(i) + ";";
    }

    /**
     * Serialize double.
     *
     * @param d     double to serialize
     * @return      serialized d
     */
    public String serialize(double d) {
        return "d:" + String.valueOf(d) + ";";
    }

    /**
     * Serialize boolean.
     *
     * @param b     boolean to serialize
     * @return      serialized b
     */
    public String serialize(boolean b) {
        return "b:" + (b ? "1" : "0") + ";";
    }

    /**
     * Serialize string
     *
     * @param s     string to serialize
     * @return      s serialized to string
     */
    public String serialize(String s) {
        if (s == null) {
            return "N;";
        }
        return "s:" + s.length() + ":\"" + s + "\";";
    }

    /**
     * Serialize list
     *
     * @param list  list to serialize
     * @return      list serialized to string
     */
    public String serialize(List<?> list) {
        if (list == null) {
            return "N;";
        }
        String out = "a:" + list.size() + ":{";
        int index = 0;
        Iterator<?> i = list.iterator();
        while (i.hasNext()) {
            out += this.serialize(index++);
            out += this.serialize(i.next());
            if (!out.endsWith(";")) {
                out += ";";
            }
        }
        out += "}";
        return out;
    }

    /**
     * Serialize map
     *
     * @param map   map to serialize
     * @return      map serialized to string
     */
    public String serialize(Map<?, ?> map) {
        if (map == null) {
            return "N;";
        }
        String out = "a:" + map.size() + ":{";
        Iterator<?> i = map.keySet().iterator();
        while (i.hasNext()) {
            Object key = i.next();
            out += this.serialize(key);
            out += this.serialize(map.get(key));
        }
        out += "}";
        return out;
    }

    /**
     * Serialize {@link SerializedPhpParser.PhpObject}
     *
     * @param value     PhpObject to serialize
     * @return          serialized value
     */
    public String serialize(SerializedPhpParser.PhpObject value) {
        if (value == null) {
            return "N;";
        }
        String out = "O:" + value.name.length() + ":\"" + value.name + "\":";
        out += value.attributes.size() + ":{";
        Iterator<Object> i = value.attributes.keySet().iterator();
        while (i.hasNext()) {
            Object key = i.next();
            out += this.serialize(key);
            out += this.serialize(value.attributes.get(key));
        }
        out += "};";
        return out;
    }

    /**
     * Detect object type and serialize it.
     *
     * @param value     object to serialize
     * @return          serialized value, or serialized value.toString
     *                  if unknown type.
     */
    public String serialize(Object value) {
        if (value == null) {
            return "N;";
        } else if (value instanceof Integer) {
            return this.serialize(((Integer) value).intValue());
        } else if (value instanceof Number) {
            return this.serialize(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            return this.serialize(((Boolean) value).booleanValue());
        } else if (value instanceof List<?>) {
            return this.serialize((List<?>) value);
        } else if (value instanceof Map<?,?>) {
            return this.serialize((Map<?, ?>) value);
        } else if (value instanceof SerializedPhpParser.PhpObject) {
            return this.serialize((SerializedPhpParser.PhpObject) value);
        }
        return this.serialize(value.toString());
    }
}
