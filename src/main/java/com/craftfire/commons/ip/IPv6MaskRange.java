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

public class IPv6MaskRange implements IPRange {
    private final IPv6Address data, mask;

    public IPv6MaskRange(IPv6Address data, IPv6Address mask) {
        this.data = data;
        this.mask = mask;
    }

    public IPv6Address getData() {
        return this.data;
    }

    public IPv6Address getMask() {
        return this.mask;
    }

    @Override
    public boolean isInRange(IPAddress address) {
        // TODO: Check if address is IPv6 and decide what to do if it's not.
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; ++i) {
            String mask = Integer.toString(this.mask.getAddress()[i], 2);
            String data = Integer.toString(this.data.getAddress()[i], 2);
            for (int j = 0; j < 16; ++j) {
                if (mask.charAt(j) == '0') {
                    builder.append("*");
                } else {
                    builder.append(data.charAt(j));
                }
            }
        }
        builder.append("b");
        return builder.toString();
    }
}
