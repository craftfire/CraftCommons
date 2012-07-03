package com.craftfire.commons.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Results {
    private final String query;
    private final int rows;
    private List<List<DataField>> array;

    public Results(String query, ResultSet rs) throws SQLException {
        this.query = query;

        ResultSetMetaData metaData = rs.getMetaData();
        this.rows = metaData.getColumnCount();
        while (rs.next()) {
            List<DataField> data = new ArrayList<DataField>();
            for (int i = 1; i <= this.rows; i++) {
                data.add(new DataField(i, metaData, rs.getObject(i)));
            }
            this.array.add(data);
        }
    }

    public int getRowsCount() {
        return this.rows;
    }

    public String getQuery() {
        return this.query;
    }

    public List<List<DataField>> getArray() {
        return this.array;
    }
}
