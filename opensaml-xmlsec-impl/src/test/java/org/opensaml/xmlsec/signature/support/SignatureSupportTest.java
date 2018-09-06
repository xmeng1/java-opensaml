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

package org.opensaml.xmlsec.signature.support;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

import org.opensaml.core.xml.XMLObjectBaseTestCase;
import org.opensaml.security.SecurityException;
import org.opensaml.security.credential.Credential;
import org.opensaml.security.credential.CredentialSupport;
import org.opensaml.security.crypto.KeySupport;
import org.opensaml.xmlsec.SignatureSigningParameters;
import org.opensaml.xmlsec.config.impl.DefaultSecurityConfigurationBootstrap;
import org.opensaml.xmlsec.keyinfo.KeyInfoGenerator;
import org.opensaml.xmlsec.signature.Signature;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SignatureSupportTest extends XMLObjectBaseTestCase {
    
    private Credential signingCredential;
    
    private KeyInfoGenerator keyInfoGenerator;
    
    @BeforeClass
    public void initializeKeyPairAndGenerator() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPair keyPair = KeySupport.generateKeyPair("RSA", 1024, null);
        signingCredential = CredentialSupport.getSimpleCredential(keyPair.getPublic(), keyPair.getPrivate());
        keyInfoGenerator = DefaultSecurityConfigurationBootstrap.buildBasicKeyInfoGeneratorManager()
                .getDefaultManager().getFactory(signingCredential).newInstance();
    }
    
    @Test
    public void testBasic() throws SecurityException {
        Signature signature = buildTemplateSignature();
        
        SignatureSigningParameters params = buildTemplateSigningParameters();
        
        SignatureSupport.prepareSignatureParams(signature, params);
        
        Assert.assertNotNull(signature.getCanonicalizationAlgorithm());
        Assert.assertNotNull(signature.getSignatureAlgorithm());
        Assert.assertNotNull(signature.getSigningCredential());
        Assert.assertNotNull(signature.getKeyInfo());
        
        URIContentReference cr = (URIContentReference) signature.getContentReferences().get(0);
        Assert.assertNotNull(cr);
        
        Assert.assertEquals(cr.getDigestAlgorithm(), SignatureConstants.ALGO_ID_DIGEST_SHA512);
        
        Assert.assertTrue(cr.getTransforms().isEmpty());
    }
    
    @Test
    public void testAddReferenceCanonicalizationTransformAlone() throws SecurityException {
        Signature signature = buildTemplateSignature();
        
        SignatureSigningParameters params = buildTemplateSigningParameters();
        params.setSignatureReferenceCanonicalizationAlgorithm(SignatureConstants.TRANSFORM_C14N_WITH_COMMENTS);
        
        SignatureSupport.prepareSignatureParams(signature, params);
        
        URIContentReference cr = (URIContentReference) signature.getContentReferences().get(0);
        Assert.assertNotNull(cr);
        
        Assert.assertEquals(cr.getTransforms().size(), 1);
        Assert.assertEquals(cr.getTransforms().get(0), SignatureConstants.TRANSFORM_C14N_WITH_COMMENTS);
    }

    @Test
    public void testAddReferenceCanonicalizationTransformAfterEnveloped() throws SecurityException {
        Signature signature = buildTemplateSignature();
        List<String> transforms = ((URIContentReference) signature.getContentReferences().get(0)).getTransforms();
        transforms.add(SignatureConstants.TRANSFORM_ENVELOPED_SIGNATURE);
        
        SignatureSigningParameters params = buildTemplateSigningParameters();
        params.setSignatureReferenceCanonicalizationAlgorithm(SignatureConstants.TRANSFORM_C14N_WITH_COMMENTS);
        
        SignatureSupport.prepareSignatureParams(signature, params);
        
        URIContentReference cr = (URIContentReference) signature.getContentReferences().get(0);
        Assert.assertNotNull(cr);
        
        Assert.assertEquals(cr.getTransforms().size(), 2);
        Assert.assertEquals(cr.getTransforms().get(1), SignatureConstants.TRANSFORM_C14N_WITH_COMMENTS);
    }

    @Test
    public void testReplaceReferenceCanonicalizationTransformAlone() throws SecurityException {
        Signature signature = buildTemplateSignature();
        List<String> transforms = ((URIContentReference) signature.getContentReferences().get(0)).getTransforms();
        transforms.add(SignatureConstants.TRANSFORM_C14N_OMIT_COMMENTS);
        
        SignatureSigningParameters params = buildTemplateSigningParameters();
        params.setSignatureReferenceCanonicalizationAlgorithm(SignatureConstants.TRANSFORM_C14N_WITH_COMMENTS);
        
        SignatureSupport.prepareSignatureParams(signature, params);
        
        URIContentReference cr = (URIContentReference) signature.getContentReferences().get(0);
        Assert.assertNotNull(cr);
        
        Assert.assertEquals(cr.getTransforms().size(), 1);
        Assert.assertEquals(cr.getTransforms().get(0), SignatureConstants.TRANSFORM_C14N_WITH_COMMENTS);
    }
    
    @Test
    public void testReplaceReferenceCanonicalizationTransformAfterEnveloped() throws SecurityException {
        Signature signature = buildTemplateSignature();
        List<String> transforms = ((URIContentReference) signature.getContentReferences().get(0)).getTransforms();
        transforms.add(SignatureConstants.TRANSFORM_ENVELOPED_SIGNATURE);
        transforms.add(SignatureConstants.TRANSFORM_C14N_OMIT_COMMENTS);
        
        SignatureSigningParameters params = buildTemplateSigningParameters();
        params.setSignatureReferenceCanonicalizationAlgorithm(SignatureConstants.TRANSFORM_C14N_WITH_COMMENTS);
        
        SignatureSupport.prepareSignatureParams(signature, params);
        
        URIContentReference cr = (URIContentReference) signature.getContentReferences().get(0);
        Assert.assertNotNull(cr);
        
        Assert.assertEquals(cr.getTransforms().size(), 2);
        Assert.assertEquals(cr.getTransforms().get(1), SignatureConstants.TRANSFORM_C14N_WITH_COMMENTS);
    }
    
    private Signature buildTemplateSignature() {
        Signature signature = buildXMLObject(Signature.DEFAULT_ELEMENT_NAME);
        URIContentReference cr = new URIContentReference("abc123");
        signature.getContentReferences().add(cr);
        // Note: no transforms by default
        return signature;
    }
    
    private SignatureSigningParameters buildTemplateSigningParameters() {
        SignatureSigningParameters params = new SignatureSigningParameters();
        params.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256);
        params.setSignatureCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
        params.setSignatureReferenceDigestMethod(SignatureConstants.ALGO_ID_DIGEST_SHA512); // Note: not the URIContentReference default
        params.setSigningCredential(signingCredential);
        params.setKeyInfoGenerator(keyInfoGenerator);
        return params;
    }

}
