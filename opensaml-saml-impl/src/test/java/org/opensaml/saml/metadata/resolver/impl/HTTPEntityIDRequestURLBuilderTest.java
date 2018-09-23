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

package org.opensaml.saml.metadata.resolver.impl;

import net.shibboleth.utilities.java.support.logic.ConstraintViolationException;
import net.shibboleth.utilities.java.support.resolver.CriteriaSet;

import org.opensaml.core.criterion.EntityIdCriterion;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HTTPEntityIDRequestURLBuilderTest {
    
    private HTTPEntityIDRequestURLBuilder function = new HTTPEntityIDRequestURLBuilder();
    
    @Test
    public void testHTTP() {
        Assert.assertEquals(function.apply(new CriteriaSet(new EntityIdCriterion("http://www.example.com/sp"))), "http://www.example.com/sp");
        Assert.assertEquals(function.apply(new CriteriaSet(new EntityIdCriterion("https://www.example.com/sp"))), "https://www.example.com/sp");
        
        Assert.assertEquals(function.apply(new CriteriaSet(new EntityIdCriterion("HTTP://www.example.com/sp"))), "HTTP://www.example.com/sp");
        Assert.assertEquals(function.apply(new CriteriaSet(new EntityIdCriterion("HTTPS://www.example.com/sp"))), "HTTPS://www.example.com/sp");
        
    }
    
    @Test
    public void testNonHTTP() {
        Assert.assertNull(function.apply(new CriteriaSet(new EntityIdCriterion("urn:test:sp"))));
        Assert.assertNull(function.apply(new CriteriaSet(new EntityIdCriterion("foo"))));
        Assert.assertNull(function.apply(new CriteriaSet(new EntityIdCriterion("httpblah://not.a.url.com"))));
        Assert.assertNull(function.apply(new CriteriaSet()));
    }
    
    @Test(expectedExceptions=ConstraintViolationException.class)
    public void testNullEntityID() {
        function.apply(null);
    }

}
