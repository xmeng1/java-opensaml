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

package org.opensaml.profile.criterion;

import javax.annotation.Nonnull;

import org.opensaml.profile.context.ProfileRequestContext;

import net.shibboleth.utilities.java.support.logic.Constraint;
import net.shibboleth.utilities.java.support.resolver.Criterion;

/**
 * {@link Criterion} representing a {@link ProfileRequestContext}.
 *
 * @since 4.0.0
 */
public final class ProfileRequestContextCriterion implements Criterion {

    /** The {@link ProfileRequestContext}. */
    @Nonnull private final ProfileRequestContext profileRequestContext;

    /**
     * Constructor.
     * 
     * @param prc the profile request context
     */
    public ProfileRequestContextCriterion(@Nonnull final ProfileRequestContext prc) {
        profileRequestContext = Constraint.isNotNull(prc, "ProfileRequestContext cannot be null");
    }

    /**
     * Gets the profile request context.
     * 
     * @return the profile request context
     */
    @Nonnull public ProfileRequestContext getProfileRequestContext() {
        return profileRequestContext;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ProfileRequestContextCriterion [prc=");
        builder.append(profileRequestContext);
        builder.append("]");
        return builder.toString();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return profileRequestContext.hashCode();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (obj instanceof ProfileRequestContextCriterion) {
            return profileRequestContext.equals(((ProfileRequestContextCriterion) obj).profileRequestContext);
        }

        return false;
    }
    
}