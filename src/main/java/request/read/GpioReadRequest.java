/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package request.read;
import io.silverspoon.bulldog.core.pin.Pin;
import java.io.IOException;
import net.ProtocolManager;
import request.IllegalRequestException;
import request.manager.GpioManager;

/**
 *
 * @author Miloslav Zezulka, 2017
 */
public class GpioReadRequest implements ReadRequest {
    private final Pin pin;
    
    public GpioReadRequest(Pin pin) {
        this.pin = pin;
    }

    /**
     * Attempts to read input denoted in getInstance() method and if successful, 
     * returns the pin numeric value as a result. String containing number is
     * returned due to interface which this class implements. 
     * @return String numeric representation of the read signal
     */
    @Override
    public String read() {
        try {
            return GpioManager.getInstance().read(this.pin.getName());
        } catch (IllegalRequestException ex) {
            return "-1";
        }
    }

    @Override
    public void giveFeedbackToClient() throws IOException {
        int status = Integer.parseInt(read()); 
        String voltageLvl = "N/A";
        if(status == 0) {
            voltageLvl = "LOW";
        }
        if(status == 1) {
            voltageLvl = "HIGH";
        }
        ProtocolManager.getInstance().setMessageToSend(String.format(
                "Pin '%s' is '%s' and its voltage level is %s", this.pin.getName(), 
                status == -1 ? "not available" : "available", voltageLvl));
    }
}
