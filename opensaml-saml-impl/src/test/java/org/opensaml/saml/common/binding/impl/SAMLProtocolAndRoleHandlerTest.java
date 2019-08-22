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

package org.opensaml.saml.common.binding.impl;

import org.opensaml.messaging.context.InOutOperationContext;
import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.context.navigate.RecursiveTypedParentContextLookup;
import org.opensaml.messaging.handler.MessageHandlerException;
import org.opensaml.saml.common.messaging.context.SAMLPeerEntityContext;
import org.opensaml.saml.common.messaging.context.SAMLPresenterEntityContext;
import org.opensaml.saml.common.messaging.context.SAMLProtocolContext;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.saml2.metadata.SPSSODescriptor;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import net.shibboleth.utilities.java.support.component.ComponentInitializationException;

/**
 *
 */
public class SAMLProtocolAndRoleHandlerTest {
    
    private SAMLProtocolAndRoleHandler handler;
    private MessageContext messageContext;
    
    @BeforeMethod
    public void setup() {
        handler = new SAMLProtocolAndRoleHandler();
        messageContext = new MessageContext();
    }
    
    @Test
    public void testSetters() throws ComponentInitializationException, MessageHandlerException {
        handler.setProtocol(SAMLConstants.SAML20P_NS); 
        handler.setRole(SPSSODescriptor.DEFAULT_ELEMENT_NAME);
        handler.initialize();

        handler.invoke(messageContext);

        Assert.assertNotNull(messageContext.getSubcontext(SAMLProtocolContext.class));
        Assert.assertEquals(messageContext.getSubcontext(SAMLProtocolContext.class).getProtocol(), SAMLConstants.SAML20P_NS);

        Assert.assertNotNull(messageContext.getSubcontext(SAMLPeerEntityContext.class));
        Assert.assertEquals(messageContext.getSubcontext(SAMLPeerEntityContext.class).getRole(), SPSSODescriptor.DEFAULT_ELEMENT_NAME);
    }

    @Test
    public void testSettersWithEntityClass() throws ComponentInitializationException, MessageHandlerException {
        handler.setProtocol(SAMLConstants.SAML20P_NS); 
        handler.setRole(SPSSODescriptor.DEFAULT_ELEMENT_NAME);
        handler.setEntityContextClass(SAMLPresenterEntityContext.class);
        handler.initialize();

        handler.invoke(messageContext);

        Assert.assertNotNull(messageContext.getSubcontext(SAMLProtocolContext.class));
        Assert.assertEquals(messageContext.getSubcontext(SAMLProtocolContext.class).getProtocol(), SAMLConstants.SAML20P_NS);

        Assert.assertNotNull(messageContext.getSubcontext(SAMLPresenterEntityContext.class));
        Assert.assertEquals(messageContext.getSubcontext(SAMLPresenterEntityContext.class).getRole(), SPSSODescriptor.DEFAULT_ELEMENT_NAME);
    }
    
    @Test
    public void testCopySource() throws ComponentInitializationException, MessageHandlerException {
        handler.setCopyContextLookup(new RecursiveTypedParentContextLookup<>(InOutOperationContext.class));
        handler.initialize();
        
        final InOutOperationContext opContext = new InOutOperationContext(messageContext, new MessageContext());
        opContext.getSubcontext(SAMLProtocolContext.class, true).setProtocol(SAMLConstants.SAML20P_NS); 
        opContext.getSubcontext(SAMLPeerEntityContext.class, true).setRole(SPSSODescriptor.DEFAULT_ELEMENT_NAME);

        handler.invoke(messageContext);

        Assert.assertNotNull(messageContext.getSubcontext(SAMLProtocolContext.class));
        Assert.assertEquals(messageContext.getSubcontext(SAMLProtocolContext.class).getProtocol(), SAMLConstants.SAML20P_NS);

        Assert.assertNotNull(messageContext.getSubcontext(SAMLPeerEntityContext.class));
        Assert.assertEquals(messageContext.getSubcontext(SAMLPeerEntityContext.class).getRole(), SPSSODescriptor.DEFAULT_ELEMENT_NAME);
    }
    
    @Test
    public void testCopySourceWithEntityClass() throws ComponentInitializationException, MessageHandlerException {
        handler.setCopyContextLookup(new RecursiveTypedParentContextLookup<>(InOutOperationContext.class));
        handler.setEntityContextClass(SAMLPresenterEntityContext.class);
        handler.initialize();
        
        final InOutOperationContext opContext = new InOutOperationContext(messageContext, new MessageContext());
        opContext.getSubcontext(SAMLProtocolContext.class, true).setProtocol(SAMLConstants.SAML20P_NS); 
        opContext.getSubcontext(SAMLPresenterEntityContext.class, true).setRole(SPSSODescriptor.DEFAULT_ELEMENT_NAME);

        handler.invoke(messageContext);

        Assert.assertNotNull(messageContext.getSubcontext(SAMLProtocolContext.class));
        Assert.assertEquals(messageContext.getSubcontext(SAMLProtocolContext.class).getProtocol(), SAMLConstants.SAML20P_NS);

        Assert.assertNotNull(messageContext.getSubcontext(SAMLPresenterEntityContext.class));
        Assert.assertEquals(messageContext.getSubcontext(SAMLPresenterEntityContext.class).getRole(), SPSSODescriptor.DEFAULT_ELEMENT_NAME);
    }

    @Test(expectedExceptions=ComponentInitializationException.class)
    public void testMissingConfiguredProtocol() throws ComponentInitializationException, MessageHandlerException {
        handler.setRole(SPSSODescriptor.DEFAULT_ELEMENT_NAME);
        handler.setEntityContextClass(SAMLPresenterEntityContext.class);
        handler.initialize();
    }
    
    @Test(expectedExceptions=ComponentInitializationException.class)
    public void testMissingConfiguredRole() throws ComponentInitializationException, MessageHandlerException {
        handler.setProtocol(SAMLConstants.SAML20P_NS);
        handler.setEntityContextClass(SAMLPresenterEntityContext.class);
        handler.initialize();
    }
    
    @Test(expectedExceptions=MessageHandlerException.class)
    public void testResolverWithNoCopySource() throws ComponentInitializationException, MessageHandlerException {
        handler.setCopyContextLookup(new RecursiveTypedParentContextLookup<>(InOutOperationContext.class));
        handler.initialize();
        
        handler.invoke(messageContext);
    }
}
