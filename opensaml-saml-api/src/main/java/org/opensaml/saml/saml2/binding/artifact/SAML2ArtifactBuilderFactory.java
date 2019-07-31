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

package org.opensaml.saml.saml2.binding.artifact;

import java.util.Map;

import net.shibboleth.utilities.java.support.codec.Base64Support;
import net.shibboleth.utilities.java.support.collection.LazyMap;

/**
 * Factory used to construct SAML 2 artifact builders.
 */
public class SAML2ArtifactBuilderFactory {

    /** Registered artifact builders. */
    private Map<String, SAML2ArtifactBuilder<?>> artifactBuilders;

    /** Constructor. */
    public SAML2ArtifactBuilderFactory() {
        artifactBuilders = new LazyMap<>();
        artifactBuilders.put(new String(SAML2ArtifactType0004.TYPE_CODE), new SAML2ArtifactType0004Builder());
    }

    /**
     * Gets the currently registered artifact builders.
     * 
     * @return currently registered artifact builders
     */
    public Map<String, SAML2ArtifactBuilder<?>> getArtifactBuilders() {
        return artifactBuilders;
    }

    /**
     * Gets the artifact builder for the given type.
     * 
     * @param type type of artifact to be built
     * 
     * @return artifact builder for the given type
     */
    public SAML2ArtifactBuilder<?> getArtifactBuilder(final byte[] type) {
        return artifactBuilders.get(new String(type));
    }
    
    /**
     * Convenience method for getting an artifact builder and parsing the given Base64 encoded artifact with it.
     * 
     * @param base64Artifact Base64 encoded artifact to parse
     * 
     * @return constructed artifact
     */
    public SAML2Artifact buildArtifact(final String base64Artifact){
        return buildArtifact(Base64Support.decode(base64Artifact));
    }

    /**
     * convenience method for getting an artifact builder and parsing the given artifact with it.
     * 
     * @param artifact artifact to parse
     * 
     * @return constructed artifact
     */
    public SAML2Artifact buildArtifact(final byte[] artifact) {
        if(artifact == null){
            return null;
        }
        
        final byte[] type = new byte[2];
        type[0] = artifact[0];
        type[1] = artifact[1];

        final SAML2ArtifactBuilder<?> artifactBuilder = getArtifactBuilder(type);
        if (artifactBuilder == null) {
            throw new IllegalArgumentException("Saw unsupported artifact type: " + new String(type));
        }
        return artifactBuilder.buildArtifact(artifact);
    }
}