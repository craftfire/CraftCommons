package com.craftfire.commons.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.util.Date;

/**
 * An interface for everything that holds different types of values and (optionally) has a name.
 */
public interface ValueHolder {

    /**
     * Returns the name of the ValueHolder
     * 
     * @return the name
     */
    String getName();

    /**
     * Returns the type of the value.
     * 
     * @return type of the value
     */
    ValueType getType();

    /**
     * Returns the value.
     * 
     * @return the value
     */
    Object getValue();

    /**
     * Returns the value as {@code String}.
     * 
     * @return the value as {@code String}
     */
    String getString();

    /**
     * Returns the value as {@code String}.
     * 
     * @param defaultValue  the default value to return if the value is not a {@code String} and can't be converted to it
     * @return              the value as {@code String} or {@code defaultValue} if the value is not a {@code String} and can't be converted
     */
    String getString(String defaultValue);

    /**
     * Returns the value as {@code int}.
     * 
     * @return the value as {@code int}
     */
    int getInt();

    /**
     * Returns the value as {@code int}.
     * 
     * @param defaultValue  the default value to return if the value is not an {@code int} and can't be converted to it
     * @return              the value as {@code int} or {@code defaultValue} if the value is not an {@code int} and can't be converted
     */
    int getInt(int defaultValue);

    /**
     * Returns the value as {@code long}.
     * 
     * @return the value as {@code long}
     */
    long getLong();

    /**
     * Returns the value as {@code long}.
     * 
     * @param defaultValue  the default value to return if the value is not a {@code long} and can't be converted to it
     * @return              the value as {@code long} or {@code defaultValue} if the value is not a {@code long} and can't be converted
     */
    long getLong(long defaultValue);

    /**
     * Returns the value as {@code BigInteger}.
     * 
     * @return the value as {@code BigInteger}
     */
    BigInteger getBigInt();

    /**
     * Returns the value as {@code BigInteger}.
     * 
     * @param defaultValue  the default value to return if the value is not a {@code BigInteger} and can't be converted to it
     * @return              the value as {@code BigInteger} or {@code defaultValue} if the value is not a {@code BigInteger} and can't be converted
     */
    BigInteger getBigInt(BigInteger defaultValue);

    /**
     * Returns the value as {@code double}.
     * 
     * @return the value as {@code double}
     */
    double getDouble();

    /**
     * Returns the value as {@code double}.
     * 
     * @param defaultValue  the default value to return if the value is not a {@code double} and can't be converted to it
     * @return              the value as {@code double} or {@code defaultValue} if the value is not a {@code double} and can't be converted
     */
    double getDouble(double defaultValue);

    /**
     * Returns the value as {@code float}.
     * 
     * @return the value as {@code float}
     */
    float getFloat();

    /**
     * Returns the value as {@code float}.
     * 
     * @param defaultValue  the default value to return if the value is not a {@code float} and can't be converted to it
     * @return              the value as {@code float} or {@code defaultValue} if the value is not a {@code float} and can't be converted
     */
    float getFloat(float defaultValue);

    /**
     * Returns the value as {@code BigDecimal}.
     * 
     * @return the value as {@code BigDecimal}
     */
    BigDecimal getDecimal();

    /**
     * Returns the value as {@code BigDecimal}.
     * 
     * @param defaultValue  the default value to return if the value is not a {@code BigDecimal} and can't be converted to it
     * @return              the value as {@code BigDeciaml} or {@code defaultValue} if the value is not a {@code BigDecimal} and can't be converted
     */
    BigDecimal getDecimal(BigDecimal defaultValue);

    /**
     * Returns the value as {@code byte} array.
     * 
     * @return the value as {@code byte} array
     */
    byte[] getBytes();

    /**
     * Returns the value as {@code byte} array.
     * 
     * @param defaultValue  the default value to return if the value is not a {@code byte} array and can't be converted to it
     * @return              the value as {@code byte} array or {@code defaultValue} if the value is not a {@code byte} array and can't be converted
     */
    byte[] getBytes(byte[] defaultValue);

    /**
     * Returns the value as {@code Date}.
     * 
     * @return the value as {@code Date}
     */
    Date getDate();

    /**
     * Returns the value as {@code Date}.
     * 
     * @param defaultValue  the default value to return if the value is not a {@code Date} and can't be converted to it
     * @return              the value as {@code Date} or {@code defaultValue} if the value is not a {@code Date} and can't be converted
     */
    Date getDate(Date defaultValue);

    /**
     * Returns the value as {@code Blob}.
     * 
     * @return the value as {@code Blob}
     */
    Blob getBlob();

    /**
     * Returns the value as {@code Blob}.
     * 
     * @param defaultValue  the default value to return if the value is not a {@code Blob} and can't be converted to it
     * @return              the value as {@code Blob} or {@code defaultValue} if the value is not a {@code Blob} and can't be converted
     */
    Blob getBlob(Blob defaultValue);

    /**
     * Returns the value as {@code boolean}.
     * 
     * @return the value as {@code boolean}
     */
    boolean getBool();

    /**
     * Returns the value as {@code boolean}.
     * 
     * @param defaultValue  the default value to return if the value is not a {@code boolean} and can't be converted to it
     * @return              the value as {@code boolean} or {@code defaultValue} if the value is not a {@code boolean} and can't be converted
     */
    boolean getBool(boolean defaultValue);

    /**
     * Checks if the value is null.
     * 
     * @return {@code true} if it's {@code null}, {@code false} otherwise
     */
    boolean isNull();

    /**
     * Checks if the value is unsigned.
     * 
     * @return {@code true} if it's unsigned, {@code false} otherwise
     */
    boolean isUnsigned();

}