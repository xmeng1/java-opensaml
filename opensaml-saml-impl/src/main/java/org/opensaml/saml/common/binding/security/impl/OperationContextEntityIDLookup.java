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

import org.opensaml.messaging.context.InOutOperationContext;
import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.context.navigate.ContextDataLookupFunction;
import org.opensaml.messaging.context.navigate.RecursiveTypedParentContextLookup;
import org.opensaml.saml.common.messaging.context.AbstractAuthenticatableSAMLEntityContext;
import org.opensaml.saml.common.messaging.context.SAMLPeerEntityContext;

import net.shibboleth.utilities.java.support.logic.Constraint;

/**
 * Function for resolving the SAML entity ID from the parent {@link InOutOperationContext}.
 */
public class OperationContextEntityIDLookup implements ContextDataLookupFunction<MessageContext, String> {
    
    /** The actual context class holding the authenticatable SAML entity. */
    @Nonnull private Class<? extends AbstractAuthenticatableSAMLEntityContext> entityContextClass;
    
    /** Parent operation context lookup function. */
    @Nonnull private RecursiveTypedParentContextLookup<MessageContext,InOutOperationContext> parentLookup = 
            new RecursiveTypedParentContextLookup(InOutOperationContext.class);
    
    /**
     * Constructor.
     */
    public OperationContextEntityIDLookup() {
        this(SAMLPeerEntityContext.class);
    }
    
    /**
     * Constructor.
     * 
     * @param clazz the entity context class. Defaults to {@link SAMLPeerEntityContext}.
     */
    public OperationContextEntityIDLookup(
            @Nonnull final Class<? extends AbstractAuthenticatableSAMLEntityContext> clazz) {
        entityContextClass = Constraint.isNotNull(clazz, "The SAML Entity context class may not be null;");
    }

    /** {@inheritDoc} */
    public String apply(@Nullable final MessageContext messageContext) {
        if (messageContext == null) {
            return null;
        }

        final InOutOperationContext opContext = parentLookup.apply(messageContext);
        if (opContext == null) {
            return null;
        }

        final AbstractAuthenticatableSAMLEntityContext entityContext = opContext.getSubcontext(entityContextClass);
        if (entityContext == null) {
            return null;
        }

        return entityContext.getEntityId();
    }
        
}
