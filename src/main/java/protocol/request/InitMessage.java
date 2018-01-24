package protocol.request;

import board.test.BoardManager;
import protocol.Feature;
import util.Unix;

import java.util.List;

public class InitMessage implements Request {

    private final BoardManager manager;

    public InitMessage(BoardManager manager) {
        this.manager = manager;
    }

    @Override
    public final String getFormattedResponse() {
        List<Feature> fs = Unix.getAppFeatures();
        StringBuilder builder = new StringBuilder(StringConstants.INIT_PREFIX)
                .append(manager.getBoardName())
                .append(StringConstants.REQ_SEPARATOR)
                .append(fs.remove(0));
        for (Feature f : fs) {
            builder.append(' ').append(f.toString());
        }
        return builder.toString();
    }

    @Override
    public void action() {
        //NO-OP
    }
}
