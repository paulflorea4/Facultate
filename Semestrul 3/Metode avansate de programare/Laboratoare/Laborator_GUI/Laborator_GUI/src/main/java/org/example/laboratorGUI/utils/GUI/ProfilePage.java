package org.example.laboratorGUI.utils.GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.example.laboratorGUI.application.MainApplication;
import org.example.laboratorGUI.controller.CleanupController;
import org.example.laboratorGUI.domain.friendship.Friendship;
import org.example.laboratorGUI.domain.user.User;
import org.example.laboratorGUI.observer.Observer;
import org.example.laboratorGUI.service.UserService;
import org.example.laboratorGUI.utils.MessageBox;
import org.example.laboratorGUI.utils.event.EntityChangeEvent;

import java.io.IOException;
import java.util.Objects;

public class ProfilePage extends VBox implements Observer, CleanupController {
    private final UserService userSrv;
    private final User loggedUser;
    private Integer friendsCount;

    private ImageView profilePic;
    private Label nameLabel;
    private Label friendsLabel;
    private Separator separator;
    private Label changePassLabel;
    private TextField passwordInput;
    private Button changePassBtn;

    public ProfilePage(UserService userSrv, User loggedUser, Integer friendsCount) throws IOException {
        this.userSrv = userSrv;
        this.loggedUser = loggedUser;
        this.friendsCount = friendsCount;
        initialize();
        updateFriendsCount();

        userSrv.addObserver(this);
    }

    private void initializeVBox() {
        this.setSpacing(15);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(30));
        this.setStyle("-fx-background-color: #f4f4f4;");
        this.getChildren().addAll(
                profilePic,
                nameLabel,
                friendsLabel,
                separator,
                changePassLabel,
                passwordInput,
                changePassBtn
        );
    }

    private void initializeProfilePicture() throws IOException {
        profilePic = new ImageView(new Image(Objects.requireNonNull(MainApplication.class.getResource("/org/example/laboratorGUI/Images/PlaceholderProfilePicture.png")).openStream()));
        profilePic.setFitWidth(120);
        profilePic.setFitHeight(120);
    }

    private void initializeLabels() {
        nameLabel = new Label(loggedUser.getUsername());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        friendsLabel = new Label();
        friendsLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
    }

    private void initializeChangePasswordObjects() {
        changePassLabel = new Label("Change password:");

        passwordInput = new PasswordField();
        passwordInput.setPromptText("New password");
        passwordInput.setMaxWidth(200);

        changePassBtn = new Button("Save");
        changePassBtn.setOnAction(event -> {
            changePassword(passwordInput.getText());
            passwordInput.clear();
        });
    }

    private void initialize() throws IOException{
        initializeProfilePicture();
        initializeLabels();
        separator = new Separator();
        separator.setMaxWidth(200);
        initializeChangePasswordObjects();
        initializeVBox();
    }

    private void changePassword(String newPassword) {
        if (!newPassword.isEmpty()) {
            long userID = loggedUser.getUserID();
            userSrv.changePassword(userID, newPassword);
            MessageBox.showInformationMessage("Succes!", "Your password has been changed!");
        } else {
            MessageBox.showErrorMessage("Invalid password!");
        }
    }

    private void updateFriendsCount() {
        if (friendsCount == 0)
            friendsLabel.setText("You don't have any friends");
        else if (friendsCount == 1)
            friendsLabel.setText("1 friend");
        else
            friendsLabel.setText(friendsCount + " friends");
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
    public void update(EntityChangeEvent event) {
        if ((event.getData() instanceof Friendship fs) && (fs.getUserId1() == loggedUser.getUserID() || fs.getUserId2() == loggedUser.getUserID())) {
            switch (event.getType()) {
                case FRIENDSHIP_ACCEPTED -> increaseFriendsCount();
                case FRIENDSHIP_REJECTED -> decreaseFriendsCount();
            }
        }
    }

    @Override
    public void cleanup() {
        userSrv.removeObserver(this);
    }
}