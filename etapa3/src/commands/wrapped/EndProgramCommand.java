package commands.wrapped;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import commands.AbstractCommand;
import databases.MyDatabase;
import entities.monetization.ArtistRevenue;
import entities.users.Artist;
import entities.users.User;
import entities.wrapper.statistics.ArtistWrapperStatistics;
import lombok.Getter;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
public class EndProgramCommand {

    public EndProgramOutput executeCommand() {
        EndProgramOutput output = new EndProgramOutput();

        Map<String, ArtistRevenue> map = new HashMap<>();

        for (User user: MyDatabase.getInstance().getUsers()) {
            user.getPayment().payToArtists();
        }
        Predicate<Artist> filterArtists = new Predicate<Artist>() {
            @Override
            public boolean test(Artist artist) {
                return ((ArtistWrapperStatistics) artist.getWrapperStatistics())
                        .wasEverListened() || (artist.getRevenue().getMerchRevenue() > 0.0);
            }
        };
        MyDatabase.getInstance().getArtists().stream()
                .filter(filterArtists)
                .toList()
                .forEach(artist -> map.put(artist.getUsername(), artist.getRevenue()));

        List<Entry<String, ArtistRevenue>> array = new ArrayList<>(map.entrySet().stream().toList());

        array.sort((stringArtistRevenueEntry, t1) -> {
            ArtistRevenue a1 = stringArtistRevenueEntry.getValue();
            ArtistRevenue a2 = t1.getValue();
            int comp = -Double.compare(a1.getSongRevenue() + a1.getMerchRevenue(),
                    a2.getMerchRevenue() + a2.getSongRevenue());
            if (comp != 0) {
                return comp;
            }

            return stringArtistRevenueEntry.getKey().compareTo(t1.getKey());
        });
        int integer = 1;
        for (Entry<String, ArtistRevenue> entry : array) {
            entry.getValue().setRanking(integer++);
            entry.getValue().updateMostProfitableSong();
        }

        output.result = new LinkedHashMap<>();

        array.forEach(entry -> output.result.put(entry.getKey(), entry.getValue()));


        return output;
    }

    public static final class EndProgramOutput extends AbstractCommand.CommandOutput {
        @JsonInclude(Include.NON_NULL)
        private final String message = null;

        @JsonInclude(Include.NON_NULL)
        private final String user = null;

        @JsonInclude(Include.NON_NULL)
        private final Integer timestamp = null;

        @Getter
        @JsonInclude(Include.NON_NULL)
        Map<String, ArtistRevenue> result = null;

        public EndProgramOutput() {
        }
    }
}
