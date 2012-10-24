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

public class IPv4MaskRange implements IPRange {
    private final IPv4Address data, mask;

    public IPv4MaskRange(IPv4Address data, IPv4Address mask) {
        this.data = data;
        this.mask = mask;
    }

    public IPv4Address getData() {
        return this.data;
    }

    public IPv4Address getMask() {
        return this.mask;
    }

    @Override
    public boolean isInRange(IPAddress address) {
        // TODO: Check if address is IPv4 and decide what to do if it's not.
        return ((address.toIPv4().getInt() ^ this.data.getInt()) & this.mask.getInt()) == 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String mask = Integer.toString(this.mask.getInt(), 2);
        String data = Integer.toString(this.data.getInt(), 2);
        for (int i = 0; i < 32; ++i) {
            if (mask.charAt(i) == '0') {
                builder.append("*");
            } else {
                builder.append(data.charAt(i));
            }
        }
        builder.append("b");
        return builder.toString();
    }
}
