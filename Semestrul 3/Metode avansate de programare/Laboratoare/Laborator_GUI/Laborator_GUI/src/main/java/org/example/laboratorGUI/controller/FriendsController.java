package org.example.laboratorGUI.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.util.Callback;
import org.example.laboratorGUI.domain.user.User;
import org.example.laboratorGUI.domain.friendship.Friendship;
import org.example.laboratorGUI.observer.Observer;
import org.example.laboratorGUI.service.UserService;
import org.example.laboratorGUI.utils.MessageBox;
import org.example.laboratorGUI.utils.Page;
import org.example.laboratorGUI.utils.event.EntityChangeEvent;

import java.util.List;

public class FriendsController implements Observer, CleanupController {
    @FXML private TableView<String> tableView;
    @FXML private TableColumn<String, String> tableColumnUsername;
    @FXML private TableColumn<String, Void> tableColumnAction;

    @FXML private TextField usernameTxt;
    @FXML private Button addFriendBtn;
    @FXML private Button prevBtn;
    @FXML private Button nextBtn;

    private UserService userSrv;
    private User selectedUser;

    int currentPage = 0;
    int totalPages = 0;

    private final ObservableList<String> friends = FXCollections.observableArrayList();

    public void initializeNecessaryServices(UserService userSrv, User selectedUser) {
        this.userSrv = userSrv;
        this.selectedUser = selectedUser;
        loadFriends();
        checkPendingRequests();
        showRejectedRequests();

        userSrv.addObserver(this);
    }

    @FXML private void initialize(){
        tableColumnUsername.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        addRemoveFriendButtonToTable();

        tableView.setItems(friends);

        prevBtn.setOnAction(event -> onPrevious());
        nextBtn.setOnAction(event -> onNext());
        addFriendBtn.setOnAction(event -> addFriend());
    }

    private void loadFriends(){
        Page<String> page = userSrv.getFriendsPage(currentPage, selectedUser.getUserID());
        friends.setAll((List<String>) page.getContent());
        totalPages = page.getTotalPages();

        prevBtn.setDisable(currentPage == 0);
        nextBtn.setDisable(currentPage + 1 >= totalPages);
    }

    private void checkPendingRequests() {
        List<Friendship> pendingRequests = userSrv.getPendingRequests(selectedUser.getUserID());
        for (Friendship friendship : pendingRequests)
            handleIncomingFriendship(friendship);
    }

    private void showRejectedRequests() {
        List<Friendship> rejectedRequests = userSrv.getRejectedRequests(selectedUser.getUserID());
        for (Friendship friendship : rejectedRequests)
            handleRejectedFriendship(friendship);
    }

    private void onNext(){
        currentPage++;
        loadFriends();
    }

    private void onPrevious(){
        currentPage--;
        loadFriends();
    }

    private void addFriend() {
        try {
            String username = usernameTxt.getText();
            long userId1 = selectedUser.getUserID();
            long userId2 = userSrv.getUserByUsername(username).getUserID();
            userSrv.addFriend(userId1, userId2);
            usernameTxt.clear();
        } catch (RuntimeException exception){
            MessageBox.showErrorMessage("Could not add friend:\n" + exception.getMessage());
        }
    }

    private void removeFriend(String username) {
        try {
            long userId1 = selectedUser.getUserID();
            long userId2 = userSrv.getUserByUsername(username).getUserID();
            userSrv.removeFriend(userId1, userId2);
            usernameTxt.clear();
            MessageBox.showInformationMessage("Success!", "Friend removed.");
        } catch (RuntimeException exception) {
            MessageBox.showErrorMessage("Could not remove friend:\n" + exception.getMessage());
        }
    }

    private void addRemoveFriendButtonToTable() {
        Callback<TableColumn<String, Void>, TableCell<String, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<String, Void> call(final TableColumn<String, Void> param) {
                return new TableCell<>() {
                    private final Button removeFriendBtn = new Button("Remove");

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            String username = getTableView().getItems().get(getIndex());
                            removeFriendBtn.setOnAction(event -> removeFriend(username));
                            setGraphic(removeFriendBtn);
                        }
                    }
                };
            }
        };
        tableColumnAction.setCellFactory(cellFactory);
    }

    private void handleIncomingFriendship(Friendship friendship) {
        String username = userSrv.getUserById(friendship.getSender()).getUsername();
        Alert confMsg = MessageBox.createConfirmationMessage("Incoming request...", username + " wants to be friends!");
        confMsg.initModality(Modality.NONE);
        confMsg.resultProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue == ButtonType.OK) {
                    userSrv.acceptFriendRequest(friendship);
                } else {
                    userSrv.rejectFriendRequest(friendship);
                }
            }
        });
        confMsg.show();
    }

    private void handleRejectedFriendship(Friendship friendship) {
        String username = userSrv.getUserById(friendship.getSentTo()).getUsername();
        Alert msg = MessageBox.createInformationMessage("That's awkward...", username + " rejected your request!");
        msg.resultProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue == ButtonType.OK) {
                    userSrv.removeFriend(friendship.getUserId1(), friendship.getUserId2());
                }
            }
        });
        msg.show();
    }

    private boolean checkIfFriendshipIsOnScreen(Friendship friendship) {
        return (friendship.getSender() == selectedUser.getUserID() && tableView.getItems().contains(userSrv.getUserById(friendship.getSentTo()).getUsername()))
                || (friendship.getSentTo() == selectedUser.getUserID() && tableView.getItems().contains(userSrv.getUserById(friendship.getSender()).getUsername()));
    }

    @Override
    public void update(EntityChangeEvent event){
        if (event.getData() instanceof Friendship fs) {
            switch (event.getType()) {
                case FRIENDSHIP_SENT -> {
                    if (fs.getSender() == selectedUser.getUserID())
                        MessageBox.showInformationMessage("Success!", "Friend request sent.");
                    if (fs.getSentTo() == selectedUser.getUserID())
                        handleIncomingFriendship(fs);
                }
                case FRIENDSHIP_ACCEPTED -> {
                    if (fs.getUserId1() == selectedUser.getUserID() || fs.getUserId2() == selectedUser.getUserID())
                        if (totalPages == 0 || currentPage == totalPages - 1)
                            loadFriends();
                }
                case FRIENDSHIP_REJECTED -> {
                    if (fs.getSender() == selectedUser.getUserID())
                        handleRejectedFriendship(fs);
                }
                case FRIENDSHIP_REMOVED -> {
                    int nrItems = tableView.getItems().size();
                    if (nrItems == 1 && currentPage != 0)
                        onPrevious();
                    else if (checkIfFriendshipIsOnScreen(fs))
                        loadFriends();
                }
            }
        }
    }

    @Override
    public void cleanup() {
        userSrv.removeObserver(this);
    }
}