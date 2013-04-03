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
package com.craftfire.commons.yaml;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;

import com.craftfire.commons.util.LoggingManager;

/**
 * A class holding settings needed by YamlManager
 */
public class Settings {
    private boolean caseSensitive = false;
    private boolean multiDocument = false;
    private String separator = ".";
    private BaseConstructor constructor;
    private Representer representer;
    private DumperOptions options;
    private Resolver resolver;
    private LoggingManager logger;

    /**
     * Creates a new Settings with default values.
     */
    public Settings() {
        this.constructor = new Constructor();
        this.representer = new EmptyNullRepresenter();
        this.resolver = new Resolver();
        this.options = new DumperOptions();
        this.options.setDefaultFlowStyle(FlowStyle.BLOCK);
        this.options.setIndent(4);
        this.logger = new LoggingManager("CraftFire.YamlManager", "[YamlManager]");
    }

    /**
     * Creates a snakeyaml parser based on these settings.
     * 
     * @return snakeyaml parser
     */
    public Yaml createYaml() {
        return new Yaml(this.constructor, this.representer, this.options, this.resolver);
    }

    /**
     * Returns the state of case-sensitive option (off by default).
     * <p>
     * If this option is on, node names will be case-sensitive. 
     * 
     * @return true if on, otherwise false
     */
    public boolean isCaseSensitive() {
        return this.caseSensitive;
    }

    /**
     * Sets the state of case-sensitive option (off by default).
     * <p>
     * If this option is on, node names will be case-sensitive.
     * 
     * @param caseSensitive  true to turn on, false to turn off
     * @return               this
     */
    public Settings setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        return this;
    }

    /**
     * NOT IMPLEMENTED YET!
     * Checks the state of multi-document option (off by default).
     * <p>
     * This option allows to read multiple yaml documents from one file (separated by {@code ---}).
     * 
     * @return true if on, false otherwise
     */
    public boolean isMultiDocument() {
        return this.multiDocument;
    }

    /**
     * NOT IMPLEMENTED YET!
     * Sets the state of multi-document option (off by default).
     * <p>
     * This option allows to read multiple yaml documents from one file (separated by {@code ---}).
     * 
     * @param multiDocument  true to turn on, false to turn off
     * @return               this
     */
    public Settings setMultiDocument(boolean multiDocument) {
        this.multiDocument = multiDocument;
        return this;
    }

    /**
     * Returns the path separator to be used (default {@code "."}).
     * 
     * @return the path separator
     */
    public String getSeparator() {
        return this.separator;
    }

    /**
     * Sets the path separator to be used (default {@code "."}).
     * 
     * @param separator  the path separator
     * @return           this
     */
    public Settings setSeparator(String separator) {
        this.separator = separator;
        return this;
    }

    /**
     * Returns the yaml constructor to be used ({@link Constructor} by default).
     * 
     * @return the yaml constructor
     */
    public BaseConstructor getConstructor() {
        return this.constructor;
    }

    /**
     * Sets the yaml constructor to be used ({@link Constructor} by default).
     * 
     * @param constructor  the yaml constructor
     * @return             this
     */
    public Settings setConstructor(BaseConstructor constructor) {
        this.constructor = constructor;
        return this;
    }

    /**
     * Returns the yaml representer to be used ({@link EmptyNullRepresenter} by default).
     * 
     * @return the yaml representer
     */
    public Representer getRepresenter() {
        return this.representer;
    }

    /**
     * Sets the yaml representer to be used ({@link EmptyNullRepresenter} by default).
     * 
     * @param representer  the yaml representer
     * @return             this
     */
    public Settings setRepresenter(Representer representer) {
        this.representer = representer;
        return this;
    }

    /**
     * Returns the yaml dumper options.
     * <p>
     * By default it's block style, 4 spaces ident.
     * 
     * @return yaml dumper options
     */
    public DumperOptions getDumperOptions() {
        return this.options;
    }

    /**
     * Sets the yaml dumper options.
     * <p>
     * By default it's block style, 4 spaces ident.
     * 
     * @param options  yaml dumper options
     * @return         this
     */
    public Settings setDumperOptions(DumperOptions options) {
        this.options = options;
        return this;
    }

    /**
     * Returns the yaml resolver to be used ({@link Resolver} by default).
     * 
     * @return the yaml resolver
     */
    public Resolver getResolver() {
        return this.resolver;
    }

    /**
     * Sets the yaml resolver to be used ({@link Resolver} by default).
     * 
     * @param resolver  the yaml resolver
     * @return          this
     */
    public Settings setResolver(Resolver resolver) {
        this.resolver = resolver;
        return this;
    }

    /**
     * Returns the logging manager to be used.
     * <p>
     * By default it's {@code new LoggingManager("CraftFire.YamlManager", "[YamlManager]")}.
     * 
     * @return the logging manager
     */
    public LoggingManager getLogger() {
        return this.logger;
    }

    /**
     * Sets the logging manager to be used.
     * <p>
     * By default it's {@code new LoggingManager("CraftFire.YamlManager", "[YamlManager]")}.
     * 
     * @param  logger                   the logging manager
     * @return                          this
     * @throws IllegalArgumentException if the logger is null
     */
    public Settings setLogger(LoggingManager logger) {
        if (logger == null) {
            throw new IllegalArgumentException("Parameter 'logger' cannot be null.");
        }
        this.logger = logger;
        return this;
    }
}