package org.dstu.lab1fx.task2;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.dstu.lab1fx.ApplicationBase;

public class Primitive2D extends ApplicationBase {
    @Override
    public void start(Stage stage) throws Exception {
        Pane root = new Pane();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOWS_HEIGHT);
        scene.getStylesheets().add(stylesheetResource);
        UI.getInstance().setupUI(root);
        stage.setTitle("Primitive2D");
        stage.getIcons().add(stageIcon);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
