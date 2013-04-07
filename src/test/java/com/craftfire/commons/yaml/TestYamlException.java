/*
 * This file is part of CraftCommons.
 *
 * Copyright (c) 2011 CraftFire <http://www.craftfire.com/>
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
