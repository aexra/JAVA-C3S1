package org.dstu.lab1fx.task6;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;

public class MoleculeAppController {
    @FXML Pane moleculePane;
    @FXML ColorPicker colorPicker;
    private Molecule molecule;
    private final double SCALE_DELTA = 1.1;

    @FXML private void processFileOpening(ActionEvent event) throws IOException {
        File file = readAtomAsFile();

        if (file != null) {
            moleculePane.getChildren().clear();

            molecule = Molecule.load(file, new Point3D(moleculePane.getWidth() / 2, moleculePane.getHeight() / 2, 100));
            System.out.println(molecule);

            molecule.build();

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

    private void normalizeOnMax(double[] array) {
        double max = Arrays.stream(array).reduce(Double.MIN_VALUE, Double::max);
        for (int i = 0; i < array.length; i++) {
            array[i] /= max;
        }
    }

    private double[] parseStringItems(String[] items) throws IOException {
        double[] parsedItems = new double[items.length];
        for (int i = 0; i < items.length; i++) {
            try {
                parsedItems[i] = Double.parseDouble(items[i]);
            }
            catch (NumberFormatException e) {
                throw new IOException();
            }
        }
        return parsedItems;
    }

    public ColorPicker getColorPicker() {
        return colorPicker;
    }

    @FXML private void moleculeZoom(ScrollEvent event) {

    }

    @FXML private void moveMolecule(MouseEvent mouseEvent) {

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

