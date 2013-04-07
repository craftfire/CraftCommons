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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.util.Date;

import org.junit.Test;

public class TestAbstractValueHolder {

    @Test
    public void testDefaults() {
        AbstractValueHolder holder = new DummyValueHolder();
        assertNull(holder.getString());
        assertEquals(0, holder.getInt());
        assertEquals(0, holder.getLong());
        assertNull(holder.getBigInt());
        assertEquals(0, holder.getFloat(), 0);
        assertEquals(0, holder.getDouble(), 0);
        assertNull(holder.getDecimal());
        assertNull(holder.getBytes());
        assertNull(holder.getDate());
        assertNull(holder.getBlob());
        assertFalse(holder.getBool());
    }

    public class DummyValueHolder extends AbstractValueHolder {

        @Override
        public String getName() {
            return null;
        }

        @Override
        public ValueType getType() {
            return null;
        }

        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public String getString(String defaultValue) {
            assertNull(defaultValue);
            return defaultValue;
        }

        @Override
        public int getInt(int defaultValue) {
            assertEquals(0, defaultValue);
            return defaultValue;

        }

        @Override
        public long getLong(long defaultValue) {
            assertEquals(0, defaultValue);
            return defaultValue;
        }

        @Override
        public BigInteger getBigInt(BigInteger defaultValue) {
            assertNull(defaultValue);
            return defaultValue;
        }

        @Override
        public double getDouble(double defaultValue) {
            assertEquals(0, defaultValue, 0);
            return defaultValue;
        }

        @Override
        public float getFloat(float defaultValue) {
            assertEquals(0, defaultValue, 0);
            return defaultValue;
        }

        @Override
        public BigDecimal getDecimal(BigDecimal defaultValue) {
            assertNull(defaultValue);
            return defaultValue;
        }

        @Override
        public byte[] getBytes(byte[] defaultValue) {
            assertNull(defaultValue);
            return defaultValue;
        }

        @Override
        public Date getDate(Date defaultValue) {
            assertNull(defaultValue);
            return defaultValue;
        }

        @Override
        public Blob getBlob(Blob defaultValue) {
            assertNull(defaultValue);
            return defaultValue;
        }

        @Override
        public boolean getBool(boolean defaultValue) {
            assertFalse(defaultValue);
            return defaultValue;
        }

        @Override
        public boolean isNull() {
            return false;
        }

        @Override
        public boolean isUnsigned() {
            return false;
        }

    }

}
