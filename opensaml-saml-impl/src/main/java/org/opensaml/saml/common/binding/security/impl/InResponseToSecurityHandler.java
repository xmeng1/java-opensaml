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

import java.util.Objects;

import javax.annotation.Nonnull;

import org.opensaml.messaging.context.InOutOperationContext;
import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.handler.AbstractMessageHandler;
import org.opensaml.messaging.handler.MessageHandlerException;
import org.opensaml.saml.common.SAMLObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.shibboleth.utilities.java.support.primitive.StringSupport;

/**
 * Client-side message handler for validating that the inbound SAML response inResponseTo ID matches the corresponding
 * outbound request ID.
 */
public class InResponseToSecurityHandler extends AbstractMessageHandler {
    
    /** Logger. */
    private Logger log = LoggerFactory.getLogger(InResponseToSecurityHandler.class);

    /** {@inheritDoc} */
    protected void doInvoke(@Nonnull final MessageContext messageContext) throws MessageHandlerException {
        final String outboundRequestID = StringSupport.trimOrNull(resolveOutboundRequestID(messageContext));
        log.debug("Resolved outbound request ID: {}", outboundRequestID);
        
        final String inboundInResponseTo = StringSupport.trimOrNull(resolveInboundInResponseTo(messageContext));
        log.debug("Resolved inbound inResponseTo: {}", inboundInResponseTo);
        
        if (!Objects.equals(outboundRequestID, inboundInResponseTo)) {
            log.warn("Inbound inResponseTo '{}' did not match outbound request ID '{}'", 
                    inboundInResponseTo, outboundRequestID);
            throw new MessageHandlerException("Inbound inResponseTo did not match outbound request ID");
        }
    }

    /**
     * Resolve the outbound request ID.
     * 
     * @param messageContext the message context
     * @return the outbound request ID, or null
     */
    private String resolveOutboundRequestID(@Nonnull final MessageContext messageContext) {
        if (messageContext.getParent() instanceof InOutOperationContext) {
            final MessageContext outboundContext = 
                    ((InOutOperationContext)messageContext.getParent()).getOutboundMessageContext();
            if (outboundContext != null && outboundContext.getMessage() instanceof SAMLObject) {
                final SAMLObject outboundMessage = (SAMLObject) outboundContext.getMessage();
                if (outboundMessage instanceof org.opensaml.saml.saml2.core.RequestAbstractType) {
                    return ((org.opensaml.saml.saml2.core.RequestAbstractType)outboundMessage).getID();
                } else if (outboundMessage instanceof org.opensaml.saml.saml1.core.RequestAbstractType) {
                    return ((org.opensaml.saml.saml1.core.RequestAbstractType)outboundMessage).getID();
                }
            }
        }
        return null;
    }

    /**
     * Resolve the inbound inResponseTo ID.
     * 
     * @param messageContext the message context
     * @return the inbound inResponseTo, or null
     */
    private String resolveInboundInResponseTo(@Nonnull final MessageContext messageContext) {
        if (messageContext.getParent() instanceof InOutOperationContext) {
            final MessageContext inboundContext = 
                    ((InOutOperationContext)messageContext.getParent()).getInboundMessageContext();
            if (inboundContext != null && inboundContext.getMessage() instanceof SAMLObject) {
                final SAMLObject inboundMessage = (SAMLObject) inboundContext.getMessage();
                if (inboundMessage instanceof org.opensaml.saml.saml2.core.StatusResponseType) {
                    return ((org.opensaml.saml.saml2.core.StatusResponseType)inboundMessage).getInResponseTo();
                } else if (inboundMessage instanceof org.opensaml.saml.saml1.core.ResponseAbstractType) {
                    return ((org.opensaml.saml.saml1.core.ResponseAbstractType)inboundMessage).getInResponseTo();
                }
            }
        }
        return null;
    }

}
