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

package org.opensaml.saml.common.binding.security.impl;

import org.opensaml.core.xml.XMLObjectBaseTestCase;
import org.opensaml.messaging.context.InOutOperationContext;
import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.handler.MessageHandlerException;
import org.opensaml.saml.saml1.core.Request;
import org.opensaml.saml.saml1.core.Response;
import org.opensaml.saml.saml2.core.ArtifactResolve;
import org.opensaml.saml.saml2.core.ArtifactResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import net.shibboleth.utilities.java.support.component.ComponentInitializationException;

/**
 *
 */
public class InResponseToSecurityHandlerTest extends XMLObjectBaseTestCase {
    
    private InOutOperationContext opContext;
    
    private InResponseToSecurityHandler handler;
    
    @BeforeClass
    public void setup() throws ComponentInitializationException {
        handler = new InResponseToSecurityHandler();
        handler.initialize();
        opContext = new InOutOperationContext<>(new MessageContext(), new MessageContext());
    }
    
    @Test
    public void testSAML2Match() throws MessageHandlerException {
        ArtifactResolve request = buildXMLObject(ArtifactResolve.DEFAULT_ELEMENT_NAME);
        request.setID("abc123");
        opContext.getOutboundMessageContext().setMessage(request);
        
        ArtifactResponse response = buildXMLObject(ArtifactResponse.DEFAULT_ELEMENT_NAME);
        response.setInResponseTo("abc123");
        opContext.getInboundMessageContext().setMessage(response);
        
        handler.invoke(opContext.getInboundMessageContext());
    }

    @Test(expectedExceptions=MessageHandlerException.class)
    public void testSAML2NonMatch() throws MessageHandlerException {
        ArtifactResolve request = buildXMLObject(ArtifactResolve.DEFAULT_ELEMENT_NAME);
        request.setID("abc123");
        opContext.getOutboundMessageContext().setMessage(request);
        
        ArtifactResponse response = buildXMLObject(ArtifactResponse.DEFAULT_ELEMENT_NAME);
        response.setInResponseTo("xyz456");
        opContext.getInboundMessageContext().setMessage(response);
        
        handler.invoke(opContext.getInboundMessageContext());
    }

    @Test
    public void testSAML1Match() throws MessageHandlerException {
        Request request = buildXMLObject(Request.DEFAULT_ELEMENT_NAME);
        request.setID("abc123");
        opContext.getOutboundMessageContext().setMessage(request);
        
        Response response = buildXMLObject(Response.DEFAULT_ELEMENT_NAME);
        response.setInResponseTo("abc123");
        opContext.getInboundMessageContext().setMessage(response);
        
        handler.invoke(opContext.getInboundMessageContext());
    }

    @Test(expectedExceptions=MessageHandlerException.class)
    public void testSAML1NonMatch() throws MessageHandlerException {
        Request request = buildXMLObject(Request.DEFAULT_ELEMENT_NAME);
        request.setID("abc123");
        opContext.getOutboundMessageContext().setMessage(request);
        
        Response response = buildXMLObject(Response.DEFAULT_ELEMENT_NAME);
        response.setInResponseTo("xyz456");
        opContext.getInboundMessageContext().setMessage(response);
        
        handler.invoke(opContext.getInboundMessageContext());   
    }

}
