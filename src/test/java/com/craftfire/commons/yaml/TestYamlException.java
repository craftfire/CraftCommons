package com.craftfire.commons.yaml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class TestYamlException {
    private static Throwable cause = mock(Throwable.class);
    private static String msg = "Hey ho!";
    private static String path = "my.awesome.node";


    @Test
    public void testConstructor1() {
        YamlException e = new YamlException();
        assertNull(e.getPath());
        assertNull(e.getMessage());
        assertNull(e.getCause());
    }

    @Test
    public void testConstructor2() {
        YamlException e = new YamlException(msg);
        assertNull(e.getPath());
        assertEquals(msg, e.getMessage());
        assertNull(e.getCause());
    }

    @Test
    public void testConstructor3() {
        YamlException e = new YamlException(msg, path);
        assertEquals(path, e.getPath());
        assertTrue(e.getMessage().contains(msg));
        assertTrue(e.getMessage().contains(path));
        assertNull(e.getCause());
    }

    @Test
    public void testConstructor4() {
        YamlException e = new YamlException(cause);
        assertNull(e.getPath());
        assertEquals(cause.toString(), e.getMessage());
        assertSame(cause, e.getCause());
    }

    @Test
    public void testConstructor5() {
        YamlException e = new YamlException(msg, cause);
        assertNull(e.getPath());
        assertEquals(msg, e.getMessage());
        assertSame(cause, e.getCause());
    }

    @Test
    public void testConstructor6() {
        YamlException e = new YamlException(msg, path, cause);
        assertEquals(path, e.getPath());
        assertTrue(e.getMessage().contains(msg));
        assertTrue(e.getMessage().contains(path));
        assertSame(cause, e.getCause());
    }

}
