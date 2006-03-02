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

package org.opensaml.saml2.core.impl;

import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.BaseID;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;

/**
 * A thread-safe Unmarshaller for {@link org.opensaml.saml2.core.Subject} objects.
 */
public class SubjectUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /** Constructor */
    public SubjectUnmarshaller() {
        super(SAMLConstants.SAML20_NS, Subject.LOCAL_NAME);
    }

    /*
     * @see org.opensaml.xml.io.AbstractXMLObjectUnmarshaller#processChildElement(org.opensaml.xml.XMLObject,
     *      org.opensaml.xml.XMLObject)
     */
    protected void processChildElement(XMLObject parentObject, XMLObject childObject) throws UnmarshallingException {
        Subject subject = (Subject) parentObject;

        if (childObject instanceof BaseID) {
            subject.setBaseID((BaseID) childObject);
        }else if(childObject instanceof NameID) {
            subject.setNameID((NameID) childObject);
        } else if (childObject instanceof SubjectConfirmation) {
            subject.getSubjectConfirmations().add((SubjectConfirmation) childObject);
        } else {
            super.processChildElement(parentObject, childObject);
        }
    }
}