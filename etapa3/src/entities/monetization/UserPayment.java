package entities.monetization;

import databases.MyDatabase;
import entities.audioFiles.Song;
import entities.helpers.Merch;
import entities.users.Artist;
import entities.users.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * class used to have an evidence for user payments
 */
public final class UserPayment {
    private final User self;
    private List<Song> songsToBePayed = new ArrayList<>();
    @Getter
    private List<Merch> boughtMerch = new ArrayList<>();
    @Getter
    private AccountType accountType = AccountType.ADS_ENJOYED;
    @Setter
    private Double totalMoney = 0.0;

    private static final Double PREMIUMMONEY = 1000000.0;
    public UserPayment(final User user) {
        this.self = user;
    }

    /**
     * function is used to send the monetization to the artists
     */
    public void payToArtists() {
        if (songsToBePayed.isEmpty()) {
            return;
        }

        Double eachPayment = totalMoney / songsToBePayed.size();

        for (Song song : songsToBePayed) {
            Artist artist = MyDatabase.getInstance()
                    .findArtistByUsername(song.getArtist());

            if (artist == null) {
                return;
            }

            artist.getRevenue().addRevenueFromSong(song, eachPayment);

        }

        songsToBePayed = new ArrayList<>();

    }

    /**
     * payToArtists but modify the money
     *
     * @param money value
     */
    public void payToArtists(final Double money) {
        totalMoney = money;
        payToArtists();
    }

    /**
     * Change the account type between Ads based and Premium
     */
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
            totalMoney = PREMIUMMONEY;
            accountType = AccountType.PREMIUM;
        } else {
            totalMoney = 0.0;
            accountType = AccountType.ADS_ENJOYED;
        }
    }

    /**
     * @param song Song added for being monetized later
     */
    public void addSong(final Song song) {
        songsToBePayed.add(song);
    }

    /**
     * @param artist Artist from which the user buy a merch
     * @param merch  the merch bought
     */
    public void buyMerch(final Artist artist, final Merch merch) {
        this.boughtMerch.add(merch);

        artist.getRevenue().addMerchRevenue(merch);
    }

    public enum AccountType {
        PREMIUM,
        ADS_ENJOYED
    }

}
