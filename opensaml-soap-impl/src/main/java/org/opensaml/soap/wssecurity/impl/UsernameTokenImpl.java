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

package org.opensaml.soap.wssecurity.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.util.AttributeMap;
import org.opensaml.core.xml.util.IndexedXMLObjectChildrenList;
import org.opensaml.soap.wssecurity.IdBearing;
import org.opensaml.soap.wssecurity.Username;
import org.opensaml.soap.wssecurity.UsernameToken;

/**
 * Implementation of {@link UsernameToken}.
 */
public class UsernameTokenImpl extends AbstractWSSecurityObject implements UsernameToken {

    /** The &lt;wsu:Id&gt; attribute value. */
    private String id;

    /** The &lt;wsse:Username&gt; child element. */
    private Username username;
    
    /** Wildcard attributes. */
    private AttributeMap unknownAttributes;
    
    /** Wildcard child elements. */
    private IndexedXMLObjectChildrenList<XMLObject> unknownChildren;

    /**
     * Constructor.
     * 
     * @param namespaceURI namespace of the element
     * @param elementLocalName name of the element
     * @param namespacePrefix namespace prefix of the element
     */
    public UsernameTokenImpl(final String namespaceURI, final String elementLocalName, final String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        unknownAttributes = new AttributeMap(this);
        unknownChildren = new IndexedXMLObjectChildrenList<>(this);
    }
 
    /** {@inheritDoc} */
    public Username getUsername() {
        return username;
    }

    /** {@inheritDoc} */
    public void setUsername(final Username newUsername) {
        username = prepareForAssignment(username, newUsername);
    }

    /** {@inheritDoc} */
    public String getWSUId() {
        return id;
    }

    /** {@inheritDoc} */
    public void setWSUId(final String newId) {
        final String oldId = id;
        id = prepareForAssignment(id, newId);
        registerOwnID(oldId, id);
        manageQualifiedAttributeNamespace(IdBearing.WSU_ID_ATTR_NAME, id != null);
    }
    
    /** {@inheritDoc} */
    public AttributeMap getUnknownAttributes() {
        return unknownAttributes;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getUnknownXMLObjects() {
        return unknownChildren;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getUnknownXMLObjects(final QName typeOrName) {
        return (List<XMLObject>) unknownChildren.subList(typeOrName);
    }
    
    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        final ArrayList<XMLObject> children = new ArrayList<>();
        if (username != null) {
            children.add(username);
        }
        
        if (!getUnknownXMLObjects().isEmpty()) {
            children.addAll(getUnknownXMLObjects());
        }
        return Collections.unmodifiableList(children);
    }

}
