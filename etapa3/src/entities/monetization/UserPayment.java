package entities.monetization;

import databases.MyDatabase;
import entities.audioFiles.Song;
import entities.helpers.Merch;
import entities.users.Artist;
import entities.users.User;
import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class UserPayment {
    private final User self;

    public UserPayment(User user) {
        this.self = user;
    }

    public enum AccountType {
        PREMIUM,
        ADS_ENJOYED
    }
    private List<Song> songsToBePayed = new ArrayList<>();

    @Getter
    private List<Merch> boughtMerch = new ArrayList<>();

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
//        totalMoney = 0.0;
    }

    public void payToArtists(Double money) {
        totalMoney = money;
        payToArtists();
    }

    public void changeAccountType() {

        if (accountType.equals(AccountType.PREMIUM)) {
            payToArtists();
            songsToBePayed = MyDatabase.getInstance().getUnpayedSongs()
                    .getOrDefault(self, new ArrayList<>());
        } else {
            MyDatabase.getInstance().getUnpayedSongs().put(self, new ArrayList<>(songsToBePayed));
            songsToBePayed = new ArrayList<>();
        }
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

    public void buyMerch(Artist artist, Merch merch) {
        this.boughtMerch.add(merch);

        artist.getRevenue().addMerchRevenue(merch);
    }

}
