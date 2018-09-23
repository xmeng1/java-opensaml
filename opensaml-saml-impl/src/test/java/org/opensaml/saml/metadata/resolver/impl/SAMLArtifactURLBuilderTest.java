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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.opensaml.saml.common.binding.artifact.SAMLSourceIDArtifact;
import org.opensaml.saml.common.binding.artifact.SAMLSourceLocationArtifact;
import org.opensaml.saml.criterion.ArtifactCriterion;
import org.opensaml.saml.metadata.resolver.index.impl.SimpleStringCriterion;
import org.opensaml.saml.saml1.binding.artifact.SAML1ArtifactType0002;
import org.opensaml.saml.saml2.binding.artifact.SAML2ArtifactType0004;
import org.opensaml.security.crypto.JCAConstants;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.common.io.BaseEncoding;

import net.shibboleth.utilities.java.support.resolver.CriteriaSet;

/**
 *
 */
public class SAMLArtifactURLBuilderTest {
    
    private SAMLArtifactURLBuilder builder = new SAMLArtifactURLBuilder();
    
    private String baseURL = "http://metadata.example.org/service/";
    
    private BaseEncoding HEX = BaseEncoding.base16().lowerCase();
    
    @Test
    public void testSourceIDArtifact() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String entityID = "https://www.example.com/saml";
        MessageDigest sha1Digester = MessageDigest.getInstance(JCAConstants.DIGEST_SHA1);
        byte[] entityIDSourceID = sha1Digester.digest(entityID.getBytes("UTF-8"));
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] messageHandle = new byte[20];
        secureRandom.nextBytes(messageHandle);
        
        SAMLSourceIDArtifact sourceIDArtifact = new SAML2ArtifactType0004(new byte[] {0, 0} , entityIDSourceID, messageHandle);
        
        Assert.assertEquals(builder.buildURL(baseURL, new CriteriaSet(new ArtifactCriterion(sourceIDArtifact))),
                "http://metadata.example.org/service/entities/%7Bsha1%7D" + HEX.encode(entityIDSourceID));
        
    }
    
    @Test
    public void testSourceLocationArtifact() throws NoSuchAlgorithmException {
        // Note: Not currently supported by MDQ protocol.  Testing for code branch correctness.
        String sourceLocation = "https://www.example.com/saml/artifactResolve1";
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        byte[] messageHandle = new byte[20];
        secureRandom.nextBytes(messageHandle);
        
        SAMLSourceLocationArtifact sourceLocationArtifact = new SAML1ArtifactType0002(messageHandle, sourceLocation);
        
        Assert.assertNull(builder.buildURL(baseURL, new CriteriaSet(new ArtifactCriterion(sourceLocationArtifact))));
    }
    
    @Test
    public void testNonArtifact() {
        Assert.assertNull(builder.buildURL(baseURL, new CriteriaSet(new SimpleStringCriterion("blah"))));
    }
    

}
