package org.example.laboratorGUI.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.laboratorGUI.domain.user.User;
import org.example.laboratorGUI.observer.Observer;
import org.example.laboratorGUI.service.FlockService;
import org.example.laboratorGUI.service.EventService;
import org.example.laboratorGUI.service.UserService;
import org.example.laboratorGUI.utils.GUI.ViewLoader;
import org.example.laboratorGUI.utils.MessageBox;
import org.example.laboratorGUI.utils.Page;
import org.example.laboratorGUI.utils.event.EntityChangeEvent;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AdminController implements Observer, CleanupController {
    @FXML private TableView<User> tableView;
    @FXML private TableColumn<User, Long> tableColumnID;
    @FXML private TableColumn<User, String> tableColumnUsername;
    @FXML private TableColumn<User, String> tableColumnEmail;
    @FXML private TableColumn<User, String> tableColumnType;
    @FXML private TableColumn<User, Void> tableColumnFriends;

    @FXML private Button prevBtn;
    @FXML private Button addUserBtn;
    @FXML private Button removeUserBtn;
    @FXML private Button nextBtn;

    @FXML private Button calcCommunitiesBtn;
    @FXML private Label communitiesLbl;
    @FXML private Button showMostSociableCommunityBtn;
    @FXML private Button showDucksBtn;

    @FXML private Button showEventsBtn;

    private UserService userSrv;
    private FlockService flockSrv;
    private EventService eventSrv;

    private int currentPage = 0;
    private int totalPages = 0;

    private final ObservableList<User> users = FXCollections.observableArrayList();

    public void initializeNecessaryServices(UserService userSrv, FlockService flockSrv, EventService eventSrv) {
        this.userSrv = userSrv;
        this.flockSrv = flockSrv;
        this.eventSrv = eventSrv;
        loadUsers();

        userSrv.addObserver(this);
    }

    @FXML private void initialize(){
        tableColumnID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        addShowFriendsButtonToTable();

        tableView.setItems(users);

        prevBtn.setOnAction(event -> onPrevious());
        nextBtn.setOnAction(event -> onNext());

        addUserBtn.setOnAction(event -> openAddUserWindow());
        removeUserBtn.setOnAction(event -> removeUser());

        calcCommunitiesBtn.setOnAction(event -> showNumberOfCommunities());
        showMostSociableCommunityBtn.setOnAction(event -> showMostSociableCommunity());
        showDucksBtn.setOnAction(event -> openDucksWindow());
        showEventsBtn.setOnAction(event -> openEventsWindow());
    }

    private void addShowFriendsButtonToTable() {
        Callback<TableColumn<User, Void>, TableCell<User, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                return new TableCell<>() {
                    private final Button showFriendsBtn = new Button("Friends");

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            User selectedUser = getTableView().getItems().get(getIndex());
                            showFriendsBtn.setOnAction(event -> openFriendsWindow(selectedUser));
                            setGraphic(showFriendsBtn);
                        }
                    }
                };
            }
        };
        tableColumnFriends.setCellFactory(cellFactory);
    }

    private void loadUsers(){
        Page<User> page = userSrv.getUserPage(currentPage);
        users.setAll((List<User>) page.getContent());
        totalPages = page.getTotalPages();

        prevBtn.setDisable(currentPage == 0);
        nextBtn.setDisable(currentPage + 1 >= totalPages);
    }

    private void onNext(){
        currentPage++;
        loadUsers();
    }

    private void onPrevious(){
        currentPage--;
        loadUsers();
    }

    private void showNumberOfCommunities(){
        try {
            int communities = userSrv.numberOfCommunities();
            communitiesLbl.setText("Numar de comunitati: " + communities);
        } catch (RuntimeException exception){
            MessageBox.showErrorMessage("Could not get number of communities:\n" + exception.getMessage());
        }
    }

    private void showMostSociableCommunity(){
        try {
            Map.Entry<List<Long>, Integer> entry = userSrv.mostSociableCommunity();
            List<Long> members = entry.getKey();
            int diameter = entry.getValue();
            StringBuilder text = new StringBuilder("Diameter: " + diameter + "\nMembers: ");
            for (long userID : members){
                User currentUser = userSrv.getUserById(userID);
                text.append(currentUser.getUsername()).append(' ');
            }
            MessageBox.showInformationMessage("Most Sociable Community", text.toString());
        } catch (RuntimeException exception) {
            MessageBox.showErrorMessage("Could not get most sociable community:\n" + exception.getMessage());
        }
    }

    private void openDucksWindow(){
        try {
            Stage stage = new Stage();
            String path = "/org/example/laboratorGUI/duck-view.fxml";
            String title = "Check out your ducks!";
            ViewLoader.<DuckController>showStage(stage, path, title, controller -> controller.initializeNecessaryServices(userSrv));
        } catch (IOException exception) {
            MessageBox.showErrorMessage("Could not open duck-view:\n" + exception.getMessage());
        }
    }

    private void openAddUserWindow(){
        try {
            Stage stage = new Stage();
            String path = "/org/example/laboratorGUI/add-user-window.fxml";
            String title = "Add a new user!";
            ViewLoader.<AddUserController>showStage(stage, path, title, controller -> controller.initializeNecessaryServices(userSrv, flockSrv));
        } catch (IOException exception) {
            MessageBox.showErrorMessage("Could not open add-user-view:\n" + exception.getMessage());
        }
    }

    private void removeUser(){
        try {
            long selectedUserID = tableView.getSelectionModel().getSelectedItem().getUserID();
            userSrv.removeUser(selectedUserID);
            MessageBox.showInformationMessage("Success!", "User removed.");
        } catch (RuntimeException exception) {
            MessageBox.showErrorMessage("Could not remove user:\n" + exception.getMessage());
        }
    }

    private void openFriendsWindow(User selectedUser){
        try {
            Stage stage = new Stage();
            String path = "/org/example/laboratorGUI/friends-view.fxml";
            String title = selectedUser.getUsername() + "'s friends!";
            ViewLoader.<FriendsController>showStage(stage, path, title, controller -> controller.initializeNecessaryServices(userSrv, selectedUser));
        } catch (IOException exception) {
            MessageBox.showErrorMessage("Could not open friends-view:\n" + exception.getMessage());
        }
    }

    private void openEventsWindow() {
        try {
            Stage stage = new Stage();
            String path = "/org/example/laboratorGUI/events-view.fxml";
            String title = "Events - Admin";
            ViewLoader.<EventsController>showStage(stage, path, title, controller -> controller.initializeNecessaryServices(eventSrv, null));
        } catch (IOException exception) {
            MessageBox.showErrorMessage("Could not open events-view:\n" + exception.getMessage());
        }
    }

    @Override
    public void update(EntityChangeEvent event){
        if (event.getData() instanceof User userData) {
            switch (event.getType()) {
                case USER_ADDED -> {
                    if (currentPage == totalPages - 1)
                        loadUsers();
                }
                case USER_REMOVED -> {
                    int nrItems = tableView.getItems().size();
                    if (nrItems == 1 && currentPage != 0)
                        onPrevious();
                    else if (tableView.getItems().contains(userData))
                        loadUsers();
                }
            }
        }
    }

    @Override
    public void cleanup() {
        userSrv.removeObserver(this);
    }
}
