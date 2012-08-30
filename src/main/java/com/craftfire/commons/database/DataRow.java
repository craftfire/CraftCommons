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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.craftfire.commons.enums.FieldType;

public class DataRow extends ArrayList<DataField> {
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
        if (f != null) {
            return f.getInt();
        }
        return 0;
    }

    public long getLongField(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) {
            return f.getLong();
        }
        return 0;
    }

    public double getDoubleField(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) {
            return f.getDouble();
        }
        return 0;
    }

    public float getFloatField(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) {
            return f.getFloat();
        }
        return 0;
    }

    public boolean getBoolField(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) {
            return f.getBool();
        }
        return false;
    }

    public String getStringField(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) {
            return f.getString();
        }
        return null;
    }

    public byte[] getBinaryField(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) {
            return f.getBytes();
        }
        return null;
    }

    public Blob getBlobField(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) {
            return f.getBlob();
        }
        return null;
    }

    public Date getDateField(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) {
            return f.getDate();
        }
        return null;
    }

    public BigInteger getBigIntField(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) {
            return f.getBigInt();
        }
        return null;
    }

    public BigDecimal getDecimalField(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) {
            return f.getDecimal();
        }
        return null;
    }

    public boolean isNullField(String fieldName) {
        DataField f = get(fieldName);
        return f != null && f.isNull();
    }

    public boolean fieldExist(String fieldName) {
        DataField f = get(fieldName);
        return f != null;
    }

    public FieldType getFieldType(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) {
            return f.getFieldType();
        }
        return null;
    }

    public int getFieldSize(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) {
            return f.getFieldSize();
        }
        return 0;
    }

    public int getFieldSQLType(String fieldName) {
        DataField f = get(fieldName);
        if (f != null) {
            return f.getSQLType();
        }
        return 0;
    }

    public String getTable(String fieldName) {
        //todo
        if (!this.isEmpty()) {
            DataField f = this.get(0);
            if (f != null) {
                return f.getTable();
            }
        }
        return null;
    }
}
