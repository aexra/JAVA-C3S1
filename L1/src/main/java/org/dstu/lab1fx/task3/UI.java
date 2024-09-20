package org.dstu.lab1fx.task3;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.dstu.lab1fx.TitledPaneStyled;
import org.dstu.lab1fx.UIBase;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class UI extends UIBase {
    private static UI instance;

    private Pane root;
    private Pane canvasPane;
    private List<Canvas> canvasLayers = new ArrayList<>();

    private static final double PANEL_WIDTH = 300;
    private static final Color borderColor = Color.GREY;

    private enum ShapeType {LINE, CIRCLE, ELLIPSE, RECTANGLE, BRUSH};
    private enum StrokeType {SOLID, DOTTED, DASHED};

    private ShapeType shape = ShapeType.LINE;
    private StrokeType strokeType = StrokeType.SOLID;

    private Color strokeColor = Color.BLACK;
    private Color fillColor = Color.WHITE;

    private float strokeWidth = 8.0f;
    private int saveWidth = 32;
    private int saveHeight = 32;
    private double x, y;
    private double width = 50;
    private double height = 100;

    private boolean isDragging = false;

    public static UI getInstance() {
        if (instance != null) return instance;
        instance = new UI();
        return instance;
    }

    private UI() {}

    public void setupUI(Pane root) {
        this.root = root;

        HBox toolBar = new HBox();

        enum MainScenarios {
            SAVE("Сохранить изображение"), EXIT("Выйти"), HELP("Помощь");
            public String label;
            MainScenarios(String label) {
                this.label = label;
            }
        }

        MenuButton mainButton = new MenuButton("Paint");
        MenuItem saveItem = new MenuItem(MainScenarios.SAVE.label);
        saveItem.setOnAction(event -> {
            RenderedImage image = layersToImage(canvasLayers);
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files","*.png"));
            File fileToSave = fc.showSaveDialog(root.getScene().getWindow());
            if (fileToSave != null) {
                try {
                    ImageIO.write(image, "png", fileToSave);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        MenuItem exitItem = new MenuItem(MainScenarios.EXIT.label);
        exitItem.setOnAction(event -> Platform.exit());
        mainButton.getItems().addAll(saveItem, exitItem);

        MenuButton helpButton = new MenuButton(MainScenarios.HELP.label);
        MenuItem helpItem = new MenuItem("Об авторе");
        helpButton.getItems().add(helpItem);

        toolBar.getChildren().addAll(mainButton, helpButton);

        canvasPane = new Pane();
        canvasPane.setStyle("-fx-background-color: whitesmoke;");
        canvasPane.setMinWidth(400);
        canvasPane.setPrefWidth(root.getWidth() - PANEL_WIDTH);
        root.widthProperty().addListener((c, o, n) -> {
            toolBar.setPrefWidth(((Double) n));
            canvasPane.setPrefWidth((Double)n - PANEL_WIDTH);
        });
        root.heightProperty().addListener((c, o, n) -> {
            canvasPane.setPrefHeight((Double)n);
        });

        FlowPane navPane = new FlowPane(10, 10);
        navPane.setPadding(new Insets(20, 20, 20, 20));

        // Кнопки переключатели выбранной фигуры
        ToggleGroup toggleGroup = new ToggleGroup();
        VBox shapePicker = new VBox();
        shapePicker.setAlignment(Pos.CENTER);
        for (ShapeType shape : ShapeType.values()) {
            ToggleButton toggleButton = new ToggleButton();
            toggleButton.getStyleClass().add("button");
            toggleButton.setText(shape.toString());
            toggleButton.setToggleGroup(toggleGroup);
            toggleButton.setPrefWidth(100);
            shapePicker.getChildren().add(toggleButton);
        }
        toggleGroup.selectToggle(toggleGroup.getToggles().getFirst());
        shape = ShapeType.values()[0];

        TitledPane shapePickerPane = new TitledPaneStyled("Фигура", shapePicker);

        // Панель выбора цвета контура
        ColorPicker strokeColorPicker = new ColorPicker();
        strokeColorPicker.setValue(Color.BLACK);
        TitledPane strokeColorPickerPane = new TitledPaneStyled("Цвет контура", strokeColorPicker);

        // Панель выбора цвета заливки
        ColorPicker fillColorPicker = new ColorPicker();
        fillColorPicker.setValue(Color.WHITE);
        TitledPane fillColorPickerPane = new TitledPaneStyled("Цвет заливки", fillColorPicker);

        // Панель выбора стиля контура
        VBox strokeStyle = new VBox(5);
        HBox strokeWidthPane = new HBox(10);  // Выбор толщины
        Label strokeWidthLabel = new Label("Толщина");
        TextField strokeWidthField = new TextField("8.0");
        strokeWidthPane.getChildren().addAll(strokeWidthLabel, strokeWidthField);

        HBox strokeTypePane = new HBox(10);   // Выбор типа
        Label strokeTypeLabel = new Label("Тип");
        ComboBox<StrokeType> strokeTypeField = new ComboBox<>();
        strokeTypeField.setValue(StrokeType.SOLID);
        strokeTypeField.getItems().addAll(StrokeType.values());
        strokeTypePane.getChildren().addAll(strokeTypeLabel, strokeTypeField);

        strokeStyle.getChildren().addAll(strokeWidthPane, strokeTypePane);
        TitledPane strokeStylePane = new TitledPaneStyled("Контур", strokeStyle);

        // Панель размера изображения для сохранения
        VBox saveImageSize = new VBox(5);

        // Ширина изображения
        HBox saveImageWidth = new HBox(10);
        Label saveImageWidthLabel = new Label("Ширина");
        TextField imageWidthField = new TextField("32");
        saveImageWidth.getChildren().addAll(saveImageWidthLabel, imageWidthField);
        // Высота изображения
        HBox saveImageHeight = new HBox(10);
        Label saveImageHeightLabel = new Label("Высота");
        TextField imageHeightField = new TextField("32");
        saveImageHeight.getChildren().addAll(saveImageHeightLabel, imageHeightField);

        saveImageSize.getChildren().addAll(saveImageWidth, saveImageHeight);
        TitledPane saveImageSizePane = new TitledPaneStyled("Размер изображения", saveImageSize);

        navPane.getChildren().addAll(shapePickerPane,
                strokeColorPickerPane,
                fillColorPickerPane,
                strokeStylePane,
                saveImageSizePane);

        HBox mainPane = new HBox();
        mainPane.getChildren().addAll(canvasPane, navPane);
        root.getChildren().addAll(toolBar, mainPane);

        // Прослушиватели
        toggleGroup.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
            if (newToggle != null) {
                switch (((ToggleButton) newToggle).getText()) {
                    case "LINE" -> shape = ShapeType.LINE;
                    case "CIRCLE" -> shape = ShapeType.CIRCLE;
                    case "ELLIPSE" -> shape = ShapeType.ELLIPSE;
                    case "RECTANGLE" -> shape = ShapeType.RECTANGLE;
                    case "BRUSH" -> shape = ShapeType.BRUSH;
                }
            }
        });

        strokeColorPicker.setOnAction(event -> strokeColor = strokeColorPicker.getValue());
        fillColorPicker.setOnAction(event -> fillColor = fillColorPicker.getValue());
        strokeWidthField.setOnAction(event -> {
            try {
                float value = Float.parseFloat(strokeWidthField.getText());
                if (value > 0 && value < 10) strokeWidth = value;
            }
            catch (NumberFormatException | NullPointerException e) {
                e.printStackTrace();
            }
        });
        strokeTypeField.setOnAction(event -> strokeType = strokeTypeField.getValue());
        imageWidthField.setOnAction(event -> {
            try {
                int value = Integer.parseInt(imageWidthField.getText());
                if (value > 0) saveWidth = value;
            }
            catch (NumberFormatException | NullPointerException e) {
                e.printStackTrace();
            }
        });
        imageHeightField.setOnAction(event -> {
            try {
                int value = Integer.parseInt(imageHeightField.getText());
                if (value > 0) saveHeight = value;
            }
            catch (NumberFormatException | NullPointerException e) {
                e.printStackTrace();
            }
        });
        canvasPane.setOnMouseClicked(mouseEvent -> {
            if (shape == ShapeType.BRUSH) {
                createNewLayer();
                drawPixel();
                isDragging = true;
            }
            else if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                x = mouseEvent.getX() - width / 2;
                y = mouseEvent.getY() - height / 2;
                createNewLayer();
                drawShape();
            }
            else if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (x <= mouseEvent.getX() && mouseEvent.getX() <= x + width
                        && y <= mouseEvent.getY() && mouseEvent.getY() <= y + height) {
                    isDragging = true;
                }
            }
        });
        canvasPane.setOnMouseDragged(mouseEvent -> {
            if (isDragging) {
                if (shape == ShapeType.BRUSH) {
                    x = mouseEvent.getX();
                    y = mouseEvent.getY();
                    drawPixel();
                }
                else {
                    x = mouseEvent.getX() - width / 2;
                    y = mouseEvent.getY() - height / 2;
                    drawShape();
                }
            }
        });
        canvasPane.setOnMouseReleased(mouseEvent -> isDragging = false);
    }

    private void createNewLayer() {
        Canvas canvas = new Canvas();
        canvas.widthProperty().bind(canvasPane.widthProperty());
        canvas.heightProperty().bind(canvasPane.heightProperty());
        canvasPane.getChildren().add(canvas);
        canvasLayers.add(canvas);
    }

    private void drawPixel() {
        Canvas canvas = canvasLayers.getLast();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        PixelWriter pixelWriter = gc.getPixelWriter();
        pixelWriter.setColor((int)x, (int)y, strokeColor);
    }

    private void drawShape() {
        Canvas canvas = canvasLayers.getLast();

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        switch (strokeType) {
            case SOLID -> gc.setLineDashes(0);
            case DASHED -> gc.setLineDashes(15);
            case DOTTED -> {
                gc.setLineDashes(10);
            }
        }
        gc.setLineWidth(strokeWidth);
        gc.setStroke(strokeColor);
        gc.setFill(fillColor);
        switch (shape) {
            case LINE -> gc.strokeLine(x, y, x + width, y + height);
            case ELLIPSE -> {
                gc.strokeOval(x, y, width, height);
                gc.fillOval(x, y, width, height);
            }
            case RECTANGLE -> {
                gc.strokeRect(x, y, width, height);
                gc.fillRect(x, y, width, height);
            }
            case CIRCLE -> {
                double xCenter = (2*x + width) / 2;
                double yCenter = (2*y + height) / 2;
                double radius = Math.min(width, height) / 2;
                gc.strokeOval(xCenter - radius, yCenter - radius,
                        2 * radius, 2 * radius);
                gc.fillOval(xCenter - radius, yCenter - radius,
                        2 * radius, 2 * radius);
            }
        }
        // BorderLine
        gc.setStroke(borderColor);
        gc.setLineDashes(6);
        gc.setLineWidth(0.5);
        gc.strokeRect(x, y, width, height);
    }

    private BufferedImage layersToImage(List<Canvas> canvasLayers) {
        BufferedImage image = new BufferedImage(saveWidth, saveHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = ((Graphics2D) image.getGraphics());
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

        SnapshotParameters snapshotParameter = new SnapshotParameters();
        for (Canvas layer : canvasLayers) {
            BufferedImage layerImage = SwingFXUtils.fromFXImage(
                    layer.snapshot(snapshotParameter, null), null);
            graphics.drawImage(layerImage, 0, 0, null);
        }
        return image;
    }
}
