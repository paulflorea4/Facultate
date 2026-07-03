package org.example.ducksocialnetworkui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.ducksocialnetworkui.domain.TipRata;
import org.example.ducksocialnetworkui.domain.User;
import org.example.ducksocialnetworkui.service.UserService;

import java.time.LocalDate;
import java.util.Optional;

public class EditUsersController {
    private UserService userService;

    @FXML private CheckBox checkBoxPerson;
    @FXML private CheckBox checkBoxDuck;
    @FXML private Label Info1;
    @FXML private Label Info2;
    @FXML private Label Info3;
    @FXML private Label Info4;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> duckType;
    @FXML private TextField textUsername;
    @FXML private TextField textEmail;
    @FXML private TextField textParola;
    @FXML private TextField textInfo1;
    @FXML private TextField textInfo2;
    @FXML private TextField textInfo3;
    @FXML private TextField textInfo4;
    @FXML private TextField idText;

    public void init(UserService userService) {
        this.userService = userService;
    }

    @FXML
    public void initialize() {
        duckType.getItems().addAll("FLYING","SWIMMING","FLYING_AND_SWIMMING");
        duckType.setVisible(false);
        datePicker.setValue(LocalDate.now());
        datePicker.setVisible(false);
        checkBoxPerson.setSelected(false);
        checkBoxDuck.setSelected(false);
        Info1.setVisible(false);
        Info2.setVisible(false);
        Info3.setVisible(false);
        Info4.setVisible(false);
        textInfo1.setVisible(false);
        textInfo2.setVisible(false);
        textInfo3.setVisible(false);
        textInfo4.setVisible(false);

        checkBoxPerson.setOnAction(event -> {
            if (checkBoxPerson.isSelected()) {
                setInfoPersoane();
            }
            else if(!checkBoxDuck.isSelected()) {
                setInfoNonVisible();
            }
        });

        checkBoxDuck.setOnAction(event -> {
            if (checkBoxDuck.isSelected()) {
                setInfoDucks();
            }else if(!checkBoxPerson.isSelected()) {
                setInfoNonVisible();
            }
        });
    }

    @FXML
    public void onSave(){
        if(checkBoxPerson.isSelected()) {
            savePerson();
        }else if(checkBoxDuck.isSelected()) {
            saveDuck();
        }
        else{
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Date invalide","Selectati tipul de user pe care doriti sa il adaugati!");
        }
    }

    private void savePerson(){
        String username = textUsername.getText();
        String email = textEmail.getText();
        String parola = textParola.getText();
        String nume=textInfo1.getText();
        String prenume=textInfo2.getText();
        String ocupatie=textInfo3.getText();
        LocalDate dataNasterii = datePicker.getValue();
        double nivelEmpatie;
        try {
            nivelEmpatie = Double.parseDouble(textInfo4.getText());
        } catch (NumberFormatException e) {
            MessageAlert.showErrorMessage(null,"Introduceti un numar real pentru nivelul empatiei!");
            return;
        }

        try{
            Optional<User> savedPerson=this.userService.addPersoana(username,email,parola,nume,prenume,dataNasterii,ocupatie,nivelEmpatie);
            if(savedPerson.isPresent()){
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Persoana adaugata","Persoana a fost adaugata cu succes!");
                clearFields();
            }
        }catch (Exception e){
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    private void saveDuck(){
        String username = textUsername.getText();
        String email = textEmail.getText();
        String parola = textParola.getText();
        TipRata tip = switch (duckType.getSelectionModel().getSelectedIndex()) {
            case 0 -> TipRata.FLYING;
            case 1 -> TipRata.SWIMMING;
            case 2 -> TipRata.FLYING_AND_SWIMMING;
            default -> {
                MessageAlert.showErrorMessage(null, "Nu ati selectat tipul ratei!");
                yield null;
            }
        };

        if(tip==null){
            return;
        }

        double viteza,rezistenta;
        try {
            viteza = Double.parseDouble(textInfo1.getText());
            rezistenta = Double.parseDouble(textInfo2.getText());
        } catch (NumberFormatException e) {
            MessageAlert.showErrorMessage(null,"Introduceti un numar real pentru viteza si rezistenta!");
            return;
        }

        long cardId;
        try {
            cardId = Long.parseLong(textInfo3.getText());
        } catch (NumberFormatException e) {
            MessageAlert.showErrorMessage(null,"Introduceti un numar natural pentru card!");
            return;
        }

        try{
            Optional<User> savedDuck=this.userService.addDuck(username,email,parola,tip,viteza,rezistenta,cardId);
            if(savedDuck.isPresent()){
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Rata adaugata","Rata a fost adaugata cu succes!");
                clearFields();
            }
        }catch (Exception e){
            MessageAlert.showErrorMessage(null, e.getMessage());
        }

    }

    @FXML
    public void onDelete(){
        try{
            long id=Long.parseLong(idText.getText());
            Optional<User> deletedUser=userService.remove(id);
            if(deletedUser.isPresent()){
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Utilizator sters","Utilizatorul "+deletedUser.get().getUsername()+" a fost sters cu succes!");
            }
            else{
                MessageAlert.showErrorMessage(null, "Nu exista utilizator cu id "+id);
            }
            clearFields();
        }catch (Exception e){
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    private void setInfoPersoane(){
        checkBoxDuck.setSelected(false);
        Info1.setVisible(true);
        Info1.setText("Nume:");
        Info2.setVisible(true);
        Info2.setText("Prenume:");
        Info3.setVisible(true);
        Info3.setText("Ocupatie:");
        Info4.setVisible(true);
        Info4.setText("Nivel empatie:");
        datePicker.setVisible(true);
        duckType.setVisible(false);
        textInfo1.setVisible(true);
        textInfo2.setVisible(true);
        textInfo3.setVisible(true);
        textInfo4.setVisible(true);
    }

    private void setInfoDucks(){
        checkBoxPerson.setSelected(false);
        Info1.setVisible(true);
        Info1.setText("Viteza:");
        Info2.setVisible(true);
        Info2.setText("Rezistenta:");
        Info3.setVisible(true);
        Info3.setText("Id-ul card:");
        Info4.setVisible(false);
        Info4.setText("");
        duckType.setVisible(true);
        datePicker.setVisible(false);
        textInfo1.setVisible(true);
        textInfo2.setVisible(true);
        textInfo3.setVisible(true);
        textInfo4.setVisible(false);
    }

    private void setInfoNonVisible() {
        textInfo1.setVisible(false);
        textInfo2.setVisible(false);
        textInfo3.setVisible(false);
        textInfo4.setVisible(false);
        Info1.setVisible(false);
        Info2.setVisible(false);
        Info3.setVisible(false);
        Info4.setVisible(false);
        duckType.setVisible(false);
        datePicker.setVisible(false);
    }

    private void clearFields(){
        textUsername.setText("");
        textEmail.setText("");
        textParola.setText("");
        textInfo1.setText("");
        textInfo2.setText("");
        textInfo3.setText("");
        textInfo4.setText("");
        idText.setText("");
    }
}
