package entities.users;

import commands.AbstractCommand;
import lombok.Getter;

@Getter
public class AbstractUser {
    protected String username;
    protected String city;
    protected Integer age;
    protected UserType userType;

    public AbstractUser() {}

    public AbstractUser(final String username, final String city, final Integer age) {
        this.username = username;
        this.city = city;
        this.age = age;
    }

    public enum UserType {
        NORMAL, ARTIST, HOST
    }
}
