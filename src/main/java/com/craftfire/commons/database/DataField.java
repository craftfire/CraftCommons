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

import com.craftfire.commons.DataManager;
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

    public DataField(int column, ResultSetMetaData metaData, Object data) throws SQLException {
        this.sqltype = metaData.getColumnType(column);
        this.size = metaData.getColumnDisplaySize(column);
        this.data = data;
        this.ftype = sqlTypeParse(sqltype, size, data);
        typeCheck();
        this.name = metaData.getColumnLabel(column);
        this.table = metaData.getTableName(column);
        this.unsigned = metaData.getColumnTypeName(column).contains("UNSIGNED");
    }
    public DataField(int size, Object data) {
        this(size, false, data);
    }
    public DataField(FieldType type, int size, Object data) {
        this(type, size, false, data);
    }
    public DataField(int size, boolean unsigned, Object data) {
        this("", size, unsigned, data);
    }
    public DataField(FieldType type, int size, boolean unsigned, Object data) {
        this(type, "", size, unsigned, data);
    }
    public DataField(String name, int size, boolean  unsigned, Object data) {
        this(name, "", size, unsigned, data);
    }
    public DataField(FieldType type, String name, int size, boolean unsigned, Object data) {
        this(type, name, "", size, unsigned, data);
    }
    public DataField(String name, String table, int size, boolean unsigned, Object data) {
        this(typeDetect(data), name, table, size, unsigned, data);
    }
    public DataField(FieldType type, String name, String table, int size, boolean unsigned, Object data) {
        sqltype = Types.NULL;
        this.ftype = type;
        this.name = name;
        this.table = table;
        this.size = size;
        this.unsigned = unsigned;
        this.data = data;
        typeCheck();
    }
    public DataField(int column, ResultSet resultset) throws SQLException {
        ResultSetMetaData metaData = resultset.getMetaData();
        this.name = metaData.getCatalogName(column);
        this.table = metaData.getTableName(column);
        this.size = metaData.getColumnDisplaySize(column);
        if (metaData.getColumnType(column) == Types.BLOB) {
            this.data = resultset.getBlob(column);
        } else {
            this.data = resultset.getObject(column);
        }
        this.sqltype = metaData.getColumnType(column);
        this.unsigned = metaData.getColumnTypeName(column).contains("UNSIGNED");
        this.ftype = sqlTypeParse(sqltype, size, data);
    }
    private void typeCheck() {
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
    private static FieldType typeDetect(Object data) {
        if (data == null) {
            return FieldType.NULL;
        } else if (data instanceof String) {
            return FieldType.STRING;
        } else if (data instanceof Number) {
            if (((Number) data).doubleValue() == ((Number) data).longValue()) {
                return FieldType.INTEGER;
            } else {
                return FieldType.REAL;
            }
        } else if (data instanceof Date) {
            return FieldType.DATE;
        } else if (data instanceof Blob) {
            return FieldType.BLOB;
        } else if (data instanceof byte[]) {
            return FieldType.BINARY;
        } else if (data instanceof Boolean) {
            return FieldType.BOOLEAN;
        }
        DataManager.getLogManager().warning("Unknown data type: " + data.toString());
        return FieldType.UNKNOWN;
    }
    private static FieldType sqlTypeParse(int sqltype, int size, Object data) {
        switch (sqltype) {
        case Types.CHAR:
        case Types.VARCHAR:
        case Types.LONGVARCHAR:
            return FieldType.STRING;
        case Types.BLOB:
        case Types.LONGVARBINARY:
            return FieldType.BLOB;
        case Types.BIT:
            if (size <= 1) {
                return FieldType.BOOLEAN;
            }
            /* falls through */
        case Types.BINARY:
        case Types.VARBINARY:
            return FieldType.BINARY;
        case Types.TINYINT:
            if (size <= 1) {
                return FieldType.BOOLEAN;
            }
            /* falls through */
        case Types.SMALLINT:
        case Types.INTEGER:
        case Types.BIGINT:
            return FieldType.INTEGER;
        case Types.FLOAT:
        case Types.DOUBLE:
        case Types.REAL:
        case Types.DECIMAL:
            return FieldType.REAL;
        case Types.DATE:
        case Types.TIMESTAMP:
        case Types.TIME:
            return FieldType.DATE;
        case Types.NULL:
            return FieldType.NULL;
        default:
            DataManager.getLogManager().warning("Unknown sql type: " + sqltype + " Field data:" + data.toString());
            return FieldType.UNKNOWN;
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
