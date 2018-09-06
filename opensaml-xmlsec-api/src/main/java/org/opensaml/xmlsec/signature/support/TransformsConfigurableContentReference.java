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

package org.opensaml.xmlsec.signature.support;

import java.util.List;

/**
 * A specialization of {@link ContentReference} which allows signature transforms to be specified.
 * 
 * Note: This sub-interface was added in a minor update because we can not add new methods to 
 * {@link ConfigurableContentReference}. In a future major release we could collapse this change 
 * into that single interface.
 */
public interface TransformsConfigurableContentReference extends ContentReference {
    
    /**
     * Gets the mutable list of transforms applied to the content prior to digest generation.
     * 
     * @return the transforms applied to the content prior to digest generation
     */
    public List<String> getTransforms();
 
}