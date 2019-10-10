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

package org.opensaml.saml.common;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.shibboleth.utilities.java.support.annotation.constraint.NotEmpty;

import org.opensaml.xmlsec.signature.AbstractSignableXMLObject;
import org.opensaml.xmlsec.signature.Signature;

/**
 * Abstract SAMLObject implementation that also implements {@link org.opensaml.xmlsec.signature.SignableXMLObject}.
 */
public abstract class AbstractSignableSAMLObject extends AbstractSignableXMLObject implements SignableSAMLObject {

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected AbstractSignableSAMLObject(@Nullable final String namespaceURI,
            @Nonnull @NotEmpty final String elementLocalName, @Nullable final String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /** {@inheritDoc} */
    public final boolean equals(final Object obj) {
        if(obj == this){
            return true;
        }
        
        return super.equals(obj);
    }
    
    /** {@inheritDoc} */
    public int hashCode() {
        return super.hashCode();
    }
    
    /**
     * {@inheritDoc}
     * 
     * When a signature is added, a default content reference that uses the ID of this object will be
     * created and added to the signature at the time of signing. See {@link SAMLObjectContentReference} 
     * for the default digest algorithm and transforms that will be used.  These default values may be 
     * changed prior to marshalling this object.
     */
    public void setSignature(@Nullable final Signature newSignature) {
        if(newSignature != null && newSignature.getContentReferences().isEmpty()) {
            newSignature.getContentReferences().add(new SAMLObjectContentReference(this));
        }
        super.setSignature(newSignature);
    }

}