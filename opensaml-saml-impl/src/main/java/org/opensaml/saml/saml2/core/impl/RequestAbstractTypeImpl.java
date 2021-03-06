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

package org.opensaml.saml.saml2.core.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.core.xml.XMLObject;
import org.opensaml.saml.common.AbstractSignableSAMLObject;
import org.opensaml.saml.common.SAMLVersion;
import org.opensaml.saml.saml2.core.Extensions;
import org.opensaml.saml.saml2.core.Issuer;
import org.opensaml.saml.saml2.core.RequestAbstractType;

/**
 * Concrete implementation of {@link org.opensaml.saml.saml2.core.RequestAbstractType}.
 */
public abstract class RequestAbstractTypeImpl extends AbstractSignableSAMLObject implements RequestAbstractType {

    /** SAML Version of the request. */
    private SAMLVersion version;

    /** Unique identifier of the request. */
    private String id;

    /** Date/time request was issued. */
    private Instant issueInstant;

    /** URI of the request destination. */
    private String destination;

    /** URI of the SAML user consent type. */
    private String consent;

    /** URI of the SAML user consent type. */
    private Issuer issuer;

    /** Extensions child element. */
    private Extensions extensions;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected RequestAbstractTypeImpl(final String namespaceURI, final String elementLocalName,
            final String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        version = SAMLVersion.VERSION_20;
    }

    /** {@inheritDoc} */
    public SAMLVersion getVersion() {
        return version;
    }

    /** {@inheritDoc} */
    public void setVersion(final SAMLVersion newVersion) {
        this.version = prepareForAssignment(this.version, newVersion);
    }

    /** {@inheritDoc} */
    public String getID() {
        return id;
    }

    /** {@inheritDoc} */
    public void setID(final String newID) {
        final String oldID = id;
        id = prepareForAssignment(id, newID);
        registerOwnID(oldID, id);
    }

    /** {@inheritDoc} */
    public Instant getIssueInstant() {
        return issueInstant;
    }

    /** {@inheritDoc} */
    public void setIssueInstant(final Instant newIssueInstant) {
        issueInstant = prepareForAssignment(issueInstant, newIssueInstant);
    }

    /** {@inheritDoc} */
    public String getDestination() {
        return destination;
    }

    /** {@inheritDoc} */
    public void setDestination(final String newDestination) {
        destination = prepareForAssignment(destination, newDestination);
    }

    /** {@inheritDoc} */
    public String getConsent() {
        return consent;
    }

    /** {@inheritDoc} */
    public void setConsent(final String newConsent) {
        consent = prepareForAssignment(consent, newConsent);
    }

    /** {@inheritDoc} */
    public Issuer getIssuer() {
        return issuer;
    }

    /** {@inheritDoc} */
    public void setIssuer(final Issuer newIssuer) {
        issuer = prepareForAssignment(issuer, newIssuer);
    }

    /** {@inheritDoc} */
    public Extensions getExtensions() {
        return extensions;
    }

    /** {@inheritDoc} */
    public void setExtensions(final Extensions newExtensions) {
        extensions = prepareForAssignment(extensions, newExtensions);
    }

    /** {@inheritDoc} */
    public String getSignatureReferenceID() {
        return id;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        final ArrayList<XMLObject> children = new ArrayList<>();

        if (issuer != null) {
            children.add(issuer);
        }
        if (getSignature() != null) {
            children.add(getSignature());
        }
        if (extensions != null) {
            children.add(extensions);
        }

        if (children.size() == 0) {
            return null;
        }

        return Collections.unmodifiableList(children);
    }
}