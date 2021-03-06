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

package org.opensaml.xmlsec.impl;

import java.util.Arrays;

import net.shibboleth.utilities.java.support.logic.ConstraintViolationException;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 */
public class WhitelistPredicateTest {

    @Test
    public void testBasic() {
        WhitelistPredicate predicate = new WhitelistPredicate(Arrays.asList("A", "B", "C", "D"));
        
        Assert.assertTrue(predicate.test("A"));
        Assert.assertTrue(predicate.test("B"));
        Assert.assertTrue(predicate.test("C"));
        Assert.assertTrue(predicate.test("D"));
        
        Assert.assertFalse(predicate.test("X"));
        Assert.assertFalse(predicate.test("Y"));
        Assert.assertFalse(predicate.test("Z"));
        Assert.assertFalse(predicate.test("foo"));
        Assert.assertFalse(predicate.test("bar"));
        Assert.assertFalse(predicate.test("bax"));
    }

    @Test
    public void testWithNullsInSet() {
        WhitelistPredicate predicate = new WhitelistPredicate(Arrays.asList("A", null, "B", null, "C", null, "D"));
        
        Assert.assertTrue(predicate.test("A"));
        Assert.assertTrue(predicate.test("B"));
        Assert.assertTrue(predicate.test("C"));
        Assert.assertTrue(predicate.test("D"));
        
        Assert.assertFalse(predicate.test("X"));
        Assert.assertFalse(predicate.test("Y"));
        Assert.assertFalse(predicate.test("Z"));
        Assert.assertFalse(predicate.test("foo"));
        Assert.assertFalse(predicate.test("bar"));
        Assert.assertFalse(predicate.test("bax"));
    }
    
    @Test(expectedExceptions=ConstraintViolationException.class)
    public void testNullSet() {
        new WhitelistPredicate(null);
    }
    
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void testNullArg() {
        WhitelistPredicate predicate = new WhitelistPredicate(Arrays.asList("A", "B", "C", "D"));
        predicate.test(null);
    }
    
}