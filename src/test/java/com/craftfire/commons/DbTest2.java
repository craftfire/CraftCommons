package com.craftfire.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.util.Date;
import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import com.craftfire.commons.database.DataField;
import com.craftfire.commons.database.DataList;
import com.craftfire.commons.enums.DataType;

public class DbTest2 {
    static DataManager datamanager;
    static DataField field;
    static final String newline = System.getProperty("line.separator");
    
    public static void main(String[] args) {
        String user = ask("MySQL user:", "root");
        String password = ask("MySQL password:", "AuthAPI");
        datamanager =  new DataManager(DataType.MYSQL, user, password);
        datamanager.setHost(ask("MySQL host:", "localhost"));
        String s_port = ask("MySQL port:", "3306");
        int port = 0;
        try {
            port = Integer.parseInt(s_port);
        } catch (NumberFormatException e) {
        }
        if (port <= 0) {
            port = 3306;
        }
        datamanager.setPort(port);
        datamanager.setDatabase(ask("MySQL database:", "test"));
        String s_timeout = ask("MySQL timeout:", "0");
        int timeout = 0;
        try {
            timeout = Integer.parseInt(s_timeout);
        } catch (NumberFormatException e) {
        }
        if (timeout < 0) {
            timeout = 0;
        }
        datamanager.setTimeout(timeout);
        String s_keepalive = ask("MySQL keepalive:", "true");
        boolean keepalive = false;
        if (s_keepalive.equalsIgnoreCase("true") || s_keepalive.equalsIgnoreCase("1")) {
            keepalive = true;
        }
        datamanager.setKeepAlive(keepalive);
        
        try {
            RunNotifier notifier = new RunNotifier();
            notifier.addListener(new RunListener() {
                @Override
                public void testFailure(Failure failure) {
                    System.out.println(failure.getMessage());
                }
            });
            Runner runner = new BlockJUnit4ClassRunner(DbTest2.class);
            DataList data = datamanager.getResults("SELECT * FROM `typetest` LIMIT 1").getFirstResult();
            Iterator<DataField> I = data.iterator();
            while (I.hasNext()) {
                field = I.next();
                System.out.println(field.getFieldName() + " from " + field.getTable());
                System.out.println("Type: " +  field.getFieldType().name());
                System.out.println("Size: " + field.getFieldSize());
                System.out.println("SQL Type: " + field.getSQLType());
                runner.run(notifier);
            }
        } catch (InitializationError e) {
            e.printStackTrace();
        }
    }

    public static String ask(String name, String defaultvalue) {
        String line = null;
        String data = null;
        boolean valid = false;
        try {
            do {
                System.out.println(newline + name + ": ");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                line = br.readLine();
                if (line != null && ! line.isEmpty()) {
                    data = line;
                    valid = true;
                } else {
                    data = defaultvalue;
                    System.out.println(defaultvalue);
                    valid = true;
                }
            } while (! valid);
        } catch (IOException ioe) {
            System.out.println("IO exception = " + ioe);
        }
        return data;
    }
    
    @Test
    public void testBigInt() {
        BigInteger x = field.getBigInt();
        Assert.assertNotNull("Doesn't work as BigInt", x);
        System.out.println("As BigInt: " + x.toString());
    }
    @Test
    public void testBlob() {
        Blob x = field.getBlob();
        Assert.assertNotNull("Doesn't work as Blob", x);
        System.out.println("As Blob: " + x.toString());
    }
    @Test
    public void testBool() {
        boolean x = field.getBool();
        Assert.assertTrue("Doesn't work as Bool or is just false", x);
        System.out.println("As Bool: " + x);
    }
    @Test
    public void testBytes() {
        byte[] x = field.getBytes();
        Assert.assertNotNull("Doesn't work as Bytes", x);
        System.out.println("As Bytes: " + x);
    }
    @Test
    public void testDate() {
        Date x = field.getDate();
        Assert.assertNotNull("Doesn't work as Date", x);
        System.out.println("As Date: " + x.toString());
    }
    @Test
    public void testDecimal() {
        BigDecimal x = field.getDecimal();
        Assert.assertNotNull("Doesn't work as BigDecimal", x);
        System.out.println("As Decimal: " + x.toString());
    }
    @Test
    public void testDouble() {
        double x = field.getDouble();
        Assert.assertTrue("Doesn't work as Double or is just zero", x != 0);
        System.out.println("As Double: " + x);
    }
    @Test
    public void testFloat() {
        float x = field.getFloat();
        Assert.assertTrue("Doesn't work as Float or is just zero", x != 0);
        System.out.println("As Float: " + x);
    }
    @Test
    public void testInt() {
        int x = field.getInt();
        Assert.assertTrue("Doesn't work as Int or is just zero", x != 0);
        System.out.println("As Int: " + x);
    }
    @Test
    public void testLong() {
        long x = field.getLong();
        Assert.assertTrue("Doesn't work as Long or is just zero", x != 0);
        System.out.println("As Long: " + x);
    }
    @Test
    public void testString() {
        String x = field.getString();
        Assert.assertNotNull("Doesn't work as String", x);
        System.out.println("As String: " + x);
    }

}
