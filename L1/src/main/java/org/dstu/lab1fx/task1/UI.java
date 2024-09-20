package org.dstu.lab1fx.task1;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.embed.swing.SwingFXUtils;
import org.dstu.lab1fx.UIBase;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UI extends UIBase {
    private Canvas drawCanvas;
    private VBox navPanel;
    private ToggleGroup imageSwitch;
    private Button saveButton;

    private List<ImageView> images;
    private ImageView selectedImage;

    private static final URL stylesheetPath = UI.class.getResource("css/style.css");

    protected static UI instance;

    public static UI getInstance() {
        if (instance != null) return instance;
        instance = new UI();
        return instance;
    }

    private UI() {}

    public void setupUI(Pane root) {
        this.root = root;
        root.setPadding(new Insets(PADDING_SIZE, PADDING_SIZE, PADDING_SIZE, PADDING_SIZE));

        images = loadImages();

        // Навигационная панель
        navPanel = new VBox(70);
        navPanel.setAlignment(Pos.CENTER);
        navPanel.setMinWidth(120);

        // Канвас
        drawCanvas = new Canvas(root.getWidth() - 200, root.getHeight() - 100);
        StackPane canvasPanel = new StackPane();
        canvasPanel.getChildren().add(drawCanvas);
        canvasPanel.setStyle("-fx-background-color: whitesmoke");

        // Обработка клика по канвасу
        drawCanvas.setOnMouseClicked((mouseEvent) -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                GraphicsContext gc = drawCanvas.getGraphicsContext2D();
                Image figureImage = selectedImage.getImage();
                double imageWidth = 128;
                double imageHeight = 128;
                gc.drawImage(figureImage, mouseEvent.getX() - imageWidth / 2, mouseEvent.getY() - imageHeight / 2, imageWidth, imageHeight);
            }
        });

        // Кнопки переключатели выбранной фигуры
        VBox figureButtons = new VBox();
        figureButtons.setAlignment(Pos.CENTER);
        for (ImageView figure : images) {
            ToggleButton toggleButton = new ToggleButton();
            toggleButton.getStyleClass().add("button");
            ImageView buttonView = new ImageView(figure.getImage());
            buttonView.setFitWidth(30);
            buttonView.setFitHeight(30);
            toggleButton.setGraphic(buttonView);
            figureButtons.getChildren().add(toggleButton);
        }

        // Панель с кнопками переключателями
        imageSwitch = new ToggleGroup();
        for (Node figureButton :
                figureButtons.getChildren()) {
            if (figureButton instanceof ToggleButton) {
                ((ToggleButton) figureButton).setToggleGroup(imageSwitch);
            }
        }

        // Прослушиватель переключения
        imageSwitch.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
            if (newToggle != null) {
                ToggleButton toggleButton = ((ToggleButton) newToggle);
                if (toggleButton.getGraphic() instanceof ImageView) {
                    selectedImage = ((ImageView) toggleButton.getGraphic());
                }
            }
        });

        //  Сразу выделим первую
        imageSwitch.selectToggle((ToggleButton)figureButtons.getChildren().getFirst());
        selectedImage = images.getFirst();

        // Кнопка "Сохранить"
        saveButton = new Button("Сохранить");
        saveButton.setAlignment(Pos.CENTER);
        saveButton.setOnMouseClicked((mouseEvent) -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                Image imageFromCanvas = drawCanvas.snapshot(null, null);
                FileChooser fc = new FileChooser();
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files","*.png"));
                File fileToSave = fc.showSaveDialog(root.getScene().getWindow());
                if (fileToSave != null) {
                    saveImage(imageFromCanvas, fileToSave);
                }
            }
        });

        navPanel.getChildren().addAll(figureButtons, saveButton);

        root.getChildren().addAll(canvasPanel, navPanel);
    }

    private ArrayList<ImageView> loadImages() {
        var images = new ArrayList<ImageView>();
        URL filesPath = getClass().getResource("assets");
        if (filesPath != null) {
            try {
                File dir = new File(filesPath.toURI());
                File[] directoryListing = dir.listFiles();
                if (directoryListing != null) {
                    for (File file : directoryListing) {
                        int i = file.getName().lastIndexOf('.');
                        String extension = file.getName().substring(i + 1);
                        if (extension.equals("png") || extension.equals("jpg")) {
                            ImageView imageView = new ImageView(new Image(file.getPath()));
                            imageView.setPreserveRatio(true);
                            imageView.setFitWidth(100);
                            imageView.setFitHeight(100);
                            images.add(imageView);
                        }
                    }
                }
            }
            catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        return images;
    }

    private void saveImage(Image image, File file) {
        try {
            BufferedImage renderedImage = SwingFXUtils.fromFXImage(image, null);
            ImageIO.write(renderedImage, "png", file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}