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

package org.opensaml.saml.metadata.resolver.impl;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.opensaml.core.criterion.EntityIdCriterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.UrlEscapers;

import net.shibboleth.utilities.java.support.annotation.constraint.NotEmpty;
import net.shibboleth.utilities.java.support.logic.Constraint;
import net.shibboleth.utilities.java.support.primitive.DeprecationSupport;
import net.shibboleth.utilities.java.support.primitive.StringSupport;
import net.shibboleth.utilities.java.support.resolver.CriteriaSet;
import net.shibboleth.utilities.java.support.primitive.DeprecationSupport.ObjectType;
import net.shibboleth.utilities.java.support.velocity.Template;

/**
 * Function which produces a URL by substituting an entity ID value from criteria into a Velocity template string.
 * 
 * <p>
 * The entity ID will be replaced in the template string according to the template variable <code>entityID</code>, 
 * e.g. "https://metadataservice.com/entity/${entityID}".
 * </p>
 * 
 * <p>
 * The value of the <code>encodingStyle</code> parameter determines whether and how the entity ID will be encoded prior
 * to substitution, and accepts an enum value from {@link EncodingStyle}.
 * Legacy deprecated constructors accept an <code>encoded</code> parameter, where <code>true</code>
 * means {@link EncodingStyle#form} and <code>false</code> means {@link EncodingStyle#none}.
 * </p>
 * 
 */
public class TemplateRequestURLBuilder implements Function<CriteriaSet, String> {
    
    /** EntityID Encoding style. */
    public enum EncodingStyle {
            /** No encoding. */
            none,
            /** URL form encoding. @see {@link UrlEscapers#urlFormParameterEscaper()} */
            form,
            /** URL path encoding. @see {@link UrlEscapers#urlPathSegmentEscaper()} */
            path,
            /** URL fragment encoding. @see {@link UrlEscapers#urlFragmentEscaper()} */
            fragment
    };

    /** The Velocity context variable name for the entity ID. */
    public static final String CONTEXT_KEY_ENTITY_ID = "entityID";
    
    /** Logger. */
    private final Logger log = LoggerFactory.getLogger(TemplateRequestURLBuilder.class);
    
    /** Velocity template instance used to render the request URL. */
    private Template template;
    
    /** The template text, for logging purposes. */
    private String templateText;
    
    /** Function which transforms the entityID prior to substitution into the template. */
    private Function<String, String> transformer;
    
    /** Enum value indicating whether and how to encode the entity ID value before substitution. */
    private EncodingStyle entityIDEncodingStyle;
    
    /**
     * Constructor.
     * 
     * <p>The template character set will be US ASCII.</p>
     *
     * @param engine the {@link VelocityEngine} instance to use
     * @param templateString the Velocity template string
     * @param encoded true if entity ID should be URL form-encoded prior to substitution, false otherwise
     * 
     * @deprecated Replacement is the variant which accepts an instance of {@link EncodingStyle}
     */
    @Deprecated 
    public TemplateRequestURLBuilder(@Nonnull final VelocityEngine engine, 
            @Nonnull @NotEmpty final String templateString, final boolean encoded) {
        this(engine, templateString, encoded ? EncodingStyle.form : EncodingStyle.none, null, 
                StandardCharsets.US_ASCII);
        
        DeprecationSupport.warnOnce(ObjectType.METHOD, getClass().getName() + ".constructor", null,
                "variant accepting EncodingStyle enum");
    }
    /**
     * Constructor.
     * 
     * <p>The template character set will be US ASCII.</p>
     *
     * @param engine the {@link VelocityEngine} instance to use
     * @param templateString the Velocity template string
     * @param transform function which transforms the entityID prior to substitution, may be null
     * @param encoded true if entity ID should be URL form-encoded prior to substitution, false otherwise
     * 
     * @deprecated Replacement is the variant which accepts an instance of {@link EncodingStyle}
     */
    @Deprecated
    public TemplateRequestURLBuilder(@Nonnull final VelocityEngine engine, 
            @Nonnull @NotEmpty final String templateString, final boolean encoded, 
            @Nullable final Function<String, String> transform) {
        this(engine, templateString, encoded ? EncodingStyle.form : EncodingStyle.none, transform, 
                StandardCharsets.US_ASCII);
        
        DeprecationSupport.warnOnce(ObjectType.METHOD, getClass().getName() + ".constructor", null,
                "variant accepting EncodingStyle enum");
    }
    
    /**
     * Constructor.
     *
     * @param engine the {@link VelocityEngine} instance to use
     * @param templateString the Velocity template string
     * @param encoded true if entity ID should be URL form-encoded prior to substitution, false otherwise
     * @param transform function which transforms the entityID prior to substitution, may be null
     * @param charSet character set of the template, may be null
     * 
     * @deprecated Replacement is the variant which accepts an instance of {@link EncodingStyle}
     */
    @Deprecated
    public TemplateRequestURLBuilder(@Nonnull final VelocityEngine engine, 
            @Nonnull @NotEmpty final String templateString, final boolean encoded, 
            @Nullable final Function<String, String> transform, @Nullable final Charset charSet) {
        this(engine, templateString, encoded ? EncodingStyle.form : EncodingStyle.none, transform, charSet);
        
        DeprecationSupport.warnOnce(ObjectType.METHOD, getClass().getName() + ".constructor", null,
                "variant accepting EncodingStyle enum");
    }
    
    /**
     * Constructor.
     * 
     * <p>The template character set will be US ASCII.</p>
     *
     * @param engine the {@link VelocityEngine} instance to use
     * @param templateString the Velocity template string
     * @param encodingStyle the style for encoding the entity ID prior to substitution,
     *          null means {@link EncodingStyle#none}
     */
    public TemplateRequestURLBuilder(@Nonnull final VelocityEngine engine, 
            @Nonnull @NotEmpty final String templateString, @Nullable final EncodingStyle encodingStyle) {
        this(engine, templateString, encodingStyle, null, StandardCharsets.US_ASCII);
    }
    /**
     * Constructor.
     * 
     * <p>The template character set will be US ASCII.</p>
     *
     * @param engine the {@link VelocityEngine} instance to use
     * @param templateString the Velocity template string
     * @param transform function which transforms the entityID prior to substitution, may be null
     * @param encodingStyle the style for encoding the entity ID prior to substitution,
     *          null means {@link EncodingStyle#none}
     */
    public TemplateRequestURLBuilder(@Nonnull final VelocityEngine engine, 
            @Nonnull @NotEmpty final String templateString, @Nullable final EncodingStyle encodingStyle, 
            @Nullable final Function<String, String> transform) {
        this(engine, templateString, encodingStyle, transform, StandardCharsets.US_ASCII);
    }
    
    /**
     * Constructor.
     *
     * @param engine the {@link VelocityEngine} instance to use
     * @param templateString the Velocity template string
     * @param encodingStyle the style for encoding the entity ID prior to substitution,
     *          null means {@link EncodingStyle#none}
     * @param transform function which transforms the entityID prior to substitution, may be null
     * @param charSet character set of the template, may be null
     */
    public TemplateRequestURLBuilder(@Nonnull final VelocityEngine engine, 
            @Nonnull @NotEmpty final String templateString, final EncodingStyle encodingStyle, 
            @Nullable final Function<String, String> transform, @Nullable final Charset charSet) {
        
        Constraint.isNotNull(engine, "VelocityEngine was null");
        
        final String trimmedTemplate = StringSupport.trimOrNull(templateString);
        templateText = Constraint.isNotNull(trimmedTemplate, "Template string was null or empty");
        
        transformer = transform;
        
        if (charSet != null) {
            template = Template.fromTemplate(engine, trimmedTemplate, charSet);
        } else {
            template = Template.fromTemplate(engine, trimmedTemplate);
        }
        
        entityIDEncodingStyle = encodingStyle != null ? encodingStyle : EncodingStyle.none;
    }

    /** {@inheritDoc} */
    @Nullable public String apply(@Nullable final CriteriaSet criteria) {
        Constraint.isNotNull(criteria, "Criteria was null");
        if (!criteria.contains(EntityIdCriterion.class)) {
            log.trace("Criteria did not contain entity ID, unable to build request URL");
            return null;
        }
        String entityID = criteria.get(EntityIdCriterion.class).getEntityId();
        
        log.debug("Saw input entityID '{}'", entityID);
        
        if (transformer != null) {
            entityID = transformer.apply(entityID);
            log.debug("Transformed entityID is '{}'", entityID);
            if (entityID == null) {
                log.debug("Transformed entityID was null");
                return null;
            }
        }

        final VelocityContext context = new VelocityContext();
        switch (entityIDEncodingStyle) {
            case none:
                context.put(CONTEXT_KEY_ENTITY_ID, entityID);
                break;
            case form:
                context.put(CONTEXT_KEY_ENTITY_ID, UrlEscapers.urlFormParameterEscaper().escape(entityID));
                break;
            case path:
                context.put(CONTEXT_KEY_ENTITY_ID, UrlEscapers.urlPathSegmentEscaper().escape(entityID));
                break;
            case fragment:
                context.put(CONTEXT_KEY_ENTITY_ID, UrlEscapers.urlFragmentEscaper().escape(entityID));
                break;
            default:
                log.warn("An unsupported EncodingStyle value was seen, treating as 'none': {}", entityIDEncodingStyle);
                context.put(CONTEXT_KEY_ENTITY_ID, entityID);
        }
        
        try {
            final String result = template.merge(context);
            log.debug("From entityID '{}' and template text '{}', built request URL: {}", 
                    entityID, templateText, result);
            return result;
        } catch (final Throwable t) {
            log.error("Encountered fatal error attempting to build request URL", t);
            return null;
        }
    }


}
