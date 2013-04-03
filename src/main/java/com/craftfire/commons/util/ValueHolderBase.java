package com.craftfire.commons.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class ValueHolderBase implements ValueHolder {

    protected final String name;
    protected final Object value;
    protected final ValueType type;
    protected final boolean unsigned;

    protected static ValueType typeDetect(Object data) {
        if (data == null) {
            return ValueType.NULL;
        } else if (data instanceof String) {
            return ValueType.STRING;
        } else if (data instanceof Number) {
            if (((Number) data).doubleValue() == ((Number) data).longValue()) {
                return ValueType.INTEGER;
            } else {
                return ValueType.REAL;
            }
        } else if (data instanceof Date) {
            return ValueType.DATE;
        } else if (data instanceof Blob) {
            return ValueType.BLOB;
        } else if (data instanceof byte[]) {
            return ValueType.BINARY;
        } else if (data instanceof Boolean) {
            return ValueType.BOOLEAN;
        }
        //TODO: DataManager.getLogManager().warning("Unknown data type: " + data.toString());
        return ValueType.UNKNOWN;
    }

    protected void typeCheck() {
        IllegalArgumentException e = new IllegalArgumentException("Data: "
                + this.value + " doesn't match the type: "
                + this.type.name());
        if (this.type.equals(ValueType.STRING)) {
            if (!(this.value instanceof String)) {
                throw e;
            }
        } else if (this.type.equals(ValueType.INTEGER) || this.type.equals(ValueType.REAL)) {
            if (!(this.value instanceof Number)) {
                throw e;
            }
        } else if (this.type.equals(ValueType.DATE)) {
            if (!(this.value instanceof Date)) {
                throw e;
            }
        } else if (this.type.equals(ValueType.BLOB)) {
            if (!(this.value instanceof Blob)) {
                throw e;
            }
        } else if (this.type.equals(ValueType.BINARY)) {
            if (!(this.value instanceof byte[])) {
                throw e;
            }
        } else if (this.type.equals(ValueType.BOOLEAN)) {
            if (!(this.value instanceof Boolean)) {
                throw e;
            }
        }
    }

    public ValueHolderBase(Object data) {
        this(false, data);
    }

    public ValueHolderBase(ValueType type, Object data) {
        this(type, false, data);
    }

    public ValueHolderBase(boolean unsigned, Object data) {
        this("", unsigned, data);
    }

    public ValueHolderBase(ValueType type, boolean unsigned, Object data) {
        this(type, "", unsigned, data);
    }

    public ValueHolderBase(String name, boolean unsigned, Object data) {
        this(typeDetect(data), name, unsigned, data);
    }

    public ValueHolderBase(ValueType type, String name, boolean unsigned, Object data) {
        if (data == null) {
            this.type = ValueType.NULL;
        } else {
            this.type = type;
        }
        this.name = name;
        this.unsigned = unsigned;
        this.value = data;
        typeCheck();
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.database.IValueHolder#getFieldName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.database.IValueHolder#getFieldType()
     */
    @Override
    public ValueType getType() {
        return this.type;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.database.IValueHolder#getData()
     */
    @Override
    public Object getValue() {
        return this.value;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.database.IValueHolder#getString()
     */
    @Override
    public String getString() {
        if (getType().equals(ValueType.STRING)) {
            return (String) this.value;
        } else if (getType().equals(ValueType.BINARY)
                || getType().equals(ValueType.BLOB)) {
            return new String(getBytes());
        }
        return this.value.toString();
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.database.IValueHolder#getInt()
     */
    @Override
    public int getInt() {
        return (int) getLong();
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.database.IValueHolder#getLong()
     */
    @Override
    public long getLong() {
        if (getType().equals(ValueType.INTEGER)
                || getType().equals(ValueType.REAL)) {
            return ((Number) this.value).longValue();
        } else if (getType().equals(ValueType.BOOLEAN)) {
            return ((Boolean) this.value).booleanValue() ? 1 : 0;
        } else if (getType().equals(ValueType.DATE)) {
            return ((Date) this.value).getTime();
        } else if (getType().equals(ValueType.BINARY)
                || getType().equals(ValueType.BLOB)) {
            byte[] bytes = { 0, 0, 0, 0, 0, 0, 0, 0 };
            byte[] bytes1 = getBytes();
            if (bytes1.length >= 8) {
                System.arraycopy(bytes1, 0, bytes, 0, 8);
            } else {
                System.arraycopy(bytes1, 0, bytes, 8 - bytes1.length,
                        bytes1.length);
            }
            return ByteBuffer.wrap(bytes).getLong();
        } else if (getType().equals(ValueType.STRING)) {
            try {
                return Long.parseLong((String) this.value);
            } catch (NumberFormatException e) {
            }
            return new Double(getDouble()).longValue();
        }
        return 0;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.database.IValueHolder#getBigInt()
     */
    @Override
    public BigInteger getBigInt() {
        try {
            if (this.value instanceof BigInteger) {
                return (BigInteger) this.value;
            } else if (getType().equals(ValueType.BOOLEAN)) {
                return ((Boolean) this.value).booleanValue() ? BigInteger.ONE
                        : BigInteger.ZERO;
            } else if (getType().equals(ValueType.BINARY)
                    || getType().equals(ValueType.BLOB)) {
                byte[] bytes = getBytes();
                return new BigInteger(bytes);
            } else if (getType().equals(ValueType.STRING)) {
                return new BigInteger(this.value.toString());
            } else {
                long l = 0;
                if (getType().equals(ValueType.INTEGER)
                        || getType().equals(ValueType.REAL)) {
                    l = ((Number) this.value).longValue();
                } else if (getType().equals(ValueType.DATE)) {
                    l = ((Date) this.value).getTime();
                } else {
                    return null;
                }
                return new BigInteger(String.valueOf(l));
            }
        } catch (NumberFormatException e) {
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.database.IValueHolder#getDouble()
     */
    @Override
    public double getDouble() {
        if (getType().equals(ValueType.INTEGER)
                || getType().equals(ValueType.REAL)) {
            return ((Number) this.value).doubleValue();
        } else if (getType().equals(ValueType.BOOLEAN)) {
            return ((Boolean) this.value).booleanValue() ? 1 : 0;
        } else if (getType().equals(ValueType.DATE)) {
            return new Long(((Date) this.value).getTime()).doubleValue();
        } else if (getType().equals(ValueType.BINARY)
                || getType().equals(ValueType.BLOB)) {
            byte[] bytes = { 0, 0, 0, 0, 0, 0, 0, 0 };
            byte[] bytes1 = getBytes();
            if (bytes1.length >= 8) {
                System.arraycopy(bytes1, 0, bytes, 0, 8);
            } else {
                System.arraycopy(bytes1, 0, bytes, 8 - bytes1.length,
                        bytes1.length);
            }
            return ByteBuffer.wrap(bytes).getLong();
        } else if (getType().equals(ValueType.STRING)) {
            try {
                return Double.parseDouble((String) this.value);
            } catch (NumberFormatException e) {
            }
            try {
                return Double.parseDouble(((String) this.value)
                        .replace(',', '.'));
            } catch (NumberFormatException e) {
                e.getCause();
            }
        }
        return 0;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.database.IValueHolder#getFloat()
     */
    @Override
    public float getFloat() {
        return (float) getDouble();
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.database.IValueHolder#getDecimal()
     */
    @Override
    public BigDecimal getDecimal() {
        try {
            if (this.value instanceof BigDecimal) {
                return (BigDecimal) this.value;
            } else if (getType().equals(ValueType.BOOLEAN)) {
                return ((Boolean) this.value).booleanValue() ? BigDecimal.ONE
                        : BigDecimal.ZERO;
            } else if (getType().equals(ValueType.STRING)) {
                return new BigDecimal(this.value.toString());
            } else if (getType().equals(ValueType.INTEGER)
                    || getType().equals(ValueType.REAL)) {
                return new BigDecimal(((Number) this.value).doubleValue());
            } else if (getType().equals(ValueType.DATE)) {
                return new BigDecimal(((Date) this.value).getTime());
            }
        } catch (NumberFormatException e) {
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.database.IValueHolder#getBytes()
     */
    @Override
    public byte[] getBytes() {
        if (getType().equals(ValueType.BINARY)) {
            return (byte[]) this.value;
        } else if (getType().equals(ValueType.BLOB)) {
            try {
                return ((Blob) this.value).getBytes(1,
                        (int) ((Blob) this.value).length());
            } catch (SQLException e) {
                e.getClass();
            }
        } else if (getType().equals(ValueType.BOOLEAN)) {
            return ByteBuffer.allocate(1)
                    .put((byte) (((Boolean) this.value).booleanValue() ? 1 : 0))
                    .array();
        } else if (getType().equals(ValueType.INTEGER)) {
            return ByteBuffer.allocate(8).putLong(getLong()).array();
        } else if (getType().equals(ValueType.REAL)) {
            return ByteBuffer.allocate(8).putDouble(getDouble()).array();
        } else if (getType().equals(ValueType.STRING)) {
            return this.value.toString().getBytes();
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.database.IValueHolder#getDate()
     */
    @Override
    public Date getDate() {
        if (getType().equals(ValueType.DATE)) {
            return (Date) this.value;
        } else if (getType().equals(ValueType.INTEGER)) {
            return new Date(getLong());
        } else if (getType().equals(ValueType.STRING)) {
            try {
                return DateFormat.getDateInstance().parse((String) this.value);
            } catch (ParseException e) {
            }
            try {
                return DateFormat.getDateTimeInstance().parse(
                        (String) this.value);
            } catch (ParseException e) {
            }
            try {
                return DateFormat.getTimeInstance().parse((String) this.value);
            } catch (ParseException e) {
            }
            try {
                return DateFormat.getInstance().parse((String) this.value);
            } catch (ParseException e) {
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.database.IValueHolder#getBlob()
     */
    @Override
    public Blob getBlob() {
        if (getType().equals(ValueType.BLOB)) {
            return (Blob) this.value;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.database.IValueHolder#getBool()
     */
    @Override
    public boolean getBool() {
        if (getType().equals(ValueType.BOOLEAN)) {
            return (Boolean) this.value;
        } else if (getType().equals(ValueType.INTEGER)
                || getType().equals(ValueType.REAL)
                || getType().equals(ValueType.DATE)) {
            return getLong() != 0;
        } else if (getType().equals(ValueType.BINARY)
                || getType().equals(ValueType.BLOB)
                || getType().equals(ValueType.STRING)) {
            String s = getString();
            return (s != null) && !s.isEmpty();
        }
        return false;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.database.IValueHolder#isNull()
     */
    @Override
    public boolean isNull() {
        return getType().equals(ValueType.NULL);
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.database.IValueHolder#isUnsigned()
     */
    @Override
    public boolean isUnsigned() {
        return this.unsigned;
    }

    @Override
    public String toString() {
        return "ValueHolder " + getType().name() + " " + this.name + " = " + this.value;
    }

}