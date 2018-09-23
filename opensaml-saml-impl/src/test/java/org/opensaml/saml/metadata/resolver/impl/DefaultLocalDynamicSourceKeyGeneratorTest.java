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

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.opensaml.core.criterion.EntityIdCriterion;
import org.opensaml.saml.common.binding.artifact.SAMLSourceIDArtifact;
import org.opensaml.saml.common.binding.artifact.SAMLSourceLocationArtifact;
import org.opensaml.saml.criterion.ArtifactCriterion;
import org.opensaml.saml.metadata.resolver.index.impl.SimpleStringCriterion;
import org.opensaml.saml.saml1.binding.artifact.SAML1ArtifactType0002;
import org.opensaml.saml.saml2.binding.artifact.SAML2ArtifactType0004;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.io.BaseEncoding;

import net.shibboleth.utilities.java.support.resolver.CriteriaSet;

/**
 *
 */
public class DefaultLocalDynamicSourceKeyGeneratorTest {
    
    private String controlValue;
    
    private String controlValueSHA1Hex;
    private byte[] controlValueSHA1Bytes;
    
    private SAMLSourceIDArtifact sourceIDArtifact;
    private SAMLSourceLocationArtifact sourceLocationArtifact;
    
    private CriteriaSet criteria;
    
    private DefaultLocalDynamicSourceKeyGenerator generator;
    
    @BeforeMethod
    public void setUp() throws NoSuchAlgorithmException {
        controlValue = "urn:test:foobar";
        controlValueSHA1Hex = "d278c9975472a6b4827b1a8723192b4e99aa969c";
        controlValueSHA1Bytes = BaseEncoding.base16().lowerCase().decode(controlValueSHA1Hex);
        criteria = new CriteriaSet();
        
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] messageHandle = new byte[20];
        secureRandom.nextBytes(messageHandle);
        sourceIDArtifact = new SAML2ArtifactType0004(new byte[] {0, 0} , controlValueSHA1Bytes, messageHandle);
        sourceLocationArtifact = new SAML1ArtifactType0002(messageHandle, "https://test.foobar.com/artifactk");
    }
    
    @Test
    public void testEntityIdCriterion() throws NoSuchAlgorithmException {
        generator = new DefaultLocalDynamicSourceKeyGenerator();
        
        Assert.assertNull(generator.apply(null));
        
        criteria.clear();
        Assert.assertNull(generator.apply(criteria));
        
        criteria.add(new EntityIdCriterion(controlValue));
        
        Assert.assertEquals(controlValueSHA1Hex, generator.apply(criteria));
        
        generator = new DefaultLocalDynamicSourceKeyGenerator("metadata-", ".xml", null);
        
        Assert.assertEquals("metadata-" + controlValueSHA1Hex + ".xml", generator.apply(criteria));
        
        generator = new DefaultLocalDynamicSourceKeyGenerator("metadata", "xml", ".");
        
        Assert.assertEquals("metadata." + controlValueSHA1Hex + ".xml", generator.apply(criteria));
    }
    
    @Test
    public void testArtifactCriterion() throws NoSuchAlgorithmException {
        generator = new DefaultLocalDynamicSourceKeyGenerator();
        
        Assert.assertNull(generator.apply(null));
        
        criteria.clear();
        Assert.assertNull(generator.apply(criteria));
        
        criteria.add(new ArtifactCriterion(sourceIDArtifact));
        
        Assert.assertEquals(controlValueSHA1Hex, generator.apply(criteria));
        
        generator = new DefaultLocalDynamicSourceKeyGenerator("metadata-", ".xml", null);
        
        Assert.assertEquals("metadata-" + controlValueSHA1Hex + ".xml", generator.apply(criteria));
        
        generator = new DefaultLocalDynamicSourceKeyGenerator("metadata", "xml", ".");
        
        Assert.assertEquals("metadata." + controlValueSHA1Hex + ".xml", generator.apply(criteria));
        
        // Source location artifact is not supported
        criteria.clear();
        criteria.add(new ArtifactCriterion(sourceLocationArtifact));
        Assert.assertNull(generator.apply(criteria));
    }
   
    @Test
    public void testUnsupportedCriterion() {
        generator = new DefaultLocalDynamicSourceKeyGenerator();
        
        criteria.add(new SimpleStringCriterion("foobar"));
        Assert.assertNull(generator.apply(criteria));
    }

}
