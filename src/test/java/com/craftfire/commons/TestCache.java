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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Test;

import com.craftfire.commons.classes.MetadatableCacheItem;
import com.craftfire.commons.managers.CacheManager;

public class TestCache {

    public TestCache() {
        // TODO Auto-generated constructor stub
    }

    @Test
    public void testPutGet() {
        CacheManager mgr = new CacheManager();
        mgr.setEnabled(true);
        Date now = new Date();
        int id = mgr.put(now);
        assertEquals(now, mgr.get(id));
    }

    @Test
    public void testMetaPutGet() {
        CacheManager mgr = new CacheManager();
        mgr.setEnabled(true);
        Date now = new Date();
        int id = mgr.putMetadatable(now);
        ((MetadatableCacheItem) mgr.getItem(id)).setMetaData("test.meta", "This is the freaky metadata");
        assertEquals(now, mgr.get(id));
        assertEquals("This is the freaky metadata", ((MetadatableCacheItem) mgr.getItem(id)).getMetaData("test.meta"));
        assertNull(((MetadatableCacheItem) mgr.getItem(id)).getMetaData("test_meta"));
        ((MetadatableCacheItem) mgr.getItem(id)).removeMetaData("test.meta");
        assertNull(((MetadatableCacheItem) mgr.getItem(id)).getMetaData("test.meta"));
    }

    @Test
    public void testGroupPutGet() {
        CacheManager mgr = new CacheManager();
        mgr.setEnabled(true);
        Date now = new Date();
        int id = mgr.put("My fancy Group", now);
        assertEquals(now, mgr.get("My fancy Group", id));
    }

    @Test
    public void testGroupMetaPutGet() {
        CacheManager mgr = new CacheManager();
        mgr.setEnabled(true);
        Date now = new Date();
        int id = mgr.putMetadatable("GrOuP", now);
        mgr.getMetadatableItem("GrOuP", id).setMetaData("test.meta", "This is the freaky metadata");
        assertEquals(now, mgr.get("GrOuP", id));
        assertEquals("This is the freaky metadata", mgr.getMetadatableItem("GrOuP", id).getMetaData("test.meta"));
        assertNull(mgr.getMetadatableItem("GrOuP", id).getMetaData("test_meta"));
        mgr.getMetadatableItem("GrOuP", id).removeMetaData("test.meta");
        assertNull(mgr.getMetadatableItem("GrOuP", id).getMetaData("test.meta"));
    }

}
