package io.seruco.encoding.base62;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

                assertArrayEquals(message, decoded);
            }
        }
    }

    @Test
    @DisplayName("should be able to handle empty inputs")
    public void emptyInputs() {
        final byte[] empty = new byte[0];

        for (Base62 encoder : encoders) {
            final byte[] encoded = encoder.encode(empty);
            assertArrayEquals(empty, encoded);

            final byte[] decoded = encoder.decode(empty);
            assertArrayEquals(empty, decoded);
        }
    }

    @Test
    @DisplayName("should behave correctly on naive test set")
    public void naiveTestSet() {
        for (Map.Entry<String, String> testSetEntry : createNaiveTestSet().entrySet()) {
            assertEquals(encode(testSetEntry.getKey()), testSetEntry.getValue());
        }
    }

    private Map<String, String> createNaiveTestSet() {
        final Map<String, String> testSet = new HashMap<String, String>();

        testSet.put("", "");
        testSet.put("Hello", "5TP3P3v");
        testSet.put("0123456789", "18XU2xYejWO9d3");
        testSet.put("The quick brown fox jumps over the lazy dog", "83UM8dOjD4xrzASgmqLOXTgTagvV1jPegUJ39mcYnwHwTlzpdfKXvpp4RL");
        testSet.put("Sphinx of black quartz, judge my vow", "1Ul5yQGNM8YFBp3sz19dYj1kTp95OW7jI8pTcTP5JhYjIaFmx");

        return testSet;
    }

    private String encode(final String input) {
        return new String(standardEncoder.encode(input.getBytes()));
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
