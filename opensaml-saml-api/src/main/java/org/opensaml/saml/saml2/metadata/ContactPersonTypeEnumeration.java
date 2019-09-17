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

package org.opensaml.saml.saml2.metadata;

import javax.annotation.Nonnull;

import net.shibboleth.utilities.java.support.annotation.constraint.NotEmpty;

/**
 * A type safe enumeration of contact types used by {@link org.opensaml.saml.saml2.metadata.ContactPerson}.
 */
public enum ContactPersonTypeEnumeration {

    /** "technical" contact type. */
    TECHNICAL("technical"),

    /** "support" contact type. */
    SUPPORT("support"),

    /** "administrative" contact type. */
    ADMINISTRATIVE("administrative"),

    /** "billing" contact type. */
    BILLING("billing"),

    /** "other" contact type. */
    OTHER("other");

    /** The contact type. */
    @Nonnull @NotEmpty private String type;

    /**
     * Constructor.
     * 
     * @param providedType the contact type
     */
    private ContactPersonTypeEnumeration(@Nonnull @NotEmpty final String providedType) {
        type = providedType;
    }

    /** {@inheritDoc} */
    public String toString() {
        return type;
    }

}