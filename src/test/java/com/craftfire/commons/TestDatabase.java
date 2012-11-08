package com.craftfire.commons;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.logging.Level;

import org.junit.BeforeClass;
import org.junit.Test;

import com.craftfire.commons.database.DataField;
import com.craftfire.commons.database.DataManager;
import com.craftfire.commons.database.DataRow;
import com.craftfire.commons.database.DataType;
import com.craftfire.commons.database.FieldType;

public class TestDatabase {
	private static final String table = "typetest";
	private static DataManager datamanager;
	
	@BeforeClass
	public static void init() {
        String user = "sa";
        String password = "";
        datamanager = new DataManager(DataType.H2, user, password);
        datamanager.getLogging().getLogger().setLevel(Level.OFF);	//Turn off logging temporarily so we won't be spammed with red warnings. 
        datamanager.setDatabase("test");
        datamanager.setDirectory("./src/test/resource/");
        datamanager.setTimeout(0);
        datamanager.setKeepAlive(true);
        datamanager.setPrefix("");
	}

	@Test
	public void testBInt() throws SQLException {
		final String name = "bint"; 
		DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();
		
		// DataRow.getField()
		DataField field = row.get(name);
		assertNotNull(field.getBigInt());
		assertTrue(field.getBool());
		assertNotNull(field.getBytes());
		assertNotNull(field.getDate());
		assertNotNull(field.getDecimal());
		assertThat(field.getDouble(), not(equalTo(0d)));
		assertThat(field.getFloat(), not(equalTo(0f)));
		assertThat(field.getInt(), not(equalTo(0)));
		assertThat(field.getLong(), not(equalTo(0L)));
		assertNotNull(field.getString());
		
		// DataManager.getField()
		field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
		assertNotNull(field.getBigInt());
		assertTrue(field.getBool());
		assertNotNull(field.getBytes());
		assertNotNull(field.getDate());
		assertNotNull(field.getDecimal());
		assertThat(field.getDouble(), not(equalTo(0d)));
		assertThat(field.getFloat(), not(equalTo(0f)));
		assertThat(field.getInt(), not(equalTo(0)));
		assertThat(field.getLong(), not(equalTo(0L)));
		assertNotNull(field.getString());
		
		// DataManager.get<Kinda>Field()
		assertTrue(datamanager.getBooleanField(table, name, "1"));
		assertNotNull(datamanager.getBinaryField(table, name, "1"));
		assertNotNull(datamanager.getBlobField(table, name, "1"));
		assertThat(datamanager.getDoubleField(table, name, "1"), not(equalTo(0d)));
		assertNotNull(datamanager.getStringField(table, name, "1"));
		
		// DataRow.get<Kinda>Field()
		assertNotNull(row.getBigIntField(name));
		assertTrue(row.getBoolField(name));
		assertNotNull(row.getBinaryField(name));
		assertNotNull(row.getDateField(name));
		assertNotNull(row.getDecimalField(name));
		assertThat(row.getDoubleField(name), not(equalTo(0d)));
		assertThat(row.getFloatField(name), not(equalTo(0f)));
		assertThat(row.getIntField(name), not(equalTo(0)));
		assertThat(row.getLongField(name), not(equalTo(0L)));
		assertNotNull(row.getStringField(name));
	}
	
	@Test
	public void testBin() throws SQLException {
		final String name = "bin"; 
		DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();
		
		// DataRow.getField()
		DataField field = row.get(name);
		assertNotNull(field.getBigInt());
		assertTrue(field.getBool());
		assertNotNull(field.getBytes());
		assertThat(field.getDouble(), not(equalTo(0d)));
		assertThat(field.getFloat(), not(equalTo(0f)));
		assertThat(field.getInt(), not(equalTo(0)));
		assertThat(field.getLong(), not(equalTo(0L)));
		assertNotNull(field.getString());
		
		// DataManager.getField()
		field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
		assertNotNull(field.getBigInt());
		assertTrue(field.getBool());
		assertNotNull(field.getBytes());
		assertThat(field.getDouble(), not(equalTo(0d)));
		assertThat(field.getFloat(), not(equalTo(0f)));
		assertThat(field.getInt(), not(equalTo(0)));
		assertThat(field.getLong(), not(equalTo(0L)));
		assertNotNull(field.getString());
		
		// DataManager.get<Kinda>Field()
		assertNotNull(datamanager.getBinaryField(table, name, "1"));
		assertNotNull(datamanager.getBlobField(table, name, "1"));
		assertThat(datamanager.getIntegerField(table, name, "1"), not(equalTo(0)));
		assertNotNull(datamanager.getStringField(table, name, "1"));
		
		// DataRow.get<Kinda>Field()
		assertNotNull(row.getBigIntField(name));
		assertTrue(row.getBoolField(name));
		assertNotNull(row.getBinaryField(name));
		assertThat(row.getDoubleField(name), not(equalTo(0d)));
		assertThat(row.getFloatField(name), not(equalTo(0f)));
		assertThat(row.getIntField(name), not(equalTo(0)));
		assertThat(row.getLongField(name), not(equalTo(0L)));
		assertNotNull(row.getStringField(name));
	}
	
	@Test
	public void testBlob() throws SQLException {
		//TODO
	}

	public void testTemplate() throws SQLException {
		final String name = ""; 
		DataRow row = datamanager.getResults("SELECT `" + name + "` FROM `" + table + "` LIMIT 1").getFirstResult();
		
		// DataRow.getField()
		DataField field = row.get(name);
		assertNotNull(field.getBigInt());
		assertNotNull(field.getBlob());
		assertTrue(field.getBool());
		assertNotNull(field.getBytes());
		assertNotNull(field.getDate());
		assertNotNull(field.getDecimal());
		assertThat(field.getDouble(), not(equalTo(0d)));
		assertThat(field.getFloat(), not(equalTo(0f)));
		assertThat(field.getInt(), not(equalTo(0)));
		assertThat(field.getLong(), not(equalTo(0L)));
		assertNotNull(field.getString());
		
		// DataManager.getField()
		field = datamanager.getField(FieldType.UNKNOWN, table, name, "1");
		assertNotNull(field.getBigInt());
		assertNotNull(field.getBlob());
		assertTrue(field.getBool());
		assertNotNull(field.getBytes());
		assertNotNull(field.getDate());
		assertNotNull(field.getDecimal());
		assertThat(field.getDouble(), not(equalTo(0d)));
		assertThat(field.getFloat(), not(equalTo(0f)));
		assertThat(field.getInt(), not(equalTo(0)));
		assertThat(field.getLong(), not(equalTo(0L)));
		assertNotNull(field.getString());
		
		// DataManager.get<Kinda>Field()
		assertTrue(datamanager.getBooleanField(table, name, "1"));
		assertNotNull(datamanager.getBinaryField(table, name, "1"));
		assertNotNull(datamanager.getBlobField(table, name, "1"));
		assertNotNull(datamanager.getDateField(table, name, "1"));
		assertThat(datamanager.getDoubleField(table, name, "1"), not(equalTo(0d)));
		assertThat(datamanager.getIntegerField(table, name, "1"), not(equalTo(0)));
		assertNotNull(datamanager.getStringField(table, name, "1"));
		
		// DataRow.get<Kinda>Field()
		assertNotNull(row.getBigIntField(name));
		assertNotNull(row.getBlobField(name));
		assertTrue(row.getBoolField(name));
		assertNotNull(row.getBinaryField(name));
		assertNotNull(row.getDateField(name));
		assertNotNull(row.getDecimalField(name));
		assertThat(row.getDoubleField(name), not(equalTo(0d)));
		assertThat(row.getFloatField(name), not(equalTo(0f)));
		assertThat(row.getIntField(name), not(equalTo(0)));
		assertThat(row.getLongField(name), not(equalTo(0L)));
		assertNotNull(row.getStringField(name));
	}

}
