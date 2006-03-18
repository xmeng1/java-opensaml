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
package org.opensaml.saml1.core.impl;

import java.util.List;

import org.opensaml.common.SAMLVersion;
import org.opensaml.saml1.core.AttributeDesignator;
import org.opensaml.xml.XMLObject;

/**
 * Concrete Implementation of the {@link org.opensaml.saml1.core.AttributeDesignator} interface.
 */
public class AttributeDesignatorImpl extends AbstractAssertionSAMLObject implements AttributeDesignator {
    
    /** Contains the AttributeName */
    private String attributeName;

    /** Contains the AttributeNamespace */
    private String attributeNamespace;

    /**
     * Hidden Constructor
     * @deprecated 
     */
     private AttributeDesignatorImpl() {
        super(AttributeDesignator.LOCAL_NAME, null);
    }
    
    /**
     * Constructor
     *
     * @param version the version we want it to have
     */
    protected AttributeDesignatorImpl(SAMLVersion version) {
        super(AttributeDesignator.LOCAL_NAME, version);
    }
    
    /**
     * Constructor
     * @param elementLocalName
     * @param version the version we want it to have
     */
    protected AttributeDesignatorImpl(String elementLocalName, SAMLVersion version) {
        super(elementLocalName, version);
    }


    /**
     * Constructor
     *
     * @param namespaceURI
     * @param elementLocalName
     * @param namespacePrefix
     * @param version the version we want it to have
     */
    protected AttributeDesignatorImpl(String namespaceURI, String elementLocalName, String namespacePrefix, SAMLVersion version) {
        super(namespaceURI, elementLocalName, namespacePrefix, version);
    }

    /*
     * @see org.opensaml.saml1.core.AttributeDesignator#getAttributeName()
     */
    public String getAttributeName() {
        return attributeName;
    }

    /*
     * @see org.opensaml.saml1.core.AttributeDesignator#setAttributeName(java.lang.String)
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = prepareForAssignment(this.attributeName, attributeName);
    }

    /*
     * @see org.opensaml.saml1.core.AttributeDesignator#getAttributeNamespace()
     */
    public String getAttributeNamespace() {
        return attributeNamespace;
    }

    /*
     * @see org.opensaml.saml1.core.AttributeDesignator#setAttributeNamespace(java.lang.String)
     */
    public void setAttributeNamespace(String attributeNamespace) {
        this.attributeNamespace = prepareForAssignment(this.attributeNamespace, attributeNamespace);
    }

    /*
     * @see org.opensaml.xml.XMLObject#getOrderedChildren()
     */
    public List<XMLObject> getOrderedChildren() {
        return null;
    }

}
