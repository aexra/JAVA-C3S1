package org.dstu.lab1fx.task3;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.dstu.lab1fx.ApplicationBase;

public class PaintApp extends ApplicationBase {
    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOWS_HEIGHT);
        scene.getStylesheets().add(stylesheetResource);
        UI ui = UI.getInstance();
        ui.setupUI(root);
        stage.setTitle("Paint");
        stage.getIcons().add(stageIcon);
        stage.setMinHeight(700);
        stage.setMinWidth(800);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
