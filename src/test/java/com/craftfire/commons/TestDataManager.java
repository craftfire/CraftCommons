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
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.craftfire.commons.database.DataManager;
import com.craftfire.commons.database.DataType;
import com.craftfire.commons.util.LoggingManager;

public class TestDataManager {
    private static DummyDataManager datamanager;
    private boolean logged = false;

    @Before
    public void init() {
        datamanager = new DummyDataManager("usr", "pss");
    }

    @Test
    public void testURL() {
        DummyDataManager dmH2 = new DummyDataManager(DataType.H2, null, null);
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

    @Test
    public void testLogging() {
        datamanager.close();
        assertFalse(this.logged);
        LoggingManager lm = new DummyLogger("CraftFire.DataManager", "[DummyDataManager]");
        datamanager.setLoggingManager(lm);
        assertTrue(datamanager.getLogger() == lm);
        datamanager.close();
        assertTrue(this.logged);
        try {
            datamanager.setLoggingManager(null);
            fail("Expected IllegalArgumentException when ttying setLoggingManager(null)");
        } catch (Exception youBetterBeThrown) {
        }
    }

    static class DummyDataManager extends DataManager {

        public DummyDataManager(DataType type, String username, String password) {
            super(type, username, password);
        }

        public DummyDataManager(String username, String password) {
            super(username, password);
        }

        @Override
        public boolean setURL() {
            return super.setURL();
        }
    }

    class DummyLogger extends LoggingManager {

        public DummyLogger(String logger, String prefix) {
            super(logger, prefix);
        }

        @Override
        public void debug(String line) {
            super.debug(line);
            TestDataManager.this.logged = true;
        }

        /*
        @Override
        public void info(String line) {
            super.info(line);
            TestDataManager.this.logged = true;
        }

        @Override
        public void severe(String line) {
            super.severe(line);
            TestDataManager.this.logged = true;
        }

        @Override
        public void warning(String line) {
            super.warning(line);
            TestDataManager.this.logged = true;
        }

        @Override
        public void logError(String line) {
            super.logError(line);
            TestDataManager.this.logged = true;
        }
        */
    }
}
