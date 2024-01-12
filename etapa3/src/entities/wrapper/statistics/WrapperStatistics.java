package entities.wrapper.statistics;

import entities.audioCollections.Album;
import entities.audioFiles.AudioFile;
import entities.wrapper.OneListen;
import entities.wrapper.handlers.AbstractDataWrapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * class used to wrap statistics of users to be sent to output
 */
public abstract class WrapperStatistics {

    protected static final Integer LIMIT = 5;
    protected static final BiFunction<AudioFile, Integer, ? extends Integer> UPDATER =
            (key, val) -> (val == null) ? 1 : val + 1;

    protected static final BiFunction<String, Integer, ? extends Integer> STRING_UPDATER =
            (key, val) -> (val == null) ? 1 : val + 1;

    protected static final BiFunction<Album, Integer, ? extends Integer> ALBUM_UPDATER =
            (key, val) -> (val == null) ? 1 : val + 1;

    /**
     * @param map  map
     * @param func function
     * @param <K>  type
     * @return a List of strings that represent the highest values in the map
     */
    protected static <K> List<String> transformToFormatList(
            final Map<K, Integer> map, final Function<K, String> func) {

        List<Entry<K, Integer>> array = new ArrayList<>(map.entrySet());
        Collections.sort(array, (kIntegerEntry, t1) -> {
            if ((int) kIntegerEntry.getValue() != t1.getValue()) {
                return -Integer.compare(kIntegerEntry.getValue(), t1.getValue());
            }
            return func.apply(kIntegerEntry.getKey()).compareTo(func.apply(t1.getKey()));
        });

        return array.stream().limit(LIMIT).map(k -> func.apply(k.getKey()))
                .collect(Collectors.toList());

    }

    /**
     * @param oldMap map
     * @param func   function
     * @param <K>    type
     * @return a map of strings to ints that represent the highest values in the map
     */
    protected static <K> Map<String, Integer> transformToFormat(
            final Map<K, Integer> oldMap, final Function<K, String> func) {

        Map<String, Integer> map = new HashMap<>();

        for (Entry<K, Integer> entry : oldMap.entrySet()) {
            K key = entry.getKey();
            Integer value = entry.getValue();

            map.compute(func.apply(key), (k, v) -> v == null ? value : value + v);
        }

        List<Entry<String, Integer>> array = new ArrayList<>(map.entrySet());
        Collections.sort(array, (kIntegerEntry, t1) -> {
            if ((int) kIntegerEntry.getValue() != t1.getValue()) {
                return -Integer.compare(kIntegerEntry.getValue(), t1.getValue());
            }
            return kIntegerEntry.getKey().compareTo(t1.getKey());
        });

        array = array.stream().limit(LIMIT).collect(Collectors.toList());

        LinkedHashMap<String, Integer> answer = new LinkedHashMap<>();

        for (Entry<String, Integer> kIntegerEntry : array) {
            answer.put(kIntegerEntry.getKey(), kIntegerEntry.getValue());
        }

        return answer;
    }

    /**
     * @return DataWrapping
     */
    public abstract AbstractDataWrapping getDataWrapping();

    /**
     * @param listen adds a listen to be counted
     */
    public abstract void addOneListen(OneListen listen);
}
