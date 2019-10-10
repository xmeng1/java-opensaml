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

/**
 * 
 */

package org.opensaml.saml.saml1.core.impl;

import org.testng.annotations.Test;
import org.testng.Assert;

import java.time.Instant;

import javax.xml.namespace.QName;

import org.opensaml.core.xml.XMLObjectProviderBaseTestCase;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.saml1.core.AudienceRestrictionCondition;
import org.opensaml.saml.saml1.core.Conditions;
import org.opensaml.saml.saml1.core.DoNotCacheCondition;

/**
 * Test class for org.opensaml.saml.saml1.core.Conditions
 */
public class ConditionsTest extends XMLObjectProviderBaseTestCase {

    /** name used to generate objects */
    private final QName qname;

    /**
     * Representation of NotBefore in test file.
     */
    private final Instant expectedNotBeforeDate;

    /**
     * Representation of NotOnOrAfter in test file.
     */
    private final Instant expectedNotOnOfAfter;

    /**
     * Constructor
     * 
     */
    public ConditionsTest() {
        singleElementFile = "/org/opensaml/saml/saml1/impl/singleConditions.xml";
        singleElementOptionalAttributesFile = "/org/opensaml/saml/saml1/impl/singleConditionsAttributes.xml";
        childElementsFile = "/org/opensaml/saml/saml1/impl/ConditionsWithChildren.xml";
        expectedNotBeforeDate = Instant.parse("1970-01-01T01:00:00.123Z");
        expectedNotOnOfAfter = Instant.parse("1970-01-01T00:00:01Z");
        
        qname = new QName(SAMLConstants.SAML1_NS, Conditions.DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML1_PREFIX);
    }

    /** {@inheritDoc} */
    @Test
    public void testSingleElementUnmarshall() {
        Conditions conditions;

        conditions = (Conditions) unmarshallElement(singleElementFile);

        Instant date = conditions.getNotBefore();
        Assert.assertNull(date, "NotBefore attribute has a value of " + date + ", expected no value");

        date = conditions.getNotOnOrAfter();
        Assert.assertNull(date, "NotOnOrAfter attribute has a value of " + date + ", expected no value");

    }

    /** {@inheritDoc} */
    @Test
    public void testSingleElementOptionalAttributesUnmarshall() {
        Conditions conditions;

        conditions = (Conditions) unmarshallElement(singleElementOptionalAttributesFile);

        Assert.assertEquals(conditions.getNotBefore(), expectedNotBeforeDate, "NotBefore attribute ");
        Assert.assertEquals(conditions.getNotOnOrAfter(), expectedNotOnOfAfter, "NotOnOrAfter attribute ");
    }

    /*
     * Test an XML file with children
     */
    @Test
    public void testChildElementsUnmarshall() {
        Conditions conditions;

        conditions = (Conditions) unmarshallElement(childElementsFile);

        Assert.assertEquals(conditions
                .getAudienceRestrictionConditions().size(), 3, "Number of AudienceRestrictionCondition elements");
        Assert.assertEquals(conditions.getDoNotCacheConditions().size(), 1, "Number of DoNotCacheCondition children");
        Assert.assertEquals(conditions.getConditions().size(), 4, "Wrong number of Condition children");
    }

    /** {@inheritDoc} */
    @Test
    public void testSingleElementMarshall() {
        Conditions conditions = (Conditions) buildXMLObject(qname);

        assertXMLEquals(expectedDOM, conditions);

    }

    /** {@inheritDoc} */
    @Test
    public void testSingleElementOptionalAttributesMarshall() {
        Conditions conditions = (Conditions) buildXMLObject(qname);

        conditions.setNotBefore(expectedNotBeforeDate);
        conditions.setNotOnOrAfter(expectedNotOnOfAfter);

        assertXMLEquals(expectedOptionalAttributesDOM, conditions);
    }

    /** {@inheritDoc} */
    @Test
    public void testChildElementsMarshall() {

        Conditions conditions = (Conditions) buildXMLObject(qname);

        QName arcQname = new QName(SAMLConstants.SAML1_NS, AudienceRestrictionCondition.DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML1_PREFIX);
        
        conditions.getConditions().add((AudienceRestrictionCondition) buildXMLObject(arcQname));
        conditions.getConditions().add((DoNotCacheCondition) buildXMLObject(new QName(SAMLConstants.SAML1_NS, DoNotCacheCondition.DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML1_PREFIX)));
        // conditions.addCondition(condition);

        conditions.getConditions().add((AudienceRestrictionCondition) buildXMLObject(arcQname));
        // conditions.addCondition(condition);
        //           
        conditions.getConditions().add((AudienceRestrictionCondition) buildXMLObject(arcQname));

        assertXMLEquals(expectedChildElementsDOM, conditions);

    }

}
