package com.craftfire.commons.yaml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.craftfire.commons.util.AbstractValueHolder;
import com.craftfire.commons.util.Util;
import com.craftfire.commons.util.ValueHolder;
import com.craftfire.commons.util.ValueHolderBase;
import com.craftfire.commons.util.ValueType;

public class YamlNode extends AbstractValueHolder {
    private List<YamlNode> listCache = null;
    private Map<String, YamlNode> mapCache = null;
    private boolean resolved = false;
    private ValueHolder holder;
    private final YamlManager manager;
    private YamlNode parent = null;

    public YamlNode(YamlManager manager, String name, Object data) {
        this.holder = new ValueHolderBase(name, false, data);
        this.manager = manager;
    }

    public YamlNode(YamlNode parent, String name, Object data) {
        this(parent.getYamlManager(), name, data);
        this.parent = parent;
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    public YamlNode getParent() {
        return this.parent;
    }

    protected void setParent(YamlNode parent) {
        this.parent = parent;
    }

    public List<String> getPathElements() {
        List<String> elements;
        if (hasParent()) {
            elements = getParent().getPathElements();
        } else {
            elements = new ArrayList<String>();
        }
        if (getName() != null) {
            elements.add(getName());
        }
        return elements;
    }

    public String getPath() {
        return Util.join(getPathElements(), ".");
    }

    public YamlManager getYamlManager() {
        return this.manager;
    }

    public boolean isMap() {
        if (this.resolved) {
            return this.mapCache != null;
        }
        return getValue() instanceof Map<?, ?>;
    }

    public boolean isList() {
        if (this.resolved) {
            return this.listCache != null;
        }
        return getValue() instanceof Collection<?>;
    }

    public boolean isScalar() {
        return !isMap() && !isList();
    }

    public boolean isResloved() {
        return this.resolved;
    }

    public YamlNode getChild(String name) throws YamlException {
        return getChild(name, false);
    }

    public YamlNode getChild(String name, boolean add) throws YamlException {
        if (add && isMap() && !hasChild(name)) {
            return addChild(name, null);
        }
        return getChildrenMap().get(name);
    }

    public boolean hasChild(String name) {
        if (!isMap()) {
            return false;
        }
        try {
            return getChildrenMap().containsKey(name);
        } catch (YamlException e) {
            this.manager.getLogger().stackTrace(e);
            return false;
        }
    }

    public Map<String, YamlNode> getChildrenMap() throws YamlException {
        if (!isMap()) {
            throw new YamlException("Node is not a map!", getPath());
        }
        if (!this.resolved) {
            this.mapCache = new HashMap<String, YamlNode>();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) getValue()).entrySet()) {
                String name = entry.getKey().toString();
                this.mapCache.put(name, new YamlNode(this, name, entry.getValue()));
            }
            this.holder = new ValueHolderBase(this.holder.getName(), false, null);
            this.resolved = true;
        }
        return new HashMap<String, YamlNode>(this.mapCache);
    }

    public List<YamlNode> getChildrenList() throws YamlException {
        if (isMap()) {
            if (this.resolved) {
                return new ArrayList<YamlNode>(this.mapCache.values());
            }
            return new ArrayList<YamlNode>(getChildrenMap().values());
        }
        if (!isList()) {
            throw new YamlException("Node is not a list!", getPath());
        }
        if (!this.resolved) {
            this.listCache = new ArrayList<YamlNode>();
            for (Object o : (Collection<?>) getValue()) {
                this.listCache.add(new YamlNode(this, "", o));
            }
            this.holder = new ValueHolderBase(this.holder.getName(), false, null);
            this.resolved = true;
        }
        return new ArrayList<YamlNode>(this.listCache);
    }

    public YamlNode addChild(Object value) throws YamlException {
        if (!isList()) {
            if (isNull()) {
                this.listCache = new ArrayList<YamlNode>();
                this.resolved = true;
            } else {
                throw new YamlException("Can't add nameless child to non-list node", getPath());
            }
        }
        return addChild("", value);
    }

    public YamlNode addChild(String name, Object value) throws YamlException {
        if (isScalar()) {
            if (isNull()) {
                this.mapCache = new HashMap<String, YamlNode>();
                this.resolved = true;
            } else {
                throw new YamlException("Can't add child to scalar node", getPath());
            }
        }
        if (value instanceof ValueHolder) {
            value = ((ValueHolder) value).getValue();
        }
        YamlNode node;
        if (!this.resolved) {
            getChildrenList(); // This can resolve both Map and List
        }
        if (isList()) {
            node = new YamlNode(this, "", value);
            this.listCache.add(node);
            return node;
        }
        node = new YamlNode(this, name, value);
        this.mapCache.put(name, node);
        return node;
    }

    public YamlNode addChild(YamlNode node) throws YamlException {
        return addChild(node.getName(), node.dump());
    }

    public void addChildren(YamlNode... nodes) throws YamlException {
        if (isScalar()) {
            if (isNull()) {
                this.mapCache = new HashMap<String, YamlNode>();
                this.resolved = true;
            } else {
                throw new YamlException("Can't add child to scalar node", getPath());
            }
        }
        if (!this.resolved) {
            getChildrenList(); // This can resolve both Map and List
        }
        if (isList()) {
            for (YamlNode node : nodes) {
                this.listCache.add(new YamlNode(this, "", node.dump()));
            }
            return;
        }
        for (YamlNode node : nodes) {
            this.mapCache.put(node.getName(), new YamlNode(this, node.getName(), node.dump()));
        }
    }

    public void addChildren(Map<?, ?> map) throws YamlException {
        if (isScalar()) {
            if (isNull()) {
                this.mapCache = new HashMap<String, YamlNode>();
                this.resolved = true;
            } else {
                throw new YamlException("Can't add child to scalar node", getPath());
            }
        }
        if (!this.resolved) {
            getChildrenList(); // This can resolve both Map and List
        }
        if (isList()) {
            for (Object value : map.values()) {
                this.listCache.add(new YamlNode(this, "", value));
            }
            return;
        }
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String name = entry.getKey().toString();
            this.mapCache.put(name, new YamlNode(this, name, entry.getValue()));
        }
    }

    public void addChildren(Collection<?> collection) throws YamlException {
        if (!isList()) {
            if (isNull()) {
                this.listCache = new ArrayList<YamlNode>();
                this.resolved = true;
            } else {
                throw new YamlException("Can't add nameless child to non-list node", getPath());
            }
        }
        if (!this.resolved) {
            getChildrenList();
        }
        for (Object value : collection) {
            this.listCache.add(new YamlNode(this, "", value));
        }
    }

    public int getChildrenCount() {
        if (isScalar()) {
            return 0;
        }
        try {
            getChildrenList(); // This can resolve both Map and List
        } catch (YamlException e) {
            this.manager.getLogger().stackTrace(e);
            return 0;
        }
        if (isList()) {
            return this.listCache.size();
        }
        return this.mapCache.size();
    }

    public int getFinalNodeCount() {
        if (isScalar()) {
            return 1;
        }
        int count = 0;
        try {
            for (YamlNode node : getChildrenList()) {
                count += node.getFinalNodeCount();
            }
        } catch (YamlException e) {
            this.manager.getLogger().stackTrace(e);
        }
        return count;
    }

    public YamlNode removeChild(String name) {
        if (hasChild(name)) {
            try {
                return removeChild(getChild(name));
            } catch (YamlException e) {
                this.manager.getLogger().stackTrace(e);
            }
        }
        return null;
    }

    public YamlNode removeChild(YamlNode node) {
        if (isScalar() || node.getParent() != this) {
            return null;
        }
        if (!this.resolved) {
            try {
                getChildrenList(); // This can resolve both Map and List
            } catch (YamlException e) {
                this.manager.getLogger().stackTrace(e);
                return null;
            }
        }
        if (isList()) {
            this.listCache.remove(node);
        } else {
            this.mapCache.remove(node.getName());
        }
        node.setParent(null);
        return node;
    }

    public void removeAllChildren() {
        if (isMap()) {
            this.mapCache = new HashMap<String, YamlNode>();
        } else if (isList()) {
            this.listCache = new ArrayList<YamlNode>();
        }
    }

    public void setValue(Object data) {
        this.holder = new ValueHolderBase(this.holder.getName(), false, data);
        this.listCache = null;
        this.mapCache = null;
        this.resolved = false;
    }

    public Object dump() {
        if (!this.resolved) {
            return getValue();
        }
        if (isList()) {
            List<Object> list = new ArrayList<Object>();
            for (YamlNode node : this.listCache) {
                list.add(node.dump());
            }
            return list;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        for (Map.Entry<String, YamlNode> entry : this.mapCache.entrySet()) {
            map.put(entry.getKey(), entry.getValue().dump());
        }
        return map;
    }

    public YamlNode getNode(String... path) throws YamlException {
        return getNode(false, path);
    }

    public YamlNode getNode(boolean add, String... path) throws YamlException {
        return getNode(Util.join(path, "."), add);
    }

    public YamlNode getNode(String path) throws YamlException {
        return getNode(path, false);
    }

    public YamlNode getNode(String path, boolean add) throws YamlException {
        String[] elements = path.split("\\.", 2);
        if (elements.length == 0) {
            return null;
        }
        YamlNode node = getChild(elements[0], add);
        if (node == null || elements.length == 1) {
            return node;
        }
        return node.getNode(elements[1], add);
    }

    public boolean hasNode(String... path) {
        return hasNode(Util.join(path, "."));
    }

    public boolean hasNode(String path) {
        String[] elements = path.split("\\.", 2);
        if (elements.length == 0 || !hasChild(elements[0])) {
            return false;
        }
        if (elements.length == 1) {
            return true;
        }
        try {
            return getChild(elements[0]).hasNode(elements[1]);
        } catch (YamlException e) {
            this.manager.getLogger().stackTrace(e);
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Object> getList() {
        if (isList()) {
            return (List<Object>) dump();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Map<Object, Object> getMap() {
        if (isMap()) {
            return (Map<Object, Object>) dump();
        }
        return null;
    }

    @Override
    public String getName() {
        return this.holder.getName();
    }

    @Override
    public ValueType getType() {
        if (this.resolved) {
            return ValueType.UNKNOWN;
        }
        return this.holder.getType();
    }

    @Override
    public Object getValue() {
        return this.holder.getValue();
    }

    @Override
    public String getString() {
        return this.holder.getString();
    }

    @Override
    public String getString(String defaultValue) {
        return this.holder.getString(defaultValue);
    }

    @Override
    public int getInt(int defaultValue) {
        return this.holder.getInt(defaultValue);
    }

    @Override
    public long getLong(long defaultValue) {
        return this.holder.getLong(defaultValue);
    }

    @Override
    public BigInteger getBigInt(BigInteger defaultValue) {
        return this.holder.getBigInt(defaultValue);
    }

    @Override
    public double getDouble(double defaultValue) {
        return this.holder.getDouble(defaultValue);
    }

    @Override
    public float getFloat(float defaultValue) {
        return this.holder.getFloat(defaultValue);
    }

    @Override
    public BigDecimal getDecimal(BigDecimal defaultValue) {
        return this.holder.getDecimal(defaultValue);
    }

    @Override
    public byte[] getBytes(byte[] defaultValue) {
        return this.holder.getBytes(defaultValue);
    }

    @Override
    public Date getDate(Date defaultValue) {
        return this.holder.getDate(defaultValue);
    }

    @Override
    public Blob getBlob(Blob defaultValue) {
        return this.holder.getBlob(defaultValue);
    }

    @Override
    public boolean getBool(boolean defaultValue) {
        return this.holder.getBool(defaultValue);
    }

    @Override
    public boolean isNull() {
        return !this.resolved && this.holder.isNull();
    }

    @Override
    public boolean isUnsigned() {
        return this.holder.isUnsigned();
    }

}
