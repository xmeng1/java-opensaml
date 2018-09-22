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

package org.opensaml.messaging.handler.impl;

import javax.annotation.Nullable;

import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.handler.MessageHandlerException;
import org.opensaml.messaging.handler.impl.CheckMandatoryIssuer;
import org.testng.annotations.Test;

import com.google.common.base.Function;

import net.shibboleth.utilities.java.support.component.ComponentInitializationException;

/** Unit test for {@link CheckMandatoryIssuer}. */
public class CheckExpectedIssuerTest {

    @Test
    public void testMatch() throws Exception {
        final CheckExpectedIssuer action = new CheckExpectedIssuer();
        action.setIssuerLookupStrategy(new MockIssuer("issuer"));
        action.setExpectedIssuerLookupStrategy(new MockIssuer("issuer"));
        action.initialize();

        final MessageContext mc = new MessageContext();
        action.invoke(mc);
    }

    @Test(expectedExceptions=MessageHandlerException.class)
    public void testNotMatch() throws Exception {
        final CheckExpectedIssuer action = new CheckExpectedIssuer();
        action.setIssuerLookupStrategy(new MockIssuer("issuer"));
        action.setExpectedIssuerLookupStrategy(new MockIssuer("issuerNOT"));
        action.initialize();

        final MessageContext mc = new MessageContext();
        action.invoke(mc);
    }

    @Test(expectedExceptions=MessageHandlerException.class)
    public void testNoIssuer() throws Exception {
        final CheckExpectedIssuer action = new CheckExpectedIssuer();
        action.setIssuerLookupStrategy(new MockIssuer(null));
        action.setExpectedIssuerLookupStrategy(new MockIssuer("issuer"));
        action.initialize();

        final MessageContext mc = new MessageContext();
        action.invoke(mc);
    }
    
    @Test(expectedExceptions=MessageHandlerException.class)
    public void testNoExpectedIssuer() throws Exception {
        final CheckExpectedIssuer action = new CheckExpectedIssuer();
        action.setIssuerLookupStrategy(new MockIssuer("issuer"));
        action.setExpectedIssuerLookupStrategy(new MockIssuer("null"));
        action.initialize();

        final MessageContext mc = new MessageContext();
        action.invoke(mc);
    }
    
    @Test(expectedExceptions=ComponentInitializationException.class)
    public void testMissingIssuerStrategy() throws Exception {
        final CheckExpectedIssuer action = new CheckExpectedIssuer();
        action.setExpectedIssuerLookupStrategy(new MockIssuer("issuer"));
        action.initialize();
    }
    
    @Test(expectedExceptions=ComponentInitializationException.class)
    public void testMissingExpectedIssuerStrategy() throws Exception {
        final CheckExpectedIssuer action = new CheckExpectedIssuer();
        action.setIssuerLookupStrategy(new MockIssuer("issuer"));
        action.initialize();
    }
    
    private class MockIssuer implements Function<MessageContext,String> {

        final String issuer;
        
        MockIssuer(@Nullable final String s) {
            issuer = s;
        }
        
        /** {@inheritDoc} */
        public String apply(MessageContext input) {
            return issuer;
        }
        
    }
}