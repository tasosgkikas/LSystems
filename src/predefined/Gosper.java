package predefined;

import base.Drawer;
import base.LSystem;

import java.awt.*;

public class Gosper extends Drawer {
    public Gosper() {
        super("A", new LSystem(
            new char[]{'A', 'B'},
            new String[]{"A-B--B+A++AA+B-", "+A-BB--B-A++A+B"}
        ));
    }

    @Override
    protected void paintComponent(Graphics2D canvas) {
        canvas.translate(getWidth()/4, getHeight()*2/3);
        canvas.rotate(Math.PI - ANGLE);
        paintBasic(canvas, 'A', 'B');
    }
}
