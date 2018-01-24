package ru.lionzxy.fastlogblock.utils;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;

import java.util.function.Function;

public class CollectionUtils {

    public static <T> TIntObjectMap<T> toHashMap(final TIntSet set, final Function<Integer, T> mapper) {
        final TIntObjectMap<T> toReturn = new TIntObjectHashMap<>();

        set.forEach((i) -> {
            toReturn.put(i, mapper.apply(i));
            return true;
        });

        return toReturn;
    }
}
