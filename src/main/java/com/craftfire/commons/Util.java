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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Util {
    protected PhpSerializer serializer;

    public Util() {
        this.serializer = new PhpSerializer();
    }

    //TODO: Rewrite this function. (PHP deserialize)
    public String forumCache(String cache, String player, int userid, String nummember, String activemembers, String newusername, String newuserid, String extrausername, String lastvalue) {
        StringTokenizer st = new StringTokenizer(cache, ":");
        int i = 0;
        List<String> array = new ArrayList<String>();
        while (st.hasMoreTokens()) { array.add(st.nextToken() + ":"); }
        StringBuffer newcache = new StringBuffer();
        while (array.size() > i) {
            if (array.get(i).equals("\"" + nummember + "\";i:") && nummember != null) {
                String temp = array.get(i + 1);
                temp = this.removeChar(temp, '"');
                temp = this.removeChar(temp, ':');
                temp = this.removeChar(temp, 's');
                temp = this.removeChar(temp, ';');
                temp = temp.trim();
                int tempnum = Integer.parseInt(temp) + 1;
                if (lastvalue.equalsIgnoreCase(nummember)) {
                    temp = tempnum + ";}";
                } else {
                    temp = tempnum + ";s:";
                }
                array.set(i + 1, temp);
            } else if (array.get(i).equals("\"" + newusername + "\";s:") && newusername != null) {
                array.set(i + 1, player.length() + ":");
                if (lastvalue.equalsIgnoreCase(newusername)) {
                    array.set(i + 2, "\"" + player + "\"" + ";}");
                } else {
                    array.set(i + 2, "\"" + player + "\"" + ";s" + ":");
                }
            } else if (array.get(i).equals("\"" + extrausername + "\";s:") && extrausername != null) {
                array.set(i + 1, player.length() + ":");
                if (lastvalue.equalsIgnoreCase(extrausername)) {
                    array.set(i + 2, "\"" + player + "\"" + ";}");
                } else {
                    array.set(i + 2, "\"" + player + "\"" + ";s" + ":");
                }
            } else if (array.get(i).equals("\"" + activemembers + "\";s:") && activemembers != null) {
                String temp = array.get(i + 2);
                temp = this.removeChar(temp, '"');
                temp = this.removeChar(temp, ':');
                temp = this.removeChar(temp, 's');
                temp = this.removeChar(temp, ';');
                temp = temp.trim();
                int tempnum = Integer.parseInt(temp) + 1;
                String templength = "" + tempnum;
                if (lastvalue.equalsIgnoreCase(activemembers)) {
                    temp = "\"" + tempnum + "\"" + ";}";
                } else {
                    temp = "\"" + tempnum + "\"" + ";s:";
                }
                array.set(i + 1, templength.length() + ":");
                array.set(i + 2, temp);
            } else if (array.get(i).equals("\"" + newuserid + "\";s:") && newuserid != null) {
                String dupe = "" + userid;
                array.set(i + 1, dupe.length() + ":");
                if (lastvalue.equalsIgnoreCase(newuserid)) {
                    array.set(i + 2, "\"" + userid + "\"" + ";}");
                } else {
                    array.set(i + 2, "\"" + userid + "\"" + ";s:");
                }
            }
            newcache.append(array.get(i));
            i++;
        }
        return newcache.toString();
    }

    public String forumCacheValue(String cache, String value) {
        StringTokenizer st = new StringTokenizer(cache, ":");
        int i = 0;
        List<String> array = new ArrayList<String>();
        while (st.hasMoreTokens()) { array.add(st.nextToken() + ":"); }
        while (array.size() > i) {
            if (array.get(i).equals("\"" + value + "\";s:") && value != null) {
                String temp = array.get(i + 2);
                temp = this.removeChar(temp, '"');
                temp = this.removeChar(temp, ':');
                temp = this.removeChar(temp, 's');
                temp = this.removeChar(temp, ';');
                temp = temp.trim();
                return temp;
            }
            i++;
        }
        return null;
    }

    public String removeChar(String s, char c) {
        StringBuilder r = new StringBuilder(s.length());
        r.setLength(s.length());
        int current = 0;
        for (int i = 0; i < s.length(); i++) {
            char cur = s.charAt(i);
            if (cur != c) {
                r.setCharAt(current++, cur);
            }
        }
        return r.toString();
    }

    /**
     * Unserialize object serialized with php serialization.
     *
     * @param serialized    string to unserialize
     * @return              unserialized data - Integer, Double, String,
     *                      Boolean, Map<Object,Object> or PhpObject
     */
    public Object phpUnserialize(String serialized) {
        SerializedPhpParser parser = new SerializedPhpParser(serialized);
        return parser.parse();
    }

    /**
     * Serialize object with php serialization.
     *
     * @param value     object to serialize
     * @return          object serialized to String.
     */
    public String phpSerialize(Object value) {
        return this.serializer.serialize(value);
    }

    /**
     * Returns default php serializer.
     *
     * @return  default php serializer.
     */
    public PhpSerializer getPhpSerializer() {
        return this.serializer;
    }
}
