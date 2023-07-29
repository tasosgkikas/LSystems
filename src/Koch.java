import java.awt.Graphics2D;

abstract class Koch extends Drawer {
    protected Koch(String axiom) {
        super(axiom, new LSystem(
            new char[]{'F'},
            new String[]{"F+F--F+F"}
        ));
    }

    @Override
    protected void paintComponent(Graphics2D canvas) {
        paintBasic(canvas, new char[]{'F'});
    }

    static class Curve extends Koch {
        Curve() {
            super("F");
        }

        @Override
        protected void paintComponent(Graphics2D canvas) {
            canvas.translate(0, 5);
            super.paintComponent(canvas);
        }
    }

    static class SnowFlake extends Koch {
        SnowFlake() {
            super("F--F--F");
        }

        @Override
        protected void paintComponent(Graphics2D canvas) {
            canvas.translate(getWidth()/3, getHeight()*2/3);
            super.paintComponent(canvas);
        }
    }
}
