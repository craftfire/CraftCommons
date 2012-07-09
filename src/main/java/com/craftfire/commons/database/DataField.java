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
package com.craftfire.commons.database;

import com.craftfire.commons.enums.FieldType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.sql.Blob;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;

public class DataField {
    private final String name, table /*, type*/ ;
    private final int size;
    private final Object data;
    private final FieldType ftype;
    private final int sqltype;
    private final boolean unsigned;

    public DataField(FieldType type, int size, boolean unsigned, Object data) {
        this(type, "", size, unsigned, data);
    }
    public DataField(FieldType type, String name, int size, boolean unsigned, Object data) {
        this(type, name, "", size, unsigned, data);
    }
    public DataField(FieldType type, String name, String table, int size, boolean unsigned, Object data) {
        sqltype = Types.NULL;
        this.ftype = type;
        this.name = name;
        this.table = table;
        this.size = size;
        this.unsigned = unsigned;
        this.data = data;
        IllegalArgumentException e = new IllegalArgumentException("Data: " + data.toString() + " doesn't match the type: " + ftype.name());
        if (ftype.equals(FieldType.STRING)) {
            if (! (data instanceof String)) {
                throw e;
            }
        } else if (ftype.equals(FieldType.INTEGER) || ftype.equals(FieldType.REAL)) {
            if (! (data instanceof Number)) {
                throw e;
            }
        } else if (ftype.equals(FieldType.DATE)) {
            if (! (data instanceof Date)) {
                throw e;
            }
        } else if (ftype.equals(FieldType.BLOB)) {
            if (! (data instanceof Blob)) {
                throw e;
            }
        } else if (ftype.equals(FieldType.BINARY)) {
            if (! (data instanceof byte[])) {
                throw e;
            }
        } else if (ftype.equals(FieldType.BOOLEAN)) {
            if (! (data instanceof Boolean)) {
                throw e;
            }
        }
    }
    public DataField(int i, ResultSet resultset) throws SQLException {
        ResultSetMetaData metaData = resultset.getMetaData();
        this.name = metaData.getCatalogName(i);
        this.table = metaData.getTableName(i);
//        this.type = metaData.getColumnTypeName(i);
        this.size = metaData.getColumnDisplaySize(i);
        this.data = resultset.getObject(i);
        if (data == null) {
            this.sqltype = Types.NULL;
        } else {
            this.sqltype = metaData.getColumnType(i);
        }
        this.unsigned = metaData.getColumnTypeName(i).contains("UNSIGNED");
        switch (sqltype) {
        case Types.CHAR:
        case Types.VARCHAR:
        case Types.LONGVARCHAR:
            this.ftype = FieldType.STRING;
            break;
        case Types.BLOB:
        case Types.LONGVARBINARY:
            this.ftype = FieldType.BLOB;
            break;
        case Types.BIT:
            if (size <= 1) {
                this.ftype = FieldType.BOOLEAN;
                break;
            }
            /* falls through */
        case Types.BINARY:
        case Types.VARBINARY:
            this.ftype = FieldType.BINARY;
            break;
        case Types.TINYINT:
            if (size <= 1) {
                this.ftype = FieldType.BOOLEAN;
                break;
            }
            /* falls through */
        case Types.SMALLINT:
        case Types.INTEGER:
        case Types.BIGINT:
            this.ftype = FieldType.INTEGER;
            break;
        case Types.FLOAT:
        case Types.DOUBLE:
        case Types.REAL:
        case Types.DECIMAL:
            this.ftype = FieldType.REAL;
            break;
        case Types.DATE:
        case Types.TIMESTAMP:
        case Types.TIME:
            this.ftype = FieldType.DATE;
            break;
        case Types.NULL:
            this.ftype = FieldType.NULL;
            break;
        default:
            this.ftype = FieldType.UNKNOWN;
            break;
        }
    }

    public String getFieldName() {
        return this.name;
    }

    public FieldType getFieldType() {
        return this.ftype;
    }
    
    public int getSQLType() {
        return this.sqltype;
    }

    public int getFieldSize() {
        return this.size;
    }

    public String getTable() {
        return this.table;
    }

    public Object getData() {
        return this.data;
    }

    public String getString() {
        if (getFieldType().equals(FieldType.STRING)) {
            return (String) this.data;
        } else if (getFieldType().equals(FieldType.BINARY) || getFieldType().equals(FieldType.BLOB)) {
            return new String(getBytes());
        }
        return data.toString();
    }

    public int getInt() {
        return (int) getLong();
    }
    public long getLong() {
        if (getFieldType().equals(FieldType.INTEGER)
                || getFieldType().equals(FieldType.REAL)) {
            return ((Number) data).longValue();
        } else if (getFieldType().equals(FieldType.BOOLEAN)) {
            return ((Boolean) data).booleanValue() ? 1 : 0;
        } else if (getFieldType().equals(FieldType.DATE)) {
            return ((Date) data).getTime();
        } else if (getFieldType().equals(FieldType.BINARY)) {
            byte[] bytes = {0, 0, 0, 0, 0, 0, 0, 0};
            if (((byte[]) data).length >= 8) { 
                System.arraycopy(data, 0, bytes, 0, 8);
            } else {
                System.arraycopy(data, 0, bytes, 8 - ((byte[]) data).length, ((byte[]) data).length);
            }
            return ByteBuffer.wrap(bytes).getLong();
        } else if (getFieldType().equals(FieldType.STRING)) {
            try {
                return Long.parseLong((String) data);
            } catch (NumberFormatException e) {
            }
        }
        return 0;
    }
    public BigInteger getBigInt() {
        try {
            if (data instanceof BigInteger) {
                return (BigInteger) data;
            } else if (getFieldType().equals(FieldType.BOOLEAN)) {
                return ((Boolean) data).booleanValue()
                        ? BigInteger.ONE : BigInteger.ZERO;
            } else if (getFieldType().equals(FieldType.BINARY)) {
                return new BigInteger((byte[]) data);
            } else if (getFieldType().equals(FieldType.STRING)){
                return new BigInteger(data.toString());
            } else {
                long l = 0;
                if (getFieldType().equals(FieldType.INTEGER)
                        || getFieldType().equals(FieldType.REAL)) {
                    l = ((Number) data).longValue();
                } else if (getFieldType().equals(FieldType.DATE)) {
                    l = ((Date) data).getTime();
                }
                return new BigInteger(String.valueOf(l));
            }
        } catch (NumberFormatException e) {
        }
        return null;
    }
    public double getDouble() {
        if (getFieldType().equals(FieldType.INTEGER)
                || getFieldType().equals(FieldType.REAL)) {
            return ((Number) data).doubleValue();
        } else if (getFieldType().equals(FieldType.BOOLEAN)) {
            return ((Boolean) data).booleanValue() ? 1 : 0;
        } else if (getFieldType().equals(FieldType.DATE)) {
            return new Long(((Date) data).getTime()).doubleValue();
        } else if (getFieldType().equals(FieldType.BINARY)) {
            byte[] bytes = {0, 0, 0, 0, 0, 0, 0, 0};
            if (((byte[]) data).length >= 8) { 
                System.arraycopy(data, 0, bytes, 0, 8);
            } else {
                System.arraycopy(data, 0, bytes, 8 - ((byte[]) data).length, ((byte[]) data).length);
            }
            return ByteBuffer.wrap(bytes).getLong();
        } else if (getFieldType().equals(FieldType.STRING)) {
            try {
                return Double.parseDouble((String) data);
            } catch (NumberFormatException e) {
            }
            try {
                return Long.parseLong(((String) data).replace(',', '.'));
            } catch (NumberFormatException e) {
            }
        }
        return 0;
    }
    public float getFloat() {
        return (float) getDouble();
    }
    public BigDecimal getDecimal() {
        try {
            if (data instanceof BigDecimal) {
                return (BigDecimal) data;
            } else if (getFieldType().equals(FieldType.BOOLEAN)) {
                return ((Boolean) data).booleanValue()
                        ? BigDecimal.ONE : BigDecimal.ZERO;
            } else if (getFieldType().equals(FieldType.STRING)){
                return new BigDecimal(data.toString());
            } else if (getFieldType().equals(FieldType.INTEGER)
                    || getFieldType().equals(FieldType.REAL)) {
                return new BigDecimal(((Number) data).doubleValue());
            } else if (getFieldType().equals(FieldType.DATE)) {
                return new BigDecimal(((Date) data).getTime());
            }
        } catch (NumberFormatException e) {
        }
        return null;
    }
    public byte[] getBytes() {
        if (getFieldType().equals(FieldType.BINARY)) {
            return (byte[]) data;
        } else if (getFieldType().equals(FieldType.BLOB)) {
            try {
                return ((Blob) data).getBytes(1, (int) ((Blob) data).length());
            } catch (SQLException e) {
                e.getClass();
            }
        } else if (getFieldType().equals(FieldType.BOOLEAN)) {
            return ByteBuffer.allocate(1).put(
                    (byte) (((Boolean) data).booleanValue() ? 1 : 0)).array();
        } else if (getFieldType().equals(FieldType.INTEGER)) {
            return ByteBuffer.allocate(8).putLong(getLong()).array();
        } else if (getFieldType().equals(FieldType.REAL)) {
            return ByteBuffer.allocate(8).putDouble(getDouble()).array();
        } else if (getFieldType().equals(FieldType.STRING)) {
            return data.toString().getBytes();
        }
        return null;
    }
    public Date getDate() {
        if (getFieldType().equals(FieldType.DATE)) {
            return (Date) this.data;
        } else if (getFieldType().equals(FieldType.INTEGER) || getFieldType().equals(FieldType.BINARY)) {
            return new Date(getLong());
        } else if (getFieldType().equals(FieldType.STRING)) {
            try {
                return DateFormat.getDateInstance().parse((String) data);
            } catch (ParseException e) {
            }
            try {
                return DateFormat.getDateTimeInstance().parse((String) data);
            } catch (ParseException e) {
            }
            try {
                return DateFormat.getTimeInstance().parse((String) data);
            } catch (ParseException e) {
            }
            try {
                return DateFormat.getInstance().parse((String) data);
            } catch (ParseException e) {
            }
        }
        return null;
    }

    public Blob getBlob() {
        if (getFieldType().equals(FieldType.BLOB)) {
            return (Blob) this.data;
        }
        return null;
    }
    
    public boolean getBool(){
        if (getFieldType().equals(FieldType.BOOLEAN)) {
            return (Boolean) data;
        } else if (getFieldType().equals(FieldType.INTEGER) || getFieldType().equals(FieldType.REAL) || getFieldType().equals(FieldType.DATE)) {
            return getInt() != 0;
        } else if (getFieldType().equals(FieldType.BINARY) || getFieldType().equals(FieldType.BLOB) || getFieldType().equals(FieldType.STRING)) {
            String s = getString();
            return (s != null) && !s.isEmpty();
        }
        return false;
    }
    public boolean isNull() {
        return getFieldType().equals(FieldType.NULL);
    }
    public boolean isUnsigned() {
        return this.unsigned;
    }
    @Override
    public String toString() {
        return "DataField " + this.getFieldType().name() + "(" + this.size + ") " + this.name + " = " + this.data.toString();
    }
}
