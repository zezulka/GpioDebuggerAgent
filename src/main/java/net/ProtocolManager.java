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

import request.manager.InterfaceManager;

/**
 * Responsibilities: provide utility methods which are going to enable request
 * processing.
 *
 * @author Miloslav Zezulka, 2017
 */
public final class ProtocolManager {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(ProtocolManager.class);
    private final Function<Interface, InterfaceManager> converter;

    public ProtocolManager(Function<Interface, InterfaceManager> converter) {
        this.converter = converter;
    }

    /**
     * Takes message which was read from the input stream and tries to parse it.
     * If the String representing the current request is not valid,
     * IllegalRequestException is thrown. In case request is valid,
     * request is performed and appropriate response is sent back to client.
     *
     * @throws IllegalRequestException if request is not valid
     * @throws IOException
     */
    public void parseRequest(String receivedMessage)
            throws IllegalRequestException, IOException {
        LOGGER.info(ProtocolMessages.REQUEST_CAPTURED + " " + receivedMessage);
        Request req = RequestParser.parse(converter, receivedMessage);
        performRequest(req);
    }

    private void performRequest(Request request) throws IOException {
        LOGGER.info(String.format("Request of type %s has "
                + "been submitted", request.getClass()));
        request.performRequest();
        ConnectionManager.setMessageToSend(request.getFormattedResponse());
    }
}

