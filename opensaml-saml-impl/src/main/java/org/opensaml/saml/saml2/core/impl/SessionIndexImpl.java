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

import java.util.List;

import org.opensaml.core.xml.AbstractXMLObject;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.saml.saml2.core.SessionIndex;

/**
 * Concrete implementation of {@link org.opensaml.saml.saml2.core.SessionIndex}.
 */
public class SessionIndexImpl extends AbstractXMLObject implements SessionIndex {

    /** The session index value. */
    private String sessionIndex;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected SessionIndexImpl(final String namespaceURI, final String elementLocalName, final String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public String getSessionIndex() {
        return this.sessionIndex;
    }

    /** {@inheritDoc} */
    public void setSessionIndex(final String newSessionIndex) {
        this.sessionIndex = prepareForAssignment(this.sessionIndex, newSessionIndex);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        // no children
        return null;
    }
}