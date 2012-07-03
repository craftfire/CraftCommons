package com.craftfire.commons.database;

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
}
