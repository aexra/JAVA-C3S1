package org.dstu.lab1fx.task5;

import javafx.fxml.FXML;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;

public class DialogController {
    @FXML private DialogPane newItemDialog;
    @FXML private TextField languageField;
    @FXML private TextField authorField;
    @FXML private TextField ageField;

    public DialogPane getNewItemDialog() {
        return newItemDialog;
    }
    public String getLanguage() {
        return languageField.getText();
    }
    public String getAuthor() {
        return authorField.getText();
    }
    public String getAge() {
        return ageField.getText();
    }
}
