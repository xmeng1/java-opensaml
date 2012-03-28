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

package org.opensaml.saml1.core.impl;

import org.opensaml.core.xml.XMLObject;
import org.opensaml.saml1.core.AssertionArtifact;
import org.opensaml.saml1.core.AssertionIDReference;
import org.opensaml.saml1.core.Query;
import org.opensaml.saml1.core.Request;
import org.opensaml.xml.io.UnmarshallingException;

/**
 * A thread safe Unmarshaller for {@link org.opensaml.saml1.core.Request} objects.
 */
public class RequestUnmarshaller extends RequestAbstractTypeUnmarshaller {

    /** {@inheritDoc} */
    protected void processChildElement(XMLObject parentElement, XMLObject childElement) throws UnmarshallingException {
        Request request = (Request) parentElement;

        try {
            if (childElement instanceof Query) {
                request.setQuery((Query) childElement);
            } else if (childElement instanceof AssertionIDReference) {
                request.getAssertionIDReferences().add((AssertionIDReference) childElement);
            } else if (childElement instanceof AssertionArtifact) {
                request.getAssertionArtifacts().add((AssertionArtifact) childElement);
            } else {
                super.processChildElement(parentElement, childElement);
            }
        } catch (IllegalArgumentException e) {
            throw new UnmarshallingException(e);
        }
    }

}