package io.seruco.encoding.base62;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Base62")
public class Base62Test {

    private final Base62 standardEncoder = Base62.createInstance();

    private final Base62[] encoders = {
            Base62.createInstanceWithGmpCharacterSet(),
            Base62.createInstanceWithInvertedCharacterSet()
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

    @Test
    @DisplayName("should throw exception when input is not encoded correctly")
    public void wrongEncoding() {
        for (final byte[] input : Environment.getWrongEncoding()) {
            assertThrows(IllegalArgumentException.class, new Executable() {
                @Override
                public void execute() throws Throwable {
                    standardEncoder.decode(input);
                }
            });
        }
    }

    @Test
    @DisplayName("should throw exception when input is null when decoding")
    public void decodeNull() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                standardEncoder.decode(null);
            }
        });
    }

    @Test
    @DisplayName("should check encoding correctly")
    public void checkEncoding() {
        assertTrue(standardEncoder.isBase62Encoding("0123456789".getBytes()));
        assertTrue(standardEncoder.isBase62Encoding("abcdefghijklmnopqrstuvwxzy".getBytes()));
        assertTrue(standardEncoder.isBase62Encoding("ABCDEFGHIJKLMNOPQRSTUVWXZY".getBytes()));

        assertFalse(standardEncoder.isBase62Encoding("!".getBytes()));
        assertFalse(standardEncoder.isBase62Encoding("@".getBytes()));
        assertFalse(standardEncoder.isBase62Encoding("<>".getBytes()));
        assertFalse(standardEncoder.isBase62Encoding("abcd%".getBytes()));
        assertFalse(standardEncoder.isBase62Encoding("😱".getBytes()));
    }

    private String encode(final String input) {
        return new String(standardEncoder.encode(input.getBytes()));
    }

    private boolean isAlphaNumeric(final String str) {
        return str.matches("^[a-zA-Z0-9]+$");
    }
}
