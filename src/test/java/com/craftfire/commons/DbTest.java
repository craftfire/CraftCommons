package com.craftfire.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.util.Date;

import com.craftfire.commons.enums.DataType;
import com.craftfire.commons.enums.FieldType;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

public class DbTest extends TestCase {
    static DataManager datamanager;
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
		junit.textui.TestRunner.run(DbTest.class);
	}
	@Test
	public final void Text(){
        Setup();
	    String x = datamanager.getField(FieldType.STRING, "SELECT `text` FROM `typetest`").getString();
	    Assert.assertNotNull(x);
	    System.out.println(x);
	}
    @Test
    public final void Bit1(){
        Setup();
        boolean x = datamanager.getField(FieldType.STRING, "SELECT `bit1` FROM `typetest`").getBool();
        Assert.assertTrue(x);
        System.out.println(x);
    }
    @Test
    public final void Bitm(){
        Setup();
        int x = datamanager.getField(FieldType.STRING, "SELECT `bitm` FROM `typetest`").getInt();
        Assert.assertTrue(x != 0);
        System.out.println(x);
    }
    @Test
    public final void Tint1(){
        Setup();
        boolean x = datamanager.getField(FieldType.STRING, "SELECT `tint1` FROM `typetest`").getBool();
        Assert.assertTrue(x);
        System.out.println(x);
    }
    @Test
    public final void Tint(){
        Setup();
        int x = datamanager.getField(FieldType.STRING, "SELECT `tint` FROM `typetest`").getInt();
        Assert.assertTrue(x != 0);
        System.out.println(x);
    }
    @Test
    public final void Sint(){
        Setup();
        int x = datamanager.getField(FieldType.STRING, "SELECT `sint` FROM `typetest`").getInt();
        Assert.assertTrue(x != 0);
        System.out.println(x);
    }
    @Test
    public final void Usint(){
        Setup();
        int x = datamanager.getField(FieldType.STRING, "SELECT `usint` FROM `typetest`").getInt();
        Assert.assertTrue(x != 0);
        System.out.println(x);
    }
    @Test
    public final void Mint(){
        Setup();
        int x = datamanager.getField(FieldType.STRING, "SELECT `mint` FROM `typetest`").getInt();
        Assert.assertTrue(x != 0);
        System.out.println(x);
    }
    @Test
    public final void Umint(){
        Setup();
        int x = datamanager.getField(FieldType.STRING, "SELECT `umint` FROM `typetest`").getInt();
        Assert.assertTrue(x != 0);
        System.out.println(x);
    }
    @Test
    public final void Int(){
        Setup();
        int x = datamanager.getField(FieldType.STRING, "SELECT `int` FROM `typetest`").getInt();
        Assert.assertTrue(x != 0);
        System.out.println(x);
    }
    @Test
    public final void Uint(){
        Setup();
        long x = datamanager.getField(FieldType.STRING, "SELECT `uint` FROM `typetest`").getLong();
        Assert.assertTrue(x != 0);
        System.out.println(x);
    }
    @Test
    public final void Bint(){
        Setup();
        long x = datamanager.getField(FieldType.STRING, "SELECT `bint` FROM `typetest`").getLong();
        Assert.assertTrue(x != 0);
        System.out.println(x);
    }
    @Test
    public final void Ubint(){
        Setup();
        BigInteger x = datamanager.getField(FieldType.STRING, "SELECT `ubint` FROM `typetest`").getBigInt();
        Assert.assertNotNull(x);
        System.out.println(x);
    }
    @Test
    public final void Dec(){
        Setup();
        BigDecimal x = datamanager.getField(FieldType.STRING, "SELECT `dec` FROM `typetest`").getDecimal();
        Assert.assertNotNull(x);
        System.out.println(x);
    }
    @Test
    public final void Float(){
        Setup();
        float x = datamanager.getField(FieldType.STRING, "SELECT `float` FROM `typetest`").getFloat();
        Assert.assertTrue(x != 0);
        System.out.println(x);
    }
    @Test
    public final void Double(){
        Setup();
        double x = datamanager.getField(FieldType.STRING, "SELECT `double` FROM `typetest`").getDouble();
        Assert.assertTrue(x != 0);
        System.out.println(x);
    }
    @Test
    public final void Date(){
        Setup();
        Date x = datamanager.getField(FieldType.STRING, "SELECT `date` FROM `typetest`").getDate();
        Assert.assertNotNull(x);
        System.out.println(x);
    }
    @Test
    public final void DateTime(){
        Setup();
        Date x = datamanager.getField(FieldType.STRING, "SELECT `datetime` FROM `typetest`").getDate();
        Assert.assertNotNull(x);
        System.out.println(x);
    }
    @Test
    public final void Time(){
        Setup();
        Date x = datamanager.getField(FieldType.STRING, "SELECT `time` FROM `typetest`").getDate();
        Assert.assertNotNull(x);
        System.out.println(x);
    }
    @Test
    public final void Timestamp(){
        Setup();
        Date x = datamanager.getField(FieldType.STRING, "SELECT `timestamp` FROM `typetest`").getDate();
        Assert.assertNotNull(x);
        System.out.println(x);
    }
    @Test
    public final void Year(){
        Setup();
        Date x = datamanager.getField(FieldType.STRING, "SELECT `year` FROM `typetest`").getDate();
        Assert.assertNotNull(x);
        System.out.println(x);
    }
    @Test
    public final void Char(){
        Setup();
        String x = datamanager.getField(FieldType.STRING, "SELECT `char` FROM `typetest`").getString();
        Assert.assertNotNull(x);
        System.out.println(x);
    }
    @Test
    public final void VChar(){
        Setup();
        String x = datamanager.getField(FieldType.STRING, "SELECT `vchar` FROM `typetest`").getString();
        Assert.assertNotNull(x);
        System.out.println(x);
    }
    @Test
    public final void VCharb(){
        Setup();
        byte[] x = datamanager.getField(FieldType.STRING, "SELECT `vcharb` FROM `typetest`").getBytes();
        Assert.assertNotNull(x);
        System.out.println(x);
    }
    @Test
    public final void Bin(){
        Setup();
        byte[] x = datamanager.getField(FieldType.STRING, "SELECT `bin` FROM `typetest`").getBytes();
        Assert.assertNotNull(x);
        System.out.println(x);
    }
    @Test
    public final void VBin(){
        Setup();
        byte[] x = datamanager.getField(FieldType.STRING, "SELECT `vbin` FROM `typetest`").getBytes();
        Assert.assertNotNull(x);
        System.out.println(x);
    }
    @Test
    public final void Bool(){
        Setup();
        boolean x = datamanager.getField(FieldType.STRING, "SELECT `bool` FROM `typetest`").getBool();
        Assert.assertTrue(x);
        System.out.println(x);
    }
    @Test
    public final void TText(){
        Setup();
        String x = datamanager.getField(FieldType.STRING, "SELECT `ttext` FROM `typetest`").getString();
        Assert.assertNotNull(x);
        System.out.println(x);
    }
    @Test
    public final void MText(){
        Setup();
        String x = datamanager.getField(FieldType.STRING, "SELECT `mtext` FROM `typetest`").getString();
        Assert.assertNotNull(x);
        System.out.println(x);
    }
    @Test
    public final void LText(){
        Setup();
        String x = datamanager.getField(FieldType.STRING, "SELECT `ltext` FROM `typetest`").getString();
        Assert.assertNotNull(x);
        System.out.println(x);
    }
    @Test
    public final void TBlob(){
        Setup();
        Blob x = datamanager.getField(FieldType.STRING, "SELECT `tblob` FROM `typetest`").getBlob();
        Assert.assertNotNull(x);
        System.out.println(x);
    }
    @Test
    public final void Blob(){
        Setup();
        Blob x = datamanager.getField(FieldType.STRING, "SELECT `blob` FROM `typetest`").getBlob();
        Assert.assertNotNull(x);
        System.out.println(x);
    }
    @Test
    public final void MBlob(){
        Setup();
        Blob x = datamanager.getField(FieldType.STRING, "SELECT `mblob` FROM `typetest`").getBlob();
        Assert.assertNotNull(x);
        System.out.println(x);
    }
    @Test
    public final void LBlob(){
        Setup();
        Blob x = datamanager.getField(FieldType.STRING, "SELECT `lblob` FROM `typetest`").getBlob();
        Assert.assertNotNull(x);
        System.out.println(x);
    }
    @Test
    public final void Set(){
        Setup();
        String x = datamanager.getField(FieldType.STRING, "SELECT `set` FROM `typetest`").getString();
        Assert.assertNotNull(x);
        System.out.println(x);
    }
    @Test
    public final void Enum(){
        Setup();
        String x = datamanager.getField(FieldType.STRING, "SELECT `enum` FROM `typetest`").getString();
        Assert.assertNotNull(x);
        System.out.println(x);
    }
    public void Setup(){
        if (datamanager != null) {
            return;
        }
        datamanager = new DataManager(DataType.MYSQL, "root", "AuthAPI");
        datamanager.setDatabase("test");
        datamanager.setHost("localhost");
        datamanager.setPort(3306);
        datamanager.setTimeout(0);
        datamanager.setKeepAlive(true);
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

}
