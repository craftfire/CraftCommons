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

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.ByteArrayInputStream;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.Map.Entry;

public class DataManager {
    private boolean keepalive, reconnect;
    private String host, username, password, database, prefix, url;
    private long startup;
    private int timeout, port;
    private Connection con = null;
    private final DataTypes datatype;
    private PreparedStatement pStmt = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public DataManager(boolean keepalive, int timeout, String host, int port, String database, String username,
                       String password, String prefix) {
        this.keepalive = keepalive;
        this.timeout = timeout;
        this.startup = System.currentTimeMillis() / 1000;
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.prefix = prefix;
        this.datatype = DataTypes.MYSQL;
        this.url = "jdbc:mysql://" + this.host + "/" + this.database + "?jdbcCompliantTruncation=false";
        if (keepalive) {
            connect();
        }
    }

    public DataManager(String host, int port, String database, String username, String password, String prefix) {
        this.keepalive = false;
        this.timeout = 0;
        this.startup = System.currentTimeMillis() / 1000;
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.prefix = prefix;
        this.datatype = DataTypes.MYSQL;
        this.url = "jdbc:mysql://" + this.host + "/" + this.database + "?jdbcCompliantTruncation=false";
    }

    public boolean exist(String table, String field, Object value) {
        if (getStringField(
                "SELECT `" + field + "` FROM `" + getPrefix() + table + "` WHERE `" + field + "` = '" + value +
                "' LIMIT 1") != null) {
            return true;
        }
        return false;
    }

    public int getLastID(String field, String table) {
        return getIntegerField(
                "SELECT `" + field + "` FROM `" + getPrefix() + table + "` ORDER BY `" + field + "` DESC LIMIT 1");
    }

    public int getLastID(String field, String table, String where) {
        return getIntegerField(
                "SELECT `" + field + "` " +
                "FROM `" + getPrefix() + table + "` " +
                "WHERE " + where + " " +
                "ORDER BY `" + field + "` DESC LIMIT 1");
    }

    public int getCount(String table, String where) {
        return getIntegerField("SELECT COUNT(*) FROM `" + getPrefix() + table + "` WHERE " + where);
    }

    public int getCount(String table) {
        return getIntegerField("SELECT COUNT(*) FROM `" + getPrefix() + table + "`");
    }

    public String getStringField(String table, String field, String where) {
        return getStringField("SELECT `" + field + "` FROM `" + getPrefix() + table + "` WHERE " + where);
    }

    public int getIntegerField(String table, String field, String where) {
        return getIntegerField("SELECT `" + field + "` FROM `" + getPrefix() + table + "` WHERE " + where);
    }

    public Date getDateField(String table, String field, String where) {
        return getDateField("SELECT `" + field + "` FROM `" + getPrefix() + table + "` WHERE " + where);
    }

    public Blob getBlobField(String table, String field, String where) {
        return getBlobField("SELECT `" + field + "` FROM `" + getPrefix() + table + "` WHERE " + where);
    }

    public String getStringField(String query) {
        try {
            connect();
            this.stmt = this.con.createStatement();
            this.rs = this.stmt.executeQuery(query);
            if (this.rs.next()) {
                String value = this.rs.getString(1);
                close();
                return value;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            close();
        }
        return null;
    }

    public int getIntegerField(String query) {
        try {
            connect();
            this.stmt = this.con.createStatement();
            this.rs = this.stmt.executeQuery(query);
            if (this.rs.next()) {
                int value = this.rs.getInt(1);
                close();
                return value;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            close();
        }
        return 0;
    }

    public Date getDateField(String query) {
        try {
            connect();
            this.stmt = this.con.createStatement();
            this.rs = this.stmt.executeQuery(query);
            if (this.rs.next()) {
                Date value = this.rs.getDate(1);
                close();
                return value;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            close();
        }
        return null;
    }

    public Blob getBlobField(String query) {
        try {
            connect();
            this.stmt = this.con.createStatement();
            this.rs = this.stmt.executeQuery(query);
            if (this.rs.next()) {
                Blob value = this.rs.getBlob(1);
                close();
                return value;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            close();
        }
        return null;
    }

    public void executeSQLquery(String query) {
        try {
            connect();
            this.pStmt = this.con.prepareStatement(query);
            this.pStmt.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBlob(String table, String field, String where, String data) {
        try {
            String query = "UPDATE `" + getPrefix() + table + "` SET `" + field + "` = ? WHERE " + where;
            byte[] array = data.getBytes();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(array);
            connect();
            this.stmt = this.con.createStatement();
            this.pStmt = this.con.prepareStatement(query);
            this.pStmt.setBlob(1, inputStream, array.length);
            this.pStmt.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateFields(HashMap<String, Object> data, String table, String where) {
        try {
            String update = updateFieldsString(data);
            String query = "UPDATE `" + getPrefix() + table + "`" + update + " WHERE " + where;
            connect();
            this.pStmt = this.con.prepareStatement(query);
            this.pStmt.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertFields(HashMap<String, Object> data, String table) {
        try {
            String insert = insertFieldString(data);
            String query = "INSERT INTO `" + getPrefix() + table + "` " + insert;
            connect();
            this.pStmt = this.con.prepareStatement(query);
            this.pStmt.executeUpdate();
            close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public TableModel resultSetToTableModel(String query) {
        try {
            connect();
            Statement stmt = this.con.createStatement();
            this.rs = stmt.executeQuery(query);
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

    public ResultSet getResultSet(String query) {
        try {
            connect();
            this.stmt = this.con.createStatement();
            this.rs = this.stmt.executeQuery(query);
            return this.rs;
        } catch (SQLException e) {
            close();
            e.printStackTrace();
        }
        return null;
    }

    public DataTypes getDataType() {
        return this.datatype;
    }

    public Connection getConnection() {
        return this.con;
    }

    public boolean isConnected() {
        try {
            return ! this.con.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void connect() {
        if (this.con != null && isConnected()) {
            return;
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.con = DriverManager.getConnection(this.url, this.username, this.password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (this.keepalive && ! this.reconnect) {
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
            if (this.keepalive) {
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

    public String getURL() {
        return this.url;
    }

    public boolean isKeepAlive() {
        return this.keepalive;
    }

    public void setKeepAlive(boolean keepalive) {
        this.keepalive = keepalive;
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
