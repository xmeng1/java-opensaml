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

package org.opensaml.saml.saml1.core;

import java.time.Instant;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.opensaml.saml.common.SAMLObject;
import org.opensaml.saml.common.xml.SAMLConstants;

/**
 * This interface defines how the object representing a SAML1 <code> AuthenticationStatment </code> element behaves.
 */
public interface AuthenticationStatement extends SAMLObject, SubjectStatement {

    /** Element name, no namespace. */
    static final String DEFAULT_ELEMENT_LOCAL_NAME = "AuthenticationStatement";
    
    /** Default element name. */
    static final QName DEFAULT_ELEMENT_NAME =
            new QName(SAMLConstants.SAML1_NS, DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML1_PREFIX);
    
    /** Local name of the XSI type. */
    static final String TYPE_LOCAL_NAME = "AuthenticationStatementType"; 
        
    /** QName of the XSI type. */
    static final QName TYPE_NAME =
            new QName(SAMLConstants.SAML1_NS, TYPE_LOCAL_NAME, SAMLConstants.SAML1_PREFIX);

    /** Name of the AuthenticationMethod attribute. */
    static final String AUTHENTICATIONMETHOD_ATTRIB_NAME = "AuthenticationMethod";

    /** Name of the AuthenticationInstant attribute. */
    static final String AUTHENTICATIONINSTANT_ATTRIB_NAME = "AuthenticationInstant";

    /** QName of the AuthenticationInstant attribute. */
    static final QName AUTHENTICATIONINSTANT_ATTRIB_QNAME =
            new QName(null, "AuthenticationInstant", XMLConstants.DEFAULT_NS_PREFIX);

    /** URI for Kerberos authentication method. */
    static final String KERBEROS_AUTHN_METHOD = "urn:ietf:rfc:1510";
    
    /** URI for Hardware Token authentication method. */
    static final String HARDWARE_TOKEN_AUTHN_METHOD = "urn:oasis:names:tc:SAML:1.0:am:HardwareToken";

    /** URI for Password authentication method. */
    static final String PASSWORD_AUTHN_METHOD = "urn:oasis:names:tc:SAML:1.0:am:password";

    /** URI for X509 Public Key authentication method. */
    static final String X509_AUTHN_METHOD = "urn:oasis:names:tc:SAML:1.0:am:X509-PKI";

    /** URI for PGP authentication method. */
    static final String PGP_AUTHN_METHOD = "urn:oasis:names:tc:SAML:1.0:am:PGP";

    /** URI for SPKI authentication method. */
    static final String SPKI_AUTHN_METHOD = "urn:oasis:names:tc:SAML:1.0:am:SPKI";

    /** URI for XKMS authentication method. */
    static final String XKMS_AUTHN_METHOD = "urn:oasis:names:tc:SAML:1.0:am:XKMS";
    
    /** URI for XML Digital Signature authentication method. */
    static final String XML_DSIG_AUTHN_METHOD = "urn:ietf:rfc:3075";

    /** URI for Secure Remote Password authentication method. */
    static final String SRP_AUTHN_METHOD = "urn:ietf:rfc:2945";

    /** URI for SSL/TLS Client authentication method. */
    static final String TLS_CLIENT_AUTHN_METHOD = "urn:ietf:rfc:2246";

    /** URI for unspecified authentication method. */
    static final String UNSPECIFIED_AUTHN_METHOD = "urn:oasis:names:tc:SAML:1.0:am:unspecified";    
    
    /**
     * Return the contents of the AuthenticationMethod attribute.
     *
     * @return the authentication method
     */
    String getAuthenticationMethod();

    /**
     * Set the contents of the AuthenticationMethod attribute.
     * 
     * @param authenticationMethod the authentication method
     */
    void setAuthenticationMethod(String authenticationMethod);

    /**
     * Return the contents of the AuthenticationInstant attribute.
     * 
     * @return the authentication instant
     */
    Instant getAuthenticationInstant();

    /**
     * Set the contents of the AuthenticationInstant attribute.
     *
     *  @param authenticationInstant the authentication instant
     */
    void setAuthenticationInstant(Instant authenticationInstant);

    /**
     * Get the {@link SubjectLocality}.
     * 
     * @return the {@link SubjectLocality} 
     */
    SubjectLocality getSubjectLocality();

    /**
     * Set the {@link SubjectLocality}.
     * 
     * @param subjectLocality the {@link SubjectLocality} 
     */
    void setSubjectLocality(SubjectLocality subjectLocality);

    /**
     * Get the list of {@link AuthorityBinding}s.
     * 
     * @return the list of {@link AuthorityBinding}s
     */
    List<AuthorityBinding> getAuthorityBindings();

}