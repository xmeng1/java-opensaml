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

package org.opensaml.messaging.pipeline.servlet;

import javax.annotation.Nonnull;

import org.opensaml.messaging.decoder.MessageDecoder;
import org.opensaml.messaging.decoder.servlet.HttpServletRequestMessageDecoder;
import org.opensaml.messaging.encoder.MessageEncoder;
import org.opensaml.messaging.encoder.servlet.HttpServletResponseMessageEncoder;
import org.opensaml.messaging.pipeline.BasicMessagePipeline;

/**
 * Basic implementation of {@link HttpServletMessagePipeline}.
 */
public class BasicHttpServletMessagePipeline extends BasicMessagePipeline implements HttpServletMessagePipeline{
    
    /**
     * Constructor.
     *
     * @param newEncoder the message encoder instance
     * @param newDecoder the message decoder instance
     */
    public BasicHttpServletMessagePipeline(@Nonnull final MessageEncoder newEncoder, 
            @Nonnull final MessageDecoder newDecoder) {
        super(newEncoder, newDecoder);
    }

    /** {@inheritDoc} */
    public HttpServletResponseMessageEncoder getEncoder() {
        return (HttpServletResponseMessageEncoder) super.getEncoder();
    }

    /** {@inheritDoc} */
    protected void setEncoder(final MessageEncoder encoder) {
        if (!(encoder instanceof HttpServletResponseMessageEncoder)) {
            throw new IllegalArgumentException("HttpServletResponseMessageEncoder is required");
        }
        super.setEncoder(encoder);
    }

    /** {@inheritDoc} */
    public HttpServletRequestMessageDecoder getDecoder() {
        return (HttpServletRequestMessageDecoder) super.getDecoder();
    }

    /** {@inheritDoc} */
    protected void setDecoder(final MessageDecoder decoder) {
        if (!(decoder instanceof HttpServletRequestMessageDecoder)) {
            throw new IllegalArgumentException("HttpServletRequestMessageDecoder is required");
        }
        super.setDecoder(decoder);
    }
    
    

}
