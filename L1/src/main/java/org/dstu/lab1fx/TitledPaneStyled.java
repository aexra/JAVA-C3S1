package org.dstu.lab1fx;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;

public class TitledPaneStyled extends TitledPane {
    public TitledPaneStyled(String s, Node node) {
        super(s, node);
        setAlignment(Pos.CENTER);
        setCollapsible(false);
        getContent().setStyle("-fx-background-color: black");
    }
}
