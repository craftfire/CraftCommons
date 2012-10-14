package com.craftfire.commons.classes;

import java.net.Inet6Address;

public class IPv6Address extends IPAddress {
    
    public IPv6Address(Inet6Address address) {
        // TODO
    }

    public IPv6Address(String address) {
        // TODO
    }

    @Override
    public boolean isIPv4() {
        return false;
    }

    @Override
    public boolean isIPv6() {
        return true;
    }

    @Override
    public boolean isInRange() {
        // TODO Auto-generated method stub
        return false;
    }

}
