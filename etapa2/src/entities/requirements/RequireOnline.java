package entities.requirements;

import databases.MyDatabase;
import entities.users.User;

/**
 * This interface represent the requirement of an user being online for executing
 * specific commands
 */
public interface RequireOnline {
    /**
     * @param user
     * @return verify if a user is online
     */
    default boolean isUserOnline(User user) {
        return user.isOnline();
    }

    /**
     * @param username
     * @return verify is a user \w given name is online or not and returns the
     * desired messages
     */
    default String isUserOnline(String username) {
        User user = (MyDatabase.getInstance().findUserByUsername(username));
        if (user == null) {
            return username + " doesn't exists.";
        }
        if (!user.isOnline()) {
            return username + " is offline.";
        }
        return "";
    }
}
