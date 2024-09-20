package org.dstu.lab1fx;

import javafx.application.Application;
import javafx.scene.image.Image;
import org.dstu.lab1fx.task1.UI;

import java.util.Objects;

public abstract class ApplicationBase extends Application {
    protected static final int WINDOW_WIDTH = 1000;
    protected static final int WINDOWS_HEIGHT = 600;
    protected static final int MIN_WIDTH = 500;
    protected static final int MIN_HEIGHT = 500;
    protected Image stageIcon;
    protected String stylesheetResource;
    public ApplicationBase() {
        stageIcon = new Image(Objects.requireNonNull(ApplicationBase.class.getResourceAsStream("icons/dstu.png")));
        stylesheetResource = Objects.requireNonNull(UI.class.getResource("css/style.css")).toExternalForm();
    }
}
