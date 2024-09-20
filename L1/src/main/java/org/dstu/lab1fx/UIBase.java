package org.dstu.lab1fx;

import javafx.scene.layout.Pane;

public abstract class UIBase {
    protected Pane root;
    protected static final int PADDING_SIZE = 25;
    public abstract void setupUI(Pane root);
}
