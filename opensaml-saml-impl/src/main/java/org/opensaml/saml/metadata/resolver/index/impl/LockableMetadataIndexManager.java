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

package org.opensaml.saml.metadata.resolver.index.impl;

import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.opensaml.saml.metadata.resolver.index.MetadataIndex;
import org.opensaml.saml.saml2.metadata.EntityDescriptor;

import com.google.common.base.Function;

import net.shibboleth.utilities.java.support.annotation.constraint.NonnullElements;
import net.shibboleth.utilities.java.support.annotation.constraint.NotLive;
import net.shibboleth.utilities.java.support.annotation.constraint.Unmodifiable;

/**
 * A specialization of {@link MetadataIndexManager} capable of read/write locking.
 * 
 * @param <T> the type of data being indexed
 */
public class LockableMetadataIndexManager<T> extends MetadataIndexManager<T> {
    
    /** The manager's read write lock. */
    @Nonnull private final ReadWriteLock readWriteLock;

    /**
     * Constructor.
     *
     * @param initIndexes indexes for which to initialize storage
     * @param extractionFunction function to extract the indexed data item from an EntityDescriptor
     */
    public LockableMetadataIndexManager(
            @Nullable @NonnullElements @Unmodifiable @NotLive final Set<MetadataIndex> initIndexes,
            @Nonnull final Function<EntityDescriptor, T> extractionFunction
            ) {
        super(initIndexes, extractionFunction);
        readWriteLock = new ReentrantReadWriteLock(true);
    }
    
    /**
     * Get the manager's instance of the {@link ReadWriteLock}.
     * 
     * <p>
     * Callers of the manager are responsible for explicitly locking (and unlocking)
     * for reading and/or writing, based on application use cases.
     * </p>
     * 
     * @return Returns the rwlock.
     */
    @Nonnull public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }


}
