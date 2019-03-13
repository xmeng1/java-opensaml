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

package org.opensaml.saml.metadata.resolver.filter.impl;

import java.time.Duration;
import java.time.Instant;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.opensaml.core.xml.XMLObject;
import org.opensaml.saml.metadata.resolver.filter.FilterException;
import org.opensaml.saml.metadata.resolver.filter.MetadataFilter;
import org.opensaml.saml.saml2.metadata.EntitiesDescriptor;
import org.opensaml.saml.saml2.metadata.EntityDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A metadata filter that requires the presence of a <code>validUntil</code> attribute on the root element of the
 * metadata document. It can optionally enforce that the validity period (now minus <code>validUntil</code> date)
 * is not longer than a specified amount.
 * 
 * A maximum validity interval of less than 1 means that no restriction is placed on the metadata's
 * <code>validUntil</code> attribute.
 */
public class RequiredValidUntilFilter implements MetadataFilter {

    /** Class logger. */
    @Nonnull private final Logger log = LoggerFactory.getLogger(RequiredValidUntilFilter.class);

    /** The maximum interval between now and the <code>validUntil</code> date. Defaults to 14 days. */
    @Nullable private Duration maxValidityInterval;

    /** Constructor. */
    public RequiredValidUntilFilter() {
        maxValidityInterval = Duration.ofDays(14);
    }

    /**
     * Get the maximum interval between now and the <code>validUntil</code> date.
     * A value <=0 indicates that there is no restriction.
     * 
     * @return maximum interval between now and the <code>validUntil</code> date
     */
    @Nullable public Duration getMaxValidityInterval() {
        return maxValidityInterval;
    }
    
    /**
     * Set the maximum interval between now and the <code>validUntil</code> date.
     * A value <=0 indicates that there is no restriction.
     * 
     * @param validity time between now and the <code>validUntil</code> date
     */
    public void setMaxValidityInterval(@Nullable final Duration validity) {
        if (validity != null && !validity.isNegative() && !validity.isZero()) {
            maxValidityInterval = validity;
        } else {
            maxValidityInterval = null;
        }
    }

    /** {@inheritDoc} */
    @Nullable public XMLObject filter(@Nullable final XMLObject metadata) throws FilterException {
        if (metadata == null) {
            return null;
        }
        
        final Instant validUntil = getValidUntil(metadata);

        if (validUntil == null) {
            throw new FilterException("Metadata did not include a validUntil attribute");
        }

        final Instant now = Instant.now();
        if (maxValidityInterval != null && validUntil.isAfter(now)) {
            final long validityInterval = validUntil.toEpochMilli() - now.toEpochMilli();
            if (Duration.ofMillis(validityInterval).compareTo(maxValidityInterval) > 0) {
                throw new FilterException(String.format("Metadata's validity interval %s is larger than is allowed %s", 
                        Duration.ofMillis(validityInterval), maxValidityInterval));
            }
        }
        
        return metadata;
    }

    /**
     * Gets the validUntil time of the metadata, if present.
     * 
     * @param metadata metadata from which to get the validUntil instant
     * 
     * @return the valid until instant or null if it is not present
     * 
     * @throws FilterException thrown if the given XML object is not an {@link EntitiesDescriptor} or
     *             {@link EntityDescriptor}
     */
    @Nullable protected Instant getValidUntil(@Nonnull final XMLObject metadata) throws FilterException {
        if (metadata instanceof EntitiesDescriptor) {
            return ((EntitiesDescriptor) metadata).getValidUntil();
        } else if (metadata instanceof EntityDescriptor) {
            return ((EntityDescriptor) metadata).getValidUntil();
        } else {
            log.error("Metadata root element was not an EntitiesDescriptor or EntityDescriptor it was a {}", metadata
                    .getElementQName());
            throw new FilterException("Metadata root element was not an EntitiesDescriptor or EntityDescriptor");
        }
    }
    
}