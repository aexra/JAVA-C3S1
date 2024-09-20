package org.dstu.lab1fx.task1;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.dstu.lab1fx.ApplicationBase;

public class Shapes2D extends ApplicationBase {
    @Override
    public void start(Stage stage) {
        HBox root = new HBox(5);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOWS_HEIGHT);
        scene.getStylesheets().add(stylesheetResource);
        UI.getInstance().setupUI(root);
        stage.setTitle("Фигуры (Задание 1)");
        stage.getIcons().add(stageIcon);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}