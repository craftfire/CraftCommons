package com.craftfire.commons.yaml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.craftfire.commons.util.LoggingManager;

public class YamlCombiner implements YamlManager {
    private List<YamlManager> managers = new ArrayList<YamlManager>();
    private YamlManager defaultManager;
    private LoggingManager loggingManager = null;
    private Settings settings = new Settings();

    public YamlCombiner() {
    }

    public List<YamlManager> getYamlManagers() {
        return new ArrayList<YamlManager>(this.managers);
    }

    public void setYamlManagers(Collection<YamlManager> managers) {
        this.managers = new ArrayList<YamlManager>(managers);
        this.defaultManager = null;
    }

    public void addYamlManager(YamlManager manager) {
        this.managers.add(manager);
    }

    public YamlManager getDefaultManager() {
        if (this.defaultManager == null) {
            if (!this.managers.isEmpty()) {
                this.defaultManager = this.managers.get(0);
            }
        }
        return this.defaultManager;
    }

    public void setDefaultManager(YamlManager manager) {
        if (!this.managers.contains(manager)) {
            throw new IllegalArgumentException("You must add a the manager before setting it as default.");
        }
        this.defaultManager = manager;
    }

    public Settings getDefaultSettings() {
        return this.settings;
    }

    public void setDefaultSettings(Settings settings) {
        this.settings = settings;
    }

    @Override
    public Set<File> getFiles() {
        Set<File> files = new HashSet<File>();
        for (YamlManager manager : this.managers) {
            files.addAll(manager.getFiles());
        }
        return files;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getLogger()
     */
    @Override
    public LoggingManager getLogger() {
        return this.settings.getLogger();
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#setLoggingManager(com.craftfire.commons.util.LoggingManager)
     */
    @Override
    public void setLoggingManager(LoggingManager loggingManager) {
        this.settings.setLogger(loggingManager);
    }

    @Override
    public YamlNode getRootNode() {
        return this.defaultManager.getRootNode();
    }

    @Override
    public YamlNode setRootNode(YamlNode node) {
        this.managers = new ArrayList<YamlManager>();
        this.managers.add(this.defaultManager);
        return this.defaultManager.setRootNode(node);
    }

    @Override
    public boolean exist(String node) {
        for (YamlManager manager : this.managers) {
            if (manager.exist(node)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean getBoolean(String node) {
        return getBoolean(node, false);
    }

    @Override
    public boolean getBoolean(String path, boolean defaultValue) {
        try {
            YamlNode node = getNode(path);
            if (node != null) {
                boolean value = node.getBool(defaultValue);
                getLogger().debug("Found node '" + path + "' with Boolean value '" + value + "', default value is '" + defaultValue + "'");
                return value;
            }
        } catch (YamlException e) {
            getLogger().stackTrace(e);
        }
        getLogger().debug("Could not find node '" + path + "', returning default value instead '" + defaultValue + "'.");
        return defaultValue;
    }

    @Override
    public String getString(String node) {
        return getString(node, null);
    }

    @Override
    public String getString(String path, String defaultValue) {
        try {
            YamlNode node = getNode(path);
            if (node != null) {
                String value = node.getString(defaultValue);
                getLogger().debug("Found node '" + path + "' with String value '" + value + "', default value is '" + defaultValue + "'");
                return value;
            }
        } catch (YamlException e) {
            getLogger().stackTrace(e);
        }
        getLogger().debug("Could not find node '" + path + "', returning default value instead '" + defaultValue + "'.");
        return defaultValue;
    }

    @Override
    public int getInt(String node) {
        return getInt(node, 0);
    }

    @Override
    public int getInt(String path, int defaultValue) {
        try {
            YamlNode node = getNode(path);
            if (node != null) {
                int value = node.getInt(defaultValue);
                getLogger().debug("Found node '" + path + "' with Integer value '" + value + "', default value is '" + defaultValue + "'");
                return value;
            }
        } catch (YamlException e) {
            getLogger().stackTrace(e);
        }
        getLogger().debug("Could not find node '" + path + "', returning default value instead '" + defaultValue + "'.");
        return defaultValue;
    }

    @Override
    public long getLong(String node) {
        return getLong(node, 0);
    }

    @Override
    public long getLong(String path, long defaultValue) {
        try {
            YamlNode node = getNode(path);
            if (node != null) {
                long value = node.getLong(defaultValue);
                getLogger().debug("Found node '" + path + "' with Long value '" + value + "', default value is '" + defaultValue + "'");
                return value;
            }
        } catch (YamlException e) {
            getLogger().stackTrace(e);
        }
        getLogger().debug("Could not find node '" + path + "', returning default value instead '" + defaultValue + "'.");
        return defaultValue;
    }

    @Override
    public void addNodes(YamlManager yamlManager) throws YamlException {
        getDefaultManager().addNodes(yamlManager);
    }

    @Override
    public void addNodes(Map<String, Object> map) throws YamlException {
        getDefaultManager().addNodes(map);
    }

    @Override
    public void setNode(String node, Object value) throws YamlException {
        if (exist(node)) {
            getNode(node).setValue(value);
        } else {
            int last = node.lastIndexOf(this.settings.getSeparator());
            String left = node.substring(last);
            String found = node.substring(0, last);

            while (!found.isEmpty()) {
                if (exist(found)) {
                    getNode(found).getChild(left, true).setValue(value);
                    return;
                }
                last = found.lastIndexOf(this.settings.getSeparator());
                left = found.substring(last) + left;
                found = found.substring(0, last);
            }
            getDefaultManager().setNode(node, value);
        }
    }

    @Override
    public YamlNode getNode(String node) throws YamlException {
        for (YamlManager manager : this.managers) {
            if (manager.exist(node)) {
                return manager.getNode(node);
            }
        }
        return null;
    }

    @Override
    public boolean save() {
        boolean result = false;
        for (YamlManager manager : this.managers) {
            result = result || manager.save();
        }
        return result;
    }

    @Override
    public boolean reload() {
        boolean result = false;
        for (YamlManager manager : this.managers) {
            result = result || manager.reload();
        }
        return result;
    }

    public void load(File file) throws IOException {
        addYamlManager(new SimpleYamlManager(file, this.settings));
    }

    public void load(String path) throws IOException {
        addYamlManager(new SimpleYamlManager(path, this.settings));
    }

}
