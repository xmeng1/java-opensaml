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

package org.opensaml.saml2.metadata.impl;

import java.util.Map.Entry;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.joda.time.format.ISODateTimeFormat;
import org.opensaml.common.impl.AbstractSAMLObjectMarshaller;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.common.CacheableSAMLObject;
import org.opensaml.saml2.common.TimeBoundSAMLObject;
import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 * A thread safe Marshaller for {@link org.opensaml.saml2.metadata.EntityDescriptor} objects.
 */
public class EntityDescriptorMarshaller extends AbstractSAMLObjectMarshaller {

    /**
     * Logger
     */
    private static Logger log = Logger.getLogger(EntityDescriptorMarshaller.class);

    /**
     * Constructor
     */
    public EntityDescriptorMarshaller() {
        super(SAMLConstants.SAML20MD_NS, EntityDescriptor.LOCAL_NAME);
    }

    /**
     * Constructor
     * 
     * @param namespaceURI
     * @param elementLocalName
     */
    protected EntityDescriptorMarshaller(String namespaceURI, String elementLocalName) {
        super(namespaceURI, elementLocalName);
    }

    /*
     * @see org.opensaml.xml.io.AbstractXMLObjectMarshaller#marshallAttributes(org.opensaml.xml.XMLObject,
     *      org.w3c.dom.Element)
     */
    protected void marshallAttributes(XMLObject samlElement, Element domElement) {
        EntityDescriptor entityDescriptor = (EntityDescriptor) samlElement;

        // Set the entityID attribute
        if (entityDescriptor.getEntityID() != null) {
            domElement.setAttributeNS(null, EntityDescriptor.ENTITY_ID_ATTRIB_NAME, entityDescriptor.getEntityID());
        }

        // Set the ID attribute
        if (entityDescriptor.getID() != null) {
            domElement.setAttributeNS(null, EntityDescriptor.ID_ATTRIB_NAME, entityDescriptor.getID());
        }

        // Set the validUntil attribute
        if (entityDescriptor.getValidUntil() != null) {
            if (log.isDebugEnabled()) {
                log.debug("Writting validUntil attribute to EntityDescriptor DOM element");
            }
            String validUntilStr = ISODateTimeFormat.dateTime().print(entityDescriptor.getValidUntil());
            domElement.setAttributeNS(null, TimeBoundSAMLObject.VALID_UNTIL_ATTRIB_NAME, validUntilStr);
        }

        // Set the cacheDuration attribute
        if (entityDescriptor.getCacheDuration() != null) {
            if (log.isDebugEnabled()) {
                log.debug("Writting cacheDuration attribute to EntityDescriptor DOM element");
            }
            String cacheDuration = DatatypeHelper.longToDuration(entityDescriptor.getCacheDuration());
            domElement.setAttributeNS(null, CacheableSAMLObject.CACHE_DURATION_ATTRIB_NAME, cacheDuration);
        }
        
        Attr attribute;
        for(Entry<QName, String> entry: entityDescriptor.getUnknownAttributes().entrySet()){
            attribute = XMLHelper.constructAttribute(domElement.getOwnerDocument(), entry.getKey());
            attribute.setValue(entry.getValue());
            domElement.setAttributeNode(attribute);
        }
    }
}