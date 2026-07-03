package org.example.template;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.template.domain.Car;
import org.example.template.domain.Rol;
import org.example.template.domain.User;
import org.example.template.service.Service;

public class ControllerCarDetails {
    public Label carDetails;
    public TextField commentsField;
    public Button send;
    private Service srv;
    private Car car;
    private User user;

    public void setService(Service srv, Car car,User user) {
        this.srv = srv;
        this.car = car;
        this.user = user;
        initModel();
    }

    private void initModel() {
        carDetails.setText(car.toString());
    }

    public void onSend(){
        srv.sendToApproval(car,commentsField.getText());
    }

}
