package io.seruco.encoding.base62;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Base62")
public class Base62Test {

    private final Base62 standardEncoder = Base62.createStandardEncoder();

    private final Base62[] encoders = {
            Base62.createStandardEncoder(),
            Base62.createInvertedEncoder()
    };

    @Test
    @DisplayName("should preserve identity of simple byte arrays")
    public void preservesIdentity() {
        for (byte[] message : Environment.getRawInputs()) {
            for (Base62 encoder : encoders) {
                final byte[] encoded = encoder.encode(message);
                final byte[] decoded = encoder.decode(encoded);

                assertArrayEquals(message, decoded);
            }
        }
    }

    @Test
    @DisplayName("should produce encodings that only contain alphanumeric characters")
    public void alphaNumericOutput() {
        for (byte[] message : Environment.getRawInputs()) {
            for (Base62 encoder : encoders) {
                final byte[] encoded = encoder.encode(message);
                final String encodedStr = new String(encoded);

                assertTrue(isAlphaNumeric(encodedStr));
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
        for (Map.Entry<String, String> testSetEntry : Environment.getNaiveTestSet().entrySet()) {
            assertEquals(encode(testSetEntry.getKey()), testSetEntry.getValue());
        }
    }

    private String encode(final String input) {
        return new String(standardEncoder.encode(input.getBytes()));
    }

    private boolean isAlphaNumeric(final String str) {
        return str.matches("^[a-zA-Z0-9]+$");
    }
}
