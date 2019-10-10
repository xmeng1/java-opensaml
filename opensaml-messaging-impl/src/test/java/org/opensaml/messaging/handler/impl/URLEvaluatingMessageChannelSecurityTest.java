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

package org.opensaml.messaging.handler.impl;

import org.opensaml.messaging.context.MessageChannelSecurityContext;
import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.handler.MessageHandlerException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import net.shibboleth.utilities.java.support.component.ComponentInitializationException;
import net.shibboleth.utilities.java.support.logic.FunctionSupport;

public class URLEvaluatingMessageChannelSecurityTest {
    
    private URLEvaluatingMessageChannelSecurity handler;
    
    private MessageContext messageContext;
    
    @BeforeMethod
    public void setUp() throws ComponentInitializationException {
        handler = new URLEvaluatingMessageChannelSecurity();
        messageContext = new MessageContext();
    }
    
    @Test
    public void testHTTPSNoPort() throws ComponentInitializationException, MessageHandlerException {
        handler.setURLLookup(FunctionSupport.constant("https://www.example.edu"));
        handler.initialize();
        
        handler.invoke(messageContext);
        
        MessageChannelSecurityContext channelSecurityContext = messageContext.getSubcontext(MessageChannelSecurityContext.class);
        Assert.assertNotNull(channelSecurityContext);
        Assert.assertFalse(channelSecurityContext.isIntegrityActive());
        Assert.assertFalse(channelSecurityContext.isConfidentialityActive());
    }

    @Test
    public void testHTTPSWithDefaultPort() throws ComponentInitializationException, MessageHandlerException {
        handler.setURLLookup(FunctionSupport.constant("https://www.example.edu:443"));
        handler.initialize();
        
        handler.invoke(messageContext);
        
        MessageChannelSecurityContext channelSecurityContext = messageContext.getSubcontext(MessageChannelSecurityContext.class);
        Assert.assertNotNull(channelSecurityContext);
        Assert.assertFalse(channelSecurityContext.isIntegrityActive());
        Assert.assertFalse(channelSecurityContext.isConfidentialityActive());
    }
    
    @Test
    public void testHTTPSWithDefaultPortAsSecure() throws ComponentInitializationException, MessageHandlerException {
        handler.setURLLookup(FunctionSupport.constant("https://www.example.edu:443"));
        handler.setDefaultPortInsecure(false);
        handler.initialize();
        
        handler.invoke(messageContext);
        
        MessageChannelSecurityContext channelSecurityContext = messageContext.getSubcontext(MessageChannelSecurityContext.class);
        Assert.assertNotNull(channelSecurityContext);
        Assert.assertTrue(channelSecurityContext.isIntegrityActive());
        Assert.assertTrue(channelSecurityContext.isConfidentialityActive());
    }
    
    @Test
    public void testHTTPSNoPortAsSecure() throws ComponentInitializationException, MessageHandlerException {
        handler.setURLLookup(FunctionSupport.constant("https://www.example.edu"));
        handler.setDefaultPortInsecure(false);
        handler.initialize();
        
        handler.invoke(messageContext);
        
        MessageChannelSecurityContext channelSecurityContext = messageContext.getSubcontext(MessageChannelSecurityContext.class);
        Assert.assertNotNull(channelSecurityContext);
        Assert.assertTrue(channelSecurityContext.isIntegrityActive());
        Assert.assertTrue(channelSecurityContext.isConfidentialityActive());
    }
    
    @Test
    public void testHTTPSWithNonDefaultPort() throws ComponentInitializationException, MessageHandlerException {
        handler.setURLLookup(FunctionSupport.constant("https://www.example.edu:8443"));
        handler.initialize();
        
        handler.invoke(messageContext);
        
        MessageChannelSecurityContext channelSecurityContext = messageContext.getSubcontext(MessageChannelSecurityContext.class);
        Assert.assertNotNull(channelSecurityContext);
        Assert.assertTrue(channelSecurityContext.isIntegrityActive());
        Assert.assertTrue(channelSecurityContext.isConfidentialityActive());
    }
    
    @Test
    public void testHTTPNoPort() throws ComponentInitializationException, MessageHandlerException {
        handler.setURLLookup(FunctionSupport.constant("http://www.example.edu"));
        handler.initialize();
        
        handler.invoke(messageContext);
        
        MessageChannelSecurityContext channelSecurityContext = messageContext.getSubcontext(MessageChannelSecurityContext.class);
        Assert.assertNotNull(channelSecurityContext);
        Assert.assertFalse(channelSecurityContext.isIntegrityActive());
        Assert.assertFalse(channelSecurityContext.isConfidentialityActive());
    }
    
    @Test
    public void testHTTPWithPort() throws ComponentInitializationException, MessageHandlerException {
        handler.setURLLookup(FunctionSupport.constant("http://www.example.edu:80"));
        handler.initialize();
        
        handler.invoke(messageContext);
        
        MessageChannelSecurityContext channelSecurityContext = messageContext.getSubcontext(MessageChannelSecurityContext.class);
        Assert.assertNotNull(channelSecurityContext);
        Assert.assertFalse(channelSecurityContext.isIntegrityActive());
        Assert.assertFalse(channelSecurityContext.isConfidentialityActive());
    }
    
    @Test
    public void testBadURL() throws ComponentInitializationException, MessageHandlerException {
        handler.setURLLookup(FunctionSupport.constant("foobar"));
        handler.initialize();
        
        handler.invoke(messageContext);
        
        MessageChannelSecurityContext channelSecurityContext = messageContext.getSubcontext(MessageChannelSecurityContext.class);
        Assert.assertNull(channelSecurityContext);
    }
    
    @Test(expectedExceptions=ComponentInitializationException.class)
    public void testMissingURLLookup() throws ComponentInitializationException {
        handler.initialize();
    }

}
