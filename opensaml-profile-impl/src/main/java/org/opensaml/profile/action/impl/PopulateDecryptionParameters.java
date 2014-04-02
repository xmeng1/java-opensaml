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

package org.opensaml.profile.action.impl;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.opensaml.profile.ProfileException;
import org.opensaml.profile.action.AbstractProfileAction;
import org.opensaml.profile.action.ActionSupport;
import org.opensaml.profile.action.EventIds;
import org.opensaml.profile.context.ProfileRequestContext;
import org.opensaml.profile.context.navigate.InboundMessageContextLookup;
import org.opensaml.xmlsec.SecurityConfigurationSupport;
import org.opensaml.xmlsec.DecryptionConfiguration;
import org.opensaml.xmlsec.DecryptionParameters;
import org.opensaml.xmlsec.DecryptionParametersResolver;
import org.opensaml.xmlsec.context.SecurityParametersContext;
import org.opensaml.xmlsec.criterion.DecryptionConfigurationCriterion;

import net.shibboleth.utilities.java.support.annotation.constraint.NonnullAfterInit;
import net.shibboleth.utilities.java.support.component.ComponentInitializationException;
import net.shibboleth.utilities.java.support.component.ComponentSupport;
import net.shibboleth.utilities.java.support.logic.Constraint;
import net.shibboleth.utilities.java.support.resolver.CriteriaSet;
import net.shibboleth.utilities.java.support.resolver.ResolverException;

import org.opensaml.messaging.context.navigate.ChildContextLookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;

/**
 * Action that resolves and populates {@link DecryptionParameters} on a {@link SecurityParametersContext}
 * created/accessed via a lookup function, by default on the inbound message context.
 * 
 * @event {@link EventIds#PROCEED_EVENT_ID}
 * @event {@link EventIds#INVALID_PROFILE_CTX}
 * @event {@link EventIds#INVALID_SEC_CFG}
 */
public class PopulateDecryptionParameters extends AbstractProfileAction {

    /** Class logger. */
    @Nonnull private final Logger log = LoggerFactory.getLogger(PopulateDecryptionParameters.class);
    
    /** Strategy used to look up the {@link SecurityParametersContext} to set the parameters for. */
    @Nonnull private Function<ProfileRequestContext,SecurityParametersContext> securityParametersContextLookupStrategy;
    
    /** Strategy used to lookup a per-request {@link DecryptionConfiguration}. */
    @Nullable private Function<ProfileRequestContext,DecryptionConfiguration> configurationLookupStrategy;
    
    /** Resolver for parameters to store into context. */
    @NonnullAfterInit private DecryptionParametersResolver resolver;
    
    /**
     * Constructor.
     * 
     * Initializes {@link #messageMetadataContextLookupStrategy} to {@link ChildContextLookup}.
     */
    public PopulateDecryptionParameters() {
        // Create context by default.
        securityParametersContextLookupStrategy = Functions.compose(
                new ChildContextLookup<>(SecurityParametersContext.class, true), new InboundMessageContextLookup());
    }

    /**
     * Set the strategy used to look up the {@link SecurityParametersContext} to set the parameters for.
     * 
     * @param strategy lookup strategy
     */
    public void setSecurityParametersContextLookupStrategy(
            @Nonnull final Function<ProfileRequestContext,SecurityParametersContext> strategy) {
        ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);

        securityParametersContextLookupStrategy = Constraint.isNotNull(strategy,
                "SecurityParametersContext lookup strategy cannot be null");
    }
    
    /**
     * Set the strategy used to look up a per-request {@link DecryptionConfiguration}.
     * 
     * @param strategy lookup strategy
     */
    public void setConfigurationLookupStrategy(
            @Nullable final Function<ProfileRequestContext,DecryptionConfiguration> strategy) {
        ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
        
        configurationLookupStrategy = strategy;
    }
    
    /**
     * Set the resolver to use for the parameters to store into the context.
     * 
     * @param newResolver   resolver to use
     */
    public void setDecryptionParametersResolver(@Nonnull final DecryptionParametersResolver newResolver) {
        ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
        
        resolver = Constraint.isNotNull(newResolver, "DecryptionParametersResolver cannot be null");
    }
    
    /** {@inheritDoc} */
    @Override
    protected void doInitialize() throws ComponentInitializationException {
        super.doInitialize();
        
        if (resolver == null) {
            throw new ComponentInitializationException("DecryptionParametersResolver cannot be null");
        }
    }
    
    /** {@inheritDoc} */
    @Override
    protected void doExecute(@Nonnull final ProfileRequestContext profileRequestContext) throws ProfileException {

        log.debug("{} Resolving DecryptionParameters for request", getLogPrefix());
        
        final SecurityParametersContext paramsCtx =
                securityParametersContextLookupStrategy.apply(profileRequestContext);
        if (paramsCtx == null) {
            log.debug("{} No SecurityParametersContext returned by lookup strategy", getLogPrefix());
            ActionSupport.buildEvent(profileRequestContext, EventIds.INVALID_PROFILE_CTX);
            return;
        }
        
        // TODO: do we include anything but the global default and the per-profile configs?
        // Maybe a global IdP config in addition or instead of the OpenSAML one?
        
        final List<DecryptionConfiguration> configs = Lists.newArrayList();
        configs.add(SecurityConfigurationSupport.getGlobalDecryptionConfiguration());
        
        if (configurationLookupStrategy != null) {
            log.debug("{} Looking up per-request DecryptionConfiguration", getLogPrefix());
            final DecryptionConfiguration perRequestConfig = configurationLookupStrategy.apply(profileRequestContext);
            if (perRequestConfig != null) {
                configs.add(perRequestConfig);
            }
        }
        
        try {
            final DecryptionParameters params = resolver.resolveSingle(
                    new CriteriaSet(new DecryptionConfigurationCriterion(configs)));
            paramsCtx.setDecryptionParameters(params);
            log.debug("{} {} DecryptionParameters", getLogPrefix(),
                    params != null ? "Resolved" : "Failed to resolve");
        } catch (final ResolverException e) {
            log.error(getLogPrefix() + " Error resolving DecryptionParameters", e);
            ActionSupport.buildEvent(profileRequestContext, EventIds.INVALID_SEC_CFG);
        }
    }
    
}