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
        short[] addrShorts = address.toIPv6().getAddress();
        short[] dataShorts = getData().getAddress();
        short[] maskShorts = getMask().getAddress();
        for (int i = 0; i < 8; ++i) {
            if (((addrShorts[i] ^ dataShorts[i]) & maskShorts[i]) != 0) {
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
            String maskStr = "0000000000000000" + Integer.toBinaryString(getMask().getAddress()[i]);
            String dataStr = "0000000000000000" + Integer.toBinaryString(getData().getAddress()[i]);
            maskStr = maskStr.substring(maskStr.length() - 16, maskStr.length());
            dataStr = dataStr.substring(dataStr.length() - 16, dataStr.length());
            for (int j = 0; j < 16; ++j) {
                if (maskStr.charAt(j) == '0') {
                    builder.append("x");
                } else {
                    builder.append(dataStr.charAt(j));
                }
            }
            builder.append(" ");
        }
        builder.append("b");
        return builder.toString();
    }
}
