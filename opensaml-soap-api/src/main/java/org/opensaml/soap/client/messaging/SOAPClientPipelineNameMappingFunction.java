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

package org.opensaml.soap.client.messaging;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.opensaml.messaging.context.InOutOperationContext;
import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.context.navigate.ChildContextLookup;
import org.opensaml.messaging.context.navigate.ContextDataLookupFunction;
import org.opensaml.messaging.context.navigate.RecursiveTypedParentContextLookup;
import org.opensaml.soap.client.SOAPClientContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicates;
import com.google.common.collect.Maps;

import net.shibboleth.utilities.java.support.annotation.ParameterName;
import net.shibboleth.utilities.java.support.logic.Constraint;

/**
 * Function implementation which resolves a delegate function based on the 
 * SOAP client pipeline name, obtained via a lookup of {@link SOAPClientContext},
 * by default a direct child of the parent {@link InOutOperationContext}.
 * 
 * @param <T> delegate function type
 */
public class SOAPClientPipelineNameMappingFunction<T> implements Function<MessageContext, T> {
    
    /** Logger. */
    private Logger log = LoggerFactory.getLogger(SOAPClientPipelineNameMappingFunction.class);
    
    /** Lookup strategy for the SOAP client context. */
    @Nonnull private Function<MessageContext, SOAPClientContext> soapClientContextLookup;
    
    /** Map of pipeline names to delegate predicates. */
    @Nonnull private Map<String, Function<MessageContext, T>> delegateMap;
    
    /**
     * Constructor.
     *
     * @param mappings the pipeline to delegate mappings
     */
    public SOAPClientPipelineNameMappingFunction(
            @Nonnull @ParameterName(name="mappings") final Map<String, Function<MessageContext, T>> mappings) {
        this(mappings,null);
    }

    /**
     * Constructor.
     *
     * @param mappings the pipeline to delegate mappings
     * @param lookupStrategy lookup strategy for SOAP client context
     */
    public SOAPClientPipelineNameMappingFunction(
            @Nonnull @ParameterName(name="mappings") final Map<String, Function<MessageContext, T>> mappings,
            @Nullable @ParameterName(name="lookupStrategy") 
                final ContextDataLookupFunction<MessageContext, SOAPClientContext> lookupStrategy) {
        
        Constraint.isNotNull(mappings, "Delegate mappings may not be null");
        delegateMap = new HashMap<>(Maps.filterKeys(
                Maps.filterValues(mappings, Predicates.notNull()), 
                Predicates.notNull()));
        
        if (lookupStrategy != null) {
            soapClientContextLookup = lookupStrategy;
        } else {
            soapClientContextLookup =
                    new ChildContextLookup(SOAPClientContext.class).compose( 
                            new RecursiveTypedParentContextLookup<>(InOutOperationContext.class));
        }
    }
    
    /** {@inheritDoc} */
    public T apply(@Nullable final MessageContext input) {
        if (input == null) {
            return null;
        }
        
        final SOAPClientContext clientContext = soapClientContextLookup.apply(input);
        if (clientContext != null && clientContext.getPipelineName() != null) {
            log.debug("Resolved SOAP client pipeline name: {}", clientContext.getPipelineName());
            final Function<MessageContext, T> delegate = delegateMap.get(clientContext.getPipelineName());
            log.debug("Resolved delegate function: {}", delegate != null ? delegate.getClass().getName() : "null");
            if (delegate != null) {
                return delegate.apply(input);
            }
        }
        
        log.debug("No delegate function could be resolved, returning null");
        return null;
    }

}
