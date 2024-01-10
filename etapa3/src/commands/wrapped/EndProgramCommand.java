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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.util.function.Predicate;


/**
 * Class designed like a command to be called once at the end of a sequence of other commands
 */
@Getter
public final class EndProgramCommand {

    /**
     * @return output like format specific for commandOutputs
     */
    public EndProgramOutput executeCommand() {
        EndProgramOutput output = new EndProgramOutput();

        Map<String, ArtistRevenue> map = new HashMap<>();

        for (User user : MyDatabase.getInstance().getUsers()) {
            user.getPayment().payToArtists();
        }
        Predicate<Artist> filterArtists = artist ->
                ((ArtistWrapperStatistics) artist.getWrapperStatistics())
                        .wasEverListened() || (artist.getRevenue().getMerchRevenue() > 0.0);
        MyDatabase.getInstance().getArtists().stream()
                .filter(filterArtists)
                .toList()
                .forEach(artist -> map.put(artist.getUsername(), artist.getRevenue()));

        List<Entry<String, ArtistRevenue>> array =
                new ArrayList<>(map.entrySet().stream().toList());

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
        private Map<String, ArtistRevenue> result = null;

        public EndProgramOutput() {
        }
    }
}
