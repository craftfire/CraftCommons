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
package com.craftfire.commons.ip;

/**
 * An IPRange implementation for IPv4 masks.
 * <p>
 * If for example the data is binary: <code>11000000 10101000 00100100 00000001</code> (192.168.36.1)<br>
 * And the bitmask is binary: <code>11111111 11111111 11111100 00000011</code> (255.255.252.3)
 * <p>
 * <code>data: 11000000 10101000 00100100 00000001<br>
 * mask: 11111111 11111111 11111100 00000011<br>
 * ----: 11000000 10101000 001001xx xxxxxx01</code>
 * <p>
 * A checked address must match 11000000 10101000 001001xx xxxxxx01 for {@link #isInRange(IPAddress)} to return true.
 * 
 */
public class IPv4MaskRange implements IPRange {
    private final IPv4Address data, mask;

    /**
     * Creates an IPv4MaskRange object with given {@link data} and {@link mask}.
     * 
     * @param data  an address that is compared to a checked address with given bitmask
     * @param mask  a bitmask - binary 0 means that the bit can be any, 1 means that the bit in a checked address must equal to corrseponding bit in {@link data}
     */
    public IPv4MaskRange(IPv4Address data, IPv4Address mask) {
        this.data = data;
        this.mask = mask;
    }

    /**
     * Returns the address that is compared to a checked address with the bitmask.
     * 
     * @return the address
     */
    public IPv4Address getData() {
        return this.data;
    }

    /**
     * Returns the bitmask that is used to compare the data with a checked address.
     * 
     * @return the bitmask
     */
    public IPv4Address getMask() {
        return this.mask;
    }

    /**
     * Returns true, if the given IP address is an IPv4 address and matches the bitmask.
     */
    @Override
    public boolean isInRange(IPAddress address) {
        if (!address.isIPv4()) {
            return false;
        }
        return ((address.toIPv4().getInt() ^ this.data.getInt()) & this.mask.getInt()) == 0;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String mask = Integer.toBinaryString(this.mask.getInt());
        String data = Integer.toBinaryString(this.data.getInt());
        for (int i = 0; i < 32; ++i) {
            if (mask.charAt(i) == '0') {
                builder.append("x");
            } else {
                builder.append(data.charAt(i));
            }
            if (i % 8 == 7) {
                builder.append(" ");
            }
        }
        builder.append("b");
        return builder.toString();
    }
}
