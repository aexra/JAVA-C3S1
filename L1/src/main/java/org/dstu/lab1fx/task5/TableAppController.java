package org.dstu.lab1fx.task5;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;

import javax.security.auth.callback.Callback;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;


public class TableAppController {
    @FXML private TableView<Language> tableView;
    @FXML private TableColumn<Language, String> languageColumn;
    @FXML private TableColumn<Language, String> authorColumn;
    @FXML private TableColumn<Language, String> ageColumn;
    private DialogPane newItemDialog;
    private DialogController dialogController;
    private Dialog<Language> dialog;
    private ObservableList<Language> languages;
    @FXML protected void initialize() throws IOException {
        languages = FXCollections.observableArrayList(
            new Language("Си", "Деннис Ритчи", "1972"),
            new Language("C++", "Бьерн Страуструп", "1983"),
            new Language("Python", "Гвидо ван Россум", "1991"),
            new Language("Java", "Джеймс Гослинг", "1995"),
            new Language("JavaScript", "Брендон Айк", "1995"),
            new Language("C#", "Андерс Хейлсберг", "2001"),
            new Language("Scala", "Мартин Одерски", "2003")
        );
        // Столбец языка
        languageColumn.setCellValueFactory(new PropertyValueFactory<>("language"));
        languageColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        languageColumn.setOnEditCommit(t ->
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setLanguage(t.getNewValue()));
        // Столбец автора
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        authorColumn.setOnEditCommit(t ->
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setAuthor(t.getNewValue()));
        // Столбец года
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        ageColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        ageColumn.setOnEditCommit(t ->
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setAge(t.getNewValue()));

        for (Language language : languages) {
            tableView.getItems().add(language);
        }

        // Подключаем диалоговое окно
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("fxml/dialog.fxml")));
        newItemDialog = fxmlLoader.load();
        dialogController = fxmlLoader.getController();

        dialog = new Dialog<>();
        dialog.setTitle("Новый элемент");
        dialog.setDialogPane(newItemDialog);
        dialog.setResultConverter(dialogButton -> {
            String language = dialogController.getLanguage();
            String author = dialogController.getAuthor();
            String age = dialogController.getAge();
            tableView.getItems().add(new Language(language, author, age));
            return null;
        });
    }
    @FXML private void newItem(ActionEvent event) {
        dialog.showAndWait();
    }
    @FXML private void editItem(ActionEvent event) {
    }
    @FXML private void changeLanguageColumnVisibility(ActionEvent event) {
        languageColumn.setVisible(!languageColumn.isVisible());
    }

    @FXML private void changeAuthorColumnVisibility(ActionEvent event) {
        authorColumn.setVisible(!authorColumn.isVisible());
    }

    @FXML private void changeAgeColumnVisibility(ActionEvent event) {
        ageColumn.setVisible(!ageColumn.isVisible());
    }

    @FXML private void closeApp(ActionEvent event) {
        Platform.exit();
    }
}
