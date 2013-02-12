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

/**
 * A YamlManager that combines multiple YamlManagers into one.
 * <p>
 * It allows to load yaml nodes from multiple files and treat them as one document, and then save them back to the files they came from.
 */
public class YamlCombiner implements YamlManager {
    private List<YamlManager> managers = new ArrayList<YamlManager>();
    private YamlManager defaultManager;
    private Settings settings = new Settings();

    /**
     * Creates a new YamlCombiner
     */
    public YamlCombiner() {
    }

    /**
     * Returns list of all YamlManagers used by the combiner.
     * 
     * @return list of YamlManagers
     */
    public List<YamlManager> getYamlManagers() {
        return new ArrayList<YamlManager>(this.managers);
    }

    /**
     * Sets the list of YamlManagers to use.
     * 
     * @param managers  list of YamlManagers
     */
    public void setYamlManagers(Collection<YamlManager> managers) {
        this.managers = new ArrayList<YamlManager>(managers);
        this.defaultManager = null;
    }

    /**
     * Adds a YamlManager to this combiner.
     * 
     * @param manager  the YamlManager to add
     */
    public void addYamlManager(YamlManager manager) {
        this.managers.add(manager);
    }

    /**
     * Returns the default YamlManager of this combiner.
     * <p>
     * All new nodes that don't match elsewhere will be added to the default YamlManager.
     * <p>
     * If the default manager is not set, this method will set it to the first one on the list. If the list is empty, will return {@code null}.
     * 
     * @return the default YamlManager or null
     */
    public YamlManager getDefaultManager() {
        if (this.defaultManager == null) {
            if (!this.managers.isEmpty()) {
                this.defaultManager = this.managers.get(0);
            }
        }
        return this.defaultManager;
    }

    /**
     * Sets the default YamlManager of this combiner.
     * <p>
     * All new nodes that don't match elsewhere will be added to the default YamlManager.
     * <p>
     * The given YamlManager must be already added to the list of managers to use.
     * 
     * @param  manager                  the YamlManager to set as default
     * @throws IllegalArgumentException if the manager is not on the list
     * @see    #addYamlManager(YamlManager)
     */
    public void setDefaultManager(YamlManager manager) {
        if (!this.managers.contains(manager)) {
            throw new IllegalArgumentException("You must add a the manager before setting it as default.");
        }
        this.defaultManager = manager;
    }

    /**
     * Returns the default settings used to create new YamlManagers by {@link #load(File)} and {@link #load(String)} methods.
     * 
     * @return the default settings
     */
    public Settings getDefaultSettings() {
        return this.settings;
    }

    /**
     * Sets the default settings to be used to create new YamlManagers by {@link #load(File)} and {@link #load(String)} methods.
     * 
     * @param settings  the default settings
     */
    public void setDefaultSettings(Settings settings) {
        this.settings = settings;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getFiles()
     */
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

    /**
     * Returns the root node of the default YamlManager of the combiner.
     * 
     * @see #setDefaultManager(YamlManager)
     * @see #getDefaultManager()
     */
    @Override
    public YamlNode getRootNode() {
        return this.defaultManager.getRootNode();
    }

    /**
     * Sets the root node of the default YamlManager of the combiner.
     * 
     * @see #setDefaultManager(YamlManager)
     * @see #getDefaultManager()
     */
    @Override
    public YamlNode setRootNode(YamlNode node) {
        this.managers = new ArrayList<YamlManager>();
        this.managers.add(this.defaultManager);
        return this.defaultManager.setRootNode(node);
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#exist(java.lang.String)
     */
    @Override
    public boolean exist(String node) {
        for (YamlManager manager : this.managers) {
            if (manager.exist(node)) {
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getBoolean(java.lang.String)
     */
    @Override
    public boolean getBoolean(String node) {
        return getBoolean(node, false);
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getBoolean(java.lang.String, boolean)
     */
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

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getString(java.lang.String)
     */
    @Override
    public String getString(String node) {
        return getString(node, null);
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getString(java.lang.String, java.lang.String)
     */
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

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getInt(java.lang.String)
     */
    @Override
    public int getInt(String node) {
        return getInt(node, 0);
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getInt(java.lang.String, int)
     */
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

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getLong(java.lang.String)
     */
    @Override
    public long getLong(String node) {
        return getLong(node, 0);
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getLong(java.lang.String, long)
     */
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

    /**
     * Adds all nodes from given YamlManager to the default YamlManager of the combiner.
     * 
     * @see #setDefaultManager(YamlManager)
     * @see #getDefaultManager()
     */
    @Override
    public void addNodes(YamlManager yamlManager) throws YamlException {
        getDefaultManager().addNodes(yamlManager);
    }

    /**
     * Adds all nodes from given map to the default YamlManager of the combiner.
     * 
     * @see #setDefaultManager(YamlManager)
     * @see #getDefaultManager()
     */
    @Override
    public void addNodes(Map<String, Object> map) throws YamlException {
        getDefaultManager().addNodes(map);
    }

    /**
     * Sets node with the given path to given value.
     * <p>
     * If the node doesn't exist, the method tries to add it to the YamlManager with most matching node. If the node doesn't match anywhere, then it's added to the default YamlManager of the combiner.
     * 
     * @see #setDefaultManager(YamlManager)
     * @see #getDefaultManager()
     */
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

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getNode(java.lang.String)
     */
    @Override
    public YamlNode getNode(String node) throws YamlException {
        for (YamlManager manager : this.managers) {
            if (manager.exist(node)) {
                return manager.getNode(node);
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.craftfire.commons.yaml.YamlManager#getFinalNodeCount()
     */
    @Override
    public int getFinalNodeCount() {
        int count = 0;
        for (YamlManager manager : this.managers) {
            count += manager.getFinalNodeCount();
        }
        return count;
    }

    /**
     * @return {@code true} saved successfully at least one YamlManager, {@code false} otherwise
     */
    @Override
    public boolean save() {
        boolean result = false;
        for (YamlManager manager : this.managers) {
            result = result || manager.save();
        }
        return result;
    }

    /**
     * @return {@code true} reloaded successfully at least one YamlManager, {@code false} otherwise
     */
    @Override
    public boolean reload() {
        boolean result = false;
        for (YamlManager manager : this.managers) {
            result = result || manager.reload();
        }
        return result;
    }

    /**
     * Creates a new SimpleYamlManager with the given file and adds it to the list.
     * <p>
     * Uses the default settings of the combiner to create the manager.
     * 
     * @param  file        the yaml file to load
     * @throws IOException if an IOException occurred
     * @see     #addYamlManager(YamlManager)
     * @see     #setDefaultSettings(Settings)
     * @see     SimpleYamlManager#SimpleYamlManager(File, Settings)
     */
    public void load(File file) throws IOException {
        addYamlManager(new SimpleYamlManager(file, this.settings));
    }

    /**
     * Creates a new SimpleYamlManager with the given classpath resource and adds it to the list.
     * <p>
     * Uses the default settings of the combiner to create the manager.
     * 
     * @param  path        path to the resource in classpath to load the yaml from
     * @throws IOException if an IOException occurred
     * @see    #addYamlManager(YamlManager)
     * @see    #setDefaultSettings(Settings)
     * @see    SimpleYamlManager#SimpleYamlManager(String, Settings)
     */
    public void load(String path) throws IOException {
        addYamlManager(new SimpleYamlManager(path, this.settings));
    }

}
