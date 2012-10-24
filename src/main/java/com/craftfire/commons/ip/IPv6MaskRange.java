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
