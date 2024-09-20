package org.dstu.lab1fx.task5;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.dstu.lab1fx.ApplicationBase;
import org.dstu.lab1fx.task1.UI;

import java.util.Objects;

public class TableApp extends ApplicationBase {
    @Override
    public void start(Stage stage) throws Exception {
        HBox root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/main.fxml")));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(UI.class.getResource("css/style.css")).toExternalForm());
        stage.setTitle("Table");
        stage.getIcons().add(stageIcon);
        stage.setMinWidth(600);
        stage.setMinHeight(500);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
