package io.seruco.encoding.base62;

import java.io.ByteArrayOutputStream;

/**
 * A Base62 encoder/decoder.
 *
 * @author Sebastian Ruhleder, sebastian@seruco.io
 */
public class Base62 {

    private static final int STANDARD_BASE = 256;

    private static final int TARGET_BASE = 62;

    private final byte[] alphabet;

    private byte[] lookup;

    private Base62(final byte[] alphabet) {
        this.alphabet = alphabet;
        createLookupTable();
    }

    /**
     * Creates a {@link Base62} instance. Defaults to the GMP-style character set.
     *
     * @return a {@link Base62} instance.
     */
    public static Base62 createInstance() {
        return createInstanceWithGmpCharacterSet();
    }

    /**
     * Creates a {@link Base62} instance using the GMP-style character set.
     *
     * @return a {@link Base62} instance.
     */
    public static Base62 createInstanceWithGmpCharacterSet() {
        return new Base62(CharacterSets.GMP);
    }

    /**
     * Creates a {@link Base62} instance using the inverted character set.
     *
     * @return a {@link Base62} instance.
     */
    public static Base62 createInstanceWithInvertedCharacterSet() {
        return new Base62(CharacterSets.INVERTED);
    }

    /**
     * Encodes a sequence of bytes in Base62 encoding.
     *
     * @param message a byte sequence.
     * @return a sequence of Base62-encoded bytes.
     */
    public byte[] encode(final byte[] message) {
        final byte[] indices = convert(message, STANDARD_BASE, TARGET_BASE);

        return translate(indices, alphabet);
    }

    /**
     * Decodes a sequence of Base62-encoded bytes.
     *
     * @param encoded a sequence of Base62-encoded bytes.
     * @return a byte sequence.
     */
    public byte[] decode(final byte[] encoded) {
        final byte[] prepared = translate(encoded, lookup);

        return convert(prepared, TARGET_BASE, STANDARD_BASE);
    }

    /**
     * Uses the elements of a byte array as indices to a dictionary and returns the corresponding values
     * in form of a byte array.
     */
    private byte[] translate(final byte[] indices, final byte[] dictionary) {
        final byte[] translation = new byte[indices.length];

        for (int i = 0; i < indices.length; i++) {
            translation[i] = dictionary[indices[i]];
        }

        return translation;
    }

    /**
     * Converts a byte array from a source base to a target base using the alphabet.
     */
    private byte[] convert(final byte[] message, final int sourceBase, final int targetBase) {
        /**
         * This algorithm is inspired by: http://codegolf.stackexchange.com/a/21672
         */
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        byte[] source = message;

        while (source.length > 0) {
            final ByteArrayOutputStream quotient = new ByteArrayOutputStream();

            int remainder = 0;

            for (int i = 0; i < source.length; i++) {
                final int accumulator = (source[i] & 0xFF) + remainder * sourceBase;
                final int digit = (accumulator - (accumulator % targetBase)) / targetBase;

                remainder = accumulator % targetBase;

                if (quotient.size() > 0 || digit > 0) {
                    quotient.write(digit);
                }
            }

            out.write(remainder);

            source = quotient.toByteArray();
        }

        for (int i = 0; i < message.length - 1 && message[i] == 0; i++) {
            out.write(0);
        }

        return reverse(out.toByteArray());
    }

    /**
     * Reverses a byte array.
     */
    private byte[] reverse(final byte[] arr) {
        final int length = arr.length;

        final byte[] reversed = new byte[length];

        for (int i = 0; i < length; i++) {
            reversed[length - i - 1] = arr[i];
        }

        return reversed;
    }

    /**
     * Creates the lookup table from character to index of character in character set.
     */
    private void createLookupTable() {
        lookup = new byte[256];

        for (int i = 0; i < alphabet.length; i++) {
            lookup[alphabet[i]] = (byte) (i & 0xFF);
        }
    }

    private static class CharacterSets {

        private static final byte[] GMP = {
                (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7',
                (byte) '8', (byte) '9', (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F',
                (byte) 'G', (byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L', (byte) 'M', (byte) 'N',
                (byte) 'O', (byte) 'P', (byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V',
                (byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd',
                (byte) 'e', (byte) 'f', (byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k', (byte) 'l',
                (byte) 'm', (byte) 'n', (byte) 'o', (byte) 'p', (byte) 'q', (byte) 'r', (byte) 's', (byte) 't',
                (byte) 'u', (byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z'
        };

        private static final byte[] INVERTED = {
                (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7',
                (byte) '8', (byte) '9', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f',
                (byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n',
                (byte) 'o', (byte) 'p', (byte) 'q', (byte) 'r', (byte) 's', (byte) 't', (byte) 'u', (byte) 'v',
                (byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z', (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D',
                (byte) 'E', (byte) 'F', (byte) 'G', (byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L',
                (byte) 'M', (byte) 'N', (byte) 'O', (byte) 'P', (byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T',
                (byte) 'U', (byte) 'V', (byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z'
        };

    }

}
