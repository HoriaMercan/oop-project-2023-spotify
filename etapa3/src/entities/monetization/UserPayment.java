package entities.monetization;

import databases.MyDatabase;
import entities.audioFiles.Song;
import entities.users.Artist;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.A;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.List;

public class UserPayment {

    public enum AccountType {
        PREMIUM,
        ADS_ENJOYED
    }
    private List<Song> songsToBePayed = new ArrayList<>();

    @Getter
    AccountType accountType = AccountType.ADS_ENJOYED;

    @Setter
    private Double totalMoney = 0.0;

    public void payToArtists() {
        if (songsToBePayed.isEmpty())
            return;

        Double eachPayment = totalMoney / songsToBePayed.size();

        for (Song song : songsToBePayed)  {
            Artist artist = MyDatabase.getInstance()
                    .findArtistByUsername(song.getArtist());

            if (artist == null)
                return;

            artist.getRevenue().addRevenueFromSong(song, eachPayment);

        }

        songsToBePayed = new ArrayList<>();
        totalMoney = 0.0;
    }

    public void payToArtists(Double money) {
        totalMoney = money;
        payToArtists();
    }

    public void changeAccountType() {
        payToArtists();
        if (accountType == AccountType.ADS_ENJOYED) {
            totalMoney = 1000000.0;
            accountType = AccountType.PREMIUM;
        } else {
            totalMoney = 0.0;
            accountType = AccountType.ADS_ENJOYED;
        }
    }

    public void addSong(Song song) {
        songsToBePayed.add(song);
    }

}
