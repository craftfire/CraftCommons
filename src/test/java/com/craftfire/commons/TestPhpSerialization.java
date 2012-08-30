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
package com.craftfire.commons;

import com.craftfire.commons.SerializedPhpParser.PhpObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestPhpSerialization {
    PhpSerializer serializer;

    public TestPhpSerialization() {
        this.serializer = new PhpSerializer();
    }

    @Test
    public void testSerializeNull() {
        String s = this.serializer.serialize((Object) null);
        assertEquals("N;", s);
    }

    @Test
    public void testSerializeInt() {
        String s = this.serializer.serialize(123);
        assertEquals("i:123;", s);
    }

    @Test
    public void testSerializeDouble() {
        String s = this.serializer.serialize(123.123d);
        assertEquals("d:123.123;", s);
    }

    @Test
    public void testSerializeBoolean() {
        String s = this.serializer.serialize(true);
        assertEquals("b:1;", s);
    }

    @Test
    public void testSerializeString() {
        String s = this.serializer.serialize("string");
        assertEquals("s:6:\"string\";", s);
    }

    @Test
    public void testSerializeArray() {
        List<Integer> list = new ArrayList<Integer>();
        list.add(2);
        String s = this.serializer.serialize(list);
        assertEquals("a:1:{i:0;i:2;}", s);
    }

    @Test
    public void testSerializeObject() {
        PhpObject obj = new PhpObject();
        obj.name = "TypeName";
        obj.attributes = new HashMap<Object, Object>();
        obj.attributes.put("foo", "bar");
        String s = this.serializer.serialize(obj);
        assertEquals("O:8:\"TypeName\":1:{s:3:\"foo\";s:3:\"bar\";};", s);
    }

    @Test
    public void testComplexDataStructuresBothWays() {
        String input = "a:1:{s:9:\"ResultSet\";a:4:{s:21:\"totalResultsAvailable\";s:7:\"1177824\";s:20:\"totalResultsReturned\";"
                + "i:2;s:19:\"firstResultPosition\";i:1;s:6:\"Result\";a:2:{i:0;a:10:{s:5:\"Title\";s:12:\"corvette.jpg\";"
                + "s:7:\"Summary\";s:150:\"bluefirebar.gif 03-Nov-2003 19:02 22k burning_frax.jpg 05-Jul-2002 14:34 169k corvette.jpg "
                + "21-Jan-2004 01:13 101k coupleblack.gif 03-Nov-2003 19:00 3k\";s:3:\"Url\";"
                + "s:48:\"http://www.vu.union.edu/~jaquezk/MG/corvette.jpg\";s:8:\"ClickUrl\";"
                + "s:48:\"http://www.vu.union.edu/~jaquezk/MG/corvette.jpg\";s:10:\"RefererUrl\";"
                + "s:35:\"http://www.vu.union.edu/~jaquezk/MG\";s:8:\"FileSize\";"
                + "s:7:\"101.5kB\";s:10:\"FileFormat\";s:4:\"jpeg\";s:6:\"Height\";s:3:\"768\";"
                + "s:5:\"Width\";s:4:\"1024\";s:9:\"Thumbnail\";a:3:{s:3:\"Url\";s:42:\"http://sp1.mm-a1.yimg.com/image/2178288556\";"
                + "s:6:\"Height\";s:3:\"120\";s:5:\"Width\";s:3:\"160\";}}i:1;a:10:{s:5:\"Title\";"
                + "s:23:\"corvette_c6_mini_me.jpg\";s:7:\"Summary\";s:48:\"Corvette I , Corvette II , Diablo , Enzo , Lotus\";"
                + "s:3:\"Url\";s:54:\"http://www.ku4you.com/minicars/corvette_c6_mini_me.jpg\";s:8:\"ClickUrl\";"
                + "s:54:\"http://www.ku4you.com/minicars/corvette_c6_mini_me.jpg\";s:10:\"RefererUrl\";"
                + "s:61:\"http://mik-blog.blogspot.com/2005_03_01_mik-blog_archive.html\";s:8:\"FileSize\";s:4:\"55kB\";"
                + "s:10:\"FileFormat\";s:4:\"jpeg\";s:6:\"Height\";s:3:\"518\";s:5:\"Width\";s:3:\"700\";"
                + "s:9:\"Thumbnail\";a:3:{s:3:\"Url\";s:42:\"http://sp1.mm-a2.yimg.com/image/2295545420\";"
                + "s:6:\"Height\";s:3:\"111\";s:5:\"Width\";s:3:\"150\";}}}}}";
        Map results = (Map) new SerializedPhpParser(input).parse();
        assertEquals(2,
                ((Map) ((Map) results.get("ResultSet")).get("Result")).size());
        String s = this.serializer.serialize(results);
        assertEquals(input, s);
    }

    @Test
    public void testParseNull() throws Exception {
        String input = "N;";
        SerializedPhpParser serializedPhpParser = new SerializedPhpParser(input);
        Object result = serializedPhpParser.parse();
        assertEquals(SerializedPhpParser.NULL, result);
    }

    @Test
    public void testParseInteger() throws Exception {
        this.assertPrimitive("i:123;", 123);
    }

    @Test
    public void testParseFloat() throws Exception {
        this.assertPrimitive("d:123.123;", 123.123d);
    }

    @Test
    public void testParseBoolean() throws Exception {
        this.assertPrimitive("b:1;", Boolean.TRUE);
    }

    @Test
    public void testParseString() throws Exception {
        this.assertPrimitive("s:6:\"string\";", "string");
    }

    @Test
    public void testParseArray() throws Exception {
        String input = "a:1:{i:1;i:2;}";
        SerializedPhpParser serializedPhpParser = new SerializedPhpParser(input);
        Object result = serializedPhpParser.parse();
        assertTrue(result instanceof Map);
        assertEquals(1, ((Map) result).size());
        assertEquals(2, ((Map) result).get(1));
    }

    @Test
    public void testParseObject() throws Exception {
        String input = "O:8:\"TypeName\":1:{s:3:\"foo\";s:3:\"bar\";}";
        SerializedPhpParser serializedPhpParser = new SerializedPhpParser(input);
        Object result = serializedPhpParser.parse();
        assertTrue(result instanceof SerializedPhpParser.PhpObject);
        assertEquals(1,
                ((SerializedPhpParser.PhpObject) result).attributes.size());
        assertEquals("bar",
                ((SerializedPhpParser.PhpObject) result).attributes.get("foo"));

    }

    private void assertPrimitive(String input, Object expected) {
        assertEquals(expected, new SerializedPhpParser(input).parse());
    }
}
