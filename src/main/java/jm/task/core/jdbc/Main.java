package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();

        userService.saveUser("Alexey", "Petrov", (byte) 27);
        userService.saveUser("Olga", "Kim", (byte) 36);
        userService.saveUser("Andrey", "Arbenin", (byte) 41);
        userService.saveUser("Ekaterina", "Kostenko", (byte) 19);

        List<User> list = userService.getAllUsers();
        for (User user : list) {
            System.out.println(user);
        }

        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
