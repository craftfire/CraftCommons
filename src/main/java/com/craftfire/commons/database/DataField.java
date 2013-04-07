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
package com.craftfire.commons.database;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import com.craftfire.commons.util.AbstractValueHolder;
import com.craftfire.commons.util.ValueHolder;
import com.craftfire.commons.util.ValueHolderBase;
import com.craftfire.commons.util.ValueType;

public class DataField extends AbstractValueHolder {
    private final String table /* , type */;
    private final int sqltype, size;
    private final ValueHolder holder;

    public DataField(int column, ResultSetMetaData metaData, Object data) throws SQLException {
        this.size = metaData.getColumnDisplaySize(column);
        ValueType vtype = sqlTypeParse(this.sqltype, this.size, data);
        String name = metaData.getColumnLabel(column);
        boolean unsigned = metaData.getColumnTypeName(column).contains("UNSIGNED");
        this.holder = new ValueHolderBase(vtype, name, unsigned, data);
        this.sqltype = metaData.getColumnType(column);
        this.table = metaData.getTableName(column);
    }

    public DataField(ValueType type, int size, Object value) {
        this.holder = new ValueHolderBase(type, value);
        this.sqltype = Types.NULL;
        this.table = "";
        this.size = size;
    }

    public DataField(String name, String table, int size, boolean unsigned,
            Object data) {
        this.holder = new ValueHolderBase(name, unsigned, data);
        this.sqltype = Types.NULL;
        this.table = table;
        this.size = size;
    }

    public DataField(ValueType type, String name, String table, int size,
            boolean unsigned, Object data) {
        this.holder = new ValueHolderBase(type, name, unsigned, data);
        this.sqltype = Types.NULL;
        this.table = table;
        this.size = size;
    }

    public DataField(int column, ResultSet resultset) throws SQLException {
        ResultSetMetaData metaData = resultset.getMetaData();
        int size = metaData.getColumnDisplaySize(column);
        Object data;
        if (metaData.getColumnType(column) == Types.BLOB || metaData.getColumnType(column) == Types.LONGVARBINARY) {
            data = resultset.getBlob(column);
        } else if (metaData.getColumnType(column) == Types.CLOB) {
            data = resultset.getString(column);
        } else {
            data = resultset.getObject(column);
        }
        this.sqltype = metaData.getColumnType(column);
        boolean unsigned = metaData.getColumnTypeName(column).contains("UNSIGNED");
        ValueType vtype = sqlTypeParse(this.sqltype, size, data, resultset.wasNull());
        String name = metaData.getColumnLabel(column);
        this.holder = new ValueHolderBase(vtype, name, unsigned, data);
        this.table = metaData.getTableName(column);
        this.size = size;
    }

    private static ValueType sqlTypeParse(int sqltype, int size, Object data, boolean wasNull) {
        if (wasNull) {
            return ValueType.NULL;
        }
        return sqlTypeParse(sqltype, size, data);
    }

    private static ValueType sqlTypeParse(int sqltype, int size, Object data) {
        if (data == null) {
            return ValueType.NULL;
        }
        switch (sqltype) {
        case Types.CHAR:
        case Types.VARCHAR:
        case Types.LONGVARCHAR:
        case Types.CLOB:
            return ValueType.STRING;
        case Types.BLOB:
        case Types.LONGVARBINARY:
            return ValueType.BLOB;
        case Types.BOOLEAN:
            return ValueType.BOOLEAN;
        case Types.BIT:
            if (size <= 1) {
                return ValueType.BOOLEAN;
            }
            /* falls through */
        case Types.BINARY:
        case Types.VARBINARY:
            return ValueType.BINARY;
        case Types.TINYINT:
            if (size <= 1) {
                return ValueType.BOOLEAN;
            }
            /* falls through */
        case Types.SMALLINT:
        case Types.INTEGER:
        case Types.BIGINT:
            return ValueType.INTEGER;
        case Types.FLOAT:
        case Types.DOUBLE:
        case Types.REAL:
        case Types.DECIMAL:
            return ValueType.REAL;
        case Types.DATE:
        case Types.TIMESTAMP:
        case Types.TIME:
            return ValueType.DATE;
        case Types.NULL:
            return ValueType.NULL;
        default:
            // TODO: DataManager.getLogManager().warning("Unknown SQL type: " + sqltype + " Field data:" + data.toString());
            return ValueType.UNKNOWN;
        }
    }

    public int getSQLType() {
        return this.sqltype;
    }

    public String getTable() {
        return this.table;
    }

    public int getSize() {
        return this.size;
    }

    @Override
    public String toString() {
        return "DataField " + getType().name() + "(" + this.size
                + ") " + this.holder.getName() + " = " + this.holder.getValue();
    }

    @Override
    public String getName() {
        return this.holder.getName();
    }

    @Override
    public ValueType getType() {
        return this.holder.getType();
    }

    @Override
    public Object getValue() {
        return this.holder.getValue();
    }

    @Override
    public String getString() {
        return this.holder.getString();
    }

    @Override
    public String getString(String defaultValue) {
        return this.holder.getString(null);
    }

    @Override
    public int getInt(int defaultValue) {
        return this.holder.getInt(defaultValue);
    }

    @Override
    public long getLong(long defaultValue) {
        return this.holder.getLong(defaultValue);
    }

    @Override
    public BigInteger getBigInt(BigInteger defaultValue) {
        return this.holder.getBigInt(defaultValue);
    }

    @Override
    public double getDouble(double defaultValue) {
        return this.holder.getDouble(defaultValue);
    }

    @Override
    public float getFloat(float defaultValue) {
        return this.holder.getFloat(defaultValue);
    }

    @Override
    public BigDecimal getDecimal(BigDecimal defaultValue) {
        return this.holder.getDecimal(defaultValue);
    }

    @Override
    public byte[] getBytes(byte[] defaultValue) {
        return this.holder.getBytes(defaultValue);
    }

    @Override
    public Date getDate(Date defaultValue) {
        return this.holder.getDate(defaultValue);
    }

    @Override
    public Blob getBlob(Blob defaultValue) {
        return this.holder.getBlob(defaultValue);
    }

    @Override
    public boolean getBool(boolean defaultValue) {
        return this.holder.getBool(defaultValue);
    }

    @Override
    public boolean isNull() {
        return this.holder.isNull();
    }

    @Override
    public boolean isUnsigned() {
        return this.holder.isUnsigned();
    }
}
