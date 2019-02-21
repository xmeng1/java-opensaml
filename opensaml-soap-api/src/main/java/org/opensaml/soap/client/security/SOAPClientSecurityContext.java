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

package org.opensaml.soap.client.security;

import javax.annotation.Nullable;

import org.opensaml.messaging.context.BaseContext;

/**
 * Context class for holding security information related to SOAP client operations.
 */
public final class SOAPClientSecurityContext extends BaseContext {
    
    /** Security configuration profile ID. */
    @Nullable private String securityConfigurationProfileId;

    /**
     * Get the security configuration profile ID.
     * 
     * @return the profile ID, or null
     */
    @Nullable public String getSecurityConfigurationProfileId() {
        return securityConfigurationProfileId;
    }

    /**
     * Set the security configuration profile ID.
     * 
     * @param profileId The profileID to set
     */
    public void setSecurityConfigurationProfileId(@Nullable final String profileId) {
        securityConfigurationProfileId = profileId;
    }

}
