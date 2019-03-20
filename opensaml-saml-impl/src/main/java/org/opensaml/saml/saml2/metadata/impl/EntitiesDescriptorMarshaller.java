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

import org.opensaml.core.xml.XMLObject;
import org.opensaml.saml.common.AbstractSAMLObjectMarshaller;
import org.opensaml.saml.saml2.common.CacheableSAMLObject;
import org.opensaml.saml.saml2.common.TimeBoundSAMLObject;
import org.opensaml.saml.saml2.metadata.EntitiesDescriptor;
import org.w3c.dom.Element;

import net.shibboleth.utilities.java.support.xml.AttributeSupport;

/**
 * A thread safe Marshaller for {@link org.opensaml.saml.saml2.metadata.EntitiesDescriptor} objects.
 */
public class EntitiesDescriptorMarshaller extends AbstractSAMLObjectMarshaller {

    /** {@inheritDoc} */
    protected void marshallAttributes(final XMLObject samlElement, final Element domElement) {

        final EntitiesDescriptor entitiesDescriptor = (EntitiesDescriptor) samlElement;

        // Set the ID attribute
        if (entitiesDescriptor.getID() != null) {
            domElement.setAttributeNS(null, EntitiesDescriptor.ID_ATTRIB_NAME, entitiesDescriptor.getID());
            domElement.setIdAttributeNS(null, EntitiesDescriptor.ID_ATTRIB_NAME, true);
        }

        // Set the validUntil attribute
        if (entitiesDescriptor.getValidUntil() != null) {
            AttributeSupport.appendDateTimeAttribute(domElement, TimeBoundSAMLObject.VALID_UNTIL_ATTRIB_QNAME,
                    entitiesDescriptor.getValidUntil());
        }

        // Set the cacheDuration attribute
        if (entitiesDescriptor.getCacheDuration() != null) {
            AttributeSupport.appendDurationAttribute(domElement, CacheableSAMLObject.CACHE_DURATION_ATTRIB_QNAME,
                    entitiesDescriptor.getCacheDuration());
        }

        // Set the Name attribute
        if (entitiesDescriptor.getName() != null) {
            domElement.setAttributeNS(null, EntitiesDescriptor.NAME_ATTRIB_NAME, entitiesDescriptor.getName());
        }
    }
}