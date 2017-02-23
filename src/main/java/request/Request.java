package request;

import java.io.Serializable;

/**
 * IMMUTABLE class representing request being sent to server.
 * @author Miloslav Zezulka, 2017
 */
public class Request implements Serializable{
    private final Interface interfaceType;
    //...
    
    private Request(Interface interfaceType) {
        this.interfaceType = interfaceType;
    }
    
}
