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


import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.shibboleth.utilities.java.support.annotation.constraint.NonnullElements;
import net.shibboleth.utilities.java.support.component.AbstractInitializableComponent;
import net.shibboleth.utilities.java.support.component.ComponentSupport;
import net.shibboleth.utilities.java.support.logic.Constraint;

import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.core.xml.io.UnmarshallingException;
import org.opensaml.core.xml.util.XMLObjectSupport;
import org.opensaml.saml.common.SAMLObjectBuilder;
import org.opensaml.saml.ext.saml2alg.DigestMethod;
import org.opensaml.saml.ext.saml2alg.SigningMethod;
import org.opensaml.saml.metadata.resolver.filter.FilterException;
import org.opensaml.saml.metadata.resolver.filter.MetadataFilter;
import org.opensaml.saml.saml2.metadata.EncryptionMethod;
import org.opensaml.saml.saml2.metadata.EntitiesDescriptor;
import org.opensaml.saml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml.saml2.metadata.Extensions;
import org.opensaml.saml.saml2.metadata.KeyDescriptor;
import org.opensaml.saml.saml2.metadata.RoleDescriptor;
import org.opensaml.security.credential.UsageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicates;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Collections2;
import com.google.common.collect.Multimap;

/**
 * A filter that adds algorithm extension content to entities in order to drive software
 * behavior based on them.
 * 
 * <p>The entities to annotate are identified with a {@link Predicate}, and multiple algorithms can be
 * associated with each.</p>
 */
public class AlgorithmFilter extends AbstractInitializableComponent implements MetadataFilter {

    /** Class logger. */
    @Nonnull private final Logger log = LoggerFactory.getLogger(AlgorithmFilter.class);

    /** Rules for adding algorithms. */
    @Nonnull @NonnullElements private Multimap<Predicate<EntityDescriptor>,XMLObject> applyMap;
    
    /** Builder for {@link Extensions}. */
    @Nonnull private final SAMLObjectBuilder<Extensions> extBuilder;

    /** Constructor. */
    public AlgorithmFilter() {
        extBuilder = (SAMLObjectBuilder<Extensions>)
                XMLObjectProviderRegistrySupport.getBuilderFactory().<Extensions>getBuilderOrThrow(
                        Extensions.DEFAULT_ELEMENT_NAME);
        applyMap = ArrayListMultimap.create();
    }
    
    /**
     * Set the mappings from {@link Predicate} to extensions of various types to apply.
     * 
     * @param rules rules to apply
     */
    public void setRules(@Nonnull @NonnullElements final Map<Predicate<EntityDescriptor>,Collection<XMLObject>> rules) {
        ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
        Constraint.isNotNull(rules, "Rules map cannot be null");
        
        applyMap = ArrayListMultimap.create(rules.size(), 1);
        for (final Map.Entry<Predicate<EntityDescriptor>,Collection<XMLObject>> entry : rules.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                applyMap.putAll(entry.getKey(), Collections2.filter(entry.getValue(), Predicates.notNull()));
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    @Nullable public XMLObject filter(@Nullable final XMLObject metadata) throws FilterException {
        if (metadata == null) {
            return null;
        }

        if (metadata instanceof EntitiesDescriptor) {
            filterEntitiesDescriptor((EntitiesDescriptor) metadata);
        } else {
            filterEntityDescriptor((EntityDescriptor) metadata);
        }
        
        return metadata;
    }
    
    /**
     * Filters entity descriptor.
     * 
     * @param descriptor entity descriptor to filter
     */
    protected void filterEntityDescriptor(@Nonnull final EntityDescriptor descriptor) {
        
        for (final Map.Entry<Predicate<EntityDescriptor>,Collection<XMLObject>> entry : applyMap.asMap().entrySet()) {
            if (!entry.getValue().isEmpty() && entry.getKey().test(descriptor)) {
                
                for (final XMLObject xmlObject : entry.getValue()) {
                    try {
                        if (xmlObject instanceof DigestMethod) {
                            log.info("Adding DigestMethod ({}) to EntityDescriptor ({})",
                                    ((DigestMethod) xmlObject).getAlgorithm(), descriptor.getEntityID());
                            getExtensions(descriptor).getUnknownXMLObjects().add(
                                    XMLObjectSupport.cloneXMLObject(xmlObject));
                        } else if (xmlObject instanceof SigningMethod) {
                            log.info("Adding SigningMethod ({}) to EntityDescriptor ({})",
                                    ((SigningMethod) xmlObject).getAlgorithm(), descriptor.getEntityID());
                            getExtensions(descriptor).getUnknownXMLObjects().add(
                                    XMLObjectSupport.cloneXMLObject(xmlObject));
                        } else if (xmlObject instanceof EncryptionMethod) {
                            log.info("Adding EncryptionMethod ({}) to EntityDescriptor ({})",
                                    ((EncryptionMethod) xmlObject).getAlgorithm(), descriptor.getEntityID());
                            addEncryptionMethod(descriptor, (EncryptionMethod) xmlObject);
                        }
                        
                    } catch (final MarshallingException | UnmarshallingException e) {
                        log.error("Error cloning XMLObject", e);
                    }
                }
            }
        }
    }
    
    /**
     * Filters entities descriptor.
     * 
     * @param descriptor entities descriptor to filter
     */
    protected void filterEntitiesDescriptor(@Nonnull final EntitiesDescriptor descriptor) {
        
        // First we check any contained EntitiesDescriptors.
        for (final EntitiesDescriptor group : descriptor.getEntitiesDescriptors()) {
            filterEntitiesDescriptor(group);
        }
        
        // Next, check contained EntityDescriptors.
        for (final EntityDescriptor entity : descriptor.getEntityDescriptors()) {
            filterEntityDescriptor(entity);
        }
    }
    
    /**
     * Return existing {@link Extensions} object or create it first.
     * 
     * @param descriptor the surrounding entity
     * 
     * @return new or existing extension block
     */
    @Nonnull protected Extensions getExtensions(@Nonnull final EntityDescriptor descriptor) {
        
        Extensions extensions = descriptor.getExtensions();
        if (extensions == null) {
            extensions = extBuilder.buildObject();
            descriptor.setExtensions(extensions);
        }
        
        return extensions;
    }

    /**
     * Add {@link EncryptionMethod} extension to every {@link KeyDescriptor} found in
     * an entity.
     * 
     * @param descriptor the entity to modify
     * @param encryptionMethod extension to add
     */
    protected void addEncryptionMethod(@Nonnull final EntityDescriptor descriptor,
            @Nonnull final EncryptionMethod encryptionMethod) {
        
        for (final RoleDescriptor role : descriptor.getRoleDescriptors()) {
            for (final KeyDescriptor key : role.getKeyDescriptors()) {
                if (key.getUse() == null || key.getUse() != UsageType.SIGNING) {
                    try {
                        key.getEncryptionMethods().add(XMLObjectSupport.cloneXMLObject(encryptionMethod));
                    } catch (final MarshallingException|UnmarshallingException e) {
                        log.error("Error cloning XMLObject", e);
                    }
                }
            }
        }
    }
    
}