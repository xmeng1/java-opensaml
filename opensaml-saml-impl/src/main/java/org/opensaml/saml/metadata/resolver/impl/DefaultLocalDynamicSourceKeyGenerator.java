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

package org.opensaml.saml.metadata.resolver.impl;

import javax.annotation.Nullable;

import org.opensaml.core.criterion.EntityIdCriterion;
import org.opensaml.saml.common.binding.artifact.SAMLArtifact;
import org.opensaml.saml.common.binding.artifact.SAMLSourceIDArtifact;
import org.opensaml.saml.criterion.ArtifactCriterion;

import com.google.common.base.Function;
import com.google.common.io.BaseEncoding;

import net.shibboleth.utilities.java.support.codec.StringDigester;
import net.shibboleth.utilities.java.support.resolver.CriteriaSet;

/**
 * A default implementation of {@link Function} for use as a source key generator 
 * with {@link LocalDynamicMetadataResolver}.
 * 
 * <p>
 * This is a simple subclass of {@link EntityIDDigestGenerator} which defaults in the {@link StringDigester}
 * to use lower-case hex encoding of the SHA-1 digest of the entity ID from {@link EntityIdCriterion}.
 * Since this is the same representation typically used for the SAML SourceID used in artifacts,
 * this implementation adds in support for understanding {@link ArtifactCriterion} carrying a 
 * {@link SAMLSourceIDArtifact}.
 * </p>
 */
public class DefaultLocalDynamicSourceKeyGenerator extends EntityIDDigestGenerator {
    
    /** Hex encoder. */
    private static final BaseEncoding HEX = BaseEncoding.base16().lowerCase();

    /**
     * Constructor.
     *
     */
    public DefaultLocalDynamicSourceKeyGenerator() {
        super();
    }

    /**
     * Constructor.
     *
     * @param keyPrefix optional prefix for the digested value
     * @param keySuffix optional suffix for the digested value
     * @param valueSeparator optional separator between the prefix, digest and suffix values
     */
    public DefaultLocalDynamicSourceKeyGenerator(@Nullable final String keyPrefix, @Nullable final String keySuffix, 
            @Nullable final String valueSeparator) {
        super(null, keyPrefix, keySuffix, valueSeparator);
    }

    /** {@inheritDoc} */
    public String apply(@Nullable final CriteriaSet criteria) {
        if (criteria == null) {
            return null;
        }
        
        if (criteria.contains(EntityIdCriterion.class)) {
            return super.apply(criteria);
        }
        
        if (criteria.contains(ArtifactCriterion.class)) {
            final SAMLArtifact artifact = criteria.get(ArtifactCriterion.class).getArtifact();
            if (artifact instanceof SAMLSourceIDArtifact) {
                return buildKey(HEX.encode(((SAMLSourceIDArtifact)artifact).getSourceID()));
            }
        }
        
        return null;
    }

}
