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

import java.sql.Blob;
import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DataField {
    private final String name, table, type;
    private final int size;
    private final Object data;

    public DataField(int i, ResultSetMetaData metaData, Object data) throws SQLException {
        this.name = metaData.getColumnLabel(i);
        this.table = metaData.getTableName(i);
        this.type = metaData.getColumnTypeName(i);
        this.size = metaData.getColumnDisplaySize(i);
        this.data = data;
    }

    public String getFieldName() {
        return this.name;
    }

    public FieldType getFieldType() {
        for (FieldType fieldType : FieldType.values()) {
            if (fieldType.toString().equalsIgnoreCase(this.type)) {
                return fieldType;
            }
        }
        return FieldType.UNKNOWN;
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
    	}
    	return null;
    }

    public int getInt() {
    	if (getFieldType().equals(FieldType.INTEGER)) {
    		return (Integer) this.data;
    	}
    	return 0;
    }

    public Date getDate() {
        if (getFieldType().equals(FieldType.DATE)) {
        	return (Date) this.data;
        }
        return null;
    }

    public Blob getBlob() {
        if (getFieldType().equals(FieldType.BLOB)) {
        	return (Blob) this.data;
        }
        return null;
    }
}
