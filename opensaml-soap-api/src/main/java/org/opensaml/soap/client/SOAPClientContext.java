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

package org.opensaml.soap.client;

import javax.annotation.Nullable;

import org.opensaml.messaging.context.BaseContext;
import org.opensaml.soap.client.SOAPClient.SOAPRequestParameters;
import org.opensaml.soap.client.http.PipelineFactoryHttpSOAPClient;

import net.shibboleth.utilities.java.support.primitive.StringSupport;

/** Message context for SOAP client messages. */
public class SOAPClientContext extends BaseContext {

    /** Binding/transport-specific SOAP request parameters. */
    @Nullable private SOAPRequestParameters requestParameters;
    
    /** Name of the specific SOAP client pipeline to use, for example with {@link PipelineFactoryHttpSOAPClient}. */
    @Nullable private String pipelineName;
    
    /** The destination URI for the SOAP message being sent. */
    @Nullable private String destinationURI;

    /**
     * Gets a set of binding/transport-specific request parameters.
     *
     * @return set of binding/transport-specific request parameters
     */
    @Nullable public SOAPRequestParameters getSOAPRequestParameters() {
        return requestParameters;
    }

    /**
     * Sets a set of binding/transport-specific request parameters.
     *
     * @param parameters a set of binding/transport-specific request parameters
     */
    public void setSOAPRequestParameters(@Nullable final SOAPRequestParameters parameters) {
        requestParameters = parameters;
    }

    /**
     * Get the name of the specific SOAP client message pipeline to use, 
     * for example with {@link PipelineFactoryHttpSOAPClient}. 
     * 
     * @return the pipeline name, or null
     */
    @Nullable public String getPipelineName() {
        return pipelineName;
    }

    /**
     * Set the name of the specific SOAP client message pipeline to use, 
     * for example with {@link PipelineFactoryHttpSOAPClient}. 
     * 
     * @param name the pipeline name, or null
     */
    public void setPipelineName(@Nullable final String name) {
        pipelineName = StringSupport.trimOrNull(name);
    }
    
    /**
     * Get the the destination URI for the SOAP message being sent.
     * 
     * @return the destination URI, or null
     */
    @Nullable public String getDestinationURI() {
        return destinationURI;
    }

    /**
     * Set the destination URI for the SOAP message being sent.
     * 
     * @param uri the destination URI, or null
     */
    public void setDestinationURI(@Nullable final String uri) {
        destinationURI = StringSupport.trimOrNull(uri);
    }

}