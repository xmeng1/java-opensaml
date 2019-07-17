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

package org.opensaml.saml.metadata.resolver.filter.impl;

import static org.testng.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import net.shibboleth.utilities.java.support.component.ComponentInitializationException;
import net.shibboleth.utilities.java.support.resolver.CriteriaSet;
import net.shibboleth.utilities.java.support.resolver.ResolverException;

import org.opensaml.core.criterion.EntityIdCriterion;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.XMLObjectBaseTestCase;
import org.opensaml.saml.ext.saml2alg.DigestMethod;
import org.opensaml.saml.ext.saml2alg.SigningMethod;
import org.opensaml.saml.metadata.resolver.impl.FilesystemMetadataResolver;
import org.opensaml.saml.metadata.resolver.impl.FilesystemMetadataResolverTest;
import org.opensaml.saml.saml2.metadata.EncryptionMethod;
import org.opensaml.saml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml.saml2.metadata.Extensions;
import org.opensaml.saml.saml2.metadata.KeyDescriptor;
import org.opensaml.saml.saml2.metadata.RoleDescriptor;
import org.opensaml.xmlsec.encryption.MGF;
import org.opensaml.xmlsec.encryption.support.EncryptionConstants;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AlgorithmFilterTest extends XMLObjectBaseTestCase implements Predicate<EntityDescriptor> {
    
    private FilesystemMetadataResolver metadataProvider;
    
    private File mdFile;
    
    @BeforeMethod
    protected void setUp() throws Exception {

        URL mdURL = FilesystemMetadataResolverTest.class
                .getResource("/org/opensaml/saml/saml2/metadata/InCommon-metadata.xml");
        mdFile = new File(mdURL.toURI());

        metadataProvider = new FilesystemMetadataResolver(mdFile);
        metadataProvider.setParserPool(parserPool);
    }
    
    @Test
    public void test() throws ComponentInitializationException, ResolverException {
        
        final DigestMethod digest1 = buildXMLObject(DigestMethod.DEFAULT_ELEMENT_NAME);
        digest1.setAlgorithm(SignatureConstants.ALGO_ID_DIGEST_SHA256);

        final DigestMethod digest2 = buildXMLObject(DigestMethod.DEFAULT_ELEMENT_NAME);
        digest2.setAlgorithm(SignatureConstants.ALGO_ID_DIGEST_SHA512);

        final SigningMethod signing1 = buildXMLObject(SigningMethod.DEFAULT_ELEMENT_NAME);
        signing1.setAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256);

        final SigningMethod signing2 = buildXMLObject(SigningMethod.DEFAULT_ELEMENT_NAME);
        signing2.setAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA512);
        
        final EncryptionMethod enc = buildXMLObject(EncryptionMethod.DEFAULT_ELEMENT_NAME);
        enc.setAlgorithm(EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSAOAEP11);
        
        final org.opensaml.xmlsec.signature.DigestMethod embeddedDigest =
                buildXMLObject(org.opensaml.xmlsec.signature.DigestMethod.DEFAULT_ELEMENT_NAME);
        embeddedDigest.setAlgorithm(SignatureConstants.ALGO_ID_DIGEST_SHA256);
        enc.getUnknownXMLObjects().add(embeddedDigest);
        
        final MGF mgf = buildXMLObject(MGF.DEFAULT_ELEMENT_NAME);
        mgf.setAlgorithm(EncryptionConstants.ALGO_ID_MGF1_SHA256);
        enc.getUnknownXMLObjects().add(mgf);

        final Collection<XMLObject> algs = Arrays.asList(digest1, digest2, signing1, signing2, enc);
        
        final AlgorithmFilter filter = new AlgorithmFilter();
        filter.setRules(Collections.<Predicate<EntityDescriptor>,Collection<XMLObject>>singletonMap(this, algs));
        filter.initialize();
        
        metadataProvider.setMetadataFilter(filter);
        metadataProvider.setId("test");
        metadataProvider.initialize();

        EntityIdCriterion crit = new EntityIdCriterion("https://carmenwiki.osu.edu/shibboleth");
        EntityDescriptor entity = metadataProvider.resolveSingle(new CriteriaSet(crit));
        assertNotNull(entity);
        final Extensions exts = entity.getExtensions();
        assertNotNull(exts);
        
        List<XMLObject> extElements = exts.getUnknownXMLObjects(DigestMethod.DEFAULT_ELEMENT_NAME);
        assertEquals(extElements.size(), 2);
        
        Iterator<XMLObject> digests = extElements.iterator();
        assertEquals(((DigestMethod) digests.next()).getAlgorithm(), SignatureConstants.ALGO_ID_DIGEST_SHA256);
        assertEquals(((DigestMethod) digests.next()).getAlgorithm(), SignatureConstants.ALGO_ID_DIGEST_SHA512);

        extElements = exts.getUnknownXMLObjects(SigningMethod.DEFAULT_ELEMENT_NAME);
        assertEquals(extElements.size(), 2);
        
        Iterator<XMLObject> signings = extElements.iterator();
        assertEquals(((SigningMethod) signings.next()).getAlgorithm(), SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256);
        assertEquals(((SigningMethod) signings.next()).getAlgorithm(), SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA512);

        for (final RoleDescriptor role : entity.getRoleDescriptors()) {
            for (final KeyDescriptor key : role.getKeyDescriptors()) {
                final List<EncryptionMethod> methods = key.getEncryptionMethods();
                assertEquals(methods.size(), 1);
                assertEquals(methods.get(0).getAlgorithm(), EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSAOAEP11);
                
                final List<XMLObject> encDigests = methods.get(0).getUnknownXMLObjects(
                        org.opensaml.xmlsec.signature.DigestMethod.DEFAULT_ELEMENT_NAME);
                assertEquals(encDigests.size(), 1);
                assertEquals(((org.opensaml.xmlsec.signature.DigestMethod) encDigests.get(0)).getAlgorithm(),
                        SignatureConstants.ALGO_ID_DIGEST_SHA256);

                final List<XMLObject> mgfs = methods.get(0).getUnknownXMLObjects(MGF.DEFAULT_ELEMENT_NAME);
                assertEquals(mgfs.size(), 1);
                assertEquals(((MGF) mgfs.get(0)).getAlgorithm(), EncryptionConstants.ALGO_ID_MGF1_SHA256);
            }
        }
        
        crit = new EntityIdCriterion("https://cms.psu.edu/Shibboleth");
        entity = metadataProvider.resolveSingle(new CriteriaSet(crit));
        assertNotNull(entity);
        assertNull(entity.getExtensions());
    }

    /** {@inheritDoc} */
    public boolean test(final EntityDescriptor input) {
        return input.getEntityID().equals("https://carmenwiki.osu.edu/shibboleth");
    }

}
