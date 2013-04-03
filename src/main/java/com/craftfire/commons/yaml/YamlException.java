package com.craftfire.commons.yaml;

public class YamlException extends Exception {
    private final String path;

    public YamlException() {
        this.path = null;
    }

    public YamlException(String message) {
        super(message);
        this.path = null;
    }

    public YamlException(String message, String path) {
        super(message + " at node: " + path);
        this.path = path;
    }

    public YamlException(Throwable cause) {
        super(cause);
        this.path = null;
    }

    public YamlException(String message, Throwable cause) {
        super(message, cause);
        this.path = null;
    }

    public YamlException(String message, String path, Throwable cause) {
        super(message + " at node: " + path, cause);
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

}
