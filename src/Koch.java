import java.awt.Graphics2D;

abstract class Koch extends Drawer {
    protected Koch(String axiom) {
        super(axiom, new LSystem(
            new String[]{"F"},
            new String[]{"F+F--F+F"}
        ));
    }

    @Override
    protected void paintComponent(Graphics2D canvas) {
        for (char c : PRODUCT.toCharArray())
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

    static class Curve extends Koch {
        Curve() {
            super("F");
        }

        @Override
        protected void paintComponent(Graphics2D canvas) {
            canvas.translate(0, getHeight()-5);
            super.paintComponent(canvas);
        }
    }

    static class SnowFlake extends Koch {
        SnowFlake() {
            super("F--F--F");
        }

        @Override
        protected void paintComponent(Graphics2D canvas) {
            canvas.translate(getHeight()/3, getHeight()/3);
            super.paintComponent(canvas);
        }
    }
}
