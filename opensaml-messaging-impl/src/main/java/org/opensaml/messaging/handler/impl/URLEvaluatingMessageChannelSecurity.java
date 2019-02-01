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

import java.net.MalformedURLException;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.opensaml.messaging.context.MessageChannelSecurityContext;
import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.handler.MessageHandlerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.shibboleth.utilities.java.support.annotation.constraint.NonnullAfterInit;
import net.shibboleth.utilities.java.support.component.ComponentInitializationException;
import net.shibboleth.utilities.java.support.component.ComponentSupport;
import net.shibboleth.utilities.java.support.net.URLBuilder;

/**
 * Message handler which populates a {@link MessageChannelSecurityContext} based on evaluating a
 * target URL resolved via a configured strategy function.
 */
public class URLEvaluatingMessageChannelSecurity extends AbstractMessageChannelSecurity {
    
    /** Logger. */
    @Nonnull private Logger log = LoggerFactory.getLogger(URLEvaluatingMessageChannelSecurity.class);

    /** Flag controlling whether traffic on the default TLS port is "secure". */
    private boolean defaultPortInsecure;
    
    /** Function which looks up the URL to evaluate. */
    @NonnullAfterInit private Function<MessageContext,String> urlLookup;
    
    /** The target resolved URL. */
    @Nullable private String url;
    
    /** Target resolved and parsed URL. */
    @Nullable private URLBuilder urlBuilder;
    
    /** Constructor. */
    public URLEvaluatingMessageChannelSecurity() {
        defaultPortInsecure = true;
    }
    
    /**
     * Set whether traffic on the default TLS port is "secure" for the purposes of this action.
     * 
     * <p>Defaults to "true"</p>
     *
     * <p>Ordinarily TLS is considered a "secure" channel, but traffic to a default port meant
     * for browser access tends to rely on server certificates that are unsuited to secure messaging
     * use cases. This flag allows software layers to recognize traffic on this port as "insecure" and
     * needing additional security measures.</p>
     * 
     * @param flag flag to set
     */
    public void setDefaultPortInsecure(final boolean flag) {
        ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
        
        defaultPortInsecure = flag;
    }
    
    /**
     * Set the function which looks up the destination URL to evaluate.
     * 
     * @param function the lookup function
     */
    public void setURLLookup(@Nullable final Function<MessageContext, String> function) {
        urlLookup = function;
    }
    
    /** {@inheritDoc} */
    @Override
    protected void doInitialize() throws ComponentInitializationException {
        super.doInitialize();
        if (urlLookup == null) {
            throw new ComponentInitializationException("Destination URL lookup function is required");
        }
    }

    /** {@inheritDoc} */
    @Override
    protected boolean doPreInvoke(@Nonnull final MessageContext messageContext) throws MessageHandlerException {
        if (!super.doPreInvoke(messageContext)) {
            return false;
        }

        url = urlLookup.apply(messageContext);
        if (url != null) {
            try {
                urlBuilder = new URLBuilder(url);
                return super.doPreInvoke(messageContext);
            } catch (final MalformedURLException e){
                log.warn("Unable to parse resolved target URL: {}", url, e);
                return false;
            }
        } else {
            log.warn("No target URL resolved, skipping MessageChannelSecurityContext population");
            return false;
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void doInvoke(@Nonnull final MessageContext messageContext) {
        final MessageChannelSecurityContext channelContext =
                getParentContext().getSubcontext(MessageChannelSecurityContext.class, true);
        
        final String scheme = urlBuilder.getScheme();
        // Note that below we don't care about port if scheme != https,
        // so only need to worry about default port for https, not all possible schemes.
        final Integer port = urlBuilder.getPort() != null 
                ? urlBuilder.getPort() 
                        : "https".equalsIgnoreCase(scheme) ? 443 : null;
                
        log.debug("Evaluating message channel security for scheme '{}' and port '{}' for URL: {}",
                scheme, port, url);
        
        if ("https".equalsIgnoreCase(scheme) && (!defaultPortInsecure || port != 443)) {
            channelContext.setConfidentialityActive(true);
            channelContext.setIntegrityActive(true);
        } else {
            channelContext.setConfidentialityActive(false);
            channelContext.setIntegrityActive(false);
        }
        
        log.debug("Set MessageChannelSecurityContext isIntegrityActive: {}", 
                channelContext.isIntegrityActive());
        log.debug("Set MessageChannelSecurityContext isConfidentialityActive: {}", 
                channelContext.isConfidentialityActive());
    }

}