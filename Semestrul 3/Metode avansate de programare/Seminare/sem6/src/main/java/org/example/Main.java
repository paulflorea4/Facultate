package org.example;

import org.example.models.User;
import org.example.repos.Repository;
import org.example.repos.UserRepository;

import java.time.LocalDateTime;

public class Main {
    static void main() {
        String url = "jdbc:postgresql://localhost:5432/s06-map";
        String username = "postgres";
        String password = "root";

        Repository<Integer, User> userRepo = new UserRepository<>(url, username, password);

        userRepo.getAll().forEach(System.out::println);
    }
}
