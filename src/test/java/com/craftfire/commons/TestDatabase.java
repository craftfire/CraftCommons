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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.junit.BeforeClass;
import org.junit.Test;

import com.craftfire.commons.database.DataField;
import com.craftfire.commons.database.DataManager;
import com.craftfire.commons.database.DataRow;
import com.craftfire.commons.database.DataType;
import com.craftfire.commons.database.FieldType;

public class TestDatabase {
    private static final String table = "typetest";
    private static DataManager datamanager;

    @BeforeClass
    public static void init() {
        String user = "sa";
        String password = "";
        datamanager = new DataManager(DataType.H2, user, password);
        datamanager.getLogging().getLogger().setLevel(Level.OFF); //Turn off logging temporarily so we won't be spammed with red warnings. 
        datamanager.setDatabase("test");
        datamanager.setDirectory("./src/test/resource/");
        datamanager.setTimeout(0);
        datamanager.setKeepAlive(true);
        datamanager.setPrefix("");
    }

    @Test
    public void testBInt() throws SQLException {
        final String name = "bint";
        DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();

        // DataRow.getField()
        DataField field = row.get(name);
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDate());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.getField()
        field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDate());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.get<Kinda>Field()
        assertTrue(datamanager.getBooleanField(table, name, "1"));
        assertNotNull(datamanager.getBinaryField(table, name, "1"));
        assertNotNull(datamanager.getBlobField(table, name, "1"));
        assertThat(datamanager.getDoubleField(table, name, "1"), not(equalTo(0d)));
        assertNotNull(datamanager.getStringField(table, name, "1"));

        // DataRow.get<Kinda>Field()
        assertNotNull(row.getBigIntField(name));
        assertTrue(row.getBoolField(name));
        assertNotNull(row.getBinaryField(name));
        assertNotNull(row.getDateField(name));
        assertNotNull(row.getDecimalField(name));
        assertThat(row.getDoubleField(name), not(equalTo(0d)));
        assertThat(row.getFloatField(name), not(equalTo(0f)));
        assertThat(row.getIntField(name), not(equalTo(0)));
        assertThat(row.getLongField(name), not(equalTo(0L)));
        assertNotNull(row.getStringField(name));
    }

    @Test
    public void testBin() throws SQLException {
        final String name = "bin";
        DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();

        // DataRow.getField()
        DataField field = row.get(name);
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.getField()
        field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.get<Kinda>Field()
        assertNotNull(datamanager.getBinaryField(table, name, "1"));
        assertNotNull(datamanager.getBlobField(table, name, "1"));
        assertThat(datamanager.getIntegerField(table, name, "1"), not(equalTo(0)));
        assertNotNull(datamanager.getStringField(table, name, "1"));

        // DataRow.get<Kinda>Field()
        assertNotNull(row.getBigIntField(name));
        assertTrue(row.getBoolField(name));
        assertNotNull(row.getBinaryField(name));
        assertThat(row.getDoubleField(name), not(equalTo(0d)));
        assertThat(row.getFloatField(name), not(equalTo(0f)));
        assertThat(row.getIntField(name), not(equalTo(0)));
        assertThat(row.getLongField(name), not(equalTo(0L)));
        assertNotNull(row.getStringField(name));
    }

    @Test
    public void testBlob() throws SQLException {
        final String name = "blob";
        DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();

        // DataRow.getField()
        DataField field = row.get(name);
        assertNotNull(field.getBigInt());
        assertNotNull(field.getBlob());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.getField()
        field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
        assertNotNull(field.getBigInt());
        assertNotNull(field.getBlob());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.get<Kinda>Field()
        assertNotNull(datamanager.getBinaryField(table, name, "1"));
        assertNotNull(datamanager.getBlobField(table, name, "1"));
        assertNotNull(datamanager.getStringField(table, name, "1"));

        // DataRow.get<Kinda>Field()
        assertNotNull(row.getBigIntField(name));
        assertNotNull(row.getBlobField(name));
        assertTrue(row.getBoolField(name));
        assertNotNull(row.getBinaryField(name));
        assertThat(row.getDoubleField(name), not(equalTo(0d)));
        assertThat(row.getFloatField(name), not(equalTo(0f)));
        assertThat(row.getIntField(name), not(equalTo(0)));
        assertThat(row.getLongField(name), not(equalTo(0L)));
        assertNotNull(row.getStringField(name));
    }

    @Test
    public void testBool() throws SQLException {
        final String name = "bool";
        DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();

        // DataRow.getField()
        DataField field = row.get(name);
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.getField()
        field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.get<Kinda>Field()
        assertTrue(datamanager.getBooleanField(table, name, "1"));
        assertNotNull(datamanager.getBlobField(table, name, "1"));
        assertThat(datamanager.getDoubleField(table, name, "1"), not(equalTo(0d)));
        assertThat(datamanager.getIntegerField(table, name, "1"), not(equalTo(0)));
        assertNotNull(datamanager.getStringField(table, name, "1"));

        // DataRow.get<Kinda>Field()
        assertNotNull(row.getBigIntField(name));
        assertTrue(row.getBoolField(name));
        assertNotNull(row.getBinaryField(name));
        assertNotNull(row.getDecimalField(name));
        assertThat(row.getDoubleField(name), not(equalTo(0d)));
        assertThat(row.getFloatField(name), not(equalTo(0f)));
        assertThat(row.getIntField(name), not(equalTo(0)));
        assertThat(row.getLongField(name), not(equalTo(0L)));
        assertNotNull(row.getStringField(name));
    }

    @Test
    public void testChar() throws SQLException {
        final String name = "char";
        DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();

        // DataRow.getField()
        DataField field = row.get(name);
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.getField()
        field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.get<Kinda>Field()
        assertTrue(datamanager.getBooleanField(table, name, "1"));
        assertNotNull(datamanager.getBlobField(table, name, "1"));
        assertThat(datamanager.getDoubleField(table, name, "1"), not(equalTo(0d)));
        assertNotNull(datamanager.getStringField(table, name, "1"));

        // DataRow.get<Kinda>Field()
        assertTrue(row.getBoolField(name));
        assertNotNull(row.getBinaryField(name));
        assertNotNull(row.getDecimalField(name));
        assertThat(row.getDoubleField(name), not(equalTo(0d)));
        assertThat(row.getFloatField(name), not(equalTo(0f)));
        assertThat(row.getIntField(name), not(equalTo(0)));
        assertThat(row.getLongField(name), not(equalTo(0L)));
        assertNotNull(row.getStringField(name));
    }

    @Test
    public void testClob() throws SQLException {
        final String name = "clob";
        DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();

        // DataRow.getField()
        DataField field = row.get(name);
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        if (field.getDate() == null) {
            // Just for debug purposes, to see what is the locale of host.
            System.out.println(DateFormat.getDateInstance().format(new Date()));
            System.out.println(DateFormat.getDateTimeInstance().format(new Date()));
        System.out.println(DateFormat.getTimeInstance().format(new Date()));
            System.out.println(DateFormat.getInstance().format(new Date()));
        }
        assertNotNull(field.getDate());
        assertNotNull(field.getString());

        // DataManager.getField()
        field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDate());
        assertNotNull(field.getString());

        // DataManager.get<Kinda>Field()
        assertNotNull(datamanager.getBlobField(table, name, "1"));
        assertNotNull(datamanager.getStringField(table, name, "1"));

        // DataRow.get<Kinda>Field()
        assertTrue(row.getBoolField(name));
        assertNotNull(row.getBinaryField(name));
        assertNotNull(row.getDateField(name));
        assertNotNull(row.getStringField(name));
    }

    @Test
    public void testDate() throws SQLException {
        final String name = "date";
        DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();

        // DataRow.getField()
        DataField field = row.get(name);
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getDate());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.getField()
        field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getDate());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.get<Kinda>Field()
        assertNotNull(datamanager.getBlobField(table, name, "1"));
        assertNotNull(datamanager.getDateField(table, name, "1"));
        assertNotNull(datamanager.getStringField(table, name, "1"));

        // DataRow.get<Kinda>Field()
        assertNotNull(row.getBigIntField(name));
        assertTrue(row.getBoolField(name));
        assertNotNull(row.getDateField(name));
        assertNotNull(row.getDecimalField(name));
        assertThat(row.getDoubleField(name), not(equalTo(0d)));
        assertThat(row.getFloatField(name), not(equalTo(0f)));
        assertThat(row.getIntField(name), not(equalTo(0)));
        assertThat(row.getLongField(name), not(equalTo(0L)));
        assertNotNull(row.getStringField(name));
    }

    @Test
    public void testDecimal() throws SQLException {
        final String name = "dec";
        DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();

        // DataRow.getField()
        DataField field = row.get(name);
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.getField()
        field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.get<Kinda>Field()
        assertTrue(datamanager.getBooleanField(table, name, "1"));
        assertNotNull(datamanager.getBlobField(table, name, "1"));
        assertThat(datamanager.getDoubleField(table, name, "1"), not(equalTo(0d)));
        assertThat(datamanager.getIntegerField(table, name, "1"), not(equalTo(0)));
        assertNotNull(datamanager.getStringField(table, name, "1"));

        // DataRow.get<Kinda>Field()
        assertNotNull(row.getBigIntField(name));
        assertTrue(row.getBoolField(name));
        assertNotNull(row.getBinaryField(name));
        assertNotNull(row.getDecimalField(name));
        assertThat(row.getDoubleField(name), not(equalTo(0d)));
        assertThat(row.getFloatField(name), not(equalTo(0f)));
        assertThat(row.getIntField(name), not(equalTo(0)));
        assertThat(row.getLongField(name), not(equalTo(0L)));
        assertNotNull(row.getStringField(name));
    }

    @Test
    public void testDouble() throws SQLException {
        final String name = "double";
        DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();

        // DataRow.getField()
        DataField field = row.get(name);
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.getField()
        field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.get<Kinda>Field()
        assertTrue(datamanager.getBooleanField(table, name, "1"));
        assertNotNull(datamanager.getBlobField(table, name, "1"));
        assertThat(datamanager.getDoubleField(table, name, "1"), not(equalTo(0d)));
        assertThat(datamanager.getIntegerField(table, name, "1"), not(equalTo(0)));
        assertNotNull(datamanager.getStringField(table, name, "1"));

        // DataRow.get<Kinda>Field()
        assertNotNull(row.getBigIntField(name));
        assertTrue(row.getBoolField(name));
        assertNotNull(row.getBinaryField(name));
        assertNotNull(row.getDecimalField(name));
        assertThat(row.getDoubleField(name), not(equalTo(0d)));
        assertThat(row.getFloatField(name), not(equalTo(0f)));
        assertThat(row.getIntField(name), not(equalTo(0)));
        assertThat(row.getLongField(name), not(equalTo(0L)));
        assertNotNull(row.getStringField(name));
    }

    @Test
    public void testFloat() throws SQLException {
        final String name = "float";
        DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();

        // DataRow.getField()
        DataField field = row.get(name);
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.getField()
        field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.get<Kinda>Field()
        assertTrue(datamanager.getBooleanField(table, name, "1"));
        assertNotNull(datamanager.getBlobField(table, name, "1"));
        assertThat(datamanager.getDoubleField(table, name, "1"), not(equalTo(0d)));
        assertThat(datamanager.getIntegerField(table, name, "1"), not(equalTo(0)));
        assertNotNull(datamanager.getStringField(table, name, "1"));

        // DataRow.get<Kinda>Field()
        assertNotNull(row.getBigIntField(name));
        assertTrue(row.getBoolField(name));
        assertNotNull(row.getBinaryField(name));
        assertNotNull(row.getDecimalField(name));
        assertThat(row.getDoubleField(name), not(equalTo(0d)));
        assertThat(row.getFloatField(name), not(equalTo(0f)));
        assertThat(row.getIntField(name), not(equalTo(0)));
        assertThat(row.getLongField(name), not(equalTo(0L)));
        assertNotNull(row.getStringField(name));
    }

    @Test
    public void testVCharI() throws SQLException {
        final String name = "vchari";
        DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();

        // DataRow.getField()
        DataField field = row.get(name);
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.getField()
        field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.get<Kinda>Field()
        assertNotNull(datamanager.getBlobField(table, name, "1"));
        assertNotNull(datamanager.getStringField(table, name, "1"));

        // DataRow.get<Kinda>Field()
        assertTrue(row.getBoolField(name));
        assertNotNull(row.getBinaryField(name));
        assertThat(row.getDoubleField(name), not(equalTo(0d)));
        assertThat(row.getFloatField(name), not(equalTo(0f)));
        assertThat(row.getIntField(name), not(equalTo(0)));
        assertThat(row.getLongField(name), not(equalTo(0L)));
        assertNotNull(row.getStringField(name));
    }

    @Test
    public void testInt() throws SQLException {
        final String name = "int";
        DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();

        // DataRow.getField()
        DataField field = row.get(name);
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDate());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.getField()
        field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDate());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.get<Kinda>Field()
        assertTrue(datamanager.getBooleanField(table, name, "1"));
        assertNotNull(datamanager.getBinaryField(table, name, "1"));
        assertNotNull(datamanager.getBlobField(table, name, "1"));
        assertThat(datamanager.getDoubleField(table, name, "1"), not(equalTo(0d)));
        assertThat(datamanager.getIntegerField(table, name, "1"), not(equalTo(0)));
        assertNotNull(datamanager.getStringField(table, name, "1"));

        // DataRow.get<Kinda>Field()
        assertNotNull(row.getBigIntField(name));
        assertTrue(row.getBoolField(name));
        assertNotNull(row.getBinaryField(name));
        assertNotNull(row.getDateField(name));
        assertNotNull(row.getDecimalField(name));
        assertThat(row.getDoubleField(name), not(equalTo(0d)));
        assertThat(row.getFloatField(name), not(equalTo(0f)));
        assertThat(row.getIntField(name), not(equalTo(0)));
        assertThat(row.getLongField(name), not(equalTo(0L)));
        assertNotNull(row.getStringField(name));
    }

    @Test
    public void testLVBin() throws SQLException {
        final String name = "lvbin";
        DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();

        // DataRow.getField()
        DataField field = row.get(name);
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.getField()
        field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.get<Kinda>Field()
        assertNotNull(datamanager.getBinaryField(table, name, "1"));
        assertNotNull(datamanager.getBlobField(table, name, "1"));
        assertNotNull(datamanager.getStringField(table, name, "1"));

        // DataRow.get<Kinda>Field()
        assertNotNull(row.getBigIntField(name));
        assertTrue(row.getBoolField(name));
        assertNotNull(row.getBinaryField(name));
        assertThat(row.getDoubleField(name), not(equalTo(0d)));
        assertThat(row.getFloatField(name), not(equalTo(0f)));
        assertThat(row.getIntField(name), not(equalTo(0)));
        assertThat(row.getLongField(name), not(equalTo(0L)));
        assertNotNull(row.getStringField(name));
    }

    @Test
    public void testLVChar() throws SQLException {
        final String name = "lvchar";
        DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();

        // DataRow.getField()
        DataField field = row.get(name);
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getString());

        // DataManager.getField()
        field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getString());

        // DataManager.get<Kinda>Field()
        assertNotNull(datamanager.getBlobField(table, name, "1"));
        assertNotNull(datamanager.getStringField(table, name, "1"));

        // DataRow.get<Kinda>Field()
        assertTrue(row.getBoolField(name));
        assertNotNull(row.getBinaryField(name));
        assertNotNull(row.getStringField(name));
    }

    @Test
    public void testNumeric() throws SQLException {
        final String name = "numeric";
        DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();

        // DataRow.getField()
        DataField field = row.get(name);
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.getField()
        field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.get<Kinda>Field()
        assertTrue(datamanager.getBooleanField(table, name, "1"));
        assertNotNull(datamanager.getBinaryField(table, name, "1"));
        assertNotNull(datamanager.getBlobField(table, name, "1"));
        assertThat(datamanager.getDoubleField(table, name, "1"), not(equalTo(0d)));
        assertThat(datamanager.getIntegerField(table, name, "1"), not(equalTo(0)));
        assertNotNull(datamanager.getStringField(table, name, "1"));

        // DataRow.get<Kinda>Field()
        assertNotNull(row.getBigIntField(name));
        assertTrue(row.getBoolField(name));
        assertNotNull(row.getBinaryField(name));
        assertNotNull(row.getDecimalField(name));
        assertThat(row.getDoubleField(name), not(equalTo(0d)));
        assertThat(row.getFloatField(name), not(equalTo(0f)));
        assertThat(row.getIntField(name), not(equalTo(0)));
        assertThat(row.getLongField(name), not(equalTo(0L)));
        assertNotNull(row.getStringField(name));
    }

    @Test
    public void testReal() throws SQLException {
        final String name = "real";
        DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();

        // DataRow.getField()
        DataField field = row.get(name);
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.getField()
        field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.get<Kinda>Field()
        assertTrue(datamanager.getBooleanField(table, name, "1"));
        assertNotNull(datamanager.getBlobField(table, name, "1"));
        assertThat(datamanager.getDoubleField(table, name, "1"), not(equalTo(0d)));
        assertThat(datamanager.getIntegerField(table, name, "1"), not(equalTo(0)));
        assertNotNull(datamanager.getStringField(table, name, "1"));

        // DataRow.get<Kinda>Field()
        assertNotNull(row.getBigIntField(name));
        assertTrue(row.getBoolField(name));
        assertNotNull(row.getBinaryField(name));
        assertNotNull(row.getDecimalField(name));
        assertThat(row.getDoubleField(name), not(equalTo(0d)));
        assertThat(row.getFloatField(name), not(equalTo(0f)));
        assertThat(row.getIntField(name), not(equalTo(0)));
        assertThat(row.getLongField(name), not(equalTo(0L)));
        assertNotNull(row.getStringField(name));
    }

    @Test
    public void testSInt() throws SQLException {
        final String name = "sint";
        DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();

        // DataRow.getField()
        DataField field = row.get(name);
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDate());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.getField()
        field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDate());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.get<Kinda>Field()
        assertTrue(datamanager.getBooleanField(table, name, "1"));
        assertNotNull(datamanager.getBinaryField(table, name, "1"));
        assertNotNull(datamanager.getBlobField(table, name, "1"));
        assertThat(datamanager.getDoubleField(table, name, "1"), not(equalTo(0d)));
        assertThat(datamanager.getIntegerField(table, name, "1"), not(equalTo(0)));
        assertNotNull(datamanager.getStringField(table, name, "1"));

        // DataRow.get<Kinda>Field()
        assertNotNull(row.getBigIntField(name));
        assertTrue(row.getBoolField(name));
        assertNotNull(row.getBinaryField(name));
        assertNotNull(row.getDateField(name));
        assertNotNull(row.getDecimalField(name));
        assertThat(row.getDoubleField(name), not(equalTo(0d)));
        assertThat(row.getFloatField(name), not(equalTo(0f)));
        assertThat(row.getIntField(name), not(equalTo(0)));
        assertThat(row.getLongField(name), not(equalTo(0L)));
        assertNotNull(row.getStringField(name));
    }

    @Test
    public void testTime() throws SQLException {
        final String name = "time";
        DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();

        // DataRow.getField()
        DataField field = row.get(name);
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getDate());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.getField()
        field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getDate());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.get<Kinda>Field()
        assertNotNull(datamanager.getBlobField(table, name, "1"));
        assertNotNull(datamanager.getDateField(table, name, "1"));
        assertNotNull(datamanager.getStringField(table, name, "1"));

        // DataRow.get<Kinda>Field()
        assertNotNull(row.getBigIntField(name));
        assertTrue(row.getBoolField(name));
        assertNotNull(row.getDateField(name));
        assertNotNull(row.getDecimalField(name));
        assertThat(row.getDoubleField(name), not(equalTo(0d)));
        assertThat(row.getFloatField(name), not(equalTo(0f)));
        assertThat(row.getIntField(name), not(equalTo(0)));
        assertThat(row.getLongField(name), not(equalTo(0L)));
        assertNotNull(row.getStringField(name));
    }

    @Test
    public void testTimeStamp() throws SQLException {
        final String name = "timestamp";
        DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();

        // DataRow.getField()
        DataField field = row.get(name);
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getDate());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.getField()
        field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getDate());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.get<Kinda>Field()
        assertNotNull(datamanager.getBlobField(table, name, "1"));
        assertNotNull(datamanager.getDateField(table, name, "1"));
        assertNotNull(datamanager.getStringField(table, name, "1"));

        // DataRow.get<Kinda>Field()
        assertNotNull(row.getBigIntField(name));
        assertTrue(row.getBoolField(name));
        assertNotNull(row.getDateField(name));
        assertNotNull(row.getDecimalField(name));
        assertThat(row.getDoubleField(name), not(equalTo(0d)));
        assertThat(row.getFloatField(name), not(equalTo(0f)));
        assertThat(row.getIntField(name), not(equalTo(0)));
        assertThat(row.getLongField(name), not(equalTo(0L)));
        assertNotNull(row.getStringField(name));
    }

    @Test
    public void testTInt() throws SQLException {
        final String name = "tint";
        DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();

        // DataRow.getField()
        DataField field = row.get(name);
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDate());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.getField()
        field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDate());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.get<Kinda>Field()
        assertTrue(datamanager.getBooleanField(table, name, "1"));
        assertNotNull(datamanager.getBinaryField(table, name, "1"));
        assertNotNull(datamanager.getBlobField(table, name, "1"));
        assertThat(datamanager.getDoubleField(table, name, "1"), not(equalTo(0d)));
        assertThat(datamanager.getIntegerField(table, name, "1"), not(equalTo(0)));
        assertNotNull(datamanager.getStringField(table, name, "1"));

        // DataRow.get<Kinda>Field()
        assertNotNull(row.getBigIntField(name));
        assertTrue(row.getBoolField(name));
        assertNotNull(row.getBinaryField(name));
        assertNotNull(row.getDateField(name));
        assertNotNull(row.getDecimalField(name));
        assertThat(row.getDoubleField(name), not(equalTo(0d)));
        assertThat(row.getFloatField(name), not(equalTo(0f)));
        assertThat(row.getIntField(name), not(equalTo(0)));
        assertThat(row.getLongField(name), not(equalTo(0L)));
        assertNotNull(row.getStringField(name));
    }

    @Test
    public void testVBin() throws SQLException {
        final String name = "vbin";
        DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();

        // DataRow.getField()
        DataField field = row.get(name);
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.getField()
        field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
        assertNotNull(field.getBigInt());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.get<Kinda>Field()
        assertNotNull(datamanager.getBinaryField(table, name, "1"));
        assertNotNull(datamanager.getBlobField(table, name, "1"));
        assertThat(datamanager.getIntegerField(table, name, "1"), not(equalTo(0)));
        assertNotNull(datamanager.getStringField(table, name, "1"));

        // DataRow.get<Kinda>Field()
        assertNotNull(row.getBigIntField(name));
        assertTrue(row.getBoolField(name));
        assertNotNull(row.getBinaryField(name));
        assertThat(row.getDoubleField(name), not(equalTo(0d)));
        assertThat(row.getFloatField(name), not(equalTo(0f)));
        assertThat(row.getIntField(name), not(equalTo(0)));
        assertThat(row.getLongField(name), not(equalTo(0L)));
        assertNotNull(row.getStringField(name));
    }

    @Test
    public void testVChar() throws SQLException {
        final String name = "vchar";
        DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();

        // DataRow.getField()
        DataField field = row.get(name);
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getString());

        // DataManager.getField()
        field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getString());

        // DataManager.get<Kinda>Field()
        assertNotNull(datamanager.getBlobField(table, name, "1"));
        assertNotNull(datamanager.getStringField(table, name, "1"));

        // DataRow.get<Kinda>Field()
        assertTrue(row.getBoolField(name));
        assertNotNull(row.getBinaryField(name));
        assertNotNull(row.getStringField(name));
    }

/*    public void testTemplate() throws SQLException {
        final String name = "";
        DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();

        // DataRow.getField()
        DataField field = row.get(name);
        assertNotNull(field.getBigInt());
        assertNotNull(field.getBlob());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDate());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.getField()
        field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
        assertNotNull(field.getBigInt());
        assertNotNull(field.getBlob());
        assertTrue(field.getBool());
        assertNotNull(field.getBytes());
        assertNotNull(field.getDate());
        assertNotNull(field.getDecimal());
        assertThat(field.getDouble(), not(equalTo(0d)));
        assertThat(field.getFloat(), not(equalTo(0f)));
        assertThat(field.getInt(), not(equalTo(0)));
        assertThat(field.getLong(), not(equalTo(0L)));
        assertNotNull(field.getString());

        // DataManager.get<Kinda>Field()
        assertTrue(datamanager.getBooleanField(table, name, "1"));
        assertNotNull(datamanager.getBinaryField(table, name, "1"));
        assertNotNull(datamanager.getBlobField(table, name, "1"));
        assertNotNull(datamanager.getDateField(table, name, "1"));
        assertThat(datamanager.getDoubleField(table, name, "1"), not(equalTo(0d)));
        assertThat(datamanager.getIntegerField(table, name, "1"), not(equalTo(0)));
        assertNotNull(datamanager.getStringField(table, name, "1"));

        // DataRow.get<Kinda>Field()
        assertNotNull(row.getBigIntField(name));
        assertNotNull(row.getBlobField(name));
        assertTrue(row.getBoolField(name));
        assertNotNull(row.getBinaryField(name));
        assertNotNull(row.getDateField(name));
        assertNotNull(row.getDecimalField(name));
        assertThat(row.getDoubleField(name), not(equalTo(0d)));
        assertThat(row.getFloatField(name), not(equalTo(0f)));
        assertThat(row.getIntField(name), not(equalTo(0)));
        assertThat(row.getLongField(name), not(equalTo(0L)));
        assertNotNull(row.getStringField(name));
    }
*/
}
