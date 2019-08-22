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

import java.time.Duration;
import java.time.Instant;

import javax.annotation.Nonnull;

import net.shibboleth.utilities.java.support.annotation.constraint.NonnullAfterInit;
import net.shibboleth.utilities.java.support.component.ComponentInitializationException;
import net.shibboleth.utilities.java.support.component.ComponentSupport;
import net.shibboleth.utilities.java.support.logic.Constraint;
import net.shibboleth.utilities.java.support.primitive.StringSupport;

import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.handler.AbstractMessageHandler;
import org.opensaml.messaging.handler.MessageHandlerException;
import org.opensaml.saml.common.messaging.context.SAMLMessageInfoContext;
import org.opensaml.saml.common.messaging.context.SAMLPeerEntityContext;
import org.opensaml.storage.ReplayCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Security message handler implementation that which checks for replay of SAML messages.
 */
public class MessageReplaySecurityHandler extends AbstractMessageHandler {

    /** Logger. */
    @Nonnull private final Logger log = LoggerFactory.getLogger(MessageReplaySecurityHandler.class);

    /** Message replay cache instance to use. */
    @NonnullAfterInit private ReplayCache replayCache;

    /** Whether this rule is required to be met. */
    private boolean requiredRule;
    
    /** Time to expire cache entries. Default value: (3 minutes) */
    @Nonnull private Duration expires;

    /** Constructor. */
    public MessageReplaySecurityHandler() {
        requiredRule = true;
        expires = Duration.ofMinutes(3);
    }

    /**
     * Get the replay cache instance to use.
     * 
     * @return Returns the replayCache.
     */
    @NonnullAfterInit public ReplayCache getReplayCache() {
        return replayCache;
    }

    /**
     * Set the replay cache instance to use.
     * 
     * @param cache The replayCache to set.
     */
    public void setReplayCache(@Nonnull final ReplayCache cache) {
        ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
        
        replayCache = Constraint.isNotNull(cache, "ReplayCache cannot be null");
    }
    
    /**
     * Set whether this rule is required to be met.
     * 
     * @param flag  flag to set
     */
    public void setRequiredRule(final boolean flag) {
        ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
        
        requiredRule = flag;
    }

    /**
     * Gets the lifetime of replay entries.
     * 
     * @return lifetime of entries
     */
    @Nonnull public Duration getExpires() {
        return expires;
    }

    /**
     * Sets the lifetime of replay entries.
     * 
     * @param exp lifetime of entries
     */
    public void setExpires(@Nonnull final Duration exp) {
        ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
        Constraint.isNotNull(exp, "Lifetime cannot be null");
        Constraint.isFalse(exp.isNegative(), "Lifetime cannot be negative");
        
        expires = exp;
    }

    /** {@inheritDoc} */
    @Override
    protected void doInitialize() throws ComponentInitializationException {
        super.doInitialize();
        
        if (getReplayCache() == null) {
            throw new ComponentInitializationException("ReplayCache cannot be null");
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void doInvoke(@Nonnull final MessageContext messageContext) throws MessageHandlerException {
        final SAMLPeerEntityContext peerContext = messageContext.getSubcontext(SAMLPeerEntityContext.class, true);
        
        String entityID = StringSupport.trimOrNull(peerContext.getEntityId());
        if (entityID == null) {
            entityID = "(unknown)";
        }
        
        final SAMLMessageInfoContext msgInfoContext = messageContext.getSubcontext(SAMLMessageInfoContext.class, true);

        final String messageId = StringSupport.trimOrNull(msgInfoContext.getMessageId());
        if (messageId == null) {
            if (requiredRule) {
                log.warn("{} Message contained no ID, replay check not possible", getLogPrefix());
                throw new MessageHandlerException("SAML message from issuer " + entityID
                        + " did not contain an ID");
            }
            log.debug("{} Message contained no ID, rule is optional, skipping further processing", getLogPrefix());
            return;
        }

        Instant issueInstant = msgInfoContext.getMessageIssueInstant();
        if (issueInstant == null) {
            issueInstant = Instant.now();
        }
        
        log.debug("{} Evaluating message replay for message ID '{}', issue instant '{}', entityID '{}'", 
                getLogPrefix(), messageId, issueInstant, entityID);
        
        if (!getReplayCache().check(getClass().getName(), messageId, issueInstant.plus(expires))) {
            log.warn("{} Replay detected of message '{}' from issuer '{}'", getLogPrefix(), messageId, entityID);
            throw new MessageHandlerException("Rejecting replayed message ID '" + messageId + "' from issuer "
                    + entityID);
        }
    }

}