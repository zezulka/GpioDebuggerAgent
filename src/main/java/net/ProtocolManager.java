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
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import request.IllegalRequestException;
import request.Request;
import request.RequestParser;
import request.read.ReadRequest;
import request.write.WriteRequest;

/**
 * Responsibilities: provide static utility methods which are going to perform
 * read/write operations and enable request processing.
 *
 * @author Miloslav Zezulka, 2017
 */
public class ProtocolManager {

    private static final Logger protocolManagerLogger = LoggerFactory.getLogger(ProtocolManager.class);
    private static final AgentConnectionManager ACM = AgentConnectionManager.getManagerWithDefaultPort();
    private static String receivedMessage = null;
    private static String messageToSend = null;

    private ProtocolManager() {
    }

    /**
     * Enables Request manager to set appropriate response which will be sent
     * back to the client.
     *
     * @param messageToSend message to write to buffer
     * @throws IllegalArgumentException if {@code messageToSend} is null
     */
    public static void setMessageToSend(String messageToSend) {
        if (messageToSend == null) {
            throw new IllegalArgumentException("msg cannot be null");
        }
        ProtocolManager.messageToSend = messageToSend;
    }

    public static void setReceivedMessage(String message) {
        receivedMessage = message;
    }

    public static String getReceivedMessage() {
        return receivedMessage;
    }

    public static String getMessageToSend() {
        return messageToSend;
    }

    private static void resetMessageToSend() {
        messageToSend = null;
    }

    /**
     * Performs the actual write operation. Reads data stored in internal buffer
     * and attempts to send them to the output stream.
     *
     * @param key
     */
    static void write(SelectionKey key) throws IOException {
        protocolManagerLogger.info(ProtocolMessages.S_CLIENT_FEEDBACK.toString());
        ACM.getSocketChannel().write(ByteBuffer.wrap(messageToSend.getBytes()));
        key.interestOps(SelectionKey.OP_READ);
        resetMessageToSend();
    }

    /**
     * Reads data stored in internal buffer.
     *
     * @param key
     * @return String representation of data received. Data sent to the server
     * should be a sequence of characters convertible into String.
     * @throws IOException
     */
    static int read(SelectionKey key) throws IOException {
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        readBuffer.clear();
        int read = ACM.getSocketChannel().read(readBuffer);
        if (read == -1) {
            protocolManagerLogger.info(ProtocolMessages.S_NOTHING_TO_READ.toString());
            ACM.getSocketChannel().close();
            key.cancel();
            return read;
        }
        readBuffer.flip();
        byte[] data = new byte[1024];
        readBuffer.get(data, 0, read);
        String msg = new String(data).replaceAll("\0", "");
        setReceivedMessage(msg);
        ACM.getSocketChannel().register(ACM.getSelector(), SelectionKey.OP_WRITE);
        return 0;
    }

    static void processRequest() throws IllegalRequestException, IOException {
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
     */
    static void handleRequest(Request request) throws IOException {
        if (request instanceof ReadRequest) {
            ReadRequest req = (ReadRequest) request;
            req.read();

        } else if (request instanceof WriteRequest) {
            WriteRequest req = (WriteRequest) request;
            req.write();
        }
        request.giveFeedbackToClient();
    }
}
