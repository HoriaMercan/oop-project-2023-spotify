package entities.monetization;

import entities.audioFiles.Song;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiFunction;


public final class ArtistRevenue {
    @Getter
    @Setter
    private double merchRevenue = 0.0;

    @Getter
    @Setter
    private double songRevenue = 0.0;

    @Getter
    @Setter
    private int ranking = 0;

    @Getter
    private String mostProfitableSong = "N/A";

    private Map<Song, Double> revenuePerSong = new HashMap<>();

    public void updateMostProfitableSong() {
        if (revenuePerSong.isEmpty())
            return;

        List<Entry<Song, Double>> arrayOfEntries =
                new ArrayList<>(revenuePerSong.entrySet().stream().toList());

        Collections.sort(arrayOfEntries,
                (songDoubleEntry, t1) -> {
                    int comp = -Double.compare(songDoubleEntry.getValue(), t1.getValue());
                    if (comp != 0){
                        return comp;
                    }

                    return songDoubleEntry.getKey().getName().compareTo(t1.getKey().getName());
                }

        );
        Song greatestRevenueSong = arrayOfEntries.get(0).getKey();

        if (revenuePerSong.get(greatestRevenueSong) == 0.0) {
            mostProfitableSong = "N/A";
        } else {
            mostProfitableSong = greatestRevenueSong.getName();
        }

        songRevenue = Math.round(songRevenue * 100) / 100.0;
    }

    public void addRevenueFromSong(Song song, Double revenue) {
        revenuePerSong.compute(song, (key, val) -> (val == null) ? revenue: val + revenue);
        songRevenue += revenue;
    }
}
