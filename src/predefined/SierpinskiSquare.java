package predefined;

import base.Drawer;
import base.LSystem;

import java.awt.*;

public class SierpinskiSquare extends Drawer {
    public SierpinskiSquare() {
        super("F+XF+F+XF", new LSystem(
            new char[]{'X'},
            new String[]{"XF-F+F-XF+F+XF-F+F-X"}
        ));
    }

    @Override
    protected void paintComponent(Graphics2D canvas) {
        canvas.translate(0, STEP/Math.sqrt(2) + 2);
        canvas.rotate(-ANGLE/2);
        paintBasic(canvas);
    }
}
