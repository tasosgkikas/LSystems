import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

abstract class Drawer extends JPanel {
    private final String AXIOM;
    private final LSystem LSYSTEM;
    private int ITERATIONS = -1;
    protected String PRODUCT; // the result of LSYSTEM.produce()
    protected int STEP; // number of pixels per drawn line
    protected double ANGLE; // rotation angle in radians

    public Drawer(String axiom, LSystem lSystem) {
        AXIOM = axiom;
        LSYSTEM = lSystem;
    }

    abstract protected void paintComponent(Graphics2D canvas);

    public void repaint(EnumMap<Parameter, Integer> paramValues) {
        int iterations = paramValues.get(Parameter.ITERATIONS);
        if (iterations != ITERATIONS) {
            PRODUCT = LSYSTEM.produce(AXIOM, iterations);
            ITERATIONS = iterations;
        }
        STEP = paramValues.get(Parameter.STEP);
        ANGLE = Math.toRadians(paramValues.get(Parameter.ANGLE));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (PRODUCT == null) return;

        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D)g;
        canvas.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        paintComponent(canvas);
    }

    private String fixPRODUCT(char[] forwardChars) {
        Map<Character, Character> substitute = new HashMap<>() {{
            for (char c : forwardChars) put(c, 'F');
        }};
        return PRODUCT.chars()
                .mapToObj(c -> (char) c)
                .map(c -> substitute.getOrDefault(c,c))
                .toString();
    }

    protected void paintBasic(Graphics2D canvas, char[] forwardChars) {
        for (char c : fixPRODUCT(forwardChars).toCharArray())
            switch (c) {
                case 'F' -> { // draw forward
                    canvas.drawLine(0, 0, STEP, 0);
                    canvas.translate(STEP, 0);
                }
                case '+' -> // turn left
                        canvas.rotate(-ANGLE);
                case '-' -> // turn right
                        canvas.rotate(+ANGLE);
            }
    }

    protected int getITERATIONS() {
        return ITERATIONS;
    }
}
