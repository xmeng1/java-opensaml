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

package org.opensaml.messaging.pipeline;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.shibboleth.utilities.java.support.logic.Constraint;

import org.opensaml.messaging.decoder.MessageDecoder;
import org.opensaml.messaging.encoder.MessageEncoder;
import org.opensaml.messaging.handler.MessageHandler;

/**
 * Basic implementation of {@link MessagePipeline}.
 */
public class BasicMessagePipeline 
        implements MessagePipeline {
    
    /** Message encoder. */
    private MessageEncoder encoder;
    
    /** Message decoder. */
    private MessageDecoder decoder;
    
    /** Outbound payload message handler. */
    private MessageHandler outboundPayloadHandler;
    
    /** Outbound transport message handler. */
    private MessageHandler outboundTransportHandler;
    
    /** Inbound message handler. */
    private MessageHandler inboundHandler;
    
    /**
     * Constructor.
     *
     * @param newEncoder the message encoder instance
     * @param newDecoder the message decoder instance
     */
    public BasicMessagePipeline(@Nonnull final MessageEncoder newEncoder, 
            @Nonnull final MessageDecoder newDecoder) {
        setEncoder(newEncoder);
        setDecoder(newDecoder);
    }

    /** {@inheritDoc} */
    public MessageEncoder getEncoder() {
        return encoder;
    }
    
    /**
     * Set the message encoder instance.
     * 
     * @param newEncoder the new message encoder
     */
    protected void setEncoder(@Nonnull final MessageEncoder newEncoder) {
       encoder = Constraint.isNotNull(newEncoder, "MessageEncoder can not be null") ;
    }

    /** {@inheritDoc} */
    public MessageDecoder getDecoder() {
        return decoder;
    }
    
    /**
     * Set the message decoder instance.
     * 
     * @param newDecoder the new message decoder
     */
    protected void setDecoder(@Nonnull final MessageDecoder newDecoder) {
       decoder = Constraint.isNotNull(newDecoder, "MessageDecoder can not be null");
    }


    /** {@inheritDoc} */
    public MessageHandler getOutboundPayloadMessageHandler() {
        return outboundPayloadHandler;
    }

    /**
     * Set the outbound payload message handler.
     * 
     * @param handler the new handler
     */
    public void setOutboundPayloadHandler(@Nullable final MessageHandler handler) {
        outboundPayloadHandler = handler;
    }

    /** {@inheritDoc} */
    public MessageHandler getOutboundTransportMessageHandler() {
        return outboundTransportHandler;
    }

    /**
     * Set the outbound transport message handler.
     * 
     * @param handler the new handler
     */
    public void setOutboundTransportHandler(final MessageHandler handler) {
        outboundTransportHandler = handler;
    }

    /** {@inheritDoc} */
    public MessageHandler getInboundMessageHandler() {
        return inboundHandler;
    }
    
    /**
     * Set the inbound message handler.
     * 
     * @param handler the new handler
     */
    public void setInboundHandler(final MessageHandler handler) {
        inboundHandler = handler;
    }

}
