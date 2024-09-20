package org.dstu.lab1fx.task6;

import javafx.fxml.FXMLLoader;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.dstu.lab1fx.ApplicationBase;

import java.util.Objects;

public class MoleculeApp extends ApplicationBase {
    @Override
    public void start(Stage stage) throws Exception {
        var loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("fxml/main.fxml")));
        HBox root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(ApplicationBase.class.getResource("css/style.css")).toExternalForm());
        stage.setTitle("Atom3D");
        stage.getIcons().add(stageIcon);
        stage.setMinWidth(600);
        stage.setMinHeight(500);

        scene.setOnKeyPressed(e -> {
            // Получаем код нажатой клавиши
            var controller = (MoleculeAppController)loader.getController();

            switch (e.getCode()) {
                case KeyCode.W, KeyCode.UP:
                    controller.molecule.turnHorizontally(5);
                    break;
                case KeyCode.S, KeyCode.DOWN:
                    controller.molecule.turnHorizontally(-5);
                    break;
                case KeyCode.A, KeyCode.LEFT:
                    controller.molecule.turnVertically(-5);
                    break;
                case KeyCode.D, KeyCode.RIGHT:
                    controller.molecule.turnVertically(5);
                    break;
            }
        });

//        PerspectiveCamera camera = new PerspectiveCamera(true);
//        camera.setTranslateZ(-500);

//        scene.setCamera(camera);
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
