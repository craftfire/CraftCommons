/*
 * This file is part of CraftCommons.
 *
 * Copyright (c) 2011 CraftFire <http://www.craftfire.com/>
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
package com.craftfire.commons.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.junit.Test;

public class TestValueHolderBase {

    @Test
    public void testTypeDetect() throws SerialException, SQLException {
        assertEquals(ValueType.NULL, ValueHolderBase.typeDetect(null));
        assertEquals(ValueType.STRING, ValueHolderBase.typeDetect("test"));
        assertEquals(ValueType.INTEGER, ValueHolderBase.typeDetect(9));
        assertEquals(ValueType.INTEGER, ValueHolderBase.typeDetect(-1L));
        assertEquals(ValueType.INTEGER, ValueHolderBase.typeDetect(BigInteger.TEN));
        assertEquals(ValueType.REAL, ValueHolderBase.typeDetect(12.5));
        assertEquals(ValueType.REAL, ValueHolderBase.typeDetect(10.5f));
        assertEquals(ValueType.REAL, ValueHolderBase.typeDetect(BigDecimal.valueOf(2.55)));
        assertEquals(ValueType.DATE, ValueHolderBase.typeDetect(new Date()));
        assertEquals(ValueType.BLOB, ValueHolderBase.typeDetect(new SerialBlob(new byte[] { 0, 1, 2, 17, 113, -10 })));
        assertEquals(ValueType.BINARY, ValueHolderBase.typeDetect(new byte[] { 0, 1, 2, 17, 113, -10 }));
        assertEquals(ValueType.BOOLEAN, ValueHolderBase.typeDetect(true));
        assertEquals(ValueType.UNKNOWN, ValueHolderBase.typeDetect(new ArrayList<Object>()));
        assertEquals(ValueType.UNKNOWN, ValueHolderBase.typeDetect(new HashMap<String, Object>()));
    }

    @Test
    public void testConstructors() {
        ValueHolderBase holder = new ValueHolderBase(-8);
        assertFalse(holder.isUnsigned());
        assertEquals("", holder.getName());

        holder = new ValueHolderBase(ValueType.INTEGER, 9);
        assertFalse(holder.isUnsigned());
        assertEquals("", holder.getName());

        holder = new ValueHolderBase(false, 10);
        assertEquals("", holder.getName());

        holder = new ValueHolderBase(ValueType.INTEGER, false, -2L);
        assertEquals("", holder.getName());
    }

    @Test
    public void testGetName() {
        assertEquals("test", new ValueHolderBase("test", false, null).getName());
        assertEquals("test", new ValueHolderBase(ValueType.NULL, "test", false, null).getName());
    }

    @Test
    public void testGetValue() {
        assertEquals(10.0f, new ValueHolderBase(10.0f).getValue());
    }

    @Test
    public void testBinary() {
        ValueHolderBase holder = new ValueHolderBase(ValueType.BINARY, new byte[] { 1, 77, -3 });
        assertEquals(ValueType.BINARY, holder.getType());
        assertArrayEquals(new byte[] { 1, 77, -3 }, (byte[]) holder.getValue());
        assertEquals(new String(new byte[] { 1, 77, -3 }), holder.getString());
        assertEquals(85501, holder.getInt());
        assertEquals(85501, holder.getLong());
        assertEquals(BigInteger.valueOf(85501), holder.getBigInt());
        assertEquals(4.2243106785072420773640808359E-319, holder.getDouble(), 0);
        assertEquals(1.19812419998236184381049803101E-40, holder.getFloat(), 0);
        assertArrayEquals(new byte[] { 1, 77, -3 }, holder.getBytes());
        assertTrue(holder.getBool());
        assertFalse(holder.isNull());
    }

    @Test
    public void testBlob() throws SerialException, SQLException {
        ValueHolderBase holder = new ValueHolderBase(ValueType.BLOB, new SerialBlob(new byte[] { 0, 1, 77, -3, 8, -14, 5, -76, 102 }));
        assertEquals(ValueType.BLOB, holder.getType());
        assertArrayEquals(new byte[] { 0, 1, 77, -3, 8, -14, 5, -76, 102 }, ((Blob) holder.getValue()).getBytes(1, 9));
        assertEquals(new String(new byte[] { 0, 1, 77, -3, 8, -14, 5, -76, 102 }), holder.getString());
        assertEquals(-234507162, holder.getInt());
        assertEquals(94009382106674278L, holder.getLong());
        assertEquals(BigInteger.valueOf(94009382106674278L), holder.getBigInt());
        assertEquals(2.18649212959259492544895369281E-302, holder.getDouble(), 0);
        assertEquals(-2.64829405664964992612190650368E30, holder.getFloat(), 0);
        assertArrayEquals(new byte[] { 0, 1, 77, -3, 8, -14, 5, -76, 102 }, holder.getBytes());
        assertArrayEquals(new byte[] { 0, 1, 77, -3, 8, -14, 5, -76, 102 }, holder.getBlob().getBytes(1, 9));
        assertTrue(holder.getBool());
        assertFalse(holder.isNull());
    }

    @Test
    public void testBool() {
        ValueHolderBase holder = new ValueHolderBase(ValueType.BOOLEAN, true);
        assertEquals(ValueType.BOOLEAN, holder.getType());
        assertEquals(true, holder.getValue());
        assertEquals("true", holder.getString());
        assertEquals(1, holder.getInt());
        assertEquals(1, holder.getLong());
        assertEquals(BigInteger.ONE, holder.getBigInt());
        assertEquals(1, holder.getDouble(), 0);
        assertEquals(1, holder.getFloat(), 0);
        assertEquals(BigDecimal.ONE, holder.getDecimal());
        assertArrayEquals(new byte[] { 1 }, holder.getBytes());
        assertTrue(holder.getBool());
        assertFalse(holder.isNull());

        holder = new ValueHolderBase(ValueType.BOOLEAN, false);
        assertEquals(ValueType.BOOLEAN, holder.getType());
        assertEquals(false, holder.getValue());
        assertEquals("false", holder.getString());
        assertEquals(0, holder.getInt());
        assertEquals(0, holder.getLong());
        assertEquals(BigInteger.ZERO, holder.getBigInt());
        assertEquals(0, holder.getDouble(), 0);
        assertEquals(0, holder.getFloat(), 0);
        assertEquals(BigDecimal.ZERO, holder.getDecimal());
        assertArrayEquals(new byte[] { 0 }, holder.getBytes());
        assertFalse(holder.getBool());
        assertFalse(holder.isNull());
    }

    @Test
    public void testDate() {
        Date now = new Date();
        ValueHolderBase holder = new ValueHolderBase(ValueType.DATE, now);
        assertEquals(ValueType.DATE, holder.getType());
        assertEquals(now, holder.getValue());
        assertEquals(now.toString(), holder.getString());
        assertEquals((int) now.getTime(), holder.getInt());
        assertEquals(now.getTime(), holder.getLong());
        assertEquals(BigInteger.valueOf(now.getTime()), holder.getBigInt());
        assertEquals(now.getTime(), holder.getDouble(), 0);
        assertEquals((float) now.getTime(), holder.getFloat(), 0);
        assertEquals(BigDecimal.valueOf(now.getTime()), holder.getDecimal());
        assertEquals(now, holder.getDate());
        assertTrue(holder.getBool());
        assertFalse(holder.isNull());
    }

    @Test
    public void testInt() {
        ValueHolderBase holder = new ValueHolderBase(ValueType.INTEGER, 480);
        assertEquals(ValueType.INTEGER, holder.getType());
        assertEquals(480, holder.getValue());
        assertEquals("480", holder.getString());
        assertEquals(480, holder.getInt());
        assertEquals(480, holder.getLong());
        assertEquals(BigInteger.valueOf(480), holder.getBigInt());
        assertEquals(480, holder.getDouble(), 0);
        assertEquals(480, holder.getFloat(), 0);
        assertEquals(BigDecimal.valueOf(480), holder.getDecimal());
        assertArrayEquals(new byte[] { 0, 0, 0, 0, 0, 0, 1, -32 }, holder.getBytes());
        assertEquals(new Date(480), holder.getDate());
        assertTrue(holder.getBool());
        assertFalse(holder.isNull());
    }

    @Test
    public void testBigInt() {
        BigInteger bint = BigInteger.valueOf(18798767);
        ValueHolderBase holder = new ValueHolderBase(ValueType.INTEGER, bint);
        assertSame(bint, holder.getBigInt());
    }

    @Test
    public void testNull() {
        ValueHolderBase holder = new ValueHolderBase(ValueType.NULL, null);
        assertEquals(ValueType.NULL, holder.getType());
        assertNull(holder.getValue());
        assertTrue(holder.isNull());
    }

    @Test
    public void testDouble() {
        ValueHolderBase holder = new ValueHolderBase(ValueType.REAL, 480.5);
        assertEquals(ValueType.REAL, holder.getType());
        assertEquals(480.5, holder.getValue());
        assertEquals("480.5", holder.getString());
        assertEquals(480, holder.getInt());
        assertEquals(480, holder.getLong());
        assertEquals(BigInteger.valueOf(480), holder.getBigInt());
        assertEquals(480.5, holder.getDouble(), 0);
        assertEquals(480.5, holder.getFloat(), 0);
        assertEquals(BigDecimal.valueOf(480.5), holder.getDecimal());
        assertArrayEquals(new byte[] { 64, 126, 8, 0, 0, 0, 0, 0 }, holder.getBytes());
        assertTrue(holder.getBool());
        assertFalse(holder.isNull());
    }

    @Test
    public void testDecimal() {
        BigDecimal dec = BigDecimal.valueOf(18798767.05);
        ValueHolderBase holder = new ValueHolderBase(ValueType.REAL, dec);
        assertSame(dec, holder.getDecimal());

    }

    @Test
    public void testString() {
        ValueHolderBase holder = new ValueHolderBase(ValueType.STRING, "14");
        assertEquals(ValueType.STRING, holder.getType());
        assertEquals("14", holder.getValue());
        assertEquals("14", holder.getString());
        assertEquals(14, holder.getInt());
        assertEquals(14, holder.getLong());
        assertEquals(BigInteger.valueOf(14), holder.getBigInt());
        assertEquals(14, holder.getFloat(), 0);
        assertEquals(14, holder.getDouble(), 0);
        assertEquals(BigDecimal.valueOf(14), holder.getDecimal());
        assertArrayEquals("14".getBytes(), holder.getBytes());
        Date defDate = new Date(1789879);
        assertEquals(defDate, holder.getDate(defDate));
        assertTrue(holder.getBool());
        assertFalse(holder.isNull());
    }

    @Test
    public void testDateFormats() {
        Date now = new Date();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(now);
        Calendar cal2 = Calendar.getInstance();

        ValueHolderBase holder = new ValueHolderBase(ValueType.STRING, DateFormat.getDateInstance().format(now));
        cal2.setTime(holder.getDate());
        assertEquals(cal1.get(Calendar.YEAR), cal2.get(Calendar.YEAR));
        assertEquals(cal1.get(Calendar.DAY_OF_YEAR), cal2.get(Calendar.DAY_OF_YEAR));

        holder = new ValueHolderBase(ValueType.STRING, DateFormat.getDateTimeInstance().format(now));
        cal2.setTime(holder.getDate());
        assertEquals(cal1.get(Calendar.YEAR), cal2.get(Calendar.YEAR));
        assertEquals(cal1.get(Calendar.DAY_OF_YEAR), cal2.get(Calendar.DAY_OF_YEAR));
        assertEquals(cal1.get(Calendar.HOUR_OF_DAY), cal2.get(Calendar.HOUR_OF_DAY));
        assertEquals(cal1.get(Calendar.MINUTE), cal2.get(Calendar.MINUTE));
        assertEquals(cal1.get(Calendar.SECOND), cal2.get(Calendar.SECOND));

        holder = new ValueHolderBase(ValueType.STRING, DateFormat.getTimeInstance().format(now));
        cal2.setTime(holder.getDate());
        assertEquals(cal1.get(Calendar.HOUR_OF_DAY), cal2.get(Calendar.HOUR_OF_DAY));
        assertEquals(cal1.get(Calendar.MINUTE), cal2.get(Calendar.MINUTE));
        assertEquals(cal1.get(Calendar.SECOND), cal2.get(Calendar.SECOND));

        holder = new ValueHolderBase(ValueType.STRING, DateFormat.getInstance().format(now));
        cal2.setTime(holder.getDate());
        assertEquals(cal1.get(Calendar.YEAR), cal2.get(Calendar.YEAR));
        assertEquals(cal1.get(Calendar.DAY_OF_YEAR), cal2.get(Calendar.DAY_OF_YEAR));
        assertEquals(cal1.get(Calendar.HOUR_OF_DAY), cal2.get(Calendar.HOUR_OF_DAY));
        assertEquals(cal1.get(Calendar.MINUTE), cal2.get(Calendar.MINUTE));
    }

    @Test
    public void testMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("alice", 1);
        map.put("bob", true);
        ValueHolderBase holder = new ValueHolderBase(map);
        assertEquals(ValueType.UNKNOWN, holder.getType());
        assertEquals(map, holder.getValue());
        assertFalse(holder.isNull());
    }

    @Test
    public void testList() {
        List<Object> list = new ArrayList<Object>();
        list.add("alice");
        list.add(1);
        ValueHolderBase holder = new ValueHolderBase(list);
        assertEquals(ValueType.UNKNOWN, holder.getType());
        assertEquals(list, holder.getValue());
        assertFalse(holder.isNull());
    }

    @Test
    public void testDefaults() throws SerialException, SQLException {
        ValueHolderBase holder = new ValueHolderBase(null);
        Random rnd = new Random();
        long randomLong = rnd.nextLong();
        int randomInt = (int) randomLong;
        double randomDouble = rnd.nextDouble();
        float randomFloat = (float) randomDouble;
        byte[] randomBytes = new byte[10];
        rnd.nextBytes(randomBytes);
        assertEquals("test" + randomInt, holder.getString("test" + randomInt));
        assertEquals(randomInt, holder.getInt(randomInt));
        assertEquals(randomLong, holder.getLong(randomLong));
        assertEquals(BigInteger.valueOf(randomInt), holder.getBigInt(BigInteger.valueOf(randomInt)));
        assertEquals(randomFloat, holder.getFloat(randomFloat), 0);
        assertEquals(randomDouble, holder.getDouble(randomDouble), 0);
        assertEquals(BigDecimal.valueOf(randomDouble), holder.getDecimal(BigDecimal.valueOf(randomDouble)));
        assertArrayEquals(randomBytes, holder.getBytes(randomBytes));
        Blob randomBlob = new SerialBlob(randomBytes);
        assertSame(randomBlob, holder.getBlob(randomBlob));
        assertTrue(holder.getBool(true));
        assertFalse(holder.getBool(false));
    }

    @Test
    public void testToString() {
        ValueHolderBase holder = new ValueHolderBase("ajjhus");
        String string = holder.toString();
        assertTrue(string.contains(String.valueOf(holder.getValue())));
        assertTrue(string.contains(holder.getName()));
    }

    @Test
    public void testTypeCheck() {
        try {
            new ValueHolderBase(ValueType.STRING, 9);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            new ValueHolderBase(ValueType.INTEGER, "abc");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            new ValueHolderBase(ValueType.REAL, "abc");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            new ValueHolderBase(ValueType.INTEGER, 10.5);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            new ValueHolderBase(ValueType.DATE, 9);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            new ValueHolderBase(ValueType.BLOB, 9);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            new ValueHolderBase(ValueType.BINARY, 9);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            new ValueHolderBase(ValueType.BOOLEAN, 9);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

}
