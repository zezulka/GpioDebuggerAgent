/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package request;

/**
 * This represents a Request on the agent's side. Please note that client does
 * not work with this request representation in any way.
 * @author Miloslav Zezulka, 2017 
 */
public interface Request {
    /**
     * After the request has been processed, information is sent back to client
     * (via {@code OutputStream} of the opened socket).
     */
    void giveFeedbackToClient();
}
