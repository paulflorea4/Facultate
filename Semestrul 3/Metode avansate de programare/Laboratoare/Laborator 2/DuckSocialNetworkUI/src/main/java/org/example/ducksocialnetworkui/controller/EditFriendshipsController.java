package org.example.ducksocialnetworkui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.ducksocialnetworkui.domain.Friendship;
import org.example.ducksocialnetworkui.event.EntityChangeEvent;
import org.example.ducksocialnetworkui.event.EntityChangeEventType;
import org.example.ducksocialnetworkui.observer.Observer;
import org.example.ducksocialnetworkui.service.FriendshipService;
import org.example.ducksocialnetworkui.utils.paging.Pageable;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EditFriendshipsController implements Observer<EntityChangeEvent<Friendship>> {
    private FriendshipService friendshipService;
    private ObservableList<Friendship> model= FXCollections.observableArrayList();

    @FXML
    private TableView<Friendship> tableView;
    @FXML private TableColumn<Friendship,Long> idColumn1;
    @FXML private TableColumn<Friendship,Long> idColumn2;
    @FXML private Button buttonPrevious;
    @FXML private Button buttonNext;
    @FXML private Label labelPage;
    @FXML private TextField textFieldId1;
    @FXML private TextField textFieldId2;

    private int pageSize = 5;
    private int currentPage = 0;
    private int totalNumberOfElements = 0;

    public void init(FriendshipService friendshipService){
        this.friendshipService = friendshipService;
        initModel();
        friendshipService.addObserver(this);
    }

    @FXML
    public void initialize() {
        idColumn1.setCellValueFactory(new PropertyValueFactory<>("userId1"));
        idColumn2.setCellValueFactory(new PropertyValueFactory<>("userId2"));

        tableView.setItems(model);
    }

    private void initModel() {
        var page = friendshipService.getAllPage(new Pageable(currentPage,pageSize));
        int maxPage = (int)Math.ceil(1.0*page.getTotalNumberOfElements()/pageSize)-1;
        if(maxPage==-1){
            maxPage=0;
        }
        if(currentPage > maxPage){
            currentPage = maxPage;
            page=friendshipService.getAllPage(new Pageable(currentPage,pageSize));
        }

        totalNumberOfElements=page.getTotalNumberOfElements();
        buttonPrevious.setDisable(currentPage == 0);
        buttonNext.setDisable((currentPage+1)*pageSize == totalNumberOfElements);

        var friendships = StreamSupport.stream(page.getElementsOnPage().spliterator(), false)
                .collect(Collectors.toList());

        model.setAll(friendships);
        labelPage.setText("Page "+(currentPage+1)+ " of " + (maxPage+1));
    }

    @Override
    public void update(EntityChangeEvent<Friendship> event) {
        if(event.getChangeType() == EntityChangeEventType.ADD || event.getChangeType() == EntityChangeEventType.UPDATE || event.getChangeType() == EntityChangeEventType.DELETE){
            currentPage=0;
            initModel();
        }
    }

    public void onNextPage(ActionEvent actionEvent) {
        currentPage ++;
        initModel();
    }

    public void onPreviousPage(ActionEvent actionEvent) {
        currentPage --;
        initModel();
    }

    @FXML
    public void addFriendShip(){
        long id1,id2;
        try {
            id1 = Long.parseLong(textFieldId1.getText());
            id2 = Long.parseLong(textFieldId2.getText());
            friendshipService.addFriendship(id1,id2);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Prietenie adaugata","Prietenie adaugata cu succes!");
            clearTextFields();
        } catch (NumberFormatException e) {
            MessageAlert.showErrorMessage(null,"Nu ati introdus corect datele pentru id-uri");
        } catch (Exception e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
    }

    @FXML
    public void deleteFriendShip() {
        long id1,id2;
        try {
            id1 = Long.parseLong(textFieldId1.getText());
            id2 = Long.parseLong(textFieldId2.getText());
            friendshipService.removeFriendship(id1,id2);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Prietenie stearsa","Prietenie stearsa cu succes!");
            clearTextFields();
        } catch (NumberFormatException e) {
            MessageAlert.showErrorMessage(null,"Nu ati introdus corect datele pentru id-uri");
        } catch (Exception e){
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
    }

    private void clearTextFields(){
        textFieldId1.setText("");
        textFieldId2.setText("");
    }


}
