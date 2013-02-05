package com.craftfire.commons.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.util.Date;

public interface ValueHolder {

    String getName();

    ValueType getType();

    Object getValue();

    String getString();

    String getString(String defaultValue);

    int getInt();

    int getInt(int defaultValue);

    long getLong();

    long getLong(long defaultValue);

    BigInteger getBigInt();

    BigInteger getBigInt(BigInteger defValue);

    double getDouble();

    double getDouble(double defaultValue);

    float getFloat();

    float getFloat(float defaultValue);

    BigDecimal getDecimal();

    BigDecimal getDecimal(BigDecimal defaultValue);

    byte[] getBytes();

    byte[] getBytes(byte[] defaultValue);

    Date getDate();

    Date getDate(Date defaultValue);

    Blob getBlob();

    Blob getBlob(Blob defaultValue);

    boolean getBool();

    boolean getBool(boolean defaultValue);

    boolean isNull();

    boolean isUnsigned();

}