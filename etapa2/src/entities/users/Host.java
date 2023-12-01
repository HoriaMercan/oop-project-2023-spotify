package entities.users;

import entities.audioCollections.Podcast;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Host extends AbstractUser {
    private final List<Podcast> podcasts = new ArrayList<>();
    public Host(String username, String city, Integer age) {
        super(username, city, age);
        this.userType = UserType.HOST;
    }
}
