package base;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public abstract class Drawer extends JPanel {
    protected String PRODUCT; // the result of LSYSTEM.produce()
    protected int STEP; // number of pixels per drawn line
    protected double ANGLE; // rotation angle in radians
    private final String AXIOM;
    private final LSystem LSYSTEM;
    private BufferedImage IMAGE;
    private int ITERATIONS = -1;
    private boolean PRODUCE; // true if different iterations
    private boolean REPAINT; // true if different iterations or step or angle

    public Drawer(String axiom, LSystem lSystem) {
        AXIOM = axiom;
        LSYSTEM = lSystem;
    }

    public void runBy(JButton button) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CompletableFuture.runAsync(() -> {
            toggleEnabledForParametersAnd(button);
            initParams();
            if (PRODUCE) PRODUCT = LSYSTEM.produce(AXIOM, ITERATIONS);
        }, executor)
        .thenRunAsync(() -> {
            if (REPAINT) drawImage();
        }, executor)
        .thenRunAsync(() -> {
            if (REPAINT) repaint();
            toggleEnabledForParametersAnd(button);
        }, executor);
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

    private void initParams() {
        int newIterations = Parameter.ITERATIONS.getValue();
        int newStep = Parameter.STEP.getValue();
        double newAngle = Math.toRadians(Parameter.ANGLE.getValue());

        PRODUCE = (newIterations != ITERATIONS);
        REPAINT = (PRODUCE || newStep != STEP || newAngle != ANGLE);

        ITERATIONS = newIterations;
        STEP = newStep;
        ANGLE = newAngle;
    }

    private void drawImage() {
        IMAGE = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = (Graphics2D) IMAGE.getGraphics();
        graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        graphics2D.translate(0, getHeight());
        graphics2D.scale(1, -1);

        paint(graphics2D);
    }

    abstract protected void paint(Graphics2D canvas);

    @Override
    protected void paintComponent(Graphics g) {
        if (PRODUCT == null) return;

        Graphics2D graphics2D = (Graphics2D) g;
        super.paintComponent(graphics2D);
        graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        graphics2D.drawImage(IMAGE, 0, 0, null);
    }

    protected void paintBasic(Graphics2D canvas, char... forwardChars) {
        canvas.setPaint(Color.BLACK);
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
