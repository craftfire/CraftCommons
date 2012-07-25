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
    private HashMap<Integer, CacheItem> items = new HashMap<Integer, CacheItem>();
    private int seconds = 300;
    
    public void setCacheTime(int seconds) {
        this.seconds = seconds;
    }
    
    public int getCacheTime() {
        return this.seconds;
    }

    public boolean contains(int id) {
        return this.items.containsKey(id);
    }
    
    public void put(int id, Object object) {
        this.items.put(id, new CacheItem(id, this.seconds, object.hashCode(), object));
    }
    
    public Object get(int id) {
        if (contains(id)) {
            this.items.get(id);
        }
        return null;
    }

    public void clear() {
        this.items.clear();
    }
}
