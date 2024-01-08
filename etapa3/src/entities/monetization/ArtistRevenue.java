package entities.monetization;

import entities.audioFiles.Song;
import entities.helpers.Merch;
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

        boolean notZero = true;
        Map<String, Double> mapSongNameRevenue = new HashMap<>();
        for (Entry<Song, Double> entry: revenuePerSong.entrySet()) {
            String songName = entry.getKey().getName();
            Double value = entry.getValue();
            if (value > 0.0) {
                notZero = false;
            }
            mapSongNameRevenue.compute(songName, (k, v) -> v == null ? value : v + value);
        }

        List<Entry<String, Double>> arrayOfEntries =
                new ArrayList<>(mapSongNameRevenue.entrySet().stream().toList());

        Collections.sort(arrayOfEntries,
                (songDoubleEntry, t1) -> {
                    int comp = -Double.compare(songDoubleEntry.getValue(), t1.getValue());
                    if (comp != 0){
                        return comp;
                    }

                    return songDoubleEntry.getKey().compareTo(t1.getKey());
                }

        );

        String greatestRevenueSong = arrayOfEntries.get(0).getKey();
        if (notZero) {
            mostProfitableSong = "N/A";
        } else {
            mostProfitableSong = greatestRevenueSong;
        }
        /***
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

         ***/
        songRevenue = Math.round(songRevenue * 100) / 100.0;
    }

    public void addRevenueFromSong(Song song, Double revenue) {
        revenuePerSong.compute(song, (key, val) -> (val == null) ? revenue: val + revenue);
        songRevenue += revenue;
    }

    public void addMerchRevenue(Merch merch) {
        this.merchRevenue += 1.0 * merch.getPrice();
    }
}
