package com.craftfire.commons.classes;

import java.net.Inet4Address;

public class IPv4Address extends IPAddress {

    public IPv4Address(Inet4Address address) {
        // TODO
    }

    public IPv4Address(String address) {
        // TODO
    }

    @Override
    public boolean isIPv4() {
        return true;
    }

    @Override
    public boolean isIPv6() {
        return false;
    }

    @Override
    public boolean isInRange() {
        // TODO Auto-generated method stub
        return false;
    }

}
