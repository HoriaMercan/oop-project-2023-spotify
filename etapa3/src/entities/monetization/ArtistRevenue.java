package entities.monetization;

import entities.audioFiles.Song;
import entities.helpers.Merch;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Class designed to maintain the evidence for an artist revenue
 */
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

    private static final Double SUTA = 100.0;

    /**
     * Function called to update the data for output by getting out the most profitable
     * song at the calling moment
     */
    public void updateMostProfitableSong() {
        if (revenuePerSong.isEmpty()) {
            return;
        }

        boolean notZero = true;
        Map<String, Double> mapSongNameRevenue = new HashMap<>();
        for (Entry<Song, Double> entry : revenuePerSong.entrySet()) {
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
                    if (comp != 0) {
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

        songRevenue = Math.round(songRevenue * SUTA) / SUTA;
    }

    /**
     * @param song    Song from which the artist get monetized
     * @param revenue the value of monetization
     */
    public void addRevenueFromSong(final Song song, final Double revenue) {
        revenuePerSong.compute(song, (key, val) -> (val == null) ? revenue : val + revenue);
        songRevenue += revenue;
    }

    /**
     * @param merch Merch to be monetized because somebody bought it
     */
    public void addMerchRevenue(final Merch merch) {
        this.merchRevenue += 1.0 * merch.getPrice();
    }
}
