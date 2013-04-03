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

import com.craftfire.commons.util.Util;
import com.craftfire.commons.util.ValueHolder;
import com.craftfire.commons.util.ValueHolderBase;
import com.craftfire.commons.util.ValueType;

public class YamlNode implements ValueHolder {
    private List<YamlNode> listCache = null;
    private Map<String, YamlNode> mapCache = null;
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
        elements.add(getName());
        return elements;
    }

    public String getPath() {
        return Util.join(getPathElements(), ".");
    }

    public YamlManager getYamlManager() {
        return this.manager;
    }

    public boolean isMap() {
        return getValue() instanceof Map<?, ?>;
    }

    public boolean isList() {
        return getValue() instanceof Collection<?>;
    }

    public boolean isScalar() {
        return !isMap() && !isList();
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
        if (this.mapCache == null) {
            this.mapCache = new HashMap<String, YamlNode>();
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) getValue()).entrySet()) {
                String name = entry.getKey().toString();
                this.mapCache.put(name, new YamlNode(this, name, entry.getValue()));
            }
        }
        return new HashMap<String, YamlNode>(this.mapCache);
    }

    public List<YamlNode> getChildrenList() throws YamlException {
        if (isMap()) {
            if (this.mapCache != null) {
                return new ArrayList<YamlNode>(this.mapCache.values());
            }
            return new ArrayList<YamlNode>(getChildrenMap().values());
        }
        if (!isList()) {
            throw new YamlException("Node is not a list!", getPath());
        }
        if (this.listCache == null) {
            this.listCache = new ArrayList<YamlNode>();
            for (Object o : (Collection<?>) getValue()) {
                this.listCache.add(new YamlNode(this, null, o));
            }
        }
        return new ArrayList<YamlNode>(this.listCache);
    }

    protected void clearCache() {
        this.listCache = null;
        this.mapCache = null;
    }

    @SuppressWarnings("unchecked")
    public YamlNode addChild(String name, Object value) throws YamlException {
        if (isScalar()) {
            if (isNull()) {
                setValue(new HashMap<String, Object>());
            } else {
                throw new YamlException("Can't add child to scalar node", getPath());
            }
        }
        if (value instanceof ValueHolder) {
            value = ((ValueHolder) value).getValue();
        }
        if (isList()) {
            List<Object> list;
            if (getValue() instanceof List<?>) {
                list = (List<Object>) getValue(); // Because Java doesn't care if it's <Object> or something else.
            } else {
                list = new ArrayList<Object>((Collection<?>) getValue());
                setValue(list);
            }
            list.add(value);
            clearCache();
            return getChildrenList().get(list.lastIndexOf(value));
        }
        Map<Object, Object> map = (Map<Object, Object>) getValue();
        map.put(name, value);
        clearCache();
        return getChild(name);
    }

    public YamlNode addChild(YamlNode node) throws YamlException {
        return addChild(node.getName(), node.getValue());
    }

    public YamlNode removeChild(String name) throws YamlException {
        if (hasChild(name)) {
            return removeChild(getChild(name));
        }
        return null;
    }

    public YamlNode removeChild(YamlNode node) {
        if (isScalar() || node.getParent() != this) {
            return null;
        }
        if (isList()) {
            ((Collection<?>) getValue()).remove(node.getValue());
        } else {
            ((Map<?, ?>) getValue()).remove(node.getName());
        }
        node.setParent(null);
        clearCache();
        return node;
    }

    public void setValue(Object data) {
        this.holder = new ValueHolderBase(this.holder.getName(), false, data);
        clearCache();
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
            Collection<?> value = (Collection<?>) getValue();
            if (value instanceof List<?>) {
                return (List<Object>) value;
            }
            return new ArrayList<Object>(value);
        }
        return null;
    }

    public Map<Object, Object> getMap() {
        if (isMap()) {
            return (Map<Object, Object>) getValue();
        }
        return null;
    }

    @Override
    public String getName() {
        return this.holder.getName();
    }

    @Override
    public ValueType getType() {
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
    public int getInt() {
        return this.holder.getInt();
    }

    @Override
    public long getLong() {
        return this.holder.getLong();
    }

    @Override
    public BigInteger getBigInt() {
        return this.holder.getBigInt();
    }

    @Override
    public double getDouble() {
        return this.holder.getDouble();
    }

    @Override
    public float getFloat() {
        return this.holder.getFloat();
    }

    @Override
    public BigDecimal getDecimal() {
        return this.holder.getDecimal();
    }

    @Override
    public byte[] getBytes() {
        return this.holder.getBytes();
    }

    @Override
    public Date getDate() {
        return this.holder.getDate();
    }

    @Override
    public Blob getBlob() {
        return this.holder.getBlob();
    }

    @Override
    public boolean getBool() {
        return this.holder.getBool();
    }

    @Override
    public boolean isNull() {
        return this.holder.isNull();
    }

    @Override
    public boolean isUnsigned() {
        return this.holder.isUnsigned();
    }

}
