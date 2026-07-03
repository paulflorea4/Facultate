package org.example.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.models.User;
import org.example.observer.EntityChangeEvent;
import org.example.observer.Observable;
import org.example.observer.Observer;
import org.example.repos.UserRepository;
import org.example.repos.dtos.Page;
import org.example.repos.dtos.Pageable;
import org.example.validators.UserValidator;
import org.example.validators.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserService implements Observable<EntityChangeEvent<User>> {
    private final UserRepository repo;
    private final UserValidator validator;
    private List<Observer<EntityChangeEvent<User>>> observers = new ArrayList<>();

    public Optional<User> findById(int id) {
        return repo.findById(id);
    }

    public Iterable<User> getAll() {
        return repo.getAll();
    }

    public Optional<User> save(User e) throws ValidationException {
        validator.validate(e);
        var user = repo.save(e);

        user.ifPresent(u -> notifyObservers(new EntityChangeEvent<>(EntityChangeEvent.ChangeType.ADD, u)));

        return user;
    }

    public Optional<User> update(User e) throws ValidationException {
        validator.validate(e);
        var user = repo.update(e);

        user.ifPresent(u -> notifyObservers(new EntityChangeEvent<>(EntityChangeEvent.ChangeType.UPDATE, u)));

        return user;
    }

    public Optional<User> delete(int id) {
        var user = repo.delete(id);

        user.ifPresent(u -> notifyObservers(new EntityChangeEvent<>(EntityChangeEvent.ChangeType.DELETE, u)));

        return user;
    }

    public Page<User> getAllPage(Pageable page) {
        return repo.getAllPage(page);
    }

    @Override
    public void addObserver(Observer<EntityChangeEvent<User>> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<EntityChangeEvent<User>> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(EntityChangeEvent<User> item) {
        observers.forEach(o -> o.update(item));
    }
}
