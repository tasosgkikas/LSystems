import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.EnumMap;

class FractalPlant extends JPanel {
    static final LSystem lSystem = new LSystem(
            new String[]{"X", "F"},
            new String[]{"F+[[X]-X]-F[-FX]+X", "FF"}
    );
    private String product;
    private int step, angle;

    String produce(int iterations) {
        return lSystem.produce("X", iterations);
    }

    public void repaint(EnumMap<Parameter, Integer> paramValues) {
        this.product = produce(paramValues.get(Parameter.ITERATIONS));
        this.step = paramValues.get(Parameter.STEP);
        this.angle = paramValues.get(Parameter.ANGLE);
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        if (product == null) return;

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2d.drawRoundRect(
                getWidth()/3,
                getHeight()/3,
                getWidth()/3,
                getHeight()/3,
                100, 50
        );
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "FractalPlant{" +
                "step=" + step +
                ", angle=" + angle +
                '}';
    }
}
