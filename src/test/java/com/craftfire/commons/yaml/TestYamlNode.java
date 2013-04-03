package com.craftfire.commons.yaml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.junit.Before;
import org.junit.Test;

import com.craftfire.commons.util.LoggingManager;
import com.craftfire.commons.util.ValueHolderBase;
import com.craftfire.commons.util.ValueType;

public class TestYamlNode {
    private static Random rnd = new Random();
    private static LoggingManager logger = new LoggingManager("CraftFire.YamlManager", "[YamlManager]");
    private SimpleYamlManager mgr;
    private YamlNode mapNode, listNode, nullNode, scalarNode;
    private Map<String, Object> testMap;
    private List<Object> testList;
    private Object testScalar = "LHCb";

    @Before
    public void setup() {
        this.mgr = mock(SimpleYamlManager.class);
        stub(this.mgr.getSeparator()).toReturn(".");
        stub(this.mgr.isCaseSensitive()).toReturn(false);
        stub(this.mgr.getLogger()).toReturn(logger);

        this.testMap = new HashMap<String, Object>();
        this.testMap.put("a", "alice");
        this.testMap.put("bob", true);
        this.mapNode = new YamlNode(this.mgr, "map", this.testMap);

        this.testList = new ArrayList<Object>();
        this.testList.add("alice");
        this.testList.add("bob");
        this.listNode = new YamlNode(this.mgr, "list", this.testList);

        this.scalarNode = new YamlNode(this.mgr, "scalar", this.testScalar);
        this.nullNode = new YamlNode(this.mgr, "nul", null);
    }

    @Test
    public void testParent() {
        YamlNode node1 = new YamlNode(this.mgr, "a", null);
        YamlNode node2 = new YamlNode(node1, "b", "bob");
        assertTrue(node2.hasParent());
        assertSame(node1, node2.getParent());
        assertFalse(node1.hasParent());
        assertNull(node1.getParent());

        node1.setParent(node2);
        node2.setParent(null);

        assertTrue(node1.hasParent());
        assertSame(node2, node1.getParent());
        assertFalse(node2.hasParent());
        assertNull(node2.getParent());
    }

    @Test
    public void testNormalizePath() throws IOException {
        YamlNode node = new YamlNode(this.mgr, "a", "alice");
        assertNull(node.normalizePath(null));

        String testStr = "asdFADS.mnj" + System.currentTimeMillis();
        assertEquals(testStr.toLowerCase(), node.normalizePath(testStr));

        stub(this.mgr.isCaseSensitive()).toReturn(true);
        node = new YamlNode(this.mgr, "a", "alice");
        assertEquals(testStr, node.normalizePath(testStr));
    }

    @Test
    public void testPath() {
        YamlNode node0 = new YamlNode(this.mgr, null, null);
        YamlNode node1 = new YamlNode(node0, "a", null);
        YamlNode node2 = new YamlNode(node1, "b", "bob");

        List<String> elements = new ArrayList<String>();
        elements.add("a");
        elements.add("b");
        assertEquals(elements, node2.getPathElements());
        assertEquals("a.b", node2.getPath());
    }

    @Test
    public void testGetYamlManager() {
        YamlNode node = new YamlNode(this.mgr, "a", "alice");
        assertSame(this.mgr, node.getYamlManager());
    }

    @Test
    public void testResolve() throws YamlException {
        assertFalse(this.mapNode.isResolved());
        assertFalse(this.listNode.isResolved());

        assertTrue(this.mapNode.isMap());
        assertFalse(this.mapNode.isList());
        assertFalse(this.mapNode.isScalar());

        assertFalse(this.listNode.isMap());
        assertTrue(this.listNode.isList());
        assertFalse(this.listNode.isScalar());

        // Let's resolve them
        this.mapNode.getChildrenList();
        this.listNode.getChildrenList();

        assertTrue(this.mapNode.isResolved());
        assertTrue(this.listNode.isResolved());

        assertTrue(this.mapNode.isMap());
        assertFalse(this.mapNode.isList());
        assertFalse(this.mapNode.isScalar());

        assertFalse(this.listNode.isMap());
        assertTrue(this.listNode.isList());
        assertFalse(this.listNode.isScalar());

        assertEquals(ValueType.UNKNOWN, this.mapNode.getType());
        assertEquals(ValueType.UNKNOWN, this.listNode.getType());

        assertFalse(this.scalarNode.isResolved());

        assertFalse(this.scalarNode.isMap());
        assertFalse(this.scalarNode.isList());
        assertTrue(this.scalarNode.isScalar());

        try {
            this.scalarNode.getChildrenList();
        } catch (YamlException e) {
        }
        assertFalse(this.scalarNode.isResolved());
    }

    @Test
    public void testMapGetChild() throws YamlException {
        assertTrue(this.mapNode.hasChild("a"));
        assertFalse(this.mapNode.hasChild("alice"));

        YamlNode child0 = this.mapNode.getChild("a");
        assertNotNull(child0);
        assertEquals("a", child0.getName());
        assertEquals("alice", child0.getValue());
        assertSame(this.mapNode, child0.getParent());

        YamlNode child1 = this.mapNode.getChild("bob", true);
        assertNotNull(child1);
        assertEquals("bob", child1.getName());
        assertEquals(true, child1.getValue());
        assertSame(this.mapNode, child1.getParent());

        assertNull(this.mapNode.getChild("charlie"));

        YamlNode child2 = this.mapNode.getChild("charlie", true);
        assertNotNull(child2);
        assertEquals("charlie", child2.getName());
        assertNull(child2.getValue());
        assertSame(this.mapNode, child2.getParent());

        YamlNode child3 = child2.getChild("atlas", true);
        assertNotNull(child3);
        assertEquals("atlas", child3.getName());
        assertNull(child3.getValue());
        assertSame(child2, child3.getParent());
    }

    @Test
    public void testGetHasChild() {
        assertFalse(this.listNode.hasChild("alice"));
        assertFalse(this.scalarNode.hasChild("alice"));

        try {
            this.listNode.getChild("alice");
            fail("Expected a YamlException");
        } catch (YamlException e) {
        }

        try {
            this.scalarNode.getChild("alice");
            fail("Expected a YamlException");
        } catch (YamlException e) {
        }
    }

    @Test
    public void testGetChildrenMap() throws YamlException {
        Map<String, YamlNode> childMap = this.mapNode.getChildrenMap();
        assertNotNull(childMap);
        assertEquals(this.testMap.keySet(), childMap.keySet());
        for (Map.Entry<String, Object> entry : this.testMap.entrySet()) {
            assertEquals(entry.getValue(), childMap.get(entry.getKey()).getValue());
        }

        try {
            this.listNode.getChildrenMap();
            fail("Expected a YamlException");
        } catch (YamlException e) {
        }

        try {
            this.scalarNode.getChildrenMap();
            fail("Expected a YamlException");
        } catch (YamlException e) {
        }
    }

    @Test
    public void testGetChildrenList() throws YamlException {
        List<YamlNode> childList = this.mapNode.getChildrenList();
        assertNotNull(childList);
        assertEquals(this.testMap.size(), childList.size());
        for (YamlNode node : childList) {
            assertTrue(this.testMap.containsValue(node.getValue()));
        }

        childList = this.listNode.getChildrenList();
        assertNotNull(childList);
        assertEquals(this.testList.size(), childList.size());
        for (YamlNode node : childList) {
            assertTrue(this.testList.contains(node.getValue()));
        }

        try {
            this.scalarNode.getChildrenList();
            fail("Expected a YamlException");
        } catch (YamlException e) {
        }
    }

    @Test
    public void testAddChild() throws YamlException {
        YamlNode nodeSpy = spy(new YamlNode(this.mgr, "", null));
        nodeSpy.addChild(this.testScalar);
        verify(nodeSpy).addChild("", this.testScalar);

        try {
            this.scalarNode.addChild("", null);
            fail("Expected a YamlException");
        } catch (YamlException e) {
        }

        YamlNode child = this.nullNode.addChild(this.testScalar);
        assertTrue(this.nullNode.isResolved());
        assertTrue(this.nullNode.isList());
        assertEquals(1, this.nullNode.getChildrenList().size());
        assertSame(this.nullNode.getChildrenList().get(0), child);
        assertEquals(this.testScalar, child.getValue());
        assertEquals("", child.getName());

        this.nullNode = new YamlNode(this.mgr, "nul", null);
        child = this.nullNode.addChild("a", this.testScalar);
        assertTrue(this.nullNode.isResolved());
        assertTrue(this.nullNode.isMap());
        assertSame(this.nullNode.getChild("a"), child);
        assertEquals(1, this.nullNode.getChildrenList().size());
        assertEquals(this.testScalar, child.getValue());

        child = this.listNode.addChild("a", this.testScalar);
        List<YamlNode> children = this.listNode.getChildrenList();
        assertSame(children.get(children.size() - 1), child);
        assertEquals(this.testScalar, child.getValue());
        assertEquals("", child.getName());

        child = this.listNode.addChild("cms");
        children = this.listNode.getChildrenList();
        assertEquals("cms", child.getValue());
        assertEquals("", child.getName());

        child = this.mapNode.addChild("c", this.testScalar);
        assertSame(this.mapNode.getChild("c"), child);
        assertEquals(this.testScalar, child.getValue());

        try {
            this.mapNode.addChild("", null);
            fail("Expected a YamlException");
        } catch (YamlException e) {
        }
        try {
            this.mapNode.addChild(null, null);
            fail("Expected a YamlException");
        } catch (YamlException e) {
        }

        nodeSpy = spy(new YamlNode(this.mgr, null, null));
        YamlNode nodeMock = mock(YamlNode.class);
        stub(nodeMock.getName()).toReturn("atlas");
        stub(nodeMock.dump()).toReturn(this.testScalar);
        nodeSpy.addChild(nodeMock);
        verify(nodeSpy).addChild("atlas", this.testScalar);
    }

    @Test
    public void testAddChildren1() throws YamlException {
        try {
            this.scalarNode.addChildren(this.nullNode, this.listNode);
            fail("Expected a YamlException");
        } catch (YamlException e) {
        }

        this.nullNode.addChildren(this.scalarNode, this.listNode);
        assertTrue(this.nullNode.isResolved());
        assertTrue(this.nullNode.isMap());
        assertEquals(this.testScalar, this.nullNode.getChild(this.scalarNode.getName()).getValue());
        assertEquals(this.testList, this.nullNode.getChild(this.listNode.getName()).dump());
        assertEquals(2, this.nullNode.getChildrenList().size());
        this.nullNode = new YamlNode(this.mgr, "nul", null);

        YamlNode namelessNode1 = new YamlNode(this.mgr, null, "a");
        YamlNode namelessNode2 = new YamlNode(this.mgr, "", "b");

        this.listNode.addChildren(this.scalarNode, namelessNode1, namelessNode2);
        List<YamlNode> children = this.listNode.getChildrenList();
        YamlNode child = children.get(children.size() - 1);
        assertEquals("", child.getName());
        assertEquals("b", child.getValue());
        child = children.get(children.size() - 2);
        assertEquals("", child.getName());
        assertEquals("a", child.getValue());
        child = children.get(children.size() - 3);
        assertEquals("", child.getName());
        assertEquals(this.testScalar, child.getValue());

        this.mapNode.addChildren(this.scalarNode, this.nullNode);
        assertEquals(this.testScalar, this.mapNode.getChild(this.scalarNode.getName()).getValue());
        assertEquals(null, this.mapNode.getChild(this.nullNode.getName()).getValue());

        try {
            this.mapNode.addChildren(this.nullNode, namelessNode1);
            fail("Expected a YamlException");
        } catch (YamlException e) {
        }
        try {
            this.scalarNode.addChildren(namelessNode2);
            fail("Expected a YamlException");
        } catch (YamlException e) {
        }
    }

    @Test
    public void testAddChildren2() throws YamlException {
        try {
            this.scalarNode.addChildren(this.testMap);
            fail("Expected a YamlException");
        } catch (YamlException e) {
        }

        this.nullNode.addChildren(this.testMap);
        assertTrue(this.nullNode.isResolved());
        assertTrue(this.nullNode.isMap());
        assertEquals("alice", this.nullNode.getChild("a").getValue());
        assertEquals(true, this.nullNode.getChild("bob").getValue());
        assertEquals(2, this.nullNode.getChildrenList().size());
        this.nullNode = new YamlNode(this.mgr, "nul", null);

        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("scalar", this.testScalar);
        map1.put("", "atlas");
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put(null, "cms");

        this.listNode.addChildren(map1);
        List<YamlNode> children = this.listNode.getChildrenList();
        YamlNode child = children.get(children.size() - 1);
        assertEquals("", child.getName());
        assertEquals(this.testScalar, child.getValue());
        child = children.get(children.size() - 2);
        assertEquals("", child.getName());
        assertEquals("atlas", child.getValue());

        this.mapNode.addChildren(this.testMap);
        assertEquals("alice", this.mapNode.getChild("a").getValue());
        assertEquals(true, this.mapNode.getChild("bob").getValue());

        try {
            this.mapNode.addChildren(map1);
            fail("Expected a YamlException");
        } catch (YamlException e) {
        }
        try {
            this.scalarNode.addChildren(map2);
            fail("Expected a YamlException");
        } catch (YamlException e) {
        }
    }

    @Test
    public void testAddChildren3() throws YamlException {
        try {
            this.scalarNode.addChildren(this.testList);
            fail("Expected a YamlException");
        } catch (YamlException e) {
        }

        this.nullNode.addChildren(this.testList);
        assertTrue(this.nullNode.isResolved());
        assertTrue(this.nullNode.isList());
        List<YamlNode> children = this.nullNode.getChildrenList();
        YamlNode child = children.get(children.size() - 1);
        assertEquals("", child.getName());
        assertEquals("bob", child.getValue());
        child = children.get(children.size() - 2);
        assertEquals("", child.getName());
        assertEquals("alice", child.getValue());
        assertEquals(2, this.nullNode.getChildrenList().size());
        this.nullNode = new YamlNode(this.mgr, "nul", null);

        this.listNode.addChildren(this.testList);
        children = this.listNode.getChildrenList();
        child = children.get(children.size() - 1);
        assertEquals("", child.getName());
        assertEquals("bob", child.getValue());
        child = children.get(children.size() - 2);
        assertEquals("", child.getName());
        assertEquals("alice", child.getValue());

        try {
            this.mapNode.addChildren(this.testList);
            fail("Expected a YamlException");
        } catch (YamlException e) {
        }
    }

    @Test
    public void testChildrenCount() throws YamlException {
        assertEquals(0, this.scalarNode.getChildrenCount());
        assertEquals(0, this.nullNode.getChildrenCount());
        assertEquals(this.testList.size(), this.listNode.getChildrenCount());
        assertEquals(this.testMap.size(), this.mapNode.getChildrenCount());
        this.listNode.addChild("charlie");
        assertEquals(this.testList.size() + 1, this.listNode.getChildrenCount());
    }

    @Test
    public void testFinalNodeCount() throws YamlException, NoSuchFieldException, IllegalAccessException {
        assertEquals(1, this.scalarNode.getFinalNodeCount());

        YamlNode spyNode1 = spy(new YamlNode(this.mgr, "", "alice"));
        YamlNode spyNode2 = spy(new YamlNode(this.mgr, "", "bob"));
        int random1 = rnd.nextInt();
        int random2 = rnd.nextInt();
        doReturn(random1).when(spyNode1).getFinalNodeCount();
        doReturn(random2).when(spyNode2).getFinalNodeCount();
        List<YamlNode> list = new ArrayList<YamlNode>();
        list.add(spyNode1);
        list.add(spyNode2);

        this.listNode.getChildrenList(); // Make sure it's resolved
        Field f = this.listNode.getClass().getDeclaredField("listCache");
        f.setAccessible(true);
        f.set(this.listNode, list);

        assertEquals(random1 + random2, this.listNode.getFinalNodeCount());
        verify(spyNode1).getFinalNodeCount();
        verify(spyNode2).getFinalNodeCount();
    }

    @Test
    public void testRemoveChild() throws YamlException {
        YamlNode spyNode = spy(this.mapNode);
        assertNull(spyNode.removeChild("zed"));
        YamlNode child = spyNode.getChild("a");
        assertSame(child, spyNode.removeChild("a"));
        verify(spyNode, times(2)).getChild("a");
        verify(spyNode).removeChild(child);
        assertFalse(spyNode.hasChild("a"));

        assertNull(this.scalarNode.removeChild(this.nullNode));
        assertNull(this.listNode.removeChild(this.mapNode.getChild("bob")));

        child = this.listNode.getChildrenList().get(0);
        assertSame(child, this.listNode.removeChild(child));
        assertFalse(this.listNode.getChildrenList().contains(child));
        assertNull(child.getParent());

        child = this.mapNode.getChild("bob");
        assertSame(child, this.mapNode.removeChild(child));
        assertFalse(this.mapNode.hasChild("bob"));
        assertNull(child.getParent());
    }

    @Test
    public void testRemoveAllChildren() throws YamlException {
        YamlNode spyNode = spy(this.scalarNode);
        spyNode.removeAllChildren();
        verify(spyNode, never()).getChildrenList();

        YamlNode child = this.mapNode.getChild("a");
        this.mapNode.removeAllChildren();
        assertNull(child.getParent());
        assertEquals(0, this.mapNode.getChildrenList().size());
        assertTrue(this.mapNode.isMap());

        child = this.listNode.getChildrenList().get(0);
        this.listNode.removeAllChildren();
        assertNull(child.getParent());
        assertEquals(0, this.listNode.getChildrenList().size());
        assertTrue(this.listNode.isList());
    }

    @Test
    public void testSetValue() throws NoSuchFieldException, IllegalAccessException {
        this.mapNode.setValue(this.testScalar);
        assertEquals(this.testScalar, this.mapNode.getValue());
        assertFalse(this.mapNode.isResolved());
        assertTrue(this.mapNode.isScalar());
    }

    @Test
    public void testDump() throws YamlException {
        assertEquals(this.testScalar, this.scalarNode.dump());
        assertEquals(this.testList, this.listNode.dump());

        YamlNode spy1 = spy(this.nullNode);
        YamlNode spy2 = spy(this.scalarNode);
        doReturn("atlas").when(spy1).dump();
        doReturn("cms").when(spy2).dump();
        List<Object> list = new ArrayList<Object>();
        list.add("atlas");
        list.add("cms");

        this.listNode.removeAllChildren();
        this.listNode.addChild(spy1);
        this.listNode.addChild(spy2);
        assertEquals(list, this.listNode.dump());
        verify(spy1).dump();
        verify(spy2).dump();

        this.mapNode.removeAllChildren();
        this.mapNode.addChild(spy1);
        this.mapNode.addChild(spy2);

        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) this.mapNode.dump();
        assertEquals(2, map.size());
        assertEquals("atlas", map.get(this.nullNode.getName()));
        assertEquals("cms", map.get(this.scalarNode.getName()));
    }

    @Test
    public void testGetMapList() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("a", "linac2");
        map.put("b", "LHC");

        YamlNode mapSpy = spy(this.mapNode);
        doReturn(map).when(mapSpy).dump();
        assertEquals(map, mapSpy.getMap());
        verify(mapSpy).dump();

        List<Object> list = new ArrayList<Object>();
        list.add("proton");
        list.add("electron");
        list.add(true);

        YamlNode listSpy = spy(this.listNode);
        doReturn(list).when(listSpy).dump();
        assertEquals(list, listSpy.getList());
        verify(listSpy).dump();

        YamlNode scalarSpy = spy(this.scalarNode);
        assertNull(scalarSpy.getMap());
        assertNull(scalarSpy.getList());
        verify(scalarSpy, never()).dump();
    }

    @Test
    public void testGetNode() throws YamlException, NoSuchFieldException, IllegalAccessException {
        YamlNode spy1 = spy(this.nullNode);

        doReturn(this.scalarNode).when(spy1).getNode("alice.is.at.LHC", false);
        assertSame(this.scalarNode, spy1.getNode("alice.is.at.LHC"));
        verify(spy1).getNode("alice.is.at.LHC", false);

        doReturn(this.scalarNode).when(spy1).getNode("alice.is.a.detector", true);
        assertSame(this.scalarNode, spy1.getNode(true, "alice", "is", "a", "detector"));
        verify(spy1).getNode("alice.is.a.detector", true);

        doReturn(this.scalarNode).when(spy1).getNode(false, "alice", "has", "a", "cat");
        assertSame(this.scalarNode, spy1.getNode("alice", "has", "a", "cat"));
        verify(spy1).getNode(false, "alice", "has", "a", "cat");

        YamlNode spy2 = spy(this.mapNode);
        YamlNode mock = mock(YamlNode.class);

        assertNull(spy2.getNode("", false));

        doReturn(mock).when(spy2).getChild(eq("atlas"), anyBoolean());
        assertSame(mock, spy2.getNode("atlas", false));
        verify(spy2).getChild("atlas", false);

        stub(mock.getNode("is.a.detector.too", true)).toReturn(this.scalarNode);
        assertSame(this.scalarNode, spy2.getNode("atlas.is.a.detector.too", true));
        verify(spy2).getChild("atlas", true);
        verify(mock).getNode("is.a.detector.too", true);
    }

    @Test
    public void testHasNode() throws YamlException {
        boolean random = rnd.nextBoolean();
        YamlNode spy1 = spy(this.mapNode);
        assertFalse(spy1.hasNode(""));

        doReturn(true).when(spy1).hasChild("cms");
        assertTrue(spy1.hasNode("cms"));
        verify(spy1).hasChild("cms");

        YamlNode mock = mock(YamlNode.class);
        stub(mock.hasNode("is.yet.another.detector")).toReturn(random);
        doReturn(mock).when(spy1).getChild("cms");
        assertEquals(random, spy1.hasNode("cms.is.yet.another.detector"));
        verify(spy1, times(2)).hasChild("cms");
        verify(spy1).getChild("cms");
        verify(mock).hasNode("is.yet.another.detector");

        doReturn(!random).when(spy1).hasNode("cms.is.at.LHC.too");
        assertEquals(!random, spy1.hasNode("cms", "is", "at", "LHC", "too"));
        verify(spy1).hasNode("cms.is.at.LHC.too");
    }

    @Test
    public void testGetterRedirects() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, SerialException, SQLException {
        int randomInt = rnd.nextInt();
        long randomLong = rnd.nextLong();
        float randomFloat = rnd.nextFloat();
        double randomDouble = rnd.nextDouble();
        boolean randomBool = rnd.nextBoolean();
        byte[] randomBytes = new byte[8];
        rnd.nextBytes(randomBytes);
        String randomString = new String(randomBytes);
        BigInteger randomBigInt = new BigInteger(randomBytes);
        BigDecimal randomDecimal = BigDecimal.valueOf(randomDouble);
        Blob randomBlob = new SerialBlob(randomBytes);
        Date now = new Date();
        Blob blob1 = new SerialBlob(new byte[] { 1, 2, (byte) randomInt });

        ValueHolderBase holderMock = mock(ValueHolderBase.class);

        Field f = this.nullNode.getClass().getDeclaredField("holder");
        f.setAccessible(true);
        f.set(this.nullNode, holderMock);

        stub(holderMock.getName()).toReturn("test" + randomInt);
        assertEquals("test" + randomInt, this.nullNode.getName());
        stub(holderMock.getType()).toReturn(ValueType.REAL);
        assertEquals(ValueType.REAL, this.nullNode.getType());
        stub(holderMock.getValue()).toReturn(this.testScalar);
        assertEquals(this.testScalar, this.nullNode.getValue());
        stub(holderMock.getString("param" + randomInt)).toReturn(randomString);
        assertEquals(randomString, this.nullNode.getString("param" + randomInt));
        stub(holderMock.getInt((int) randomLong)).toReturn(randomInt);
        assertEquals(randomInt, this.nullNode.getInt((int) randomLong));
        stub(holderMock.getLong(randomInt)).toReturn(randomLong);
        assertEquals(randomLong, this.nullNode.getLong(randomInt));
        stub(holderMock.getBigInt(randomBigInt)).toReturn(BigInteger.valueOf(randomInt));
        assertEquals(BigInteger.valueOf(randomInt), this.nullNode.getBigInt(randomBigInt));
        stub(holderMock.getDouble(randomFloat)).toReturn(randomDouble);
        assertEquals(randomDouble, this.nullNode.getDouble(randomFloat), 0);
        stub(holderMock.getFloat(randomInt)).toReturn(randomFloat);
        assertEquals(randomFloat, this.nullNode.getFloat(randomInt), 0);
        stub(holderMock.getDecimal(BigDecimal.valueOf(randomLong))).toReturn(randomDecimal);
        assertEquals(randomDecimal, this.nullNode.getDecimal(BigDecimal.valueOf(randomLong)));
        stub(holderMock.getBytes("test".getBytes())).toReturn(randomBytes);
        assertEquals(randomBytes, this.nullNode.getBytes("test".getBytes()));
        stub(holderMock.getDate(new Date(randomLong))).toReturn(now);
        assertEquals(now, this.nullNode.getDate(new Date(randomLong)));
        stub(holderMock.getBlob(blob1)).toReturn(randomBlob);
        assertEquals(randomBlob, this.nullNode.getBlob(blob1));
        stub(holderMock.getBool(randomInt > 0)).toReturn(randomBool);
        assertEquals(randomBool, this.nullNode.getBool(randomInt > 0));
        stub(holderMock.isNull()).toReturn(randomBool);
        assertEquals(randomBool, this.nullNode.isNull());
        stub(holderMock.isUnsigned()).toReturn(!randomBool);
        assertEquals(!randomBool, this.nullNode.isUnsigned());
    }

    @Test
    public void testConstructor() {
        try {
            new YamlNode((SimpleYamlManager) null, "test", 18);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {

        }
    }
}
