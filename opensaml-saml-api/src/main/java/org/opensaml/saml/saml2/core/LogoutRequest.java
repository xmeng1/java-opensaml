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

/**
 * 
 */
package org.opensaml.saml.saml2.core;

import java.time.Instant;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.opensaml.saml.common.xml.SAMLConstants;

/**
 * SAML 2.0 Core LogoutRequest.
 */
public interface LogoutRequest extends RequestAbstractType {
    
    /** Element local name. */
    static final String DEFAULT_ELEMENT_LOCAL_NAME = "LogoutRequest";
    
    /** Default element name. */
    static final QName DEFAULT_ELEMENT_NAME = 
        new QName(SAMLConstants.SAML20P_NS, DEFAULT_ELEMENT_LOCAL_NAME, SAMLConstants.SAML20P_PREFIX);
    
    /** Local name of the XSI type. */
    static final String TYPE_LOCAL_NAME = "LogoutRequestType"; 
        
    /** QName of the XSI type. */
    static final QName TYPE_NAME = 
        new QName(SAMLConstants.SAML20P_NS, TYPE_LOCAL_NAME, SAMLConstants.SAML20P_PREFIX);
    
    /** Reason attribute name. */
    static final String REASON_ATTRIB_NAME = "Reason";
    
    /** NotOnOrAfter attribute name. */
    static final String NOT_ON_OR_AFTER_ATTRIB_NAME = "NotOnOrAfter";

    /** QName for the NotOnOrAfter attribute. */
    static final QName NOT_ON_OR_AFTER_ATTRIB_QNAME =
            new QName(null, "NotOnOrAfter", XMLConstants.DEFAULT_NS_PREFIX);
    
    /** User-initiated logout reason. */
    static final String USER_REASON = "urn:oasis:names:tc:SAML:2.0:logout:user";

    /** Admin-initiated logout reason. */
    static final String ADMIN_REASON = "urn:oasis:names:tc:SAML:2.0:logout:admin";
    
    /** Global timeout logout reason. */
    static final String GLOBAL_TIMEOUT_REASON = "urn:oasis:names:tc:SAML:2.0:logout:global-timeout";
    
    /** SP timeout logout reason. */
    static final String SP_TIMEOUT_REASON = "urn:oasis:names:tc:SAML:2.0:logout:sp-timeout";
    
    /**
     * Get the Reason attrib value of the request.
     * 
     * @return the Reason value of the request
     */
    String getReason();

    /**
     * Set the Reason attrib value of the request.
     * 
     * @param newReason the new Reason value of the request
     */
    void setReason(String newReason);
    
    /**
     * Get the NotOnOrAfter attrib value of the request.
     * 
     * @return the NotOnOrAfter value of the request
     */
    Instant getNotOnOrAfter();

    /**
     * Set the NotOnOrAfter attrib value of the request.
     * 
     * @param newNotOnOrAfter the new NotOnOrAfter value of the request
     */
    void setNotOnOrAfter(Instant newNotOnOrAfter);
    
    /**
     * Gets the base identifier of the principal for this request.
     * 
     * @return the base identifier of the principal for this request
     */
    BaseID getBaseID();
    
    /**
     * Sets the base identifier of the principal for this request.
     * 
     * @param newBaseID the base identifier of the principal for this request
     */
    void setBaseID(BaseID newBaseID);
    
    /**
     * Gets the name identifier of the principal for this request.
     * 
     * @return the name identifier of the principal for this request
     */
    NameID getNameID();
    
    /**
     * Sets the name identifier of the principal for this request.
     * 
     * @param newNameID the name identifier of the principal for this request
     */
    void setNameID(NameID newNameID);
    
    /**
     * Gets the encrytped name identifier of the principal for this request.
     * 
     * @return the encrytped name identifier of the principal for this request
     */
    EncryptedID getEncryptedID();
    
    /**
     * Sets the encrypted name identifier of the principal for this request.
     * 
     * @param newEncryptedID the new encrypted name identifier of the principal for this request
     */
    void setEncryptedID(EncryptedID newEncryptedID);
       
    /**
     *  Get the list of SessionIndexes for the request.
     * 
     * 
     * @return the list of SessionIndexes
     */
    List<SessionIndex> getSessionIndexes();


}
