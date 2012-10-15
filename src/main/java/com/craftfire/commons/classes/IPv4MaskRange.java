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

}
