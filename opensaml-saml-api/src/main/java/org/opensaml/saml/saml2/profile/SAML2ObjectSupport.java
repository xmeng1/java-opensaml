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

package org.opensaml.saml.saml2.profile;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.opensaml.saml.saml2.core.NameID;

/**
 * A helper class for working with SAMLObjects.
 */
public final class SAML2ObjectSupport {
    
    /** Constructor. */
    private SAML2ObjectSupport() {
        
    }
    
    /**
     * Return true iff the two input {@link NameID} formats are equivalent for SAML 2.0
     * purposes.
     * 
     * @param format1   first format to check
     * @param format2   second format to check
     * 
     * @return  true iff the two format values should be viewed as equivalent
     */
    public static boolean areNameIDFormatsEquivalent(@Nullable final String format1,
            @Nullable final String format2) {
        
        return Objects.equals(
                format1 != null ? format1 : NameID.UNSPECIFIED,
                format2 != null ? format2 : NameID.UNSPECIFIED);
    }

    /**
     * Return true iff the two input {@link NameID} objects are equivalent for SAML 2.0 purposes, with
     * the assumption that the qualifier attributes must match exactly.
     * 
     * @param name1   first NameID to check
     * @param name2   second NameID to check
     * 
     * @return  true iff the two values should be viewed as equivalent
     */
    public static boolean areNameIDsEquivalent(@Nonnull final NameID name1, @Nonnull final NameID name2) {
        return areNameIDFormatsEquivalent(name1.getFormat(), name2.getFormat())
                && Objects.equals(name1.getValue(), name2.getValue())
                && Objects.equals(name1.getNameQualifier(), name2.getNameQualifier())
                && Objects.equals(name1.getSPNameQualifier(), name2.getSPNameQualifier());
    }

    /**
     * Return true iff the two input {@link NameID} objects are equivalent for SAML 2.0 purposes, allowing
     * thw qualifier attributes to assume default values if not otherwise set.
     * 
     * @param name1   first NameID to check
     * @param name2   second NameID to check
     * @param assertingParty optional name of asserting party to default in as NameQualifier
     * @param relyingParty optional name of relying party to default in as SPNameQualifier 
     * 
     * @return  true iff the two values should be viewed as equivalent
     * 
     * @since 3.4.0
     */
    public static boolean areNameIDsEquivalent(@Nonnull final NameID name1, @Nonnull final NameID name2,
            @Nullable final String assertingParty, @Nullable final String relyingParty) {
        
        if (!areNameIDFormatsEquivalent(name1.getFormat(), name2.getFormat())
                || !Objects.equals(name1.getValue(), name2.getValue())) {
            return false;
        }
        
        String name1qual = name1.getNameQualifier();
        String name2qual = name2.getNameQualifier();
        if (name1qual == null) {
            name1qual = assertingParty;
        }
        if (name2qual == null) {
            name2qual = assertingParty;
        }
        if (!Objects.equals(name1qual, name2qual)) {
            return false;
        }

        name1qual = name1.getSPNameQualifier();
        name2qual = name2.getSPNameQualifier();
        if (name1qual == null) {
            name1qual = relyingParty;
        }
        if (name2qual == null) {
            name2qual = relyingParty;
        }
        return Objects.equals(name1qual, name2qual);
    }

}