package com.craftfire.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.util.Date;
import java.util.Iterator;

import org.junit.Test;

import com.craftfire.commons.database.DataField;
import com.craftfire.commons.database.DataList;
import com.craftfire.commons.enums.DataType;

public class DbTest2 {
    static DataManager datamanager;
    static DataField field;
    static final String newline = System.getProperty("line.separator");
    static String seperate = newline
            + "|------------------------------------------------------------------|"
            + newline;
    static int succeed = 0;
    static int gsucceed = 0;
    static int count = 0;
    static int gcount = 0;

    public static void main(String[] args) {
        String user = ask("MySQL user", "root");
        String password = ask("MySQL password", "AuthAPI");
        datamanager = new DataManager(DataType.MYSQL, user, password);
        datamanager.setHost(ask("MySQL host", "localhost"));
        String s_port = ask("MySQL port", "3306");
        int port = 0;
        try {
            port = Integer.parseInt(s_port);
        } catch (NumberFormatException e) {
        }
        if (port <= 0) {
            port = 3306;
        }
        datamanager.setPort(port);
        datamanager.setDatabase(ask("MySQL database", "test"));
        String s_timeout = ask("MySQL timeout", "0");
        int timeout = 0;
        try {
            timeout = Integer.parseInt(s_timeout);
        } catch (NumberFormatException e) {
        }
        if (timeout < 0) {
            timeout = 0;
        }
        datamanager.setTimeout(timeout);
        String s_keepalive = ask("MySQL keepalive", "true");
        boolean keepalive = false;
        if (s_keepalive.equalsIgnoreCase("true")
                || s_keepalive.equalsIgnoreCase("1")) {
            keepalive = true;
        }
        datamanager.setKeepAlive(keepalive);

        DataList data = datamanager.getResults(
                "SELECT * FROM `typetest` LIMIT 1").getFirstResult();
        Iterator<DataField> I = data.iterator();
        while (I.hasNext()) {
            field = I.next();
            System.out.println(seperate);
            System.out.println(field.getFieldName() + " from "
                    + field.getTable());
            System.out.println("Type: " + field.getFieldType().name());
            System.out.println("Size: " + field.getFieldSize());
            System.out.println("SQL Type: " + field.getSQLType());
            DbTest2 instance = new DbTest2();
            instance.testBigInt();
            instance.testBlob();
            instance.testBool();
            instance.testBytes();
            instance.testDate();
            instance.testDecimal();
            instance.testDouble();
            instance.testFloat();
            instance.testInt();
            instance.testLong();
            instance.testString();
            System.out.println("SUCCEED: " + succeed + "/" + count);
            succeed = 0;
            count = 0;
        }
        System.out.println(seperate);
        System.out.println("GLOBAL SUCCEED: " + gsucceed + "/" + gcount);
    }

    public static String ask(String name, String defaultvalue) {
        String line = null;
        String data = null;
        boolean valid = false;
        try {
            do {
                System.out.println(newline + name + ": ");
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        System.in));
                line = br.readLine();
                if (line != null && !line.isEmpty()) {
                    data = line;
                    valid = true;
                } else {
                    data = defaultvalue;
                    System.out.println(defaultvalue);
                    valid = true;
                }
            } while (!valid);
        } catch (IOException ioe) {
            System.out.println("IO exception = " + ioe);
        }
        return data;
    }

    public static void printResult(String name, boolean success, Object x) {
        String s = success ? "+" : "-";
        ++count;
        ++gcount;
        if (success) {
            ++succeed;
            ++gsucceed;
        }
        s += name;
        s += ": ";
        if (x != null) {
            s += x.toString();
        } else {
            s += "Doesn't work";
        }
        System.out.println(s);
    }

    public static void printResult(String name, Object x) {
        printResult(name, x != null, x);
    }

    public static String arrayToString(Object[] array) {
        String s = "";
        if (array == null) {
            return null;
        }
        for (int i = 0; i < array.length; ++i) {
            s += "[" + i + "] => [" + array[i].toString() + "] | ";
        }
        return s;
    }

    public static String arrayToString(byte[] array) {
        String s = "";
        if (array == null) {
            return null;
        }
        for (int i = 0; i < array.length; ++i) {
            s += "[" + i + "] => [" + array[i] + "] | ";
        }
        return s;
    }

    @Test
    public void testBigInt() {
        BigInteger x = field.getBigInt();
        printResult("asBigInt", x);
    }

    @Test
    public void testBlob() {
        Blob x = field.getBlob();
        printResult("asBlob", x);
    }

    @Test
    public void testBool() {
        boolean x = field.getBool();
        printResult("asBool", x, x);
    }

    @Test
    public void testBytes() {
        byte[] x = field.getBytes();
        printResult("asBytes", arrayToString(x));
    }

    @Test
    public void testDate() {
        Date x = field.getDate();
        printResult("asDate", x);
    }

    @Test
    public void testDecimal() {
        BigDecimal x = field.getDecimal();
        printResult("asDecimal", x);
    }

    @Test
    public void testDouble() {
        double x = field.getDouble();
        printResult("asDouble", x != 0, x);
    }

    @Test
    public void testFloat() {
        float x = field.getFloat();
        printResult("asFloat", x != 0, x);
    }

    @Test
    public void testInt() {
        int x = field.getInt();
        printResult("asInt", x != 0, x);
    }

    @Test
    public void testLong() {
        long x = field.getLong();
        printResult("asLong", x != 0, x);
    }

    @Test
    public void testString() {
        String x = field.getString();
        printResult("asString", x);
    }

}
