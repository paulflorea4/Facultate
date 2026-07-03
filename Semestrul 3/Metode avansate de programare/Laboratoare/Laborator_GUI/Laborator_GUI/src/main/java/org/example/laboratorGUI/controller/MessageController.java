package org.example.laboratorGUI.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.laboratorGUI.domain.user.User;
import org.example.laboratorGUI.domain.message.Message;
import org.example.laboratorGUI.observer.Observer;
import org.example.laboratorGUI.service.EventService;
import org.example.laboratorGUI.service.MessageService;
import org.example.laboratorGUI.service.UserService;
import org.example.laboratorGUI.utils.GUI.BubbleMessage;
import org.example.laboratorGUI.utils.GUI.ProfilePage;
import org.example.laboratorGUI.utils.GUI.ViewLoader;
import org.example.laboratorGUI.utils.MessageBox;
import org.example.laboratorGUI.utils.event.EntityChangeEvent;
import org.example.laboratorGUI.utils.event.EntityChangeEventType;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MessageController implements Observer, CleanupController {
    @FXML private ListView<String> listViewFriends;
    @FXML private ListView<Message> listViewMessages;

    @FXML private Label usernameLbl;

    @FXML private TextField sendMessageTxt;
    @FXML private Button sendMessageBtn;

    @FXML private Button toProfilePage;
    @FXML private Button toEventsPage;

    private User loggedUser;
    private UserService userSrv;
    private MessageService msgSrv;
    private EventService eventSrv;

    private Message selectedReplyMessage;

    public void setMsgService(UserService userSrv, MessageService msgSrv, EventService eventSrv, User loggedUser){
        this.loggedUser = loggedUser;
        this.userSrv = userSrv;
        this.msgSrv = msgSrv;
        this.eventSrv = eventSrv;
        loadFriends();

        msgSrv.addObserver(this);
    }

    private void loadFriends() {
        List<String> friendsList = userSrv.getUsersFriends(loggedUser.getUserID())
                .stream()
                .map(u -> userSrv.getUserById(u).getUsername())
                .toList();
        listViewFriends.getItems().setAll(friendsList);
    }

    @FXML private void initialize() {
        listViewFriends.getSelectionModel().selectedItemProperty().addListener((obs, old, friendsUsername) -> {
            if (friendsUsername != null) {
                usernameLbl.setText(friendsUsername);
                loadMessages(friendsUsername);
            }
        });

        createMessageBubblesForListView();
        listViewMessages.setOnMouseClicked(event -> selectedReplyMessage = listViewMessages.getSelectionModel().getSelectedItem());

        sendMessageBtn.setOnAction(event -> onSend());
        toProfilePage.setOnAction(event -> openProfileWindow());
        toEventsPage.setOnAction(event -> openEventsWindow());
    }

    private void createMessageBubblesForListView() {
        Callback<ListView<Message>, ListCell<Message>> cellFactory = new Callback<>() {
            @Override
            public ListCell<Message> call(ListView<Message> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Message msg, boolean empty) {
                        super.updateItem(msg, empty);

                        if (empty || msg == null) {
                            setText(null);
                            setGraphic(null);
                            return;
                        }

                        BubbleMessage bubble = new BubbleMessage();
                        HBox container = new HBox();

                        setupBubbleMessage(msg, bubble, container);
                        setGraphic(container);
                        setText(null);
                    }
                };
            }
        };
        listViewMessages.setCellFactory(cellFactory);
    }

    private void setupBubbleMessage(Message msg, BubbleMessage bubble, HBox container) {
        String text = msg.getMessage();
        if (msg.getReply() != null)
            text = "[Reply to: " + msg.getReply().getMessage() + "]\n" + text;
        bubble.setText(text);

        if (Objects.equals(msg.getFrom(), loggedUser.getUserID())) {
            bubble.changeStyleToIndicateMessageSent();
            container.setAlignment(Pos.CENTER_RIGHT);
        } else {
            bubble.changeStyleToIndicateMessageReceived();
            container.setAlignment(Pos.CENTER_LEFT);
        }
        container.getChildren().add(bubble);
    }

    private void loadMessages(String username) {
        User friend = userSrv.getUserByUsername(username);
        List<Message> conv = msgSrv.getConversation(loggedUser.getUserID(), friend.getUserID());
        listViewMessages.getItems().setAll(conv);
        listViewMessages.scrollTo(listViewMessages.getItems().size() - 1);
    }

    private void onSend() {
        String username = listViewFriends.getSelectionModel().getSelectedItem();
        String text = sendMessageTxt.getText().trim();

        User selectedFriend = userSrv.getUserByUsername(username);
        Long friendID = selectedFriend.getUserID();
        Long loggedUserID = loggedUser.getUserID();
        if (selectedReplyMessage != null) {
            msgSrv.replyToMessage(selectedReplyMessage, loggedUserID, List.of(friendID), text);
            selectedReplyMessage = null;
        } else
            msgSrv.sendMessage(loggedUserID, List.of(friendID), text);

        loadMessages(username);
        sendMessageTxt.clear();
    }

    private void openProfileWindow() {
        try {
            ProfilePage profilePage = new ProfilePage(userSrv, loggedUser, listViewFriends.getItems().size());
            Scene scene = new Scene(profilePage, 300, 400);
            Stage stage = new Stage();
            stage.setTitle("Profile");
            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(event -> profilePage.cleanup());
        } catch (IOException exception) {
            MessageBox.showErrorMessage("Could not open profile page:\n" + exception.getMessage());
        }
    }

    private void openEventsWindow() {
        try {
            Stage stage = new Stage();
            String path = "/org/example/laboratorGUI/events-view.fxml";
            String title = "Events";
            ViewLoader.<EventsController>showStage(stage, path, title, controller -> controller.initializeNecessaryServices(eventSrv, loggedUser));
        } catch (IOException exception) {
            MessageBox.showErrorMessage("Could not open events-view:\n" + exception.getMessage());
        }
    }

    @Override
    public void update(EntityChangeEvent event) {
        if (Objects.equals(event.getType(), EntityChangeEventType.MESSAGE_RECEIVED)) {
            Message msg = (Message) event.getData();
            Long loggedUserID = loggedUser.getUserID();
            if (msg.getTo().contains(loggedUserID)) {
                listViewMessages.getItems().add(msg);
                listViewMessages.scrollTo(listViewMessages.getItems().size() - 1);
            }
        }
    }

    @Override
    public void cleanup() {
        msgSrv.removeObserver(this);
    }
}
