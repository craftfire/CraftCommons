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

    public String getName() {
        return this.name;
    }

    public String getTable() {
        return this.table;
    }

    public String getType() {
        return this.type;
    }

    public int getSize() {
        return this.size;
    }

    public Object getData() {
        return this.data;
    }

    public String getString() {
        return (String) this.data;
    }

    public int getInt() {
        return (Integer) this.data;
    }

    public Date getDate() {
        return (Date) this.data;
    }

    public Blob getBlob() {
        return (Blob) this.data;
    }
}
