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

package org.opensaml.saml.ext.saml2mdui.impl;

import java.util.ArrayList;

import net.shibboleth.utilities.java.support.xml.XMLConstants;

import org.opensaml.core.xml.LangBearing;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.io.UnmarshallingException;
import org.opensaml.saml.common.AbstractSAMLObjectUnmarshaller;
import org.opensaml.saml.ext.saml2mdui.Keywords;
import org.w3c.dom.Attr;

/**
 * A thread-safe unmarshaller for {@link org.opensaml.saml.ext.saml2mdui.Keywords} objects.
 */
public class KeywordsUnmarshaller extends AbstractSAMLObjectUnmarshaller {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void processAttribute(final XMLObject samlObject, final Attr attribute) throws UnmarshallingException {
        if (attribute.getLocalName().equals(LangBearing.XML_LANG_ATTR_LOCAL_NAME)
                && XMLConstants.XML_NS.equals(attribute.getNamespaceURI())) {
            final Keywords keywords = (Keywords) samlObject;

            keywords.setXMLLang(attribute.getValue());
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void processElementContent(final XMLObject samlObject, final String elementContent) {
        final Keywords keywords = (Keywords) samlObject;
        final String[] words = elementContent.split("\\s+");
        final ArrayList<String> wordlist = new ArrayList<>(words.length);
        
        for (final String s : words) {
            wordlist.add(s);
        }

        keywords.setKeywords(wordlist);
    }
}