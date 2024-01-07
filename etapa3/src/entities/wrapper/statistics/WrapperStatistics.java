package entities.wrapper.statistics;

import entities.audioCollections.Album;
import entities.audioFiles.AudioFile;
import entities.wrapper.OneListen;
import entities.wrapper.handlers.AbstractDataWrapping;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class WrapperStatistics {

    protected static final Integer LIMIT = 5;
    protected final static BiFunction<AudioFile, Integer, ? extends Integer> updater =
            (key, val) -> (val == null) ? 1 : val + 1;

    protected final static BiFunction<String, Integer, ? extends Integer> stringUpdater =
            (key, val) -> (val == null) ? 1 : val + 1;

    protected final static BiFunction<Album, Integer, ? extends Integer> albumUpdater =
            (key, val) -> (val == null) ? 1 : val + 1;

    protected static <K> List<String> transformToFormatList(
            Map<K, Integer> map, Function<K, String> func) {

        List<Entry<K, Integer>> array = new ArrayList<>(map.entrySet());
        Collections.sort(array, (kIntegerEntry, t1) -> {
            if ((int) kIntegerEntry.getValue() != t1.getValue()) {
                return -Integer.compare(kIntegerEntry.getValue(), t1.getValue());
            }
            return func.apply(kIntegerEntry.getKey()).compareTo(func.apply(t1.getKey()));
        });

        return array.stream().limit(LIMIT).map(k->func.apply(k.getKey())).collect(Collectors.toList());

    }

    protected static <K> Map<String, Integer> transformToFormat(
            Map<K, Integer> oldMap, Function<K, String> func) {

        Map<String, Integer> map = new HashMap<>();

        for (Entry<K, Integer> entry: oldMap.entrySet()) {
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

    public AbstractDataWrapping getDataWrapping() {
        return null;
    }

    public void addOneListen(OneListen listen) {
    }
}
