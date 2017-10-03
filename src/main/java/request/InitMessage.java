package request;

import board.manager.BoardManager;
import board.manager.TestingBoardManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.Util;

public class InitMessage implements Request {

    private final BoardManager manager;
    private final List<Feature> features = new ArrayList<>();

    public InitMessage(BoardManager manager) {
        this.manager = manager;
    }

    @Override
    public final String getFormattedResponse() {
        performRequest();
        StringBuilder builder
                = new StringBuilder(StringConstants.INIT_PREFIX);
        builder.append(manager.getBoardName())
                .append(StringConstants.REQ_SEPARATOR);
        for (Feature f : features) {
            builder.append(f.name()).append(' ');
        }
        return builder.toString();
    }

    @Override
    public void performRequest() {
        if (manager instanceof TestingBoardManager || Util.isUserRoot()) {
            features.addAll(Arrays.asList(Feature.values()));
        } else if (Util.isUserInGpioGroup()) {
            features.add(Feature.GPIO);
            features.add(Feature.INTERRUPTS);
        }
    }

    /**
     * For testing purposes only.
     */
    protected final List<Feature> getFeatures() {
        return features;
    }

    protected enum Feature {
        GPIO,
        INTERRUPTS,
        I2C,
        SPI;
    }

}
