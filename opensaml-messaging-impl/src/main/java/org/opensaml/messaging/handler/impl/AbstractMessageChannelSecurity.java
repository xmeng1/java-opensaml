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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.opensaml.messaging.context.BaseContext;
import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.handler.AbstractMessageHandler;
import org.opensaml.messaging.handler.MessageHandlerException;

import com.google.common.base.Function;

import net.shibboleth.utilities.java.support.component.ComponentSupport;
import net.shibboleth.utilities.java.support.logic.Constraint;

/**
 * Abstract base class for message handlers which populate a
 * {@link org.opensaml.messaging.context.MessageChannelSecurityContext} on a {@link BaseContext},
 * where the latter is located using a lookup strategy.
 * 
 * @param <MessageType> the type of message being carried
 */
public abstract class AbstractMessageChannelSecurity<MessageType> extends AbstractMessageHandler<MessageType> {
    
    /**
     * Strategy used to look up the parent {@link BaseContext} on which the
     * {@link org.opensaml.messaging.context.MessageChannelSecurityContext} will be populated.
     */
    @Nonnull private Function<MessageContext, BaseContext> parentContextLookupStrategy;
    
    /** Parent for eventual context. */
    @Nullable private BaseContext parentContext;
    
    /** Constructor. */
    public AbstractMessageChannelSecurity() {
        //TODO this just returns the input MC - need better default?
        parentContextLookupStrategy = new Function<MessageContext, BaseContext>() {
            @Nullable public BaseContext apply(@Nullable final MessageContext input) {
                return input;
            }
        };
    }
    
    /**
     * Set the strategy used to look up the parent {@link BaseContext} on which the
     * {@link org.opensaml.messaging.context.MessageChannelSecurityContext} will be populated.
     * 
     * @param strategy strategy used to look up the parent {@link BaseContext} on which to populate
     *          the {@link org.opensaml.messaging.context.MessageChannelSecurityContext}
     */
    public void setParentContextLookupStrategy(@Nonnull final Function<MessageContext, BaseContext> strategy) {
        ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);

        parentContextLookupStrategy = Constraint.isNotNull(strategy, "Parent context lookup strategy cannot be null");
    }

    /** {@inheritDoc} */
    @Override
    protected boolean doPreInvoke(@Nonnull final MessageContext messageContext) throws MessageHandlerException {

        parentContext = parentContextLookupStrategy.apply(messageContext);
        if (parentContext != null) {
            return super.doPreInvoke(messageContext);
        }
        return false;
    }
    
    /**
     * Get the parent context on which the {@link org.opensaml.messaging.context.MessageChannelSecurityContext}
     * will be populated.
     * 
     * @return the parent context
     */
    protected BaseContext getParentContext() {
        return parentContext;
    }

}