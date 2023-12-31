package predefined;

import base.Drawer;
import base.LSystem;

import java.awt.*;

public class SierpinskiTriangle extends Drawer {
    public SierpinskiTriangle() {
        super("F-F-F", new LSystem(
            new char[]{'F', 'G'},
            new String[]{"F-G+F+G-F", "GG"}
        ));
    }

    @Override
    protected void paint(Graphics2D canvas) {
        canvas.translate(0, 5);
        canvas.scale(1, -1);
        paintBasic(canvas, 'F', 'G');
    }
}
