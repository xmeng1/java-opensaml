/*
 * Copyright [2005] [University Corporation for Advanced Internet Development, Inc.]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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

package org.opensaml.saml2.core.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.saml2.core.ArtifactResponse;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.XMLObjectChildrenList;

/**
 * Concrete implementation of {@link org.opensaml.saml2.core.ArtifactResponse}
 */
public class ArtifactResponseImpl extends StatusResponseImpl implements ArtifactResponse {

    /** child "any" elements */
    private final XMLObjectChildrenList<XMLObject> unknownChildren;
    
    /**
     * Constructor
     * 
     * @param namespaceURI
     * @param elementLocalName
     * @param namespacePrefix
     */
    protected ArtifactResponseImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        unknownChildren = new XMLObjectChildrenList<XMLObject>(this);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<XMLObject> getUnknownXMLObjects() {
        return unknownChildren;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();
        
        children.addAll(super.getOrderedChildren());
        children.addAll(unknownChildren);
        
        return Collections.unmodifiableList(children);
    }
}