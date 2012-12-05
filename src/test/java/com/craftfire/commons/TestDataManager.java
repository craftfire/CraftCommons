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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.craftfire.commons.database.DataManager;
import com.craftfire.commons.database.DataType;

public class TestDataManager {
    private static TestingDataManager datamanager;

    @BeforeClass
    public static void init() {
        datamanager = new TestingDataManager("usr", "pss");
    }

    @Test
    public void testURL() {
        TestingDataManager dmH2 = new TestingDataManager(DataType.H2, null, null);
        assertFalse(dmH2.setURL());
        assertNull(dmH2.getURL());
        dmH2.setDirectory("../blah/meh/");
        dmH2.setDatabase("db");
        assertTrue(dmH2.setURL());
        assertEquals("jdbc:h2:../blah/meh/db;AUTO_RECONNECT=TRUE", dmH2.getURL());
        datamanager.setHost(null);
        datamanager.setDatabase(null);
        assertFalse(datamanager.setURL());
        assertNull(datamanager.getURL());
        datamanager.setHost("localhost");
        datamanager.setDatabase("db");
        assertTrue(datamanager.setURL());
        assertEquals("jdbc:mysql://localhost/db?zeroDateTimeBehavior=convertToNull&jdbcCompliantTruncation=false&autoReconnect=true&characterEncoding=UTF-8&characterSetResults=UTF-8",
                datamanager.getURL());
    }

    static class TestingDataManager extends DataManager {

        public TestingDataManager(DataType type, String username, String password) {
            super(type, username, password);
        }

        public TestingDataManager(String username, String password) {
            super(username, password);
        }

        @Override
        public boolean setURL() {
            return super.setURL();
        }
    }
}
