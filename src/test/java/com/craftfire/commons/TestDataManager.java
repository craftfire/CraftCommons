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

import java.sql.SQLException;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.craftfire.commons.database.DataManager;
import com.craftfire.commons.database.DataType;
import com.craftfire.commons.util.LoggingManager;

public class TestDataManager {
    private static DummyDataManager datamanager;
    private boolean logged = false;
    private static int randomInt = new Random().nextInt(1000);

    @Before
    public void init() {
        datamanager = new DummyDataManager("usr", "pss");
    }

    @Test
    public void testURL() {
        DummyDataManager dmH2 = new DummyDataManager(DataType.H2, null, null);
        dmH2.getLogger().setDebug(true);
        assertFalse(dmH2.setURL());
        assertNull(dmH2.getURL());
        dmH2.setDirectory("./target/test/blah/meh/");
        dmH2.setDatabase("db");
        assertTrue(dmH2.setURL());
        assertEquals("jdbc:h2:./target/test/blah/meh/db;AUTO_RECONNECT=TRUE", dmH2.getURL());
        datamanager.setHost(null);
        datamanager.setDatabase(null);
        assertFalse(datamanager.setURL());
        assertNull(datamanager.getURL());
        datamanager.setHost("localhost");
        datamanager.setDatabase("db");
        assertTrue(datamanager.setURL());
        assertEquals("jdbc:mysql://localhost/db?zeroDateTimeBehavior=convertToNull&jdbcCompliantTruncation=false&autoReconnect=true&characterEncoding=UTF-8&characterSetResults=UTF-8",
                datamanager.getURL());
        assertFalse(datamanager.hasConnection());
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

    @Test
    public void testTimeout() {
        assertEquals(0, datamanager.getTimeout());
        datamanager.setTimeout(1000);
        assertEquals(1000, datamanager.getTimeout());
    }

    @Test
    public void testHost() {
        assertNull(datamanager.getHost());
        datamanager.setHost("localhost");
        assertEquals("localhost", datamanager.getHost());
    }

    @Test
    public void testPort() {
        assertEquals(3306, datamanager.getPort());
        datamanager.setPort(1234);
        assertEquals(1234, datamanager.getPort());
    }

    @Test
    public void testDatabase() {
        assertNull(datamanager.getDatabase());
        datamanager.setDatabase("fancydb");
        assertEquals("fancydb", datamanager.getDatabase());
    }

    @Test
    public void testUsername() {
        assertEquals("usr", datamanager.getUsername());
        datamanager.setUsername("wolf480pl");
        assertEquals("wolf480pl", datamanager.getUsername());
    }

    @Test
    public void testPassword() {
        assertEquals("pss", datamanager.getPassword());
        datamanager.setPassword("shh...it's secret");
        assertEquals("shh...it's secret", datamanager.getPassword());
    }

    @Test
    public void testDirectory() {
        assertNull(datamanager.getDirectory());
        datamanager.setDirectory("./target/test/" + randomInt + "/");
        assertEquals("./target/test/" + randomInt + "/", datamanager.getDirectory());
        /*
        datamanager.setDirectory("./target/test/\0"); // You will never be able to create such a file ;) Error should be printed
        assertEquals("./target/test/\0", datamanager.getDirectory());
        // In linux there is no invalid path :/
        */
    }

    @Test
    public void testPrefix() {
        assertEquals("", datamanager.getPrefix());
        datamanager.setPrefix("test_");
        assertEquals("test_", datamanager.getPrefix());
    }

    @Test
    public void testDataType() {
        DataManager dmH2 = new DataManager(DataType.H2, null, null);
        assertEquals(DataType.MYSQL, datamanager.getDataType());
        assertEquals(DataType.H2, dmH2.getDataType());
    }

    @Test
    public void testFieldGettersFail() {
        String table = "tb";
        String name = "afield";
        assertFalse(datamanager.getBooleanField(table, name, "1"));
        assertNull(datamanager.getBinaryField(table, name, "1"));
        assertNull(datamanager.getBlobField(table, name, "1"));
        assertNull(datamanager.getDateField(table, name, "1"));
        assertEquals(0, datamanager.getDoubleField(table, name, "1"), 0);
        assertEquals(0, datamanager.getIntegerField(table, name, "1"));
        assertNull(datamanager.getStringField(table, name, "1"));
        assertFalse(datamanager.getBooleanField("SELECT `bool` FROM `" + table + "`"));
        assertNull(datamanager.getBinaryField("SELECT `bin` FROM `" + table + "`"));
        assertNull(datamanager.getBlobField("SELECT `blob` FROM `" + table + "`"));
        assertNull(datamanager.getDateField("SELECT `date` FROM `" + table + "`"));
        assertEquals(0, datamanager.getDoubleField("SELECT `double` FROM `" + table + "`"), 0);
        assertEquals(0, datamanager.getIntegerField("SELECT `int` FROM `" + table + "`"));
        assertNull(datamanager.getStringField("SELECT `vchar` FROM `" + table + "`"));
    }

    @Test
    public void testQueriesFail() {
        datamanager.executeQueryVoid("SELECT `blah` FROOM `pookit` WHERE thisqueryshouldfail");
        datamanager.updateBlob("a", "b", "1", "alice has a cat");
        try {
            datamanager.increaseField("a", "b", "1");
            fail("Expected SQLException");
        } catch (SQLException ignore) {
        }

    }

    @Test
    public void testStatus() {
        assertFalse(datamanager.isConnected());
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
