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

package org.opensaml.profile.context;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Nonnull;

import org.opensaml.messaging.context.BaseContext;

import net.shibboleth.utilities.java.support.annotation.constraint.Live;
import net.shibboleth.utilities.java.support.annotation.constraint.NonnullElements;


/**
 * A context which expresses one or more requesters on whose behalf a request is being handled.
 * 
 * <p>An ordering of proxied requesters is not implied or guaranteed.</p>
 * 
 * @since 3.4.0
 */
public final class ProxiedRequesterContext extends BaseContext {

    /** The resource. */
    @Nonnull @NonnullElements private Collection<String> requesters;

    /** Constructor. */
    public ProxiedRequesterContext() {
        requesters = new ArrayList<>();
    }
    
    /**
     * Get the proxied requesters.
     * 
     * @return the proxied requesters
     */
    @Nonnull @NonnullElements @Live public Collection<String> getRequesters() {
        return requesters;
    }

}