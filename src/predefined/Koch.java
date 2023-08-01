package predefined;

import base.*;
import java.awt.Graphics2D;

public abstract class Koch extends Drawer {
    protected Koch(String axiom) {
        super(axiom, new LSystem(
            new char[]{'F'},
            new String[]{"F+F--F+F"}
        ));
    }

    @Override
    protected void paint(Graphics2D canvas) {
        paintBasic(canvas, 'F');
    }

    public static class Curve extends Koch {
        public Curve() {
            super("F");
        }

        @Override
        protected void paint(Graphics2D canvas) {
            canvas.translate(0, 5);
            super.paint(canvas);
        }
    }

    public static class SnowFlake extends Koch {
        public SnowFlake() {
            super("F--F--F");
        }

        @Override
        protected void paint(Graphics2D canvas) {
            canvas.translate(getWidth()/3, getHeight()*2/3);
            super.paint(canvas);
        }
    }
}
