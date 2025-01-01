package org.hometree.workspacefx.plugin.clock;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

public class AnalogClockWithClockwork extends StackPane {

    private final double unit = 100.0;

    private final AnalogClockClockwork clockwork = new AnalogClockClockwork();

    public AnalogClockWithClockwork() {
        Group g = new Group(outerRim(),
                minuteHand(),
                hourHand(),
                secondsHand(),
                tickMarks(),
                centerPoint());
        getChildren().add(g);
    }

    private Node hourHand() {
        Rotate rotate = rotationAroundCenter();
        rotate.angleProperty().bind(clockwork.hour.multiply(360 / 60));
        Line line = new Line(unit, unit * 1.1, unit, unit * 0.4);
        line.getTransforms().add(rotate);
        line.setStrokeWidth(6);
        return line;
    }

    private Node minuteHand() {
        Rotate rotate = rotationAroundCenter();
        rotate.angleProperty().bind(clockwork.minute.multiply(360 / 60));
        Line line = new Line(unit, unit * 1.1, unit, unit * 0.3);
        line.getTransforms().add(rotate);
        line.setStrokeWidth(4);
        return line;
    }

    private Node secondsHand() {
        Rotate rotate = rotationAroundCenter();
        rotate.angleProperty().bind(clockwork.second.multiply(360 / 60));
        Line line = new Line(unit, unit * 1.1, unit, unit * 0.2);
        line.getTransforms().add(rotate);
        return line;
    }

    private Rotate rotationAroundCenter() {
        return new Rotate(0.0, unit, unit);
    }

    private Node tickMarks() {
        Group tickMarkGroup = new Group();
        for (int n = 0; n < 12; n++) {
            tickMarkGroup.getChildren().add(tickMark(n));
        }
        return tickMarkGroup;
    }

    private Node tickMark(int n) {
        double angle = 360d / 12 * n;
        Rotate rotate = new Rotate(angle, unit, unit);
        Line line = new Line(unit, unit * 0.12, unit, unit * (n % 3 == 0 ? 0.3 : 0.2));
        line.getTransforms().add(rotate);
        return line;
    }

    private Node centerPoint() {
        double radius = 0.05 * unit;
        return new Circle(unit, unit, radius, Color.BLACK);
    }

    private Node outerRim() {
        double centerX = 0.5;
        double centerY = 0.5;
        double radius = 0.5;
        Circle circle = new Circle(unit, unit, unit);
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);
        return circle;
    }
}