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
