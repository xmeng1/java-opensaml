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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.namespace.QName;

import org.opensaml.messaging.context.BaseContext;
import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.context.navigate.ContextDataLookupFunction;
import org.opensaml.messaging.handler.AbstractMessageHandler;
import org.opensaml.messaging.handler.MessageHandlerException;
import org.opensaml.saml.common.messaging.context.AbstractSAMLEntityContext;
import org.opensaml.saml.common.messaging.context.SAMLPeerEntityContext;
import org.opensaml.saml.common.messaging.context.SAMLProtocolContext;

import net.shibboleth.utilities.java.support.annotation.constraint.NonnullAfterInit;
import net.shibboleth.utilities.java.support.annotation.constraint.NotEmpty;
import net.shibboleth.utilities.java.support.component.ComponentInitializationException;
import net.shibboleth.utilities.java.support.component.ComponentSupport;
import net.shibboleth.utilities.java.support.logic.Constraint;
import net.shibboleth.utilities.java.support.primitive.StringSupport;

/**
 * SAML {@link org.opensaml.messaging.handler.MessageHandler} that attaches protocol
 * and role information to a message context via {@link SAMLProtocolContext} and
 * an instance of {@link AbstractSAMLEntityContext} objects. The entity context class
 * is configurable and defaults to {@link SAMLPeerEntityContext}.
 * 
 * <p>A profile flow would typically run this handler after message decoding occurs,
 * to bootstrap subsequent handlers.</p>
 */
public class SAMLProtocolAndRoleHandler extends AbstractMessageHandler {
    
    /** Protocol value to add to context. */
    @NonnullAfterInit @NotEmpty private String samlProtocol;

    /** Role type to add to context. */
    @NonnullAfterInit private QName peerRole;
    
    /** The context class representing the SAML entity for whom data is to be attached. 
     * Defaults to: {@link SAMLPeerEntityContext}. */
    @Nonnull private Class<? extends AbstractSAMLEntityContext> entityContextClass = SAMLPeerEntityContext.class;
    
    /** Optional lookup function for a context from which to copy the protocol and role data,
     * for example from a parent operation context. */
    @Nullable private ContextDataLookupFunction<MessageContext, ? extends BaseContext> copyContextLookup;
    
    /**
     * Set the optional lookup function for a context from which to copy the protocol and role data,
     * for example from a parent operation context.
     * 
     * @param lookup the lookup function, may be null
     */
    public void setCopyContextLookup(
            @Nullable final ContextDataLookupFunction<MessageContext, ? extends BaseContext> lookup) {
       copyContextLookup = lookup; 
    }
    
    /**
     * Set the class type holding the SAML entity data.
     * 
     * <p>Defaults to: {@link SAMLPeerEntityContext}.</p>
     * 
     * @param clazz the entity context class type
     */
    public void setEntityContextClass(@Nonnull final Class<? extends AbstractSAMLEntityContext> clazz) {
        ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
        
        entityContextClass = Constraint.isNotNull(clazz, "SAML entity context class may not be null");
    }

    /**
     * Set the protocol constant to attach.
     * 
     * @param protocol the protocol constant to set
     */
    public void setProtocol(@Nonnull @NotEmpty final String protocol) {
        samlProtocol = Constraint.isNotNull(StringSupport.trimOrNull(protocol), "SAML protocol cannot be null");
    }

    /**
     * Set the operational role to attach.
     * 
     * @param role the operational role to set
     */
    public void setRole(@Nonnull final QName role) {
        peerRole = Constraint.isNotNull(role, "SAML peer role cannot be null");
    }

    /** {@inheritDoc} */
    @Override
    protected void doInitialize() throws ComponentInitializationException {
        super.doInitialize();
        
        if (copyContextLookup == null && (samlProtocol == null || peerRole == null)) {
            throw new ComponentInitializationException(
                    "Either SAML protocol and peer role, or context copy function must be supplied");
        }
    }
    
    /** {@inheritDoc} */
    @Override
    protected void doInvoke(final MessageContext messageContext) throws MessageHandlerException {
        
        BaseContext copySource = null;
        if (copyContextLookup != null) {
            copySource = copyContextLookup.apply(messageContext);
        }
        
        if (samlProtocol != null) {
            messageContext.getSubcontext(SAMLProtocolContext.class, true).setProtocol(samlProtocol);
        } else if (copySource != null) {
            final SAMLProtocolContext sourceProtocolContext = copySource.getSubcontext(SAMLProtocolContext.class);
            if (sourceProtocolContext != null) {
                messageContext.getSubcontext(SAMLProtocolContext.class, true).setProtocol(
                        sourceProtocolContext.getProtocol());
            }
        }
        if (messageContext.getSubcontext(SAMLProtocolContext.class, true).getProtocol() == null) {
            throw new MessageHandlerException("SAML protocol was not supplied and could not be dynamically resolved");
        }
        
        if (peerRole != null) {
            messageContext.getSubcontext(entityContextClass, true).setRole(peerRole);
        } else if (copySource != null) {
            final AbstractSAMLEntityContext sourceEntityContext = copySource.getSubcontext(entityContextClass);
            if (sourceEntityContext != null) {
                messageContext.getSubcontext(entityContextClass, true).setRole(sourceEntityContext.getRole());
            }
        }
        if (messageContext.getSubcontext(entityContextClass, true).getRole() == null) {
            throw new MessageHandlerException("SAML role was not supplied and could not be dynamically resolved");
        }
        
    }
    
}