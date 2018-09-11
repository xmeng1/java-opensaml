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

package org.opensaml.xmlsec.config;

import java.util.HashMap;

import org.opensaml.core.config.ConfigurationService;
import org.opensaml.core.config.InitializationException;
import org.opensaml.core.config.Initializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.shibboleth.utilities.java.support.component.ComponentInitializationException;
import net.shibboleth.utilities.java.support.xml.BasicParserPool;

/**
 * An initializer for the global parser pool for XML decryption use, wrapped by {@link DecryptionParserPool}.
 * 
 * <p>
 * The ParserPool configured by default here is an instance of
 * {@link BasicParserPool}, with a maxPoolSize property of 50, 
 * an additional feature added specifically for decryption usage 
 * (http://apache.org/xml/features/dom/defer-node-expansion = False)
 * and all other properties with default values.
 * </p>
 * 
 */
public class DecryptionParserPoolInitializer implements Initializer {
    
    /** Logger. */
    private Logger log = LoggerFactory.getLogger(DecryptionParserPoolInitializer.class);

    /** {@inheritDoc} */
    public void init() throws InitializationException {
        final BasicParserPool pp = new BasicParserPool();
        pp.setMaxPoolSize(50);
        
        // Start with a clone of the default pool features
        // Mostly importantly this includes the existing features for hardening against known
        // security issues.
        final HashMap<String, Boolean> features = new HashMap<>(pp.getBuilderFeatures());
        
        // Add decryption-specific feature.
        // Note: this feature config is necessary due to an unresolved Xerces deferred DOM issue/bug
        features.put("http://apache.org/xml/features/dom/defer-node-expansion", Boolean.FALSE);
        
        pp.setBuilderFeatures(features);
        
        try {
            pp.initialize();
        } catch (final ComponentInitializationException e) {
            throw new InitializationException("Error initializing parser pool", e);
        }
        
        ConfigurationService.register(DecryptionParserPool.class, new DecryptionParserPool(pp));
    }

}
