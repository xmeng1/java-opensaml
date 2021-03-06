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

package org.opensaml.saml.saml1.core.impl;

import net.shibboleth.utilities.java.support.xml.ElementSupport;

import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.saml.common.AbstractSAMLObjectMarshaller;
import org.opensaml.saml.saml1.core.NameIdentifier;
import org.w3c.dom.Element;

/**
 * A thread safe Marshaller for {@link org.opensaml.saml.saml1.core.NameIdentifier} objects.
 */
public class NameIdentifierMarshaller extends AbstractSAMLObjectMarshaller {

    /** {@inheritDoc} */
    protected void marshallAttributes(final XMLObject samlElement, final Element domElement)
            throws MarshallingException {
        final NameIdentifier nameIdentifier = (NameIdentifier) samlElement;

        if (nameIdentifier.getNameQualifier() != null) {
            domElement
                    .setAttributeNS(null, NameIdentifier.NAMEQUALIFIER_ATTRIB_NAME, nameIdentifier.getNameQualifier());
        }

        if (nameIdentifier.getFormat() != null) {
            domElement.setAttributeNS(null, NameIdentifier.FORMAT_ATTRIB_NAME, nameIdentifier.getFormat());
        }
    }

    /** {@inheritDoc} */
    protected void marshallElementContent(final XMLObject samlObject, final Element domElement)
            throws MarshallingException {
        final NameIdentifier nameIdentifier = (NameIdentifier) samlObject;

        if (nameIdentifier.getValue() != null) {
            ElementSupport.appendTextContent(domElement, nameIdentifier.getValue());
        }
    }
}