package com.craftfire.commons.yaml;

/**
 * An exception thrown when something goes wrong with accessing a yaml tree.
 */
public class YamlException extends Exception {
    // TODO: Should it be a RuntimeException?
    private static final long serialVersionUID = -592286590230664399L;
    private final String path;

    /**
     * Creates a new YamlException.
     */
    public YamlException() {
        this.path = null;
    }

    /**
     * Creates a new YamlException with given detail message.
     * 
     * @param message  the detail message
     */
    public YamlException(String message) {
        super(message);
        this.path = null;
    }

    /**
     * Creates a new YamlException with given detail message and node path.
     * 
     * @param message  the detail message
     * @param path     the node path
     */
    public YamlException(String message, String path) {
        super(message + " at node: " + path);
        this.path = path;
    }

    /**
     * Creates a new YamlExceptin with given cause.
     * 
     * @param cause  the cause
     */
    public YamlException(Throwable cause) {
        super(cause);
        this.path = null;
    }

    /**
     * Creates a new YamlException with given detail message and cause.
     * 
     * @param message  the detail message
     * @param cause    the cause
     */
    public YamlException(String message, Throwable cause) {
        super(message, cause);
        this.path = null;
    }

    /**
     * Creates a new YamlException with given detail message, node path, and cause.
     * 
     * @param message  the detail message
     * @param path     the node path
     * @param cause    the cause
     */
    public YamlException(String message, String path, Throwable cause) {
        super(message + " at node: " + path, cause);
        this.path = path;
    }

    /**
     * Path of the node this exception was thrown at.
     * 
     * @return  the node path
     */
    public String getPath() {
        return this.path;
    }

}
