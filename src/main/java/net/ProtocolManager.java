/*
 * Copyright 2017 Miloslav.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net;

import java.io.IOException;
import request.Interface;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import request.IllegalRequestException;
import request.Request;
import request.RequestParser;
import request.interrupt.InterruptListenerRequest;
import request.read.ReadRequest;
import request.write.WriteRequest;

import request.manager.InterfaceManager;

/**
 * Responsibilities: provide utility methods which are going to
 * enable request processing.
 *
 * @author Miloslav Zezulka, 2017
 */
public class ProtocolManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProtocolManager.class);
    private final Function<Interface, InterfaceManager> converter;

    private ProtocolManager(Function<Interface, InterfaceManager> converter) {
        this.converter = converter;
    }    
    
    public static ProtocolManager getInstance(Function<Interface, InterfaceManager> converter) {
        return new ProtocolManager(converter);
    }

    /**
     * Takes message which was read from the input stream and tries to parse it.
     * If the String representing the current request is not valid, IllegalRequestException
     * is thrown. In case request is valid, ProtocolManager.performRequest is invoked.
     * @throws IllegalRequestException if request is not valid
     * @throws IOException
     */
    public void parseRequest(String receivedMessage) throws IllegalRequestException, IOException {
        LOGGER.info(ProtocolMessages.S_REQUEST_CAPTURED + " " +receivedMessage);
        Request req = RequestParser.parse(this.converter, receivedMessage);
        performRequest(req);
        LOGGER.info(ProtocolMessages.S_REQUEST_OK.toString());
    }

    /**
     * This method takes care of determining which kind of request was sent to
     * server and then calls the appropriate method in the given interface
     * (please consult {@code request.read.ReadRequest} and
     * {@code request.write.WriteRequest} for more information.
     *
     * @param request
     * @throws java.io.IOException
     */
    private void performRequest(Request request) throws IOException {
        LOGGER.info(String.format("Request of type %s has "
                    + "been submitted",request.getClass()));
        if (request instanceof ReadRequest) {
            ReadRequest req = (ReadRequest) request;
            LOGGER.info(String.format(", the result was=%s",request.getClass(), req.read()));
        } else if (request instanceof WriteRequest) {
            WriteRequest req = (WriteRequest) request;
            req.write();
        } else if (request instanceof InterruptListenerRequest) {
            InterruptListenerRequest req = (InterruptListenerRequest) request;
            req.handleInterruptRequest();
        }
        request.giveFeedbackToClient();
    }
}
