package com.craftfire.commons.database;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.craftfire.commons.enums.FieldType;

public class DataList extends ArrayList<DataField> {
    public DataField get(String fieldName) {
        Iterator<DataField> iterator = iterator();
        while (iterator.hasNext()) {
            DataField dataField = iterator.next();
            if (dataField.getFieldName().equalsIgnoreCase(fieldName)) {
                return dataField;
            }
        }
        return null;
    }
    public int getIntField(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) return f.getInt();
        return 0;
    }
    public long getLongField(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) return f.getLong();
        return 0;
    }
    public double getDoubleField(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) return f.getDouble();
        return 0;
    }
    public float getFloatField(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) return f.getFloat();
        return 0;
    }
    public boolean getBoolField(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) return f.getBool();
        return false;
    }
    public String getStringField(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) return f.getString();
        return null;
    }
    public byte[] getBinaryField(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) return f.getBytes();
        return null;
    }
    public Blob getBlobField(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) return f.getBlob();
        return null;
    }
    public Date getDateField(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) return f.getDate();
        return null;
    }
    public BigInteger getBigIntField(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) return f.getBigInt();
        return null;
    }
    public BigDecimal getDecimalField(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) return f.getDecimal();
        return null;
    }
    public boolean isNullField(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) return f.isNull();
        return false;
    }
    public boolean fieldExist(String fieldName) {
        DataField f = get(fieldName);
        return f != null;
    }
    public FieldType getFieldType(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) return f.getFieldType();
        return null;
    }
    public int getFieldSize(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) return f.getFieldSize();
        return 0;
    }
    public int getFieldSQLType(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) return f.getSQLType();
        return 0;
    }
    public String getTable(String fieldName) {
        if (!this.isEmpty()) {
            DataField f = this.get(0);
            if (f != null) {
                return f.getTable();
            }
        }
        return null;
    }


}
