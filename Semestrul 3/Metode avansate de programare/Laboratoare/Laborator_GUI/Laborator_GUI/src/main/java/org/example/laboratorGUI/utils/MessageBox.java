package org.example.laboratorGUI.utils;

import javafx.scene.control.Alert;

public class MessageBox {
    public static void showErrorMessage(String errMsg){
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Error");
        message.setContentText(errMsg);
        message.showAndWait();
    }

    public static void showInformationMessage(String title, String msg) {
        Alert message = new Alert(Alert.AlertType.INFORMATION);
        message.setTitle(title);
        message.setContentText(msg);
        message.show();
    }

    public static Alert createInformationMessage(String title, String msg) {
        Alert message = new Alert(Alert.AlertType.INFORMATION);
        message.setTitle(title);
        message.setContentText(msg);
        return message;
    }

    public static Alert createConfirmationMessage(String title, String msg) {
        Alert message = new Alert(Alert.AlertType.CONFIRMATION);
        message.setTitle(title);
        message.setContentText(msg);
        return message;
    }
}
