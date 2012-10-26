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

import com.craftfire.commons.ip.IPAddress;
import com.craftfire.commons.ip.IPRange;
import com.craftfire.commons.ip.IPv4Address;
import com.craftfire.commons.ip.IPv4IntervalRange;
import com.craftfire.commons.ip.IPv4MaskRange;
import com.craftfire.commons.ip.IPv6Address;
import com.craftfire.commons.ip.IPv6IntervalRange;
import com.craftfire.commons.ip.IPv6MaskRange;

public class TestIPRanges {
    private static IPAddress v4a1 = new IPv4Address(10, 200, 20, 210);
    private static IPAddress v4a2 = new IPv4Address(192, 168, 1, 1);
    private static IPAddress v4a3 = new IPv4Address(192, 168, 1, 99);
    private static IPAddress v4a4 = new IPv4Address(192, 168, 1, 255);
    private static IPAddress v4a5 = new IPv4Address(192, 168, 3, 2);
    private static IPAddress v4a6 = new IPv4Address(192, 168, 10, 1);
    private static IPAddress v4a7 = new IPv4Address(192, 168, 10, 2);
    private static IPAddress v4a8 = new IPv4Address(192, 168, 11, 0);
    private static IPAddress v4a9 = new IPv4Address(192, 168, 100, 200);
    private static IPAddress v4a10 = new IPv4Address(213, 158, 0, 1);

    private static IPAddress v6a1 = new IPv6Address(31, 0, 0, 0, 0, 0, 0xcdfd, 0xc7b6);
    private static IPAddress v6a2 = new IPv6Address(0x2002, 0, 0, 0, 0, 0, 17, 1);
    private static IPAddress v6a3 = new IPv6Address(0x2002, 0, 0, 0, 0, 0, 0xcdfd, 0xc7b5);
    private static IPAddress v6a4 = new IPv6Address(0x2002, 0, 0, 0, 0, 0, 0xcdfd, 0xc7b6);
    private static IPAddress v6a5 = new IPv6Address(0x2002, 0, 0, 0, 0, 0, 0xcdfe, 0xc000);
    private static IPAddress v6a6 = new IPv6Address(0x2002, 0, 0, 0, 0, 0, 0xcdff, 0x010);
    private static IPAddress v6a7 = new IPv6Address(0x2002, 0, 0, 0, 0, 0, 0xcdff, 0x101);
    private static IPAddress v6a8 = new IPv6Address(0x2002, 0, 0, 0, 0, 0, 0xcdff, 0x102);
    private static IPAddress v6a9 = new IPv6Address(0x2002, 0, 0, 0, 0, 0, 0xedff, 0x100);
    private static IPAddress v6a10 = new IPv6Address(0x2002, 0, 0, 0, 1, 0, 0xcdfe, 0x100);
    private static IPAddress v6a11 = new IPv6Address(0xfe80, 0, 0, 0, 0, 0, 0xcdfe, 0x100);

    @Test
    public void testIntervalV4() {
        IPRange range = new IPv4IntervalRange(new IPv4Address(192, 168, 1, 99), new IPv4Address(192, 168, 10, 1));
        System.out.println(range.toString());
        assertFalse(range.isInRange(v4a1));
        assertFalse(v4a2.isInRange(range));
        assertTrue(range.isInRange(v4a3));
        assertTrue(v4a4.isInRange(range));
        assertTrue(range.isInRange(v4a5));
        assertTrue(v4a6.isInRange(range));
        assertFalse(range.isInRange(v4a7));
        assertFalse(v4a8.isInRange(range));
        assertFalse(range.isInRange(v4a9));
        assertFalse(v4a10.isInRange(range));

        assertFalse(range.isInRange(v6a1));
        assertFalse(v6a2.isInRange(range));
        assertFalse(range.isInRange(v6a3));
        assertFalse(v6a4.isInRange(range));
        assertFalse(range.isInRange(v6a5));
        assertFalse(v6a6.isInRange(range));
        assertFalse(range.isInRange(v6a7));
        assertFalse(v6a8.isInRange(range));
        assertFalse(range.isInRange(v6a9));
        assertFalse(v6a10.isInRange(range));
        assertFalse(range.isInRange(v6a11));
    }

    @Test
    public void testMaskV4() {
        IPRange range = new IPv4MaskRange(new IPv4Address(192, 168, 1, 1), new IPv4Address(255, 255, 252, 0));
        System.out.println(range.toString());
        assertFalse(range.isInRange(v4a1));
        assertTrue(v4a2.isInRange(range));
        assertTrue(range.isInRange(v4a3));
        assertTrue(v4a4.isInRange(range));
        assertTrue(range.isInRange(v4a5));
        assertFalse(v4a6.isInRange(range));
        assertFalse(range.isInRange(v4a7));
        assertFalse(v4a8.isInRange(range));
        assertFalse(range.isInRange(v4a9));
        assertFalse(v4a10.isInRange(range));

        assertFalse(range.isInRange(v6a1));
        assertFalse(v6a2.isInRange(range));
        assertFalse(range.isInRange(v6a3));
        assertFalse(v6a4.isInRange(range));
        assertFalse(range.isInRange(v6a5));
        assertFalse(v6a6.isInRange(range));
        assertFalse(range.isInRange(v6a7));
        assertFalse(v6a8.isInRange(range));
        assertFalse(range.isInRange(v6a9));
        assertFalse(v6a10.isInRange(range));
        assertFalse(range.isInRange(v6a11));
    }

    @Test
    public void testIntervalV6() {
        IPRange range = new IPv6IntervalRange(IPAddress.valueOf("2002::cdfd:c7b6").toIPv6(), IPAddress.valueOf("2002::cdff:0101").toIPv6());
        System.out.println(range.toString());
        assertFalse(range.isInRange(v6a1));
        assertFalse(v6a2.isInRange(range));
        assertFalse(range.isInRange(v6a3));
        assertTrue(v6a4.isInRange(range));
        assertTrue(range.isInRange(v6a5));
        assertTrue(v6a6.isInRange(range));
        assertTrue(range.isInRange(v6a7));
        assertFalse(v6a8.isInRange(range));
        assertFalse(range.isInRange(v6a9));
        assertFalse(v6a10.isInRange(range));
        assertFalse(range.isInRange(v6a11));

        assertFalse(range.isInRange(v4a1));
        assertFalse(v4a2.isInRange(range));
        assertFalse(range.isInRange(v4a3));
        assertFalse(v4a4.isInRange(range));
        assertFalse(range.isInRange(v4a5));
        assertFalse(v4a6.isInRange(range));
        assertFalse(range.isInRange(v4a7));
        assertFalse(v4a8.isInRange(range));
        assertFalse(range.isInRange(v4a9));
        assertFalse(v4a10.isInRange(range));
    }

    @Test
    public void testMaskV6() {
        IPRange range = new IPv6MaskRange(new IPv6Address(0x2002, 0, 0, 0, 0, 0, 0, 0), IPAddress.valueOf("ffff:ffff:ffff:ffff:ffff:ffff::0").toIPv6());
        System.out.println(range.toString());
        assertFalse(range.isInRange(v6a1));
        assertTrue(v6a2.isInRange(range));
        assertTrue(range.isInRange(v6a3));
        assertTrue(v6a4.isInRange(range));
        assertTrue(range.isInRange(v6a5));
        assertTrue(v6a6.isInRange(range));
        assertTrue(range.isInRange(v6a7));
        assertTrue(v6a8.isInRange(range));
        assertTrue(range.isInRange(v6a9));
        assertFalse(v6a10.isInRange(range));
        assertFalse(range.isInRange(v6a11));

        assertFalse(range.isInRange(v4a1));
        assertFalse(v4a2.isInRange(range));
        assertFalse(range.isInRange(v4a3));
        assertFalse(v4a4.isInRange(range));
        assertFalse(range.isInRange(v4a5));
        assertFalse(v4a6.isInRange(range));
        assertFalse(range.isInRange(v4a7));
        assertFalse(v4a8.isInRange(range));
        assertFalse(range.isInRange(v4a9));
        assertFalse(v4a10.isInRange(range));
    }

}
