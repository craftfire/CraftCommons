/*
 * This file is part of CraftCommons.
 *
 * Copyright (c) 2011 CraftFire <http://www.craftfire.com/>
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
