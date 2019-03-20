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

package org.opensaml.core.xml.schema.impl;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.shibboleth.utilities.java.support.annotation.constraint.NotEmpty;

import org.opensaml.core.xml.AbstractXMLObject;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.schema.XSDateTime;


/**
 * Concrete implementation of {@link org.opensaml.core.xml.schema.XSDateTime}.
 */
public class XSDateTimeImpl extends AbstractXMLObject implements XSDateTime {
    
    /** Value of this dateTime element. */
    private Instant value;
    
    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected XSDateTimeImpl(@Nullable final String namespaceURI, @Nonnull @NotEmpty final String elementLocalName,
            @Nullable final String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }
    
    /** {@inheritDoc} */
    @Nullable public Instant getValue() {
        return value;
    }

    /** {@inheritDoc} */
    public void setValue(@Nullable final Instant newValue) {
        value = prepareForAssignment(value, newValue);
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        return Collections.emptyList();
    }

}