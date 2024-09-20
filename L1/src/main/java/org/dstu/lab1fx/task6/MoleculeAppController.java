package org.dstu.lab1fx.task6;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.file.Path;

public class MoleculeAppController {
    @FXML Pane moleculePane;
    @FXML ColorPicker colorPicker;

    private Molecule molecule;
    private final double SCALE_MIN = 0.1;
    private final double SCALE_MAX = 5;
    private final double SCALE_DELTA = 0.1;

    private double anchorX;
    private double anchorY;

    @FXML private void processFileOpening(ActionEvent event) throws IOException {
        File file = readAtomAsFile();

        if (file != null) {
            moleculePane.getChildren().clear();
            anchorX = 0;
            anchorY = 0;

            molecule = Molecule.load(file, new Point3D(moleculePane.getWidth() / 3, moleculePane.getHeight() / 3, 100));

            System.out.println(molecule);

            molecule.build();

            molecule.boundingRectangle.setOnMousePressed(e -> {
                anchorX = e.getX();
                anchorY = e.getY();
            });
            molecule.boundingRectangle.setOnMouseDragged(e -> {
                molecule.setTranslateX(e.getSceneX() - anchorX);
                molecule.setTranslateY(e.getSceneY() - anchorY);
            });

            moleculePane.getChildren().add(molecule);

            molecule.setTranslateX(moleculePane.getWidth() / 2 - molecule.getBounds().getWidth());
        }
    }
    private File readAtomAsFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Открыть файл");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XYZ coordinates", "*.xyz"));
        fc.setInitialDirectory(Path.of(System.getProperty("user.dir") + "\\src\\main\\resources\\org\\dstu\\lab1fx\\task6\\xyz").toFile());
        File file;
        if ((file = fc.showOpenDialog(moleculePane.getScene().getWindow())) != null) {
            System.out.println("Файл успешно открыт: " + file.getAbsolutePath());
        }
        return file;
    }

    public ColorPicker getColorPicker() {
        return colorPicker;
    }

    @FXML private void moleculePaneScroll(ScrollEvent event) {
        molecule.scale(event.getDeltaY() > 0 ? molecule.getScaleX() < SCALE_MAX ? molecule.getScaleX() + SCALE_DELTA : SCALE_MAX : molecule.getScaleX() > SCALE_MIN ? molecule.getScaleX() - SCALE_DELTA : SCALE_MIN);
    }

    @FXML private void saveToFile() {
        if (molecule == null) {
            System.out.println("Molecule is absent");
            return;
        }
        FileChooser fc = new FileChooser();
        fc.setTitle("Открыть файл");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files", "*.png"));
        fc.setInitialDirectory(Path.of(System.getProperty("user.dir") + "\\src\\main\\resources\\org\\dstu\\lab1fx\\task6\\captures").toFile());
        fc.setInitialFileName("image");
        File file;
        if ((file = fc.showSaveDialog(moleculePane.getScene().getWindow())) != null) {
            try {
                WritableImage writableImage = new WritableImage((int) moleculePane.getWidth() + 20,
                        (int) moleculePane.getHeight() + 20);
                moleculePane.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
                System.out.println("Файл успешно сохранен: " + file.getAbsolutePath());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

