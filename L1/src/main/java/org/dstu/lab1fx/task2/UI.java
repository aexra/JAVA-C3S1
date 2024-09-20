package org.dstu.lab1fx.task2;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import org.dstu.lab1fx.UIBase;

import java.util.Objects;
import java.util.Random;

public class UI extends UIBase {
    protected static UI instance;

    private enum ShapeType {LINE, CIRCLE, ELLIPSE, RECTANGLE};

    private ShapeType shapeType;
    private Canvas canvas;

    private static final Color canvasColor = Color.rgb(45, 45, 48, 0.5);
    private static final Color borderColor = Color.GREY;

    private double x, y;
    private double width = 100;
    private double height = 200;

    private static final double MAX_WIDTH = 500;
    private static final double MAX_HEIGHT = 700;
    private static final double resizeValue = 5;
    private static final double moveValue = 5;

    private boolean isDragging = false;

    public static UI getInstance() {
        if (instance != null) return instance;
        instance = new UI();
        return instance;
    }

    private UI() {}

    public void setupUI(Pane root) {
        this.root = root;

        canvas = new Canvas(root.getWidth(), root.getHeight());
        root.getChildren().add(canvas);

        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());

        Random random = new Random();

        int shapeTypeIndex = random.nextInt(ShapeType.values().length);

        shapeType = ShapeType.values()[shapeTypeIndex];

        x = canvas.getWidth() / 2 - width;
        y = canvas.getHeight() / 2 - width;

        canvas.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                if (x <= mouseEvent.getX() && mouseEvent.getX() <= x + width
                        && y <= mouseEvent.getY() && mouseEvent.getY() <= y + height) {
                    isDragging = true;
                }
            }
        });

        canvas.setOnMouseReleased((mouseEvent -> isDragging = false));
        canvas.setOnMouseDragged(mouseEvent -> {
            if (isDragging) {
                x = mouseEvent.getX() - width / 2;
                y = mouseEvent.getY() - height / 2;
                repaint();
            }
        });

        root.widthProperty().addListener((observableValue, oldSize, newSize) -> {
            x *= newSize.doubleValue() / oldSize.doubleValue();
            repaint();
        });

        root.heightProperty().addListener((observableValue, oldSize, newSize) -> {
            y *= newSize.doubleValue() / oldSize.doubleValue();
            repaint();
        });

        root.getScene().setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case EQUALS -> {
                    if (height < MAX_HEIGHT &&
                        y + height + resizeValue < canvas.getHeight()) height += resizeValue;
                }
                case MINUS -> {
                    if (height > 0) height -= resizeValue;
                }
                case COMMA -> {
                    if (width > 0) width -= resizeValue;
                }
                case PERIOD -> {
                    if (width < MAX_WIDTH &&
                    x + width + resizeValue < canvas.getWidth()) width += resizeValue;
                }
                case W -> {
                    if (y > moveValue) y -= moveValue;
                }
                case S -> {
                    if (y < canvas.getHeight() - height - moveValue) y += moveValue;
                }
                case D -> {
                    if (x < canvas.getWidth() - width - moveValue) x += moveValue;
                }
                case A -> {
                    if (x > moveValue) x -= moveValue;
                }
            }
            repaint();
        });
        repaint();
    }

    private void repaint() {
        drawCanvasBackground();
        drawShape();
    }

    private void drawShape() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(3);
        gc.setStroke(Color.WHITE);
        switch (shapeType) {
            case LINE -> gc.strokeLine(x, y, x + width, y + height);
            case ELLIPSE -> gc.strokeOval(x, y, width, height);
            case RECTANGLE -> gc.strokeRect(x, y, width, height);
            case CIRCLE -> {
                double xCenter = (2*x + width) / 2;
                double yCenter = (2*y + height) / 2;
                double radius = Math.min(width, height) / 2;
                gc.strokeOval(xCenter - radius, yCenter - radius,
                             2 * radius, 2 * radius);
            }
        }
        gc.setStroke(borderColor);
        gc.setLineDashes(6);
        gc.strokeRect(x, y, width, height);
        gc.setLineDashes(0);
    }

    private void drawCanvasBackground() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(canvasColor);
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        gc.fillRect(0, 0, width, height);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(0.5);
        for (double x = 0; x < width; x += width / 30) {
            gc.strokeLine(x, 0, x, height);
        }
        for (double y = 0; y < height; y += height / (30 / (width / height))) {
            gc.strokeLine(0, y, width, y);
        }
    }
}
