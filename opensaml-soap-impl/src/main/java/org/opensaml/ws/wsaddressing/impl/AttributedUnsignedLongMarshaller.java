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

package org.opensaml.ws.wsaddressing.impl;

import net.shibboleth.utilities.java.support.xml.ElementSupport;

import org.opensaml.core.xml.XMLObject;
import org.opensaml.ws.wsaddressing.AttributedUnsignedLong;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLObjectSupport;
import org.w3c.dom.Element;

/**
 * Marshaller for instances of {@link AttributedUnsignedLong}.
 */
public class AttributedUnsignedLongMarshaller extends AbstractWSAddressingObjectMarshaller {

    /** {@inheritDoc} */
    protected void marshallAttributes(XMLObject xmlObject, Element domElement) throws MarshallingException {
        AttributedUnsignedLong aul = (AttributedUnsignedLong) xmlObject;
        XMLObjectSupport.marshallAttributeMap(aul.getUnknownAttributes(), domElement);
    }

    /** {@inheritDoc} */
    protected void marshallElementContent(XMLObject xmlObject, Element domElement) throws MarshallingException {
        AttributedUnsignedLong aul = (AttributedUnsignedLong) xmlObject;
        if (aul.getValue() != null) {
            ElementSupport.appendTextContent(domElement, aul.getValue().toString());
        }
    }

}
