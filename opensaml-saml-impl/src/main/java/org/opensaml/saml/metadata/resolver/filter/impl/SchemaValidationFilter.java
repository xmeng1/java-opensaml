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

package org.opensaml.saml.metadata.resolver.filter.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Validator;

import org.opensaml.core.xml.XMLObject;
import org.opensaml.saml.common.xml.SAMLSchemaBuilder;
import org.opensaml.saml.metadata.resolver.filter.FilterException;
import org.opensaml.saml.metadata.resolver.filter.MetadataFilter;
import org.opensaml.saml.metadata.resolver.filter.MetadataFilterContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import net.shibboleth.utilities.java.support.annotation.ParameterName;
import net.shibboleth.utilities.java.support.logic.Constraint;
import net.shibboleth.utilities.java.support.xml.SchemaBuilder;

/**
 * A metadata filter that schema validates an incoming metadata file.
 */
public class SchemaValidationFilter implements MetadataFilter {

    /** Class logger. */
    @Nonnull private final Logger log = LoggerFactory.getLogger(SchemaValidationFilter.class);

    /** Self-managed SchemaBuilder to support old extension schema parameter. */
    @Nullable private SchemaBuilder schemaBuilder;

    /** SAML schema source. */
    @Nonnull private SAMLSchemaBuilder samlSchemaBuilder;
        
    /**
     * Constructor.
     * 
     * @param builder SAML schema source to use
     */
    public SchemaValidationFilter(@Nonnull @ParameterName(name="builder") final SAMLSchemaBuilder builder) {
        samlSchemaBuilder = Constraint.isNotNull(builder, "SAMLSchemaBuilder cannot be null");
    }

    /** {@inheritDoc} */
    @Override
    @Nullable public XMLObject filter(@Nullable final XMLObject metadata, @Nonnull final MetadataFilterContext context)
            throws FilterException {
        if (metadata == null) {
            return null;
        }
        
        final Validator schemaValidator;
        try {
            schemaValidator = samlSchemaBuilder.getSAMLSchema().newValidator();
        } catch (final SAXException e) {
            log.error("Unable to build metadata validation schema", e);
            throw new FilterException("Unable to build metadata validation schema", e);
        }

        try {
            schemaValidator.validate(new DOMSource(metadata.getDOM()));
        } catch (final Exception e) {
            log.error("Incoming metadata was not schema valid", e);
            throw new FilterException("Incoming metadata was not schema valid", e);
        }
        
        return metadata;
    }
    
}