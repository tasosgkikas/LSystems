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
        for (char c : product.toCharArray())
            switch (c) {
                case 'F' -> { // draw forward
                    canvas.drawLine(0, 0, dx, 0);
                    canvas.translate(dx, 0);
                }
                case '+' -> // turn left
                    canvas.rotate(-da);
                case '-' -> // turn right
                    canvas.rotate(+da);
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
