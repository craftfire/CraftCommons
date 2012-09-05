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

import java.util.HashMap;

public class MetadatableCacheItem extends CacheItem {
    private HashMap<String, Object> metaMap = new HashMap<String, Object>();

    public MetadatableCacheItem(Object id, int cacheTime, Object object) {
        super(id, cacheTime, object);
    }

    public Object getMetaData(String metaKey) {
        return this.metaMap.get(metaKey);
    }

    public void setMetaData(String metaKey, Object metaValue) {
        this.metaMap.put(metaKey, metaValue);
    }

    public void removeMetaData(String metaKey) {
        this.metaMap.remove(metaKey);
    }

}
