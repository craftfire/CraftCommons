/*
 * This file is part of CraftCommons <http://www.craftfire.com/>.
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
package com.craftfire.commons;

import com.craftfire.commons.enums.DataType;
import com.craftfire.commons.enums.FieldType;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.Map.Entry;

public class DataManager {
    private boolean keepAlive, reconnect;
    private String host, username, password, database, prefix, url = null, query, directory;
    private Map<Long, String> queries = new HashMap<Long, String>();
    private long startup;
    private int timeout = 0, port = 3306, queriesCount = 0;
    private Connection con = null;
    private final DataType datatype;
    private PreparedStatement pStmt = null;
    private Statement stmt = null;
    private ResultSet rs = null;
    
    public DataManager(DataType type, String username, String password) {
        this.datatype = type;
        this.username = username;
        this.password = password;
        this.startup = System.currentTimeMillis() / 1000;
    }

    public String getURL() {
        return this.url;
    }

    public boolean isKeepAlive() {
        return this.keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
        if (keepAlive) {
            connect();
        }
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Long getStartup() {
        return this.startup;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabase() {
        return this.database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDirectory() {
        return this.directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public ResultSet getCurrentResultSet() {
        return this.rs;
    }

    public PreparedStatement getCurrentPreparedStatement() {
        return this.pStmt;
    }

    public Statement getCurrentStatement() {
        return this.stmt;
    }

    public int getQueriesCount() {
        return this.queriesCount;
    }

    public Map<Long, String> getQueries() {
        return this.queries;
    }

    public String getLastQuery() {
        return this.query;
    }

    public DataType getDataType() {
        return this.datatype;
    }

    public Connection getConnection() {
        return this.con;
    }

    protected void setURL() {
        switch(datatype) {
            case MYSQL:     this.url = "jdbc:mysql://" + this.host + "/"
                                     + this.database + "?jdbcCompliantTruncation=false";
                            break;
            case H2:        this.url = "jdbc:h2:" + this.directory;
                            break;
        }
    }

    public boolean exist(String table, String field, Object value) {
        return getField(FieldType.STRING,
                                        ("SELECT `" + field + "` " +
                                         "FROM `" + getPrefix() + table + "` " +
                                         "WHERE `" + field + "` = '" + value + "' " +
                                         "LIMIT 1")) != null;
    }

    public int getLastID(String field, String table) {
        return (Integer) getField(FieldType.INTEGER,
                "SELECT `" + field + "` FROM `" + getPrefix() + table + "` ORDER BY `" + field + "` DESC LIMIT 1");
    }

    public int getLastID(String field, String table, String where) {
        return (Integer) getField(FieldType.INTEGER,
                "SELECT `" + field + "` " +
                "FROM `" + getPrefix() + table + "` " +
                "WHERE " + where + " " +
                "ORDER BY `" + field + "` DESC LIMIT 1");
    }

    public int getCount(String table, String where) {
        return (Integer) getField(FieldType.INTEGER, "SELECT COUNT(*) FROM `" + getPrefix() + table + "` WHERE " +
                                                                                                    where + " LIMIT 1");
}

    public int getCount(String table) {
        return (Integer) getField(FieldType.INTEGER, "SELECT COUNT(*) FROM `" + getPrefix() + table + "` LIMIT 1");
    }

    public Object getField(FieldType fieldType, String table, String field, String where) {
        return getField(fieldType, "SELECT `" + field + "` FROM `" + getPrefix() + table + "` WHERE " + where +
                                                                                                            " LIMIT 1");
    }
    
    public void increaseField(String table, String field, String where) {
        executeQueryVoid("UPDATE `" + getPrefix() + table + "` SET `" + field + "` =" + " " + field +
                        " + 1 WHERE " + where);
    }

    public Object getField(FieldType field, String query) {
        try {
            connect();
            this.stmt = this.con.createStatement();
            this.rs = this.stmt.executeQuery(query);
            log(query);
            if (this.rs.next()) {
                Object value = null;
                if (field.equals(FieldType.STRING)) {
                    value = this.rs.getString(1);
                } else if (field.equals(FieldType.INTEGER)) {
                    value = this.rs.getInt(1);
                } else if (field.equals(FieldType.DATE)) {
                    value = this.rs.getDate(1);
                } else if (field.equals(FieldType.BLOB)) {
                    value = this.rs.getBlob(1);
                } else if (field.equals(FieldType.BINARY)) {
                    value = CraftCommons.convertStreamToString(this.rs.getBinaryStream(1));
                }
                close();
                return value;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            close();
        }
        return null;
    }

    public void executeQuery(String query) throws SQLException {
        connect();
        this.pStmt = this.con.prepareStatement(query);
        this.pStmt.executeUpdate();
        log(query);
        close();
    }

    public void executeQueryVoid(String query) {
        try {
            connect();
            this.pStmt = this.con.prepareStatement(query);
            this.pStmt.executeUpdate();
            log(query);
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBlob(String table, String field, String where, String data) {
        try {
            String query = "UPDATE `" + getPrefix() + table + "` " +
                           "SET `" + field + "` = ? " +
                           "WHERE " + where;
            byte[] array = data.getBytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(array);
            connect();
            log(query);
            this.stmt = this.con.createStatement();
            this.pStmt = this.con.prepareStatement(query);
            this.pStmt.setBlob(1, inputStream, array.length);
            this.pStmt.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateFields(HashMap<String, Object> data, String table, String where) throws SQLException {
        String update = updateFieldsString(data);
        String query = "UPDATE `" + getPrefix() + table + "`" + update + " WHERE " + where;
        connect();
        this.pStmt = this.con.prepareStatement(query);
        log(query);
        this.pStmt.executeUpdate();
        close();
    }

    public void insertFields(HashMap<String, Object> data, String table) throws SQLException {
        String insert = insertFieldString(data);
        String query = "INSERT INTO `" + getPrefix() + table + "` " + insert;
        connect();
        this.pStmt = this.con.prepareStatement(query);
        log(query);
        this.pStmt.executeUpdate();
        close();
    }

    public TableModel resultSetToTableModel(String query) {
        try {
            connect();
            Statement stmt = this.con.createStatement();
            this.rs = stmt.executeQuery(query);
            log(query);
            ResultSetMetaData metaData = this.rs.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
            Vector<String> columnNames = new Vector<String>();

            for (int column = 0; column < numberOfColumns; column++) {
                columnNames.addElement(metaData.getColumnLabel(column + 1));
            }

            Vector<Vector<Object>> rows = new Vector<Vector<Object>>();

            while (rs.next()) {
                Vector<Object> newRow = new Vector<Object>();

                for (int i = 1; i <= numberOfColumns; i++) {
                    newRow.addElement(rs.getObject(i));
                }

                rows.addElement(newRow);
            }
            close();
            return new DefaultTableModel(rows, columnNames);
        } catch (Exception e) {
            e.printStackTrace();
            close();
            return null;
        }
    }

    public HashMap<String, Object> getArray(String query) {
        try {
            connect();
            List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
            this.stmt = this.con.createStatement();
            this.rs = this.stmt.executeQuery(query);
            log(query);
            ResultSetMetaData metaData = this.rs.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
            HashMap<String, Object> data = new HashMap<String, Object>();
            while (rs.next()) {
                for (int i = 1; i <= numberOfColumns; i++) {
                    data.put(metaData.getColumnLabel(i), this.rs.getObject(i));
                }
            }
            close();
            return data;
        } catch (SQLException e) {
            close();
            e.printStackTrace();
        }
        return null;
    }

    public List<HashMap<String, Object>> getArrayList(String query) {
        try {
            connect();
            List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
            this.stmt = this.con.createStatement();
            this.rs = this.stmt.executeQuery(query);
            log(query);
            ResultSetMetaData metaData = this.rs.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
            while (this.rs.next()) {
                HashMap<String, Object> data = new HashMap<String, Object>();
                for (int i = 1; i <= numberOfColumns; i++) {
                    data.put(metaData.getColumnLabel(i), this.rs.getString(i));
                }
                list.add(data);
            }
            close();
            return list;
        } catch (SQLException e) {
            close();
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getResultSet(String query) throws SQLException {
        connect();
        this.stmt = this.con.createStatement();
        log(query);
        this.rs = this.stmt.executeQuery(query);
        return this.rs;
    }

    private void log(String query) {
        this.query = query;
        this.queries.put(System.currentTimeMillis(), query);
        this.queriesCount++;
    }

    public boolean isConnected() {
        try {
            if (this.con != null) {
                return !this.con.isClosed();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean hasConnection() {
        try {
            boolean result = false;
            connect();
            if (this.con != null) {
                result = !this.con.isClosed();
            } 
            close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void connect() {
        if (this.url == null) {
            setURL();
        }
        if (this.con != null && isConnected()) {
            return;
        }
        try {
            switch(this.datatype) {
                case MYSQL:     Class.forName("com.mysql.jdbc.Driver");
                                this.con = DriverManager.getConnection(this.url, this.username, this.password);
                                break;
                case H2:        Class.forName("org.h2.Driver");
                                this.con = DriverManager.getConnection(this.url, this.username, this.password);
                                break;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (this.keepAlive && !this.reconnect) {
            if (this.timeout == 0) {
                return;
            } else if ((System.currentTimeMillis() / 1000) < (this.startup + this.timeout)) {
                return;
            }
        }
        try {
            this.con.close();
            if (this.rs != null) {
                this.rs.close();
                this.rs = null;
            }
            if (this.pStmt != null) {
                this.pStmt.close();
                this.pStmt = null;
            }
            if (this.stmt != null) {
                this.stmt.close();
                this.stmt = null;
            }
            if (this.keepAlive) {
                connect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void reconnect() {
        this.reconnect = true;
        close();
        connect();
    }

    private String updateFieldsString(HashMap<String, Object> data) {
        String query = " SET", suffix = ",";
        int i = 1;
        Iterator<Entry<String, Object>> it = data.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> pairs = (Map.Entry<String, Object>) it.next();
            if (i == data.size()) {
                suffix = "";
            }
            query += " `" + pairs.getKey() + "` =  '" + pairs.getValue() + "'" + suffix;
            i++;
        }
        return query;
    }

    private String insertFieldString(HashMap<String, Object> data) {
        String fields = "", values = "", query = "", suffix = ",";
        int i = 1;
        Iterator<Entry<String, Object>> it = data.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> pairs = (Map.Entry<String, Object>) it.next();
            if (i == data.size()) {
                suffix = "";
            }
            fields += " `" + pairs.getKey() + "`" + suffix;
            values += " '" + pairs.getValue() + "'" + suffix;
            i++;
        }
        query = "(" + fields + ") VALUES (" + values + ")";
        return query;
    }
}
