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
package com.craftfire.commons.classes;

public class CacheItem {
    private final int time, cacheTime, hash;
    private final Object id, object;
    
    public CacheItem(Object id, int cacheTime, int hash, Object object) {
        this.id = id;
        this.time = (int) System.currentTimeMillis() / 1000;
        this.cacheTime = cacheTime;
        this.hash = hash;
        this.object = object;
    }
    
    public Object getID() {
        return this.id;
    }
    
    public int getHash() {
        return this.hash;
    }
    
    public int getTimeAdded() {
        return this.time;
    }
    
    public int getSecondsLeft() {
        return (int) ((System.currentTimeMillis() / 1000) - this.time - this.cacheTime);
    }
    
    public int getCacheTime() {
        return this.cacheTime;
    }
    
    public Object getObject() {
        return this.object;
    }
}
