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

import com.craftfire.commons.classes.CacheItem;

import java.util.HashMap;

public class CacheManager {
    protected final String defaultGroup = "default";
    private HashMap<String, HashMap<Integer, CacheItem>> items = new HashMap<String, HashMap<Integer, CacheItem>>();
    private HashMap<String, Integer> lastID = new HashMap<String, Integer>();
    private int seconds = 300;
    
    public void setCacheTime(int seconds) {
        this.seconds = seconds;
    }
    
    public int getCacheTime() {
        return this.seconds;
    }
    
    public HashMap<String, HashMap<Integer, CacheItem>> getCache() {
        return this.items;
    }
    
    public HashMap<Integer, CacheItem> getCache(String group) {
        if (this.containsGroup(group)) {
            return this.items.get(group);
        }
        return null;
    }
    
    public int getLastID() {
        return getLastID(this.defaultGroup);
    }
    
    public int getLastID(String group) {
        if (this.lastID.containsKey(group)) {
            return this.lastID.get(group);
        }
        this.lastID.put(group, 0);
        return 0;
    }

    public boolean contains(int id) {
        return contains(this.defaultGroup, id);
    }
    
    public boolean containsGroup(String group) {
        return this.items.containsKey(group);
    }

    public boolean contains(String group, int id) {
        return containsGroup(group) && this.items.get(group).containsKey(id);
    }
    
    public void put(int id, Object object) {
        put(this.defaultGroup, id, object);
    }

    public int put(Object object) {
        return put(this.defaultGroup, object);
    }

    public int put(String group, Object object) {
        int id = getLastID(group) + 1;
        put(group, id, object);
        return id;
    }

    public void put(String group, int id, Object object) {
        if (!containsGroup(group)) {
            this.items.put(group, new HashMap<Integer, CacheItem>());
        }
        HashMap<Integer, CacheItem> temp = this.items.get(group);
        temp.put(id, new CacheItem(id, this.seconds, object.hashCode(), object));
        this.items.put(group, temp);
        this.lastID.put(group, id);
    }

    public CacheItem getItem(int id) {
        return getItem(this.defaultGroup, id);
    }
    
    public CacheItem getItem(String group, int id) {
        if (contains(group, id)) {
            return this.items.get(group).get(id);
        }
        return null;
    }

    public Object get(int id) {
        return getItem(this.defaultGroup, id);
    }

    public Object get(String group, int id) {
        if (contains(group, id)) {
            return this.items.get(group).get(id).getObject();
        }
        return null;
    }

    public void remove(int id) {
        remove(this.defaultGroup, id);
    }

    public void remove(String group, int id) {
        if (contains(group, id)) {
            this.items.get(group).remove(id);
        }
    }

    public void clear() {
        this.items.clear();
        this.lastID.clear();
    }

}
