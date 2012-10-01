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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.craftfire.commons.classes.Version;
import com.craftfire.commons.classes.VersionRange;

public class TestVersionRanges {
    private static VersionRange versionRange = new VersionRange("1.2", "1.3.11");

    @Test
    public void test1() {
        assertFalse(versionRange.inVersionRange(new Version("1")));
    }

    @Test
    public void test2() {
        assertFalse(versionRange.inVersionRange("1.1.19"));
    }

    @Test
    public void test3() {
        assertTrue(versionRange.inVersionRange(new Version("1_2", "_")));
    }

    @Test
    public void test4() {
        assertTrue(versionRange.inVersionRange("1.2.5"));
    }

    @Test
    public void test5() {
        assertTrue(versionRange.inVersionRange(new Version("1.3")));
    }

    @Test
    public void test6() {
        assertTrue(versionRange.inVersionRange("1.3.9"));
    }

    @Test
    public void test7() {
        assertTrue(versionRange.inVersionRange(new Version("1.3.11")));
    }

    @Test
    public void test8() {
        assertTrue(versionRange.inVersionRange("1.3.11.2"));
    }

    @Test
    public void test9() {
        assertFalse(versionRange.inVersionRange(new Version("1:3:12", ":")));
    }

    @Test
    public void test10() {
        assertFalse(versionRange.inVersionRange("1.3.100"));
    }

    @Test
    public void test11() {
        assertFalse(versionRange.inVersionRange(new Version("1-4", "-")));
    }

    @Test
    public void test12() {
        assertFalse(versionRange.inVersionRange("2"));
    }

    @Test
    public void testTime() {
        long start = System.nanoTime();
        for (int i = 0; i < 100; ++i) {
            versionRange.inVersionRange("1.3.11.2");
        }
        long end = System.nanoTime();
        double delta = (end - start) / 100.0;
        System.out.print("Checking took " + delta + "ns (" + (delta / 1000000) + "ms)");
    }

}
