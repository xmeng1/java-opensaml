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

package org.opensaml.messaging.context.navigate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.opensaml.messaging.context.BaseContext;

import net.shibboleth.utilities.java.support.annotation.ParameterName;
import net.shibboleth.utilities.java.support.logic.Constraint;

/**
 * A {@link ContextDataLookupFunction} that gets the root of context tree.
 * 
 * @param <StartContext> type of the starting context
 * @param <RootContext> type of the root context
 */
public class RootContextLookup<StartContext extends BaseContext, RootContext extends BaseContext> implements
        ContextDataLookupFunction<StartContext, RootContext> {

    /** Class type being returned. */
    @Nullable private final Class<RootContext> claz;

    /** Constructor. */
    public RootContextLookup() {
        claz = null;
    }

    /**
     * Constructor.
     *
     * @param targetClass the type to return
     */
    public RootContextLookup(@Nonnull @ParameterName(name="targetClass") final Class<RootContext> targetClass) {
        claz = Constraint.isNotNull(targetClass, "RootContext type cannot be null");
    }
    
    /** {@inheritDoc} */
    @Nullable public RootContext apply(@Nullable final BaseContext input) {
        if (input == null) {
            return null;
        }

        if (input.getParent() == null){
            if (claz != null) {
                if (claz.isInstance(input)) {
                    return claz.cast(input);
                }
                throw new ClassCastException("Root context was not of the expected type");
            }
            
            return (RootContext) input;
        }
        
        return apply(input.getParent());
    }
    
}