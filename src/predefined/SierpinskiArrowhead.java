package predefined;

import base.Drawer;
import base.LSystem;

import java.awt.*;

public class SierpinskiArrowhead extends Drawer {
    public SierpinskiArrowhead() {
        super("A", new LSystem(
            new char[]{'A', 'B'},
            new String[]{"B-A-B", "A+B+A"}
        ));
    }

    @Override
    protected void paint(Graphics2D canvas) {
        if (getITERATIONS() % 2 == 1) ANGLE = -ANGLE;

        canvas.translate(0, 5);
        paintBasic(canvas, 'A', 'B');
    }
}
