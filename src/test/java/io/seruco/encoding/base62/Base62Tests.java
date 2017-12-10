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
                createZeroesByteArray(512),
                createPseudoRandomByteArray(0xAB, 40),
                createPseudoRandomByteArray(0x1C, 40),
                createPseudoRandomByteArray(0xF2, 40)
        };

        for (byte[] message : inputs) {
            for (Base62 encoder : encoders) {
                final byte[] encoded = encoder.encode(message);
                final byte[] decoded = encoder.decode(encoded);

                Assertions.assertArrayEquals(message, decoded);
            }
        }
    }

    @Test
    @DisplayName("should be able to handle empty inputs")
    public void emptyInputs() {
        final byte[] empty = new byte[0];

        for (Base62 encoder : encoders) {
            final byte[] encoded = encoder.encode(empty);
            Assertions.assertArrayEquals(empty, encoded);

            final byte[] decoded = encoder.decode(empty);
            Assertions.assertArrayEquals(empty, decoded);
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

    private byte[] createPseudoRandomByteArray(int seed, int size) {
        final byte[] arr = new byte[size];
        int state = seed;
        for (int i = 0; i < size; i += 4) {
            state = xorshift(state);
            for (int j = 0; j < 4 && i + j < size; j++) {
                arr[i + j] = (byte) ((state >> j) & 0xFF);
            }
        }
        return arr;
    }

    private int xorshift(int x) {
        x ^= (x << 13);
        x ^= (x >> 17);
        x ^= (x << 5);
        return x;
    }
}
