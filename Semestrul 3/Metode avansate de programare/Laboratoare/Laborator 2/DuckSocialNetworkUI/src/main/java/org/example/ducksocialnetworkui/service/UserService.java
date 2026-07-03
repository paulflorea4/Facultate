package org.example.ducksocialnetworkui.service;

import org.example.ducksocialnetworkui.domain.Duck;
import org.example.ducksocialnetworkui.domain.Persoana;
import org.example.ducksocialnetworkui.domain.TipRata;
import org.example.ducksocialnetworkui.domain.User;
import org.example.ducksocialnetworkui.dto.UserDto;
import org.example.ducksocialnetworkui.event.EntityChangeEvent;
import org.example.ducksocialnetworkui.event.EntityChangeEventType;
import org.example.ducksocialnetworkui.exception.ServiceException;
import org.example.ducksocialnetworkui.factory.Factory;
import org.example.ducksocialnetworkui.factory.UserFactory;
import org.example.ducksocialnetworkui.observer.Observable;
import org.example.ducksocialnetworkui.observer.Observer;
import org.example.ducksocialnetworkui.repository.UserRepository;
import org.example.ducksocialnetworkui.utils.paging.Page;
import org.example.ducksocialnetworkui.utils.paging.Pageable;
import org.example.ducksocialnetworkui.validation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService implements Observable<EntityChangeEvent<User>> {

    private final UserRepository userRepository;
    private final Validator<Persoana> persoanaValidator;
    private final Validator<Duck> duckValidator;
    private final List<Observer<EntityChangeEvent<User>>> observers=new ArrayList<>();

    private final Factory<User> userFactory=UserFactory.getInstance();

    public UserService(UserRepository userRepository,Validator<Persoana> persoanaValidator,Validator<Duck> duckValidator) {
        this.userRepository = userRepository;
        this.persoanaValidator = persoanaValidator;
        this.duckValidator = duckValidator;
    }

    public Optional<User> addPersoana(String username, String email, String password, String nume, String prenume, LocalDate dataNasterii, String ocupatie, double nivelEmpatie){
        User p = userFactory.create("persoana", null, username, email, password, nume, prenume, dataNasterii, ocupatie, nivelEmpatie);
        persoanaValidator.validate((Persoana) p);
        Optional<User> savedUser=userRepository.save(p);
        savedUser.ifPresent(m ->
                notifyObservers(new EntityChangeEvent<>(EntityChangeEventType.ADD,m)));
        return savedUser;
    }

    public Optional<User> addDuck(String username, String email, String password, TipRata tip,double viteza,double rezistenta,long cardId){
        User d = userFactory.create("duck", null, username, email, password, tip, viteza, rezistenta, cardId);
        duckValidator.validate((Duck) d);
        Optional<User> savedUser=userRepository.save(d);
        savedUser.ifPresent(m ->
                notifyObservers(new EntityChangeEvent<>(EntityChangeEventType.ADD,m)));
        return savedUser;
    }

    public Optional<User> remove(Long id){
        Optional<User> removedUser=userRepository.delete(id);
        removedUser.ifPresent(m ->
                notifyObservers(new EntityChangeEvent<>(EntityChangeEventType.DELETE, null, m)));
        return removedUser;
    }

    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> verifyCredentials(String username){
        return userRepository.findByUsername(username);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findOne(id);
    }

    public double getAverageSpeed(Long flockId){
        Iterable<User> users=getAllUsers();
        double speeds=0;
        int nr=0;
        for(User u:users){
            if(u instanceof Duck d && d.getCardId()==flockId){
                speeds+=d.getViteza();
                nr++;
            }
        }
        if(nr==0)
            throw new ServiceException("Nu exista rate in cardul cu id "+flockId);
        return speeds/nr;
    }

    public double getAverageStamina(Long flockId){
        Iterable<User> users=getAllUsers();
        double staminas=0;
        int nr=0;
        for(User u:users){
            if(u instanceof Duck d && d.getCardId()==flockId){
                staminas+=d.getRezistenta();
                nr++;
            }
        }
        if(nr==0)
            throw new ServiceException("Nu exista rate in cardul cu id "+flockId);
        return staminas/nr;
    }

    @Override
    public void addObserver(Observer<EntityChangeEvent<User>> o) { observers.add(o); }

    @Override
    public void removeObserver(Observer<EntityChangeEvent<User>> e) { observers.remove(e); }

    @Override
    public void notifyObservers(EntityChangeEvent<User> e) { observers.forEach(o -> o.update(e)); }

    public Page<User> getAllPage(Pageable page, UserDto filter){ return userRepository.getAllPage(page,filter); }

    public Page<User> findFriends(Pageable page,Long userId){ return userRepository.findFriends(page,userId); }
}
