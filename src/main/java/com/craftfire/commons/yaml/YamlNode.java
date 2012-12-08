package com.craftfire.commons.yaml;

import java.security.PublicKey;

public class YamlNode {
    private String node;
    private Object data;

    public YamlNode(String node, Object data) {
        this.node = node;
        this.data = data;
    }

    public String getNode() {
        return this.node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public void set(Object data) {
        this.data = data;
    }

    public Object getObject() {
        return this.data;
    }

    public String getString() {
        if (this.data instanceof String) {
            return (String) this.data;
        }
        return null;
    }

    public boolean getBoolean() {
        if (this.data instanceof Boolean) {
            return (Boolean) this.data;
        }
        return false;
    }

    public int getInt() {
        if (this.data instanceof Integer) {
            return (Integer) this.data;
        }
        return 0;
    }

    public double getDouble() {
        if (this.data instanceof Double) {
            return (Double) this.data;
        }
        return 0;
    }

    public long getLong() {
        if (this.data instanceof Long) {
            return (Long) this.data;
        }
        return 0;
    }
}
