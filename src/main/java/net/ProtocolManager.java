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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import request.IllegalRequestException;
import request.Request;
import request.RequestParser;
import request.read.ReadRequest;
import request.write.WriteRequest;

/**
 * Responsibilities: provide utility methods which are going to 
 * enable request processing.
 *
 * @author Miloslav Zezulka, 2017
 */
public class ProtocolManager {

    private static final Logger protocolManagerLogger = LoggerFactory.getLogger(ProtocolManager.class);
    private static String receivedMessage = null;
    private static String messageToSend = null;

    private static final ProtocolManager INSTANCE = new ProtocolManager();

    private ProtocolManager() {
    }

    public static ProtocolManager getInstance() {
        return INSTANCE;
    }

    /**
     * Enables Request manager to set appropriate response which will be sent
     * back to the client.
     *
     * @param messageToSend message to write to buffer
     * @throws IllegalArgumentException if {@code messageToSend} is null
     */
    public void setMessageToSend(String messageToSend) {
        if (messageToSend == null) {
            throw new IllegalArgumentException("msg cannot be null");
        }
        ProtocolManager.messageToSend = messageToSend;
    }

    public void setReceivedMessage(String message) {
        receivedMessage = message;
    }

    public String getReceivedMessage() {
        return receivedMessage;
    }

    public String getMessageToSend() {
        return messageToSend;
    }
    /**
     * Sets message to send to null. This is to indicate that the previous
     * message has been processed.
     */
    public void resetMessageToSend() {
        messageToSend = null;
    }

    /**
     * Takes message which was read from the input stream and tries to parse it.
     * If the String representing the current request is not valid, IllegalRequestException
     * is thrown. In case request is valid, ProtocolManager.handleRequest is invoked.
     * @throws IllegalRequestException if request is not valid
     * @throws IOException
     */
    public void processRequest() throws IllegalRequestException, IOException {
        protocolManagerLogger.info(ProtocolMessages.S_REQUEST_CAPTURED + " " +receivedMessage);
        Request req = RequestParser.parse(receivedMessage);
        handleRequest(req);
        protocolManagerLogger.info(ProtocolMessages.S_REQUEST_OK.toString());
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
    public void handleRequest(Request request) throws IOException {
        if (request instanceof ReadRequest) {
            ReadRequest req = (ReadRequest) request;
            protocolManagerLogger.info(String.format("Request of type %s has "
                    + "been submitted, the result was=%s",request.getClass(), req.read()));
        } else if (request instanceof WriteRequest) {
            WriteRequest req = (WriteRequest) request;
            req.write();
        }
        request.giveFeedbackToClient();
    }
}
