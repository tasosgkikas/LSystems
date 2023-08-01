package base;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Drawer extends JPanel {
    private final String AXIOM;
    private final LSystem LSYSTEM;
    private int ITERATIONS = -1;
    private int PREVIOUS_ITERATIONS = -1;
    protected String PRODUCT; // the result of LSYSTEM.produce()
    protected int STEP; // number of pixels per drawn line
    protected double ANGLE; // rotation angle in radians

    public Drawer(String axiom, LSystem lSystem) {
        AXIOM = axiom;
        LSYSTEM = lSystem;
    }

    public void runBy(JButton button) {
        toggleEnableControls(button);
        initParams();
        produce();
        repaint();
        toggleEnableControls(button);
    }

    private void toggleEnableControls(JButton button) {
        toggleEnabled(button);
        for (Parameter parameter : Parameter.values()) {
            toggleEnabled(parameter.slider);
            toggleEnabled(parameter.valueField);
        }
    }

    private void toggleEnabled(JComponent comp) {
        comp.setEnabled(!comp.isEnabled());
    }

    private void initParams() {
        PREVIOUS_ITERATIONS = ITERATIONS;
        ITERATIONS = Parameter.ITERATIONS.getValue();
        STEP = Parameter.STEP.getValue();
        ANGLE = Math.toRadians(Parameter.ANGLE.getValue());
    }

    private void produce() {
        if (ITERATIONS != PREVIOUS_ITERATIONS)
            PRODUCT = LSYSTEM.produce(AXIOM, ITERATIONS);
    }

    abstract protected void paintComponent(Graphics2D canvas);

    @Override
    protected void paintComponent(Graphics g) {
        if (PRODUCT == null) return;

        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D)g;
        canvas.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        canvas.translate(0, getHeight());
        canvas.scale(1, -1);

        paintComponent(canvas);
    }

    private String fixPRODUCT(char... forwardChars) {
        Map<Character, Character> substitute = new HashMap<>() {{
            for (char c : forwardChars) put(c, 'F');
        }};
        return PRODUCT.chars()
                .mapToObj(c -> (char) c)
                .map(c -> substitute.getOrDefault(c,c))
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    protected void paintBasic(Graphics2D canvas, char... forwardChars) {
        for (char c : fixPRODUCT(forwardChars).toCharArray())
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

    protected int getITERATIONS() {
        return ITERATIONS;
    }
}
