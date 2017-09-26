package request.writeread;

import request.Request;

public final class WriteReadRequestFactory {

    private WriteReadRequestFactory() {
    }

    public static Request of() {
        throw new UnsupportedOperationException();
    }

    private static Request spi() {
        throw new UnsupportedOperationException();
    }

    private static Request i2c() {
        throw new UnsupportedOperationException();
    }
}
