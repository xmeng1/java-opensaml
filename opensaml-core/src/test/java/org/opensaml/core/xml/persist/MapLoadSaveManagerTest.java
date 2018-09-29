/*
 * Licensed to the University Corporation for Advanced Internet Development,
 * Inc. (UCAID) under one or more contributor license agreements.  See the
 * NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The UCAID licenses this file to You under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.core.xml.persist;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.opensaml.core.xml.XMLObjectBaseTestCase;
import org.opensaml.core.xml.mock.SimpleXMLObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.Sets;
import com.google.common.util.concurrent.Uninterruptibles;

import net.shibboleth.utilities.java.support.collection.Pair;

/**
 *
 */
public class MapLoadSaveManagerTest extends XMLObjectBaseTestCase {
    
    private MapLoadSaveManager<SimpleXMLObject> manager;
    
    @BeforeMethod
    public void setup() {
        manager = new MapLoadSaveManager<>();
    }
    
    @Test
    public void emptyMap() throws IOException {
        testState(Sets.<String>newHashSet());
    }
    
    @Test
    public void saveLoadUpdateRemove() throws IOException {
        testState(Sets.<String>newHashSet());
        
        Assert.assertNull(manager.load("bogus"));
        
        manager.save("foo", (SimpleXMLObject) buildXMLObject(SimpleXMLObject.ELEMENT_NAME));
        testState(Sets.newHashSet("foo"));
        
        manager.save("bar", (SimpleXMLObject) buildXMLObject(SimpleXMLObject.ELEMENT_NAME));
        manager.save("baz", (SimpleXMLObject) buildXMLObject(SimpleXMLObject.ELEMENT_NAME));
        testState(Sets.newHashSet("foo", "bar", "baz"));
        
        // Duplicate with overwrite
        manager.save("bar", (SimpleXMLObject) buildXMLObject(SimpleXMLObject.ELEMENT_NAME), true);
        testState(Sets.newHashSet("foo", "bar", "baz"));
        
        // Duplicate without overwrite
        try {
            manager.save("bar", (SimpleXMLObject) buildXMLObject(SimpleXMLObject.ELEMENT_NAME), false);
            Assert.fail("Should have failed on duplicate save without overwrite");
        } catch (IOException e) {
            // expected, do nothing
        }
        testState(Sets.newHashSet("foo", "bar", "baz"));
        
        // Test again. Since checkModifyTime=false, we should get back data even though unmodified
        testState(Sets.newHashSet("foo", "bar", "baz"));
        
        Assert.assertTrue(manager.updateKey("foo", "foo2"));
        testState(Sets.newHashSet("foo2", "bar", "baz"));
        
        // Doesn't exist anymore
        Assert.assertFalse(manager.updateKey("foo", "foo2"));
        testState(Sets.newHashSet("foo2", "bar", "baz"));
        
        // Can't update to an existing name
        try {
            manager.updateKey("bar", "baz");
            Assert.fail("updateKey should have failed to due existing new key name");
        } catch (IOException e) {
            // expected, do nothing
        }
        testState(Sets.newHashSet("foo2", "bar", "baz"));
        
        // Doesn't exist anymore
        Assert.assertFalse(manager.remove("foo"));
        testState(Sets.newHashSet("foo2", "bar", "baz"));
        
        Assert.assertTrue(manager.remove("foo2"));
        testState(Sets.newHashSet("bar", "baz"));
        
        Assert.assertTrue(manager.remove("bar"));
        Assert.assertTrue(manager.remove("baz"));
        testState(Sets.<String>newHashSet());
    }
    
    @Test
    public void checkCheckModifyTimeTracking() throws IOException {
        manager = new MapLoadSaveManager<>(true);
        
        Assert.assertNull(manager.load("foo"));
        Assert.assertNull(manager.getLoadLastModified("foo"));
        
        manager.save("foo", (SimpleXMLObject) buildXMLObject(SimpleXMLObject.ELEMENT_NAME));
        
        Assert.assertNotNull(manager.load("foo"));
        Long initialCachedModified = manager.getLoadLastModified("foo");
        Assert.assertNotNull(initialCachedModified);
        
        // Hasn't changed
        Assert.assertNull(manager.load("foo"));
        Assert.assertEquals(manager.getLoadLastModified("foo"), initialCachedModified);
        
        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
        
        // Change it
        manager.save("foo", (SimpleXMLObject) buildXMLObject(SimpleXMLObject.ELEMENT_NAME), true);
        
        Assert.assertNotNull(manager.load("foo"));
        Long updatedCachedModified = manager.getLoadLastModified("foo");
        Assert.assertNotNull(updatedCachedModified);
        Assert.assertNotEquals(updatedCachedModified, initialCachedModified);
        
        // Hasn't changed (again)
        Assert.assertNull(manager.load("foo"));
        Assert.assertEquals(manager.getLoadLastModified("foo"), updatedCachedModified);
        
        // Test update of key
        manager.updateKey("foo", "bar");
        Assert.assertNull(manager.load("foo"));
        Assert.assertNull(manager.load("bar"));
        Assert.assertNull(manager.getLoadLastModified("foo"));
        Assert.assertNotNull(manager.getLoadLastModified("bar"));
        Assert.assertEquals(manager.getLoadLastModified("bar"), updatedCachedModified);
        
        // Test removal of key
        manager.remove("bar");
        Assert.assertNull(manager.getLoadLastModified("bar"));
    }
    
    @Test
    public void iterator() throws IOException {
        Iterator<Pair<String,SimpleXMLObject>> iterator = null;
        
        iterator = manager.listAll().iterator();
        Assert.assertFalse(iterator.hasNext());
        try {
            iterator.next();
            Assert.fail("Should have failed due to no more elements");
        } catch (NoSuchElementException e) {
            //expected, do nothing
        }
        
        manager.save("foo", (SimpleXMLObject) buildXMLObject(SimpleXMLObject.ELEMENT_NAME));
        iterator = manager.listAll().iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertNotNull(iterator.next());
        Assert.assertFalse(iterator.hasNext());
        try {
            iterator.next();
            Assert.fail("Should have failed due to no more elements");
        } catch (NoSuchElementException e) {
            //expected, do nothing
        }
        
        manager.save("bar", (SimpleXMLObject) buildXMLObject(SimpleXMLObject.ELEMENT_NAME));
        manager.save("baz", (SimpleXMLObject) buildXMLObject(SimpleXMLObject.ELEMENT_NAME));
        iterator = manager.listAll().iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertNotNull(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertNotNull(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertNotNull(iterator.next());
        Assert.assertFalse(iterator.hasNext());
        try {
            iterator.next();
            Assert.fail("Should have failed due to no more elements");
        } catch (NoSuchElementException e) {
            //expected, do nothing
        }
        
        manager.remove("foo");
        iterator = manager.listAll().iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertNotNull(iterator.next());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertNotNull(iterator.next());
        Assert.assertFalse(iterator.hasNext());
        try {
            iterator.next();
            Assert.fail("Should have failed due to no more elements");
        } catch (NoSuchElementException e) {
            //expected, do nothing
        }
        
        manager.remove("bar");
        manager.remove("baz");
        iterator = manager.listAll().iterator();
        Assert.assertFalse(iterator.hasNext());
        try {
            iterator.next();
            Assert.fail("Should have failed due to no more elements");
        } catch (NoSuchElementException e) {
            //expected, do nothing
        }
        
    }
    
    
    
    
    // Helpers
    
    private void testState(Set<String> expectedKeys) throws IOException {
        Assert.assertEquals(manager.listKeys().isEmpty(), expectedKeys.isEmpty() ? true : false);
        Assert.assertEquals(manager.listKeys(), expectedKeys);
        for (String expectedKey : expectedKeys) {
            Assert.assertTrue(manager.exists(expectedKey));
            SimpleXMLObject sxo = manager.load(expectedKey);
            Assert.assertNotNull(sxo);
        }
        
        Assert.assertEquals(manager.listAll().iterator().hasNext(), expectedKeys.isEmpty() ? false: true);
        
        int sawCount = 0;
        for (Pair<String,SimpleXMLObject> entry : manager.listAll()) {
            sawCount++;
            Assert.assertTrue(expectedKeys.contains(entry.getFirst()));
            Assert.assertNotNull(entry.getSecond());
        }
        Assert.assertEquals(sawCount, expectedKeys.size());
    }

}
