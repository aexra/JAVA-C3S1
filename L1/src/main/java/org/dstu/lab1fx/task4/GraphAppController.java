package org.dstu.lab1fx.task4;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;

import javafx.scene.chart.XYChart;
import javafx.scene.control.*;


public class GraphAppController {
    @FXML private LineChart<Number, Number> lineChart;
    @FXML private ToggleGroup functionToggleGroup = new ToggleGroup();
    @FXML private ToggleGroup visibilityToggleGroup = new ToggleGroup();
    @FXML private TextField minFuncArg, maxFuncArg;
    @FXML private TextField stepsField;

    private enum Function {SIN, SINCOS2, SINSQR};
    private Function selectedFunction;

    private double minArg = -3.5;
    private double maxArg = 3.5;
    private int steps = 20;

    @FXML protected void initialize() {
        setData(Function.values());

        functionToggleGroup.selectedToggleProperty().addListener(event -> pickFunction());
        selectedFunction = Function.SIN;
    }

    private void setData(Function... functions) {
        // Removing old series
        try {
            for (Function function : functions) lineChart.getData().remove(function.ordinal());
        } catch (IndexOutOfBoundsException ignored) {}
        // Setting new series
        for (Function function : functions) {
            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            double gap = (maxArg - minArg) / steps;
            switch (function) {
                case SIN -> {
                    series.setName("sin(x)");
                    for (double x = minArg; x < maxArg; x += gap) {
                        series.getData().add(new XYChart.Data<>(x, Math.sin(x)));
                    }
                }
                case SINCOS2 -> {
                    series.setName("sin(x)+2cos(x)");
                    for (double x = minArg; x < maxArg; x += gap) {
                        series.getData().add(new XYChart.Data<>(x, Math.sin(x) + 2 * Math.cos(x)));
                    }
                }
                case SINSQR -> {
                    series.setName("sin(x)^2");
                    for (double x = minArg; x < maxArg; x += gap) {
                        series.getData().add(new XYChart.Data<>(x, Math.pow(Math.sin(x), 2)));
                    }
                }
            }
            lineChart.getData().add(function.ordinal(), series);
        }
    }

    @FXML private void changeSteps() {
        try {
            int newSteps = Integer.parseInt(stepsField.getText());
            if (newSteps > 5 && newSteps < 1000) {
                steps = newSteps;
                setData(selectedFunction);
            }
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @FXML private void changeMinArg() {
        try {
            double newMinArg = Double.parseDouble(minFuncArg.getText());
            if (newMinArg < maxArg) {
                minArg = newMinArg;
                setData(selectedFunction);
            }
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @FXML private void changeMaxArg() {
        try {
            double newMaxArg = Double.parseDouble(maxFuncArg.getText());
            if (newMaxArg > minArg) {
                maxArg = newMaxArg;
                setData(selectedFunction);
            }
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @FXML private void showGraph() {
        lineChart.getData().get(selectedFunction.ordinal()).getNode().setVisible(true);
    }

    @FXML private void hideGraph() {
        lineChart.getData().get(selectedFunction.ordinal()).getNode().setVisible(false);
    }

    private void pickFunction() {
        ToggleButton selectedToggle = ((ToggleButton) functionToggleGroup.getSelectedToggle());
        switch (selectedToggle.getId()) {
            case "sin" -> selectedFunction = Function.SIN;
            case "sincos2" -> selectedFunction = Function.SINCOS2;
            case "sinsqr" -> selectedFunction = Function.SINSQR;
        }

        if (lineChart.getData().get(selectedFunction.ordinal()).getNode().isVisible()) {
            visibilityToggleGroup.selectToggle(visibilityToggleGroup.getToggles().getFirst());
        } else {
            visibilityToggleGroup.selectToggle(visibilityToggleGroup.getToggles().get(1));
        }
    }
}
