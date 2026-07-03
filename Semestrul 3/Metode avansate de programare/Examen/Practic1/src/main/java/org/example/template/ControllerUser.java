package org.example.template;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.template.domain.*;
import org.example.template.observer.Observer;
import org.example.template.observer.events.EntityChangeEvent;
import org.example.template.observer.events.EntityChangeEventType;
import org.example.template.service.Service;

import java.io.IOException;
import java.util.List;

public class ControllerUser implements Observer {
    public Label notifications;
    public Button aproveBtn;
    public Button rejectBtn;
    public Button details;
    private Service srv;

    @FXML
    TableView<Car> tableView;

    @FXML
    TableColumn<Car, String> nameColumn;
    @FXML
    TableColumn<Car, String> descColumn;
    @FXML
    TableColumn<Car, String> pretColumn;
    @FXML
    TableColumn<Car, Status> statusColumn;

    @FXML
    Label rolLabel;

    private final ObservableList<Car> model = FXCollections.observableArrayList();
    User user;

    public void setService(Service srv, User user) {
        this.srv = srv;
        this.user = user;
        srv.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        pretColumn.setCellValueFactory(new PropertyValueFactory<>("pret"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableView.setItems(model);
    }


    private void initModel(){
        rolLabel.setText(this.user.getRol().toString());
        if(user.getRol()== Rol.DEALER){
            List<Car> cars=srv.getCars();model.setAll(cars);
            aproveBtn.setVisible(false);
            rejectBtn.setVisible(false);
        }else{
            List<Car> cars=srv.getCarsThatNeedApproval();
            model.setAll(cars);
            details.setVisible(false);
        }
    }

    public void onDetails() throws IOException {
        if(tableView.getSelectionModel().getSelectedItem() != null){
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("car-details-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            ControllerCarDetails  controller = fxmlLoader.getController();
            controller.setService(srv,tableView.getSelectionModel().getSelectedItem(),user);
            stage.setTitle("Car Details");
            stage.setScene(scene);
            stage.show();
        }
    }

    public void onAccept(){
        if(tableView.getSelectionModel().getSelectedItem() != null){
            srv.acceptCar(tableView.getSelectionModel().getSelectedItem());
        }
    }

    public void onReject(){
        if(tableView.getSelectionModel().getSelectedItem() != null){
            srv.rejectCar(tableView.getSelectionModel().getSelectedItem());
        }
    }

    @Override
    public void update(EntityChangeEvent event) {
        if(event.getType()== EntityChangeEventType.CAR_NEEDS_APPROVAL){
            initModel();
            if(user.getRol()== Rol.ADMIN){
                notifications.setText("Car needs approval.Comments: "+event.getData().toString());
            }
        }

        if(event.getType()== EntityChangeEventType.CAR_REJECTED || event.getType()== EntityChangeEventType.CAR_ACCEPTED ){
            initModel();
        }
    }
}
