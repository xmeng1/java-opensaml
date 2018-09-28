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

package org.opensaml.core.xml;

import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.core.xml.io.Unmarshaller;
import org.opensaml.core.xml.io.UnmarshallingException;
import org.opensaml.core.xml.mock.SimpleXMLObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import net.shibboleth.utilities.java.support.component.ComponentInitializationException;
import net.shibboleth.utilities.java.support.xml.BasicParserPool;
import net.shibboleth.utilities.java.support.xml.XMLParserException;

/**
 * Unit test for unmarshalling functions.
 */
public class UnmarshallingSecurityTest extends XMLObjectBaseTestCase {
    
    private BasicParserPool parserPoolDefaults, parserPoolInsecure;

    @BeforeClass
    public void setup() throws ComponentInitializationException {
        parserPoolDefaults = new BasicParserPool();
        parserPoolDefaults.initialize();
        
        parserPoolInsecure = new BasicParserPool();
        parserPoolInsecure.setIgnoreComments(false);
        parserPoolInsecure.setCoalescing(false);
        parserPoolInsecure.initialize();
    }

    /**
     * Tests unmarshalling an element with comment in content with default parser.
     * 
     * @throws XMLParserException
     * @throws UnmarshallingException
     */
    @Test
    public void testUnmarshallingWithCommentInElementContentDefaults() throws XMLParserException, UnmarshallingException {
        String documentLocation = "/org/opensaml/core/xml/SimpleXMLObjectWithCommentInContent.xml";
        Document document = parserPoolDefaults.parse(UnmarshallingSecurityTest.class.getResourceAsStream(documentLocation));

        Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(document.getDocumentElement());
        SimpleXMLObject sxObject = (SimpleXMLObject) unmarshaller.unmarshall(document.getDocumentElement());
        
        Assert.assertEquals(sxObject.getValue(), "Content1");
    }
    
    /**
     * Tests unmarshalling an element with comment in content with insecure parser.
     * 
     * @throws XMLParserException
     * @throws UnmarshallingException
     */
    @Test(expectedExceptions=UnmarshallingException.class)
    public void testUnmarshallingWithCommentInElementContentInsecure() throws XMLParserException, UnmarshallingException {
        String documentLocation = "/org/opensaml/core/xml/SimpleXMLObjectWithCommentInContent.xml";
        Document document = parserPoolInsecure.parse(UnmarshallingSecurityTest.class.getResourceAsStream(documentLocation));

        Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(document.getDocumentElement());
        SimpleXMLObject sxObject = (SimpleXMLObject) unmarshaller.unmarshall(document.getDocumentElement());
    }

    /**
     * Tests unmarshalling an element with CDATA in content with default parser.
     * 
     * @throws XMLParserException
     * @throws UnmarshallingException
     */
    @Test
    public void testUnmarshallingWithCDATAInElementContentDefaults() throws XMLParserException, UnmarshallingException {
        String documentLocation = "/org/opensaml/core/xml/SimpleXMLObjectWithCDATAInContent.xml";
        Document document = parserPoolDefaults.parse(UnmarshallingSecurityTest.class.getResourceAsStream(documentLocation));

        Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(document.getDocumentElement());
        SimpleXMLObject sxObject = (SimpleXMLObject) unmarshaller.unmarshall(document.getDocumentElement());
        
        Assert.assertEquals(sxObject.getValue(), "Content1");
    }
    
    /**
     * Tests unmarshalling an element with CDATA in content with insecure parser.
     * 
     * @throws XMLParserException
     * @throws UnmarshallingException
     */
    @Test(expectedExceptions=UnmarshallingException.class)
    public void testUnmarshallingWithCDATAInElementContentInsecure() throws XMLParserException, UnmarshallingException {
        String documentLocation = "/org/opensaml/core/xml/SimpleXMLObjectWithCDATAInContent.xml";
        Document document = parserPoolInsecure.parse(UnmarshallingSecurityTest.class.getResourceAsStream(documentLocation));

        Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(document.getDocumentElement());
        SimpleXMLObject sxObject = (SimpleXMLObject) unmarshaller.unmarshall(document.getDocumentElement());
    }

    /**
     * Tests unmarshalling an element with comment between child elements with default parser.
     * 
     * @throws XMLParserException
     * @throws MarshallingException
     */
    @Test
    public void testUnmarshallingWithCommentBetweenChildElementsDefaults() throws XMLParserException, UnmarshallingException {
        String documentLocation = "/org/opensaml/core/xml/SimpleXMLObjectWithCommentBetweenChildren.xml";
        Document document = parserPoolDefaults.parse(UnmarshallingSecurityTest.class.getResourceAsStream(documentLocation));

        Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(document.getDocumentElement());
        SimpleXMLObject sxObject = (SimpleXMLObject) unmarshaller.unmarshall(document.getDocumentElement());
        
        Assert.assertEquals(sxObject.getSimpleXMLObjects().size(), 2, "Number of children elements was not expected value");
    }
    
    /**
     * Tests unmarshalling an element with comment between child elements with insecure parser.
     * 
     * @throws XMLParserException
     * @throws MarshallingException
     */
    @Test(expectedExceptions=UnmarshallingException.class)
    public void testUnmarshallingWithCommentBetweenChildElementsInsecure() throws XMLParserException, UnmarshallingException {
        String documentLocation = "/org/opensaml/core/xml/SimpleXMLObjectWithCommentBetweenChildren.xml";
        Document document = parserPoolInsecure.parse(UnmarshallingSecurityTest.class.getResourceAsStream(documentLocation));

        Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(document.getDocumentElement());
        SimpleXMLObject sxObject = (SimpleXMLObject) unmarshaller.unmarshall(document.getDocumentElement());
    }
    
}