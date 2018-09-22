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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.opensaml.messaging.context.InOutOperationContext;
import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.context.httpclient.HttpClientRequestContext;
import org.opensaml.messaging.context.navigate.ContextDataLookupFunction;
import org.opensaml.messaging.context.navigate.RecursiveTypedParentContextLookup;
import org.opensaml.messaging.handler.AbstractMessageHandler;
import org.opensaml.messaging.handler.MessageHandlerException;
import org.opensaml.saml.common.messaging.context.AbstractAuthenticatableSAMLEntityContext;
import org.opensaml.saml.common.messaging.context.SAMLPeerEntityContext;
import org.opensaml.security.httpclient.HttpClientSecurityConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.shibboleth.utilities.java.support.component.ComponentSupport;
import net.shibboleth.utilities.java.support.logic.Constraint;

/**
 * Handler implementation that checks and records the result of {@link HttpClient} server TLS authentication 
 * as stored in the @link {@link HttpClientContext} resolved via strategy function.
 * 
 * <p>
 * If server TLS was performed and successful, 
 * store a positive authentication result in the configured {@link AbstractAuthenticatableSAMLEntityContext}.
 * If the entity context's entityID is not already populated, and the appropriate entityID strategy function 
 * is configured, also attempt to resolve the authenticated entityID and if successful populate the entity context.
 * </p>
 */
public class CheckAndRecordServerTLSEntityAuthenticationtHandler extends AbstractMessageHandler {
    
    /** Logger. */
    private Logger log = LoggerFactory.getLogger(CheckAndRecordServerTLSEntityAuthenticationtHandler.class);
    
    /** The strategy function for resolving the {@link HttpClientContext to evaluate}. */
    @Nonnull private ContextDataLookupFunction<MessageContext, HttpClientContext> httpClientContextLookup;
    
    /** The strategy function for resolving the authenticated entityID. */
    @Nonnull private ContextDataLookupFunction<MessageContext, String> entityIDLookup;
    
    /** The actual context class holding the authenticatable SAML entity. */
    @Nonnull private Class<? extends AbstractAuthenticatableSAMLEntityContext> entityContextClass;
    
    /** Constructor. */
    public CheckAndRecordServerTLSEntityAuthenticationtHandler() {
        super();
        entityContextClass = SAMLPeerEntityContext.class;
        httpClientContextLookup = new DefaultHttpClientContextLookup();
        entityIDLookup = new OperationContextEntityIDLookup(entityContextClass);
    }
    
    
    
    /**
     * Set the strategy function for resolving the {@link HttpClientContext to evaluate}.
     * 
     * @param strategy the new strategy function
     */
    public void setHttpClientContextLookup(
            @Nonnull final ContextDataLookupFunction<MessageContext, HttpClientContext> strategy) {
        ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
        httpClientContextLookup = Constraint.isNotNull(strategy, 
                "The HttpClientContext lookup strategy may not be null");
    }

    /**
     * Set the class type holding the authenticatable SAML entity data.
     * 
     * <p>Defaults to: {@link SAMLPeerEntityContext}.</p>
     * 
     * @param clazz the entity context class type
     */
    public void setEntityContextClass(@Nonnull final Class<? extends AbstractAuthenticatableSAMLEntityContext> clazz) {
        ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
        entityContextClass = Constraint.isNotNull(clazz, "The SAML entity context class may not be null");
    }

    /**
     * Set the strategy function for resolving the authenticated entityID.
     * 
     * @param strategy the new strategy function, or null
     */
    public void setEntityIDLookup(@Nullable final ContextDataLookupFunction<MessageContext, String> strategy) {
        ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
        entityIDLookup = strategy;
    }

    /** {@inheritDoc} */
    protected void doInvoke(@Nonnull final MessageContext messageContext) throws MessageHandlerException {
        final HttpClientContext clientContext = httpClientContextLookup.apply(messageContext);
        if (clientContext == null) {
            log.debug("Could not resolve HttpClientContext");
            return;
        }
        
        final Boolean trusted = clientContext.getAttribute(
                HttpClientSecurityConstants.CONTEXT_KEY_SERVER_TLS_CREDENTIAL_TRUSTED, Boolean.class);
        if (trusted == null) {
            log.debug("HttpClientContext attribute not found: {}", 
                    HttpClientSecurityConstants.CONTEXT_KEY_SERVER_TLS_CREDENTIAL_TRUSTED);
        } else {
            if (trusted) {
                log.debug("HttpClientContext indicates successful server TLS, storing result in entity context {}", 
                        entityContextClass.getName());
                final AbstractAuthenticatableSAMLEntityContext entityContext = 
                        messageContext.getSubcontext(entityContextClass, true);
                entityContext.setAuthenticated(true);
                if (entityContext.getEntityId() == null && entityIDLookup != null) {
                    log.debug("Context entityID was null, attempting to resolve");
                    final String entityID = entityIDLookup.apply(messageContext);
                    if (entityID != null) {
                        log.debug("Resolved authenticated entityID, populating on entity context: {}", entityID);
                        entityContext.setEntityId(entityID);
                    } else {
                        log.debug("Unable to resolve authenticated entityID");
                    }
                }
                log.debug("Current authenticated entityID is: {}", entityContext.getEntityId());
            } else {
                log.debug("HttpClientContext indicates non-successful server TLS");
            }
        }
        
    }

    /**
     * The default {@link HttpClientContext} strategy function, which resolves from the {@link HttpClientRequestContext}
     * of the outbound {@link MessageContext} of the parent {@link InOutOperationContext.
     */
    public class DefaultHttpClientContextLookup 
            implements ContextDataLookupFunction<MessageContext, HttpClientContext> {

        /** {@inheritDoc} */
        public HttpClientContext apply(@Nullable final MessageContext messageContext) {
            if (messageContext == null) {
                return null;
            }
            
            final InOutOperationContext opContext = 
                    new RecursiveTypedParentContextLookup<>(InOutOperationContext.class).apply(messageContext);
            if (opContext == null) {
                return null;
            }
            
            final MessageContext outboundMessageContext = opContext.getOutboundMessageContext();
            if (outboundMessageContext == null) {
                return null;
            }
            
            final HttpClientRequestContext requestContext = 
                    outboundMessageContext.getSubcontext(HttpClientRequestContext.class);
            if (requestContext == null) {
                return null;
            }
            
            return requestContext.getHttpClientContext();
        }
        
    }

}
