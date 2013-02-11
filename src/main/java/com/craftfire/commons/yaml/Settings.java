package com.craftfire.commons.yaml;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;

import com.craftfire.commons.util.LoggingManager;

class Settings {
    private boolean caseSensitive = false;
    private boolean multiDocument = false;
    private String separator = ".";
    private BaseConstructor constructor;
    private Representer representer;
    private DumperOptions options;
    private Resolver resolver;
    private LoggingManager logger;

    public Settings() {
        this.constructor = new Constructor();
        this.representer = new EmptyNullRepresenter();
        this.resolver = new Resolver();
        this.options = new DumperOptions();
        this.options.setDefaultFlowStyle(FlowStyle.BLOCK);
        this.options.setIndent(4);
        this.logger = new LoggingManager("CraftFire.YamlManager", "[YamlManager]");
    }

    public Yaml createYaml() {
        return new Yaml(this.constructor, this.representer, this.options, this.resolver);
    }

    public boolean isCaseSensitive() {
        return this.caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public boolean isMultiDocument() {
        return this.multiDocument;
    }

    public void setMultiDocument(boolean multiDocument) {
        this.multiDocument = multiDocument;
    }

    public String getSeparator() {
        return this.separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public BaseConstructor getConstructor() {
        return this.constructor;
    }

    public void setConstructor(BaseConstructor constructor) {
        this.constructor = constructor;
    }

    public Representer getRepresenter() {
        return this.representer;
    }

    public void setRepresenter(Representer representer) {
        this.representer = representer;
    }

    public DumperOptions getDumperOptions() {
        return this.options;
    }

    public void setDumperOptions(DumperOptions options) {
        this.options = options;
    }

    public Resolver getResolver() {
        return this.resolver;
    }

    public void setResolver(Resolver resolver) {
        this.resolver = resolver;
    }

    public LoggingManager getLogger() {
        return this.logger;
    }

    public void setLogger(LoggingManager logger) {
        if (logger == null) {
            throw new IllegalArgumentException("Parameter 'logger' cannot be null.");
        }
        this.logger = logger;
    }
}