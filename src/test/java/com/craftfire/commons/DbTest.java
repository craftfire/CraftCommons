package com.craftfire.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.util.Date;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import com.craftfire.commons.database.DataList;
import com.craftfire.commons.enums.DataType;

//import com.craftfire.commons.enums.FieldType;

public class DbTest extends TestCase {
    static DataManager datamanager = null;
    static final String newline = System.getProperty("line.separator");
    static DataList data = null;

    public static void main(String[] args) {
        String user = ask("MySQL user:", "root");
        String password = ask("MySQL password:", "AuthAPI");
        datamanager = new DataManager(DataType.MYSQL, user, password);
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
        if (s_keepalive.equalsIgnoreCase("true")
                || s_keepalive.equalsIgnoreCase("1")) {
            keepalive = true;
        }
        datamanager.setKeepAlive(keepalive);
        junit.textui.TestRunner.run(DbTest.class);
    }

    @Test
    public final void testText() {
        Setup();
        String x = data.getStringField("text");
        Assert.assertNotNull(x);
        System.out.println(x);
    }

    @Test
    public final void testBit1() {
        Setup();
        boolean x = data.getBoolField("bit1");
        Assert.assertTrue(x);
        System.out.println(x);
    }

    @Test
    public final void testBitm() {
        Setup();
        byte[] x = data.getBinaryField("bitm");
        Assert.assertNotNull(x);
        System.out.println(x);
        int y = data.getIntField("bitm");
        System.out.println(y);
    }

    @Test
    public final void testTint1() {
        Setup();
        boolean x = data.getBoolField("tint1");
        Assert.assertTrue(x);
        System.out.println(x);
    }

    @Test
    public final void testTint() {
        Setup();
        int x = data.getIntField("tint");
        Assert.assertTrue(x != 0);
        System.out.println(x);
    }

    @Test
    public final void testSint() {
        Setup();
        int x = data.getIntField("sint");
        Assert.assertTrue(x != 0);
        System.out.println(x);
    }

    @Test
    public final void testUsint() {
        Setup();
        int x = data.getIntField("usint");
        Assert.assertTrue(x != 0);
        System.out.println(x);
    }

    @Test
    public final void testMint() {
        Setup();
        int x = data.getIntField("mint");
        Assert.assertTrue(x != 0);
        System.out.println(x);
    }

    @Test
    public final void testUmint() {
        Setup();
        int x = data.getIntField("umint");
        Assert.assertTrue(x != 0);
        System.out.println(x);
    }

    @Test
    public final void testInt() {
        Setup();
        int x = data.getIntField("int");
        Assert.assertTrue(x != 0);
        System.out.println(x);
    }

    @Test
    public final void testUint() {
        Setup();
        long x = data.getLongField("uint");
        Assert.assertTrue(x != 0);
        System.out.println(x);
    }

    @Test
    public final void testBint() {
        Setup();
        long x = data.getLongField("bint");
        Assert.assertTrue(x != 0);
        System.out.println(x);
    }

    @Test
    public final void testUbint() {
        Setup();
        BigInteger x = data.getBigIntField("ubint");
        Assert.assertNotNull(x);
        System.out.println(x);
    }

    @Test
    public final void testDec() {
        Setup();
        BigDecimal x = data.getDecimalField("dec");
        Assert.assertNotNull(x);
        System.out.println(x);
    }

    @Test
    public final void testFloat() {
        Setup();
        float x = data.getFloatField("float");
        Assert.assertTrue(x != 0);
        System.out.println(x);
    }

    @Test
    public final void testDouble() {
        Setup();
        double x = data.getDoubleField("double");
        Assert.assertTrue(x != 0);
        System.out.println(x);
    }

    @Test
    public final void testDate() {
        Setup();
        Date x = data.getDateField("date");
        Assert.assertNotNull(x);
        System.out.println(x);
    }

    @Test
    public final void testDateTime() {
        Setup();
        Date x = data.getDateField("datetime");
        Assert.assertNotNull(x);
        System.out.println(x);
    }

    @Test
    public final void testTime() {
        Setup();
        Date x = data.getDateField("time");
        Assert.assertNotNull(x);
        System.out.println(x);
    }

    @Test
    public final void testTimestamp() {
        Setup();
        Date x = data.getDateField("timestamp");
        Assert.assertNotNull(x);
        System.out.println(x);
    }

    @Test
    public final void testYear() {
        Setup();
        Date x = data.getDateField("year");
        Assert.assertNotNull(x);
        System.out.println(x);
    }

    @Test
    public final void testChar() {
        Setup();
        String x = data.getStringField("char");
        Assert.assertNotNull(x);
        System.out.println(x);
    }

    @Test
    public final void testVChar() {
        Setup();
        String x = data.getStringField("vchar");
        Assert.assertNotNull(x);
        System.out.println(x);
    }

    @Test
    public final void testVCharb() {
        Setup();
        byte[] x = data.getBinaryField("vcharb");
        Assert.assertNotNull(x);
        System.out.println(x);
    }

    @Test
    public final void testBin() {
        Setup();
        byte[] x = data.getBinaryField("bin");
        Assert.assertNotNull(x);
        System.out.println(x);
    }

    @Test
    public final void testVBin() {
        Setup();
        byte[] x = data.getBinaryField("vbin");
        Assert.assertNotNull(x);
        System.out.println(x);
    }

    @Test
    public final void testBool() {
        Setup();
        boolean x = data.getBoolField("bool");
        Assert.assertTrue(x);
        System.out.println(x);
    }

    @Test
    public final void testTText() {
        Setup();
        String x = data.getStringField("ttext");
        Assert.assertNotNull(x);
        System.out.println(x);
    }

    @Test
    public final void testMText() {
        Setup();
        String x = data.getStringField("mtext");
        Assert.assertNotNull(x);
        System.out.println(x);
    }

    @Test
    public final void testLText() {
        Setup();
        String x = data.getStringField("ltext");
        Assert.assertNotNull(x);
        System.out.println(x);
    }

    @Test
    public final void testTBlob() {
        Setup();
        byte[] x = data.getBinaryField("tblob");
        Assert.assertNotNull(x);
        System.out.println(x);
    }

    @Test
    public final void testBlob() {
        Setup();
        Blob x = data.getBlobField("blob");
        Assert.assertNotNull(x);
        System.out.println(x);
    }

    @Test
    public final void testMBlob() {
        Setup();
        Blob x = data.getBlobField("mblob");
        Assert.assertNotNull(x);
        System.out.println(x);
    }

    @Test
    public final void testLBlob() {
        Setup();
        Blob x = data.getBlobField("lblob");
        Assert.assertNotNull(x);
        System.out.println(x);
    }

    @Test
    public final void testSet() {
        Setup();
        String x = data.getStringField("set");
        Assert.assertNotNull(x);
        System.out.println(x);
    }

    @Test
    public final void testEnum() {
        Setup();
        String x = data.getStringField("enum");
        Assert.assertNotNull(x);
        System.out.println(x);
    }

    public void Setup() {
        if (datamanager == null) {
            datamanager = new DataManager(DataType.MYSQL, "root", "AuthAPI");
            datamanager.setDatabase("test");
            datamanager.setHost("localhost");
            datamanager.setPort(3306);
            datamanager.setTimeout(0);
            datamanager.setKeepAlive(true);
        }
        if (data == null) {
            data = datamanager.getResults("SELECT * FROM `typetest` LIMIT 1")
                    .getFirstResult();
        }

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

}
