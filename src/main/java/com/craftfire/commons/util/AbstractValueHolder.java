package com.craftfire.commons.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.util.Date;

public abstract class AbstractValueHolder implements ValueHolder {

    @Override
    public String getString() {
        return getString(null);
    }

    @Override
    public int getInt() {
        return getInt(0);
    }

    @Override
    public long getLong() {
        return getLong(0);
    }

    @Override
    public BigInteger getBigInt() {
        return getBigInt(null);
    }

    @Override
    public double getDouble() {
        return getDouble(0);
    }

    @Override
    public float getFloat() {
        return getFloat(0);
    }

    @Override
    public BigDecimal getDecimal() {
        return getDecimal(null);
    }

    @Override
    public byte[] getBytes() {
        return getBytes(null);
    }

    @Override
    public Date getDate() {
        return getDate(null);
    }

    @Override
    public Blob getBlob() {
        return getBlob(null);
    }

    @Override
    public boolean getBool() {
        return getBool(false);
    }

}
