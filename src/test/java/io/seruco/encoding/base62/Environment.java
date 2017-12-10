package io.seruco.encoding.base62;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    public static final byte[][] getRawInputs() {
        return new byte[][]{
                createIncreasingByteArray(),
                createZeroesByteArray(512),
                createPseudoRandomByteArray(0xAB, 40),
                createPseudoRandomByteArray(0x1C, 40),
                createPseudoRandomByteArray(0xF2, 40)
        };
    }

    public static Map<String, String> getNaiveTestSet() {
        final Map<String, String> testSet = new HashMap<String, String>();

        testSet.put("", "");
        testSet.put("Hello", "5TP3P3v");
        testSet.put("0123456789", "18XU2xYejWO9d3");
        testSet.put("The quick brown fox jumps over the lazy dog", "83UM8dOjD4xrzASgmqLOXTgTagvV1jPegUJ39mcYnwHwTlzpdfKXvpp4RL");
        testSet.put("Sphinx of black quartz, judge my vow", "1Ul5yQGNM8YFBp3sz19dYj1kTp95OW7jI8pTcTP5JhYjIaFmx");

        return testSet;
    }

    private static byte[] createIncreasingByteArray() {
        final byte[] arr = new byte[256];
        for (int i = 0; i < 256; i++) {
            arr[i] = (byte) (i & 0xFF);
        }
        return arr;
    }

    private static byte[] createZeroesByteArray(int size) {
        return new byte[size];
    }

    private static byte[] createPseudoRandomByteArray(int seed, int size) {
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

    private static int xorshift(int x) {
        x ^= (x << 13);
        x ^= (x >> 17);
        x ^= (x << 5);
        return x;
    }
}
