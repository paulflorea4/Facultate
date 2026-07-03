package org.example.template.service;

import org.example.template.domain.Car;
import org.example.template.domain.Status;
import org.example.template.domain.User;
import org.example.template.observer.Observable;
import org.example.template.observer.events.EntityChangeEvent;
import org.example.template.observer.events.EntityChangeEventType;
import org.example.template.repository.CarDBRepository;
import org.example.template.repository.UserDBRepository;

import java.util.ArrayList;
import java.util.List;

public class Service extends Observable {
    UserDBRepository userDBRepository;
    CarDBRepository carDBRepository;

    public Service(UserDBRepository userDBRepository,CarDBRepository carDBRepository) {
        this.userDBRepository = userDBRepository;
        this.carDBRepository = carDBRepository;
    }

    public User login(String username, String password)
    {
        return userDBRepository.findByUsernameAndPassword(username, password);
    }

    public List<Car> getCars()
    {
        return carDBRepository.findAll();
    }

    public List<Car> getCarsThatNeedApproval()
    {
        List<Car> cars = carDBRepository.findAll();
        List<Car> carsThatNeedApproval = new ArrayList<Car>();

        for(Car car : cars){
            if(car.getStatus()==Status.NEEDS_APPROVAL){
                carsThatNeedApproval.add(car);
            }
        }
        return  carsThatNeedApproval;
    }

    public void acceptCar(Car car)
    {
        carDBRepository.updateStatus(car.getID(),Status.APPROVED.toString());
        notifyObservers(new EntityChangeEvent(EntityChangeEventType.CAR_ACCEPTED,null));
    }

    public void rejectCar(Car car){
        carDBRepository.updateStatus(car.getID(),Status.REJECTED.toString());
        notifyObservers(new EntityChangeEvent(EntityChangeEventType.CAR_REJECTED,null));
    }


    public void sendToApproval(Car car,String comments){
        if(car.getStatus()==Status.NEW || car.getStatus()==Status.REJECTED){
            carDBRepository.updateStatus(car.getID(),Status.NEEDS_APPROVAL.toString());
            notifyObservers(new EntityChangeEvent(EntityChangeEventType.CAR_NEEDS_APPROVAL,comments));
        }
    }
}
