package board.test;

import io.silverspoon.bulldog.core.pin.Pin;
import io.silverspoon.bulldog.core.platform.AbstractBoard;

public final class TestingBoard extends AbstractBoard {

    private static final int NUM_PINS = 10;
    private static final String NAME_FORMATTER = "T_%02d";

    public TestingBoard() {
        createPins();
        createI2c();
        createSpi();
    }

    @Override
    public String getName() {
        return "Testing board";
    }

    private void createPins() {
        for (int i = 1; i <= NUM_PINS; i++) {
            String name = String.format(NAME_FORMATTER, i);
            Pin p = new Pin(name, i, Integer.toString(i), i);
            p.addFeature(new TestingDigitalIoFeature(p,
                    new TestingDigitalInput(p), new TestingDigitalOutput(p)));
            getPins().add(p);
        }
    }

    private void createI2c() {
        getI2cBuses().add(new TestingI2cBus("i2c-1"));
    }

    private void createSpi() {
        getSpiBuses().add(new TestingSpiBus("spi-1"));
    }
}
