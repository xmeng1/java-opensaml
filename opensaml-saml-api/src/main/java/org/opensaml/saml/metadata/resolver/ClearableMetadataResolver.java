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

package org.opensaml.saml.metadata.resolver;

import javax.annotation.Nonnull;

import net.shibboleth.utilities.java.support.resolver.ResolverException;

/**
 * Specialization of {@link MetadataResolver} that supports on-demand clearing of the resolver's
 * internal cache of data.
 * 
 * <p>
 * This would typically be implemented by "dynamic" resolvers of metadata such as {@link DynamicMetadataResolver},
 * rather than "batch" resolvers such as {@link BatchMetadataResolver}.
 * </p>
 */
public interface ClearableMetadataResolver extends MetadataResolver {
    
    /**
     * Attempt to clear all data from the internal cache of the resolver.
     * 
     * @throws ResolverException if the clear operation was unsuccessful
     */
    void clear() throws ResolverException;
    
    /**
     * Attempt to clear data from the internal cache of the resolver for the specified entityID.
     * 
     * @param entityID the target entityID
     * 
     * @throws ResolverException if the clear operation was unsuccessful
     */
    void clear(@Nonnull final String entityID) throws ResolverException;
    
}