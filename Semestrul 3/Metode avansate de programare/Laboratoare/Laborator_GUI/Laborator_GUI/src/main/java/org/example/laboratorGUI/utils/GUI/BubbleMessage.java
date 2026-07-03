package org.example.laboratorGUI.utils.GUI;

import javafx.scene.control.Label;

public class BubbleMessage extends Label {
    public BubbleMessage() {
        super();
        super.setWrapText(true);
        super.setMaxWidth(250);
        super.setStyle("-fx-padding: 8px; -fx-background-radius: 10px;");
    }

    public void changeStyleToIndicateMessageSent() {
        super.setStyle(getStyle() + "; -fx-background-color: #b3e5fc;");
    }

    public void changeStyleToIndicateMessageReceived() {
        super.setStyle(getStyle() + "; -fx-background-color: #e0e0e0;");
    }
}
