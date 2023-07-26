import java.awt.*;

class SierpinskiArrowhead extends Drawer {
    public SierpinskiArrowhead() {
        super("A", new LSystem(
            new String[]{"A", "B"},
            new String[]{"B-A-B", "A+B+A"}
        ));
    }

    @Override
    protected void paintComponent(Graphics2D canvas) {
        canvas.translate(0, getHeight()-5);

        for (char c : PRODUCT.toCharArray())
            switch (c) {
                case 'A', 'B' -> { // draw forward
                    canvas.drawLine(0, 0, DX, 0);
                    canvas.translate(DX, 0);
                }
                case '+' -> // turn left
                    canvas.rotate(DA);
                case '-' -> // turn right
                    canvas.rotate(-DA);
            }
    }
}
