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

package org.opensaml.saml.saml2.metadata.impl;

import java.util.List;

import org.opensaml.core.xml.AbstractXMLObject;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.saml.saml2.metadata.AdditionalMetadataLocation;

/**
 * Concrete implementation of {@link org.opensaml.saml.saml2.metadata.AdditionalMetadataLocation}.
 */
public class AdditionalMetadataLocationImpl extends AbstractXMLObject implements AdditionalMetadataLocation {

    /** The metadata location. */
    private String location;

    /** Namespace scope of the root metadata element at the location. */
    private String namespace;

    /**
     * Constructor.
     * 
     * @param namespaceURI the URI of the name space
     * @param elementLocalName the local name
     * @param namespacePrefix the prefix name space
     */
    protected AdditionalMetadataLocationImpl(final String namespaceURI, final String elementLocalName,
            final String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public String getLocationURI() {
        return location;
    }

    /** {@inheritDoc} */
    public void setLocationURI(final String locationURI) {
        location = prepareForAssignment(location, locationURI);
    }

    /** {@inheritDoc} */
    public String getNamespaceURI() {
        return namespace;
    }

    /** {@inheritDoc} */
    public void setNamespaceURI(final String namespaceURI) {
        namespace = prepareForAssignment(namespace, namespaceURI);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        // No children for this element
        return null;
    }
}