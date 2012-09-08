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

import java.util.HashMap;

import com.craftfire.commons.CraftCommons;
import com.craftfire.commons.classes.CacheItem;
import com.craftfire.commons.classes.MetadatableCacheItem;

public class CacheManager {
    protected final String defaultGroup = "default";
    private HashMap<String, HashMap<Object, CacheItem>> items = new HashMap<String, HashMap<Object, CacheItem>>();
    private HashMap<Object, Integer> lastID = new HashMap<Object, Integer>();
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

    public HashMap<String, HashMap<Object, CacheItem>> getCache() {
        return this.items;
    }

    public HashMap<Object, CacheItem> getCache(String group) {
        if (this.containsGroup(group)) {
            return this.items.get(group);
        }
        return null;
    }

    public int getLastID() {
        return getLastID(this.defaultGroup);
    }

    public int getLastID(String group) {
        group = group.toLowerCase();
        if (this.lastID.containsKey(group)) {
            return this.lastID.get(group);
        }
        this.lastID.put(group, 0);
        return 0;
    }

    public boolean contains(Object id) {
        return contains(this.defaultGroup, id);
    }

    public boolean containsGroup(String group) {
        return this.enabled && this.items.containsKey(group);
    }

    public boolean contains(String group, Object id) {
        group = group.toLowerCase();
        if (this.enabled && containsGroup(group) && this.items.get(group).containsKey(id)) {
            if (this.items.get(group).get(id).getSecondsLeft() >= 1) {
                return true;
            } else {
                this.items.get(group).remove(id);
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
            group = group.toLowerCase();
            if (!containsGroup(group)) {
                this.items.put(group, new HashMap<Object, CacheItem>());
            }
            HashMap<Object, CacheItem> temp = this.items.get(group);
            temp.put(id, new CacheItem(id, this.seconds, object));
            this.items.put(group, temp);
            if (id instanceof String && CraftCommons.isInteger((String) id)) {
                this.lastID.put(group, Integer.parseInt((String) id));
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
            group = group.toLowerCase();
            if (!containsGroup(group)) {
                this.items.put(group, new HashMap<Object, CacheItem>());
            }
            HashMap<Object, CacheItem> temp = this.items.get(group);
            temp.put(id, new MetadatableCacheItem(id, this.seconds, object));
            this.items.put(group, temp);
            if (id instanceof String && CraftCommons.isInteger((String) id)) {
                this.lastID.put(group, Integer.parseInt((String) id));
            }
        }

    }

    public CacheItem getItem(Object id) {
        return getItem(this.defaultGroup, id);
    }

    public CacheItem getItem(String group, Object id) {
        group = group.toLowerCase();
        if (contains(group, id)) {
            return this.items.get(group).get(id);
        }
        return null;
    }

    public MetadatableCacheItem getMetadatableItem(Object id) {
        return getMetadatableItem(this.defaultGroup, id);
    }

    public MetadatableCacheItem getMetadatableItem(String group, Object id) {
        group = group.toLowerCase();
        if (contains(group, id)) {
            CacheItem item = this.items.get(group).get(id);
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
        group = group.toLowerCase();
        if (contains(group, id)) {
            return this.items.get(group).get(id).getObject();
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
        group = group.toLowerCase();
        if (containsGroup(group)) {
            this.items.get(group).clear();
        }
        if (this.lastID.containsKey(group)) {
            this.items.remove(group);
        }
    }

    public void clear() {
        this.items.clear();
        this.lastID.clear();
    }
}
