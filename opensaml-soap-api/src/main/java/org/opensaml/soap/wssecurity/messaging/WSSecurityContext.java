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

package org.opensaml.soap.wssecurity.messaging;

import java.time.Instant;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.shibboleth.utilities.java.support.collection.LazyList;

import org.opensaml.messaging.context.BaseContext;

/**
 * A subcontext that carries information related to WS-Security processing.
 */
public final class WSSecurityContext extends BaseContext {
    
    //TODO implement support for remaining items of WS-Security data model
    
    /** List of known WS-Security tokens. */
    private LazyList<Token<?>> tokens;
    
    /** Value for Timestamp Created. */
    private Instant timestampCreated;
 
    /** Value for Timestamp Expires. */
    private Instant timestampExpires;
    
    /** Constructor. */
    public WSSecurityContext() {
        super();
        tokens = new LazyList<>();
    }
    
    /**
     * Get the list of WS-Security tokens.
     * 
     * @return the list of tokens
     */
    @Nonnull public List<Token<?>> getTokens() {
        return tokens;
    }

    /**
     * Get the value for Timestamp Created.
     * 
     * @return the Timestamp Created value
     */
    @Nullable public Instant getTimestampCreated() {
        return timestampCreated;
    }

    /**
     * Set the value for Timestamp Created.
     * 
     * @param value the Timestamp Created value
     */
    public void setTimestampCreated(@Nullable final Instant value) {
        timestampCreated = value;
    }

    /**
     * Get the value for Timestamp Expires.
     * 
     * @return the Timestamp Expires value
     */
    @Nullable public Instant getTimestampExpires() {
        return timestampExpires;
    }

    /**
     * Set the value for Timestamp Expires.
     * 
     * @param value the Timestamp Expires value
     */
    public void setTimestampExpires(@Nullable final Instant value) {
        timestampExpires = value;
    }

}
