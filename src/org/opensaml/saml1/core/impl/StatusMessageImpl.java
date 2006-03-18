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

package org.opensaml.saml1.core.impl;

import java.util.List;

import org.opensaml.common.SAMLVersion;
import org.opensaml.saml1.core.StatusMessage;
import org.opensaml.xml.XMLObject;

/**
 * Concrete implementation of org.opensaml.saml1.core StatusMessage object
 */
public class StatusMessageImpl extends AbstractProtocolSAMLObject implements StatusMessage {

    /**
     * Contents of the element
     */
    private String message;

    /**
     * Hidden Constructor
     * @deprecated
     */
    private StatusMessageImpl() {
        super(StatusMessage.LOCAL_NAME, null);
    }
    
    /**
     * Constructor
     *
     * @param version the version to set
     */
    protected StatusMessageImpl(SAMLVersion version) {
        super(StatusMessage.LOCAL_NAME, version);
    }

    /*
     * @see org.opensaml.saml1.core.StatusMessage#getMessage()
     */
    public String getMessage() {
        return message;
    }

    /*
     * @see org.opensaml.saml1.core.StatusMessage#setMessage(java.lang.String)
     */
    public void setMessage(String message) {
        this.message = prepareForAssignment(this.message, message);
    }

    /*
     * @see org.opensaml.xml.XMLObject#getOrderedChildren()
     */
    public List<XMLObject> getOrderedChildren() {
        return null;
    }
}