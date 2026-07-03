package org.example.ducksocialnetworkui.utils.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.example.ducksocialnetworkui.application.MainApplication;
import org.example.ducksocialnetworkui.controller.MessageAlert;
import org.example.ducksocialnetworkui.domain.*;
import org.example.ducksocialnetworkui.observer.Observer;
import org.example.ducksocialnetworkui.service.FriendshipService;
import org.example.ducksocialnetworkui.event.EntityChangeEvent;

import java.io.IOException;
import java.util.Objects;

public class ProfilePage extends VBox implements Observer<EntityChangeEvent<Friendship>> {
    private final FriendshipService friendshipService;
    private final User loggedUser;
    private Integer friendsCount;

    private ImageView profilePic;
    private Label nameLabel;
    private Label friendsLabel;
    private Separator separator;
    private Label statusLabel;
    private Label roleLabel;
    private Label statsLabel;

    public ProfilePage(FriendshipService friendshipService, User loggedUser) throws IOException {
        this.friendshipService = friendshipService;
        this.loggedUser = loggedUser;

        initialize();
        updateFriendsCount();

        friendshipService.addObserver(this);
    }


    private void initializeVBox() {
        this.setSpacing(15);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(30));
        this.setStyle("-fx-background-color: #f4f4f4;");

        this.getChildren().addAll(
                profilePic,
                nameLabel,
                roleLabel,
                statusLabel,
                friendsLabel,
                statsLabel,
                separator
        );
    }

    private void initializeProfilePicture() throws IOException {
        profilePic = new ImageView(new Image(Objects.requireNonNull(MainApplication.class.getResource("/org/example/ducksocialnetworkui/Images/PlaceholderProfilePicture.png")).openStream()));
        profilePic.setFitWidth(120);
        profilePic.setFitHeight(120);
    }

    private void initializeLabels() {
        nameLabel = new Label(loggedUser.getUsername());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        roleLabel = new Label("Role: " + loggedUser.getClass().getSimpleName());
        roleLabel.setStyle("-fx-text-fill: #555;");

        statusLabel = new Label("Status: Online");
        statusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");

        friendsLabel = new Label();
        friendsLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");

        statsLabel = new Label();
        statsLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #777;");
    }

    private void updateStats() {
        StringBuilder sb = new StringBuilder();

        if (loggedUser instanceof Duck d) {
            sb.append("\nSpeed = ").append(d.getViteza()).append(" | Stamina = ").append(d.getRezistenta());
        } else {
            if(loggedUser instanceof Persoana p)
                sb.append("\nOccupation = ").append(p.getOcupatie());
        }

        statsLabel.setText(sb.toString());
    }


    private void initialize() throws IOException{
        initializeProfilePicture();
        initializeLabels();
        separator = new Separator();
        separator.setMaxWidth(200);
        initializeVBox();
        friendsCount=friendshipService.getNumberOfFriends(loggedUser.getId());
    }

    private void updateFriendsCount() {
        if (friendsCount == 0)
            friendsLabel.setText("You don't have any friends");
        else if (friendsCount == 1)
            friendsLabel.setText("1 friend");
        else
            friendsLabel.setText(friendsCount + " friends");

        updateStats();
    }

    private void increaseFriendsCount() {
        friendsCount++;
        updateFriendsCount();
    }

    private void decreaseFriendsCount() {
        friendsCount--;
        updateFriendsCount();
    }

    @Override
    public void update(EntityChangeEvent<Friendship> event) {
        Friendship f = event.getData() != null
                ? event.getData()
                : event.getOldData();

        if (f == null) return;

        Long me = loggedUser.getId();

        boolean involvesMe =
                f.getUserId1().equals(me) || f.getUserId2().equals(me);

        if (!involvesMe) return;

        switch (event.getChangeType()) {
            case ADD -> increaseFriendsCount();
            case DELETE -> decreaseFriendsCount();
            default -> {}
        }
    }

}
