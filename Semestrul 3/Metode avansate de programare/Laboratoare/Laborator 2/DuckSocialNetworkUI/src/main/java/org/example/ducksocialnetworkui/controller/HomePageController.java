package org.example.ducksocialnetworkui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.ducksocialnetworkui.domain.FriendRequest;
import org.example.ducksocialnetworkui.domain.Message;
import org.example.ducksocialnetworkui.domain.User;
import org.example.ducksocialnetworkui.event.EntityChangeEvent;
import org.example.ducksocialnetworkui.event.EntityChangeEventType;
import org.example.ducksocialnetworkui.observer.Observer;
import org.example.ducksocialnetworkui.service.*;
import org.example.ducksocialnetworkui.utils.gui.ProfilePage;
import org.example.ducksocialnetworkui.utils.paging.Pageable;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class HomePageController implements Observer<EntityChangeEvent<Message>> {
    private User currentUser;
    private UserService userService;
    private FriendshipService friendshipService;
    private FriendRequestService friendRequestService;
    private MessageService messageService;
    private EventService eventService;
    private ObservableList<User> model= FXCollections.observableArrayList();
    
    @FXML TableView<User> friendsTable;
    @FXML ListView<Message> listMessages;
    @FXML Label welcomeUserLabel;
    @FXML Button buttonPrevious;
    @FXML Button buttonNext;
    @FXML TableColumn<User,Long> tableColumnId;
    @FXML TableColumn<User,String> tableColumnUsername;
    @FXML Label friendLabel;
    @FXML Button sendButton;
    @FXML TextField sendField;
    @FXML Label labelPage;
    @FXML TextField addFriendField;
    @FXML Label typingLabel;

    private int pageSize = 4;
    private int currentPage = 0;
    private int totalNumberOfElements = 0;

    private Message selectedReplyMessage;
    
    public void init(UserService userService, FriendshipService friendshipService,MessageService messageService,FriendRequestService friendRequestService,EventService eventService,User user){
        this.userService=userService;
        this.friendshipService=friendshipService;
        this.currentUser=user;
        this.messageService=messageService;
        this.friendRequestService=friendRequestService;
        this.eventService=eventService;
        welcomeUserLabel.setText("Welcome, "+user.getUsername()+"!");

        userService.addObserver(event -> initModel());
        friendshipService.addObserver(event -> initModel());
        messageService.addObserver(this);
        friendRequestService.addObserver(event -> {

            if (event.getChangeType() != EntityChangeEventType.FRIEND_REQUEST_RECEIVED) {
                return;
            }

            FriendRequest fr = event.getData();

            if (!fr.getToId().equals(currentUser.getId())) {
                return;
            }

            String fromUsername = userService
                    .findUserById(fr.getFromId())
                    .map(User::getUsername)
                    .orElse("Unknown user");

            MessageAlert.showMessage(
                    null,
                    Alert.AlertType.INFORMATION,
                    "Friend request",
                    "Ai primit o cerere de prietenie de la userul " + fromUsername
            );
        });
        initModel();
    }

    @FXML
    public void initialize(){
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnUsername.setCellValueFactory(new PropertyValueFactory<>("username"));

        friendsTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldUser, newUser) -> {

                    if (newUser == null) {
                        friendLabel.setText("Friend");
                        listMessages.getItems().clear();
                        return;
                    }

                    friendLabel.setText(newUser.getUsername());
                    loadMessages(newUser.getId());
                });

        listMessages.setOnMouseClicked(e -> {
            selectedReplyMessage = listMessages.getSelectionModel().getSelectedItem();
            listMessages.refresh();
        });

        listMessages.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Message item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    return;
                }

                User selectedFriend = friendsTable.getSelectionModel().getSelectedItem();
                Long currentId = currentUser.getId();

                String senderName;
                if (item.getFrom().equals(currentId)) {
                    senderName = "Me";
                } else if (selectedFriend != null) {
                    senderName = selectedFriend.getUsername();
                } else {
                    senderName = "Friend";
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String dateString = item.getData().format(formatter);

                StringBuilder sb = new StringBuilder();
                sb.append(senderName)
                        .append(": ")
                        .append(item.getMessage());

                if (item.getReply() != null) {
                    sb.append(" (reply to: ")
                            .append(item.getReply().getMessage())
                            .append(")");
                }

                sb.append("   ").append(dateString);

                setText(sb.toString());
            }
        });

        sendField.textProperty().addListener((obs, oldText, newText) -> {

            User selectedFriend =
                    friendsTable.getSelectionModel().getSelectedItem();

            if (selectedFriend == null) {
                return;
            }

            if (oldText.isEmpty() && !newText.isEmpty()) {
                messageService.typingStarted(
                        currentUser.getId(),
                        selectedFriend.getId()
                );
            }

            if (!oldText.isEmpty() && newText.isEmpty()) {
                messageService.typingStopped(
                        currentUser.getId(),
                        selectedFriend.getId()
                );
            }
        });

        friendsTable.setItems(model);
    }

    private void initModel() {
        var page = userService.findFriends(new Pageable(currentPage,pageSize),currentUser.getId());
        int maxPage = (int)Math.ceil(1.0*page.getTotalNumberOfElements()/pageSize)-1;
        if(maxPage==-1){
            maxPage=0;
        }
        if(currentPage > maxPage){
            currentPage = maxPage;
            page=userService.findFriends(new Pageable(currentPage,pageSize),currentUser.getId());
        }

        totalNumberOfElements=page.getTotalNumberOfElements();
        buttonPrevious.setDisable(currentPage == 0);
        buttonNext.setDisable((currentPage+1)*pageSize == totalNumberOfElements);

        var friends = StreamSupport.stream(page.getElementsOnPage().spliterator(), false)
                .toList();

        model.setAll(friends);
        labelPage.setText("Page "+(currentPage+1)+ " of " + (maxPage+1));

        User selected = friendsTable.getSelectionModel().getSelectedItem();

        if (selected != null && model.contains(selected)) {
            loadMessages(selected.getId());
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

    private void loadMessages(Long id) {
        Optional<User> friend = userService.findUserById(id);
        if(friend.isEmpty()){
            return;
        }
        List<Message> conversation = messageService.getMessagesBetweenUsers(currentUser.getId(), friend.get().getId());
        listMessages.getItems().setAll(conversation);
    }

    @FXML
    public void onSend() {
        User selectedFriend =
                friendsTable.getSelectionModel().getSelectedItem();

        if (selectedFriend == null) {
            MessageAlert.showMessage(
                    null,
                    Alert.AlertType.WARNING,
                    "",
                    "Selectează un prieten înainte de a trimite mesaj"
            );
            return;
        }

        String text = sendField.getText().trim();
        if (text.isEmpty()) {
            return;
        }

        Optional<User> friend =
                userService.findUserById(selectedFriend.getId());

        if (friend.isEmpty()) {
            return;
        }

        if (selectedReplyMessage == null) {
            messageService.sendMessage(
                    currentUser.getId(),
                    List.of(friend.get().getId()),
                    text
            );
        } else {
            messageService.replyMessage(
                    selectedReplyMessage,
                    currentUser.getId(),
                    List.of(friend.get().getId()),
                    text
            );
            selectedReplyMessage = null;
        }

        loadMessages(friend.get().getId());
        sendField.clear();
    }

    @FXML
    public void onAddFriend() {
        String username = addFriendField.getText().trim();

        if (username.isEmpty()) {
            MessageAlert.showMessage(null, Alert.AlertType.WARNING,
                    "", "Introdu un username");
            return;
        }

        Optional<User> userOpt = userService.verifyCredentials(username);
        if (userOpt.isEmpty()) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,
                    "", "User inexistent");
            return;
        }

        User target = userOpt.get();

        if (target.getId().equals(currentUser.getId())) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,
                    "", "Nu te poți adauga pe tine");
            return;
        }

        try {
            friendRequestService.send(currentUser.getId(), target.getId());
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,
                    "", "Cerere trimisa!");
            addFriendField.clear();
        } catch (Exception e) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,
                    "", e.getMessage());
        }
    }

    @FXML
    public void openNotifications() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ducksocialnetworkui/notifications-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Notifications");
            stage.setScene(scene);
            NotificationsController notificationsController = fxmlLoader.getController();
            notificationsController.init(friendRequestService, currentUser);
            stage.show();
        }
        catch(Exception e){
            e.printStackTrace();
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "", e.getMessage());
        }
    }

    @FXML
    public void openProfileWindow(){
        try {
            ProfilePage profilePage = new ProfilePage(friendshipService, currentUser);
            Scene scene = new Scene(profilePage, 300, 400);
            Stage stage = new Stage();
            stage.setTitle("Profile");
            stage.setScene(scene);
            stage.show();

        } catch (IOException exception) {
            MessageAlert.showErrorMessage(null,"Could not open profile page:\n" + exception.getMessage());
        }
    }

    @FXML
    public void openEvents() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/ducksocialnetworkui/user-events-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Events");
            stage.setScene(scene);
            UserEventsController userEventsController = fxmlLoader.getController();
            userEventsController.init(eventService, currentUser);
            stage.setOnCloseRequest(e -> userEventsController.onClose());
            stage.show();
        }
        catch(Exception e){
            e.printStackTrace();
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "", e.getMessage());
        }
    }

    @Override
    public void update(EntityChangeEvent<Message> event) {
        Message msg = event.getData();

        User selectedFriend =
                friendsTable.getSelectionModel().getSelectedItem();

        if (selectedFriend == null) {
            return;
        }

        Long me = currentUser.getId();
        Long friendId = selectedFriend.getId();

        if (event.getChangeType() == EntityChangeEventType.TYPING_STARTED) {

            if (msg.getFrom().equals(friendId)) {
                typingLabel.setText(
                        selectedFriend.getUsername() + " is typing..."
                );
            }
            return;
        }

        if (event.getChangeType() == EntityChangeEventType.TYPING_STOPPED) {
            typingLabel.setText("");
            return;
        }


        if (event.getChangeType() != EntityChangeEventType.MESSAGE_RECEIVED) {
            return;
        }

        boolean isFromFriendToMe =
                msg.getFrom().equals(friendId) && msg.getTo().contains(me);

        boolean isFromMeToFriend =
                msg.getFrom().equals(me) && msg.getTo().contains(friendId);

        if (!isFromFriendToMe && !isFromMeToFriend) {
            return;
        }

        typingLabel.setText("");

        listMessages.getItems().add(msg);
        listMessages.scrollTo(listMessages.getItems().size() - 1);
    }
}
