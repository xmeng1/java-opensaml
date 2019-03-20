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

import java.time.Instant;

import org.opensaml.core.xml.util.AttributeMap;
import org.opensaml.soap.wssecurity.AttributedDateTime;
import org.opensaml.soap.wssecurity.IdBearing;

import net.shibboleth.utilities.java.support.xml.DOMTypeSupport;

/**
 * Implementation of {@link AttributedDateTime}.
 * 
 */
public class AttributedDateTimeImpl extends AbstractWSSecurityObject implements AttributedDateTime {

    /** DateTime object. */
    private Instant dateTimeValue;

    /** String dateTime representation. */
    private String stringValue;
    
    /** wsu:id attribute value. */
    private String id;
    
    /** Wildcard attributes. */
    private AttributeMap unknownAttributes;

    /**
     * Constructor.
     * 
     * @param namespaceURI namespace of the element
     * @param elementLocalName name of the element
     * @param namespacePrefix namespace prefix of the element
     */
    public AttributedDateTimeImpl(final String namespaceURI, final String elementLocalName,
            final String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        unknownAttributes = new AttributeMap(this);
    }

    /** {@inheritDoc} */
    public Instant getDateTime() {
        return dateTimeValue;
    }

    /** {@inheritDoc} */
    public void setDateTime(final Instant newDateTime) {
        dateTimeValue = newDateTime;
        stringValue = prepareForAssignment(stringValue, DOMTypeSupport.instantToDateTime(dateTimeValue));
    }

    /** {@inheritDoc} */
    public String getValue() {
        return stringValue;
    }

    /** {@inheritDoc} */
    public void setValue(final String newValue) {
        dateTimeValue = DOMTypeSupport.dateTimeToInstant(newValue);
        stringValue = prepareForAssignment(stringValue, newValue);
    }

    /** {@inheritDoc} */
    public String getWSUId() {
        return id;
    }

    /** {@inheritDoc} */
    public void setWSUId(final String newId) {
        final String oldID = id;
        id = prepareForAssignment(id, newId);
        registerOwnID(oldID, id);
        manageQualifiedAttributeNamespace(IdBearing.WSU_ID_ATTR_NAME, id != null);
    }

    /** {@inheritDoc} */
    public AttributeMap getUnknownAttributes() {
        return unknownAttributes;
    }
    
}