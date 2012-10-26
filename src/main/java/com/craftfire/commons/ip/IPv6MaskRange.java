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
 * An IPRange implementation for IPv6 masks.
 * 
 * @see IPv4MaskRange
 */
public class IPv6MaskRange implements IPRange {
    private final IPv6Address data, mask;

    /**
     * Creates an IPv6MaskRange object with given {@link data} and {@link mask}.
     * 
     * @param data  an address that is compared to a checked address with given bitmask
     * @param mask  a bitmask - binary 0 means that the bit can be any, 1 means that the bit in a checked address must equal to corrseponding bit in {@link data}
     */
    public IPv6MaskRange(IPv6Address data, IPv6Address mask) {
        this.data = data;
        this.mask = mask;
    }

    /**
     * Returns the address that is compared to a checked address with the bitmask.
     * 
     * @return the address
     */
    public IPv6Address getData() {
        return this.data;
    }

    /**
     * Returns the bitmask that is used to compare the data with a checked address.
     * 
     * @return the bitmask
     */
    public IPv6Address getMask() {
        return this.mask;
    }

    /**
     * Returns true, if the given IP address is an IPv4 address and matches the bitmask.
     */
    @Override
    public boolean isInRange(IPAddress address) {
        if (!address.isIPv6()) {
            return false;
        }
        short[] addr = address.toIPv6().getAddress();
        short[] data = getData().getAddress();
        short[] mask = getMask().getAddress();
        for (int i = 0; i < 8; ++i) {
            if (((addr[i] ^ data[i]) & mask[i]) != 0) {
                return false;
            }
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; ++i) {
            String mask = "0000000000000000" + Integer.toBinaryString(this.mask.getAddress()[i]);
            String data = "0000000000000000" + Integer.toBinaryString(this.data.getAddress()[i]);
            mask = mask.substring(mask.length() - 16, mask.length());
            data = data.substring(data.length() - 16, data.length());
            for (int j = 0; j < 16; ++j) {
                if (mask.charAt(j) == '0') {
                    builder.append("x");
                } else {
                    builder.append(data.charAt(j));
                }
            }
            builder.append(" ");
        }
        builder.append("b");
        return builder.toString();
    }
}
