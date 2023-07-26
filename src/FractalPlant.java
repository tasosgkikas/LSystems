import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.Deque;

class FractalPlant extends Drawer {
    static final LSystem lSystem = new LSystem(
        new String[]{"X", "F"},
        new String[]{"F+[[X]-X]-F[-FX]+X", "FF"}
    );

    protected String produce(int iterations) {
        return lSystem.produce("X", iterations);
    }

    @Override
    protected void paintComponent(Graphics2D canvas) {
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
}
