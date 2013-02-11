package com.craftfire.commons.yaml;

import java.io.File;
import java.util.Map;
import java.util.Set;

import com.craftfire.commons.util.LoggingManager;

public interface YamlManager {

    Set<File> getFiles();

    LoggingManager getLogger();

    void setLoggingManager(LoggingManager loggingManager);

    YamlNode getRootNode();

    YamlNode setRootNode(YamlNode node);

    boolean exist(String node);

    boolean getBoolean(String node);

    boolean getBoolean(String node, boolean defaultValue);

    String getString(String node);

    String getString(String node, String defaultValue);

    int getInt(String node);

    int getInt(String node, int defaultValue);

    long getLong(String node);

    long getLong(String node, long defaultValue);

    void addNodes(YamlManager yamlManager) throws YamlException;

    void addNodes(Map<String, Object> map) throws YamlException;

    void setNode(String node, Object value) throws YamlException;

    YamlNode getNode(String node) throws YamlException;

    int getFinalNodeCount();

    boolean save();

    boolean reload();

}