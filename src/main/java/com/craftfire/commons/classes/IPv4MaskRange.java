package com.craftfire.commons.classes;

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
