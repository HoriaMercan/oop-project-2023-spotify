package entities.requirements;

import databases.MyDatabase;
import entities.users.User;

public interface RequireOnline {
    default boolean isUserOnline(User user) {
        return user.isOnline();
    }
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
