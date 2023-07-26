import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumMap;

class FractalPlant extends Drawer {
    static final LSystem lSystem = new LSystem(
        new String[]{"X", "F"},
        new String[]{"F+[[X]-X]-F[-FX]+X", "FF"}
    );
    private String product;
    private int iterations, dx;
    private double da; // a for angle

    protected String produce(int iterations) {
        return lSystem.produce("X", iterations);
    }

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

        Deque<Double> stackA = new ArrayDeque<>();
        Deque<Integer> stackX = new ArrayDeque<>();

        double a = 0; // angle
        int x = 0;
        final int y = getHeight()/2;

        for (char c : product.toCharArray())
            switch (c) {
                case 'F' -> { // draw forward
                    canvas.drawLine(x, y, x + dx, y);
                    x += dx;
                }
                case '-' -> { // turn right
                    canvas.rotate(-da, x, y);
                    a -= da;
                }
                case '+' -> { // turn left
                    canvas.rotate(da, x, y);
                    a += da;
                }
                case '[' -> { // save
                    stackA.push(a);
                    stackX.push(x);
                }
                case ']' -> { // restore
                    double aNext = stackA.pop();
                    canvas.rotate(aNext - a, x, y);
                    a = aNext;
                    x = stackX.pop();
                }
            }
    }

    @Override
    public String toString() {
        return "FractalPlant{" +
                "dx=" + dx +
                ", da=" + da +
                '}';
    }
}
