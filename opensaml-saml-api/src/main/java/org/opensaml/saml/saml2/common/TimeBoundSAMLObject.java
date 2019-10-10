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

package org.opensaml.saml.saml2.common;

import java.time.Instant;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.opensaml.saml.common.SAMLObject;

import net.shibboleth.utilities.java.support.annotation.constraint.NotEmpty;

/**
 * A functional interface for SAMLElements that are bounded with a 
 * "validUntil" attribute. 
 */
public interface TimeBoundSAMLObject extends SAMLObject{

    /** "validUntil" attribute's local name. */
    @Nonnull @NotEmpty static final String VALID_UNTIL_ATTRIB_NAME = "validUntil";

    /** "validUntil" attribute's QName. */
    @Nonnull static final QName VALID_UNTIL_ATTRIB_QNAME =
            new QName(null, "validUntil", XMLConstants.DEFAULT_NS_PREFIX);

    /**
     * Checks to see if the current time is past the validUntil time.
     * 
     * @return true of this descriptor is still valid otherwise false
     */
    boolean isValid();

    /**
     * Gets the date until which this descriptor is valid.
     * 
     * @return the date until which this descriptor is valid
     */
    @Nullable Instant getValidUntil();

    /**
     * Sets the date until which this descriptor is valid.
     * 
     * @param validUntil the date until which this descriptor is valid
     */
    void setValidUntil(@Nullable final Instant validUntil);

}