package base;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Drawer extends JPanel {
    protected String PRODUCT; // the result of LSYSTEM.produce()
    protected int STEP; // number of pixels per drawn line
    protected double ANGLE; // rotation angle in radians
    private final String AXIOM;
    private final LSystem LSYSTEM;
    private int ITERATIONS = -1;

    public Drawer(String axiom, LSystem lSystem) {
        AXIOM = axiom;
        LSYSTEM = lSystem;
    }

    public void runBy(JButton button) {
        toggleEnabledForParametersAnd(button);
        produce();
        repaint();
        toggleEnabledForParametersAnd(button);
    }

    private void toggleEnabledForParametersAnd(JButton button) {
        toggleEnabled(button);
        for (Parameter parameter : Parameter.values()) {
            toggleEnabled(parameter.slider);
            toggleEnabled(parameter.valueField);
        }
    }

    private void toggleEnabled(JComponent comp) {
        comp.setEnabled(!comp.isEnabled());
    }

    private void produce() {
        int newIterations = Parameter.ITERATIONS.getValue();
        if (ITERATIONS != newIterations) {
            ITERATIONS = newIterations;
            PRODUCT = LSYSTEM.produce(AXIOM, ITERATIONS);
        }
        STEP = Parameter.STEP.getValue();
        ANGLE = Math.toRadians(Parameter.ANGLE.getValue());
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (PRODUCT == null) return;

        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
        canvas.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        canvas.translate(0, getHeight());
        canvas.scale(1, -1);

        paint(canvas);
    }

    abstract protected void paint(Graphics2D canvas);

    protected void paintBasic(Graphics2D canvas, char... forwardChars) {
        for (char c : formatPRODUCT(forwardChars).toCharArray())
            switch (c) {
                case 'F' -> { // draw forward
                    canvas.drawLine(0, 0, STEP, 0);
                    canvas.translate(STEP, 0);
                }
                case '+' -> // turn left
                    canvas.rotate(ANGLE);
                case '-' -> // turn right
                    canvas.rotate(-ANGLE);
            }
    }

    private String formatPRODUCT(char... forwardChars) {
        Map<Character, Character> substitute = new HashMap<>() {{
            for (char c : forwardChars) put(c, 'F');
        }};
        return PRODUCT.chars()
                .mapToObj(c -> (char) c)
                .map(c -> substitute.getOrDefault(c, c))
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    protected int getITERATIONS() {
        return ITERATIONS;
    }
}
