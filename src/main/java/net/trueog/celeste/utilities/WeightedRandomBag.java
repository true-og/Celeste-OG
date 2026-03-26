package net.trueog.celeste.utilities;
// From https://gamedev.stackexchange.com/a/162987

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightedRandomBag<T extends Object> {

    private class Entry {

        double accumulatedWeight;
        T object;

    }

    public List<Entry> entries = new ArrayList<>();
    private double accumulatedWeight;
    private Random rand = new SecureRandom();

    public void addEntry(T object, double weight) {

        accumulatedWeight += weight;

        final Entry e = new Entry();
        e.object = object;
        e.accumulatedWeight = accumulatedWeight;

        entries.add(e);

    }

    public T getRandom() {

        final double r = rand.nextDouble() * accumulatedWeight;
        for (Entry entry : entries) {

            if (entry.accumulatedWeight >= r) {

                return entry.object;

            }

        }

        // Should only happen when there are no entries.
        return null;

    }

}