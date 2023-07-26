import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.EnumMap;

abstract class Drawer extends JPanel {
    protected String product;
    protected int iterations, dx;
    protected double da; // a for angle

    abstract protected String produce(int iterations);
    abstract protected void paintComponent(Graphics2D canvas);

    public void repaint(EnumMap<Parameter, Integer> paramValues) {
        int iterations = paramValues.get(Parameter.ITERATIONS);
        if (iterations != this.iterations) {
            this.product = produce(iterations);
            this.iterations = iterations;
        }
        this.dx = paramValues.get(Parameter.STEP);
        this.da = Math.toRadians(paramValues.get(Parameter.ANGLE));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (product == null) return;

        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D)g;
        canvas.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        paintComponent(canvas);
    }
}
