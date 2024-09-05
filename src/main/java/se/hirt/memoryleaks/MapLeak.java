package se.hirt.memoryleaks;

import java.util.HashMap;
import java.util.Map;

public class MapLeak {
    private Map<Integer, String> leakMap = new HashMap<>();
    private int currentPosition;
    private int leakSpeed;
    private int batchSize;

    public MapLeak(int leakSpeed, int batchSize) {
        this.leakSpeed = leakSpeed;
        this.batchSize = batchSize;
        if (leakSpeed > batchSize) {
            throw new IllegalArgumentException("Leak speed is greater than batch size");
        }
    }

    /**
     * Simulating a leak by putting a number of entries into a map, but then not removing all of them.
     */
    public void runBatch() {
        int startPos = currentPosition;
        for (int i = 0; i < batchSize; i++) {
            leakMap.put(currentPosition++, "This is a map leak");
        }
        for (int i = 0; i < batchSize - leakSpeed; i++) {
            leakMap.remove(startPos++);
        }
    }

    public void reset() {
        leakMap.clear();
    }
}
