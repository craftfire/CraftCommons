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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.util.Date;

/**
 * An abstract implementation of {@link ValueHolder}.
 * <p>
 * It redirects all getters without default value to ones with default value, using {@code null}, {@code 0} or {@code false} as the default value.
 */
public abstract class AbstractValueHolder implements ValueHolder {

    /* (non-Javadoc)
     * @see com.craftfire.commons.util.ValueHolder#getString()
     */
    @Override
    public String getString() {
        return getString(null);
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.util.ValueHolder#getInt()
     */
    @Override
    public int getInt() {
        return getInt(0);
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.util.ValueHolder#getLong()
     */
    @Override
    public long getLong() {
        return getLong(0);
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.util.ValueHolder#getBigInt()
     */
    @Override
    public BigInteger getBigInt() {
        return getBigInt(null);
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.util.ValueHolder#getDouble()
     */
    @Override
    public double getDouble() {
        return getDouble(0);
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.util.ValueHolder#getFloat()
     */
    @Override
    public float getFloat() {
        return getFloat(0);
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.util.ValueHolder#getDecimal()
     */
    @Override
    public BigDecimal getDecimal() {
        return getDecimal(null);
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.util.ValueHolder#getBytes()
     */
    @Override
    public byte[] getBytes() {
        return getBytes(null);
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.util.ValueHolder#getDate()
     */
    @Override
    public Date getDate() {
        return getDate(null);
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.util.ValueHolder#getBlob()
     */
    @Override
    public Blob getBlob() {
        return getBlob(null);
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.util.ValueHolder#getBool()
     */
    @Override
    public boolean getBool() {
        return getBool(false);
    }

}
