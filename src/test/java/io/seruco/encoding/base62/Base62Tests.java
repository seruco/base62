package io.seruco.encoding.base62;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Base62")
public class Base62Tests {

    private final Base62 standardEncoder = Base62.createStandardEncoder();

    private final Base62 invertedEncoder = Base62.createInvertedEncoder();

    private final Base62[] encoders = {
            Base62.createStandardEncoder(),
            Base62.createInvertedEncoder()
    };

    @Test
    @DisplayName("should preserve identity of simple byte arrays")
    public void preservesIdentity() {
        final byte[][] inputs = {
                createIncreasingByteArray(),
                createZeroesByteArray(512)
        };

        for (byte[] message : inputs) {
            for (Base62 encoder : encoders) {
                final byte[] encoded = standardEncoder.encode(message);
                final byte[] decoded = standardEncoder.decode(encoded);

                Assertions.assertArrayEquals(message, decoded);
            }
        }
    }

    private byte[] createIncreasingByteArray() {
        final byte[] arr = new byte[256];
        for (int i = 0; i < 256; i++) {
            arr[i] = (byte) (i & 0xFF);
        }
        return arr;
    }

    private byte[] createZeroesByteArray(int size) {
        return new byte[size];
    }
}
