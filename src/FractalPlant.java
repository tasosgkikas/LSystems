import java.awt.Graphics2D;
import java.util.ArrayDeque;
import java.util.Deque;

class FractalPlant extends Drawer {
    FractalPlant() {
        super("X", new LSystem(
            new String[]{"X", "F"},
            new String[]{"F+[[X]-X]-F[-FX]+X", "FF"}
        ));
    }

    @Override
    protected void paintComponent(Graphics2D canvas) {
        Deque<Double> stackA = new ArrayDeque<>();
        Deque<Integer> stackX = new ArrayDeque<>();

        double a = 0; // angle
        int x = 0;
        final int y = getHeight()/2;

        for (char c : PRODUCT.toCharArray())
            switch (c) {
                case 'F' -> { // draw forward
                    canvas.drawLine(x, y, x + STEP, y);
                    x += STEP;
                }
                case '-' -> { // turn right
                    canvas.rotate(-ANGLE, x, y);
                    a -= ANGLE;
                }
                case '+' -> { // turn left
                    canvas.rotate(ANGLE, x, y);
                    a += ANGLE;
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
