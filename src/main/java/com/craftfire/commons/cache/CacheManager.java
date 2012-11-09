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
package com.craftfire.commons.cache;

import java.util.HashMap;
import java.util.Map;

import com.craftfire.commons.util.Util;

public class CacheManager {
    protected static final String defaultGroup = "default";
    private Map<String, Map<Object, CacheItem>> items = new HashMap<String, Map<Object, CacheItem>>();
    private Map<Object, Integer> lastID = new HashMap<Object, Integer>();
    private int seconds = 300;
    private boolean enabled = true;

    public void setCacheTime(int seconds) {
        this.seconds = seconds;
    }

    public int getCacheTime() {
        return this.seconds;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public Map<String, Map<Object, CacheItem>> getCache() {
        return this.items;
    }

    public Map<Object, CacheItem> getCache(String group) {
        if (this.containsGroup(group)) {
            return this.items.get(group);
        }
        return null;
    }

    public int getLastID() {
        return getLastID(this.defaultGroup);
    }

    public int getLastID(String group) {
        String newGroup = group.toLowerCase();
        if (this.lastID.containsKey(newGroup)) {
            return this.lastID.get(newGroup);
        }
        this.lastID.put(newGroup, 0);
        return 0;
    }

    public boolean contains(Object id) {
        return contains(this.defaultGroup, id);
    }

    public boolean containsGroup(String group) {
        return this.enabled && this.items.containsKey(group);
    }

    public boolean contains(String group, Object id) {
        String newGroup = group.toLowerCase();
        if (this.enabled && containsGroup(newGroup) && this.items.get(newGroup).containsKey(id)) {
            if (this.items.get(newGroup).get(id).getSecondsLeft() >= 1) {
                return true;
            } else {
                this.items.get(newGroup).remove(id);
            }
        }
        return false;
    }

    public void put(Object id, Object object) {
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

    public void put(String group, Object id, Object object) {
        if (this.enabled) {
            String newGroup = group.toLowerCase();
            if (!containsGroup(newGroup)) {
                this.items.put(newGroup, new HashMap<Object, CacheItem>());
            }
            Map<Object, CacheItem> temp = this.items.get(newGroup);
            temp.put(id, new CacheItem(id, this.seconds, object));
            this.items.put(newGroup, temp);
            if (id instanceof String && Util.isInteger((String) id)) {
                this.lastID.put(newGroup, Integer.parseInt((String) id));
            }
        }
    }

    public void putMetadatable(Object id, Object object) {
        putMetadatable(this.defaultGroup, id, object);
    }

    public int putMetadatable(Object object) {
        return putMetadatable(this.defaultGroup, object);
    }

    public int putMetadatable(String group, Object object) {
        int id = getLastID(group) + 1;
        putMetadatable(group, id, object);
        return id;
    }

    public void putMetadatable(String group, Object id, Object object) {
        if (this.enabled) {
            String newGroup = group.toLowerCase();
            if (!containsGroup(newGroup)) {
                this.items.put(newGroup, new HashMap<Object, CacheItem>());
            }
            Map<Object, CacheItem> temp = this.items.get(newGroup);
            temp.put(id, new MetadatableCacheItem(id, this.seconds, object));
            this.items.put(newGroup, temp);
            if (id instanceof String && Util.isInteger((String) id)) {
                this.lastID.put(newGroup, Integer.parseInt((String) id));
            }
        }

    }

    public CacheItem getItem(Object id) {
        return getItem(this.defaultGroup, id);
    }

    public CacheItem getItem(String group, Object id) {
        String newGroup = group.toLowerCase();
        if (contains(newGroup, id)) {
            return this.items.get(newGroup).get(id);
        }
        return null;
    }

    public MetadatableCacheItem getMetadatableItem(Object id) {
        return getMetadatableItem(this.defaultGroup, id);
    }

    public MetadatableCacheItem getMetadatableItem(String group, Object id) {
        String newGroup = group.toLowerCase();
        if (contains(newGroup, id)) {
            CacheItem item = this.items.get(newGroup).get(id);
            if (item instanceof MetadatableCacheItem) {
                return (MetadatableCacheItem) item;
            }
        }
        return null;
    }

    public CacheItem getLastItem() {
        return getLastItem(this.defaultGroup);
    }

    public CacheItem getLastItem(String group) {
        return getItem(group, getLastID(group));
    }

    public Object get(Object id) {
        return get(this.defaultGroup, id);
    }

    public Object get(String group, Object id) {
        String newGroup = group.toLowerCase();
        if (contains(newGroup, id)) {
            return this.items.get(newGroup).get(id).getObject();
        }
        return null;
    }

    public Object getLast() {
        return getLast(this.defaultGroup);
    }

    public Object getLast(String group) {
        return get(group, getLastID());
    }

    public void remove(Object id) {
        remove(this.defaultGroup, id);
    }

    public void remove(String group, Object id) {
        if (contains(group, id)) {
            this.items.get(group.toLowerCase()).remove(id);
        }
    }

    public void clear(String group) {
        String newGroup = group.toLowerCase();
        if (containsGroup(newGroup)) {
            this.items.get(newGroup).clear();
        }
        if (this.lastID.containsKey(newGroup)) {
            this.items.remove(newGroup);
        }
    }

    public void clear() {
        this.items.clear();
        this.lastID.clear();
    }
}
