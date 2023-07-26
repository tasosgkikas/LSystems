import java.awt.*;

class DragonCurve extends Drawer {
    DragonCurve() {
        super("F", new LSystem(
            new String[]{"F", "G"},
            new String[]{"F+G", "F-G"}
        ));
    }

    @Override
    protected void paintComponent(Graphics2D canvas) {
        canvas.translate(getWidth()/2, getHeight()/2);
        for (char c : PRODUCT.toCharArray())
            switch (c) {
                case 'F', 'G' -> { // draw forward
                    canvas.drawLine(0, 0, STEP, 0);
                    canvas.translate(STEP, 0);
                }
                case '+' -> // turn left
                        canvas.rotate(ANGLE);
                case '-' -> // turn right
                        canvas.rotate(-ANGLE);
            }
    }
}
