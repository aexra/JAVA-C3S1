package org.dstu.lab1fx.task4;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.dstu.lab1fx.ApplicationBase;

import java.util.Objects;

public class GraphApp extends ApplicationBase {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("fxml/main.fxml")));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(ApplicationBase.class.getResource("css/style.css")).toExternalForm());
        stage.setTitle("Graph");
        stage.getIcons().add(stageIcon);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
