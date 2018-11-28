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

package org.opensaml.soap.client.security;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.opensaml.messaging.context.InOutOperationContext;
import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.context.navigate.ChildContextLookup;
import org.opensaml.messaging.context.navigate.ContextDataLookupFunction;
import org.opensaml.messaging.context.navigate.RecursiveTypedParentContextLookup;

import com.google.common.base.Function;
import com.google.common.base.Functions;

import net.shibboleth.utilities.java.support.logic.Constraint;

/**
 * Function to resolve SOAP client security profile ID from message context.
 */
public class SOAPClientSecurityProfileIdLookupFunction implements ContextDataLookupFunction<MessageContext, String> {
    
    /** Lookup function for {@link SOAPClientSecurityContext}. */
    private Function<MessageContext, SOAPClientSecurityContext> soapContextLookup;
    
    /**
     * Constructor.
     */
    public SOAPClientSecurityProfileIdLookupFunction() {
        soapContextLookup = Functions.compose(
                new ChildContextLookup<>(SOAPClientSecurityContext.class),
                new RecursiveTypedParentContextLookup(InOutOperationContext.class)
                );
    }
    
    /**
     * Set lookup function for for {@link SOAPClientSecurityContext}.
     * 
     * @param lookup the lookup function
     */
    public void setSOAPClientSecurityContextLookup(
            @Nonnull final Function<MessageContext, SOAPClientSecurityContext> lookup) {
       soapContextLookup = Constraint.isNotNull(lookup, "SOAPClientSecurityContext lookup function was null") ;
    }

    /** {@inheritDoc} */
    public String apply(@Nullable final MessageContext messageContext) {
        if (messageContext == null) {
            return null;
        }
        
        final SOAPClientSecurityContext context = soapContextLookup.apply(messageContext);
        if (context != null) {
            return context.getSecurityConfigurationProfileId();
        } else {
            return null;
        }
    }

}
