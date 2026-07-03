package org.example;

import org.example.config.Configuration;
import org.example.models.User;
import org.example.observer.EntityChangeEvent;
import org.example.observer.Observer;
import org.example.repos.Repository;
import org.example.repos.UserDbRepository;
import org.example.repos.UserRepository;
import org.example.repos.dtos.Pageable;
import org.example.services.UserService;
import org.example.validators.UserValidator;
import org.example.validators.ValidationException;

import java.time.LocalDateTime;

public class Main {
    static void main() throws ValidationException {
        var cfg = Configuration.getConfig();

        UserRepository userRepo = new UserDbRepository(
            cfg.getProperty("db.url"),
            cfg.getProperty("db.username"),
            cfg.getProperty("db.password")
        );
        UserValidator userValidator = new UserValidator();

        UserService service = new UserService(userRepo, userValidator);
        PrintObserver printObserver = new PrintObserver();
        service.addObserver(printObserver);

        var user = new User("username55315", LocalDateTime.now(), null, 1);
        user.setId(2);
        service.update(user);
        service.getAllPage(new Pageable(0, 2)).getPageElements().forEach(System.out::println);
    }
}

class PrintObserver implements Observer<EntityChangeEvent<User>> {
    @Override
    public void update(EntityChangeEvent<User> item) {
        System.out.println(item);
    }
}