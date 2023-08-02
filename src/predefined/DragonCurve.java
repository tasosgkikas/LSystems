package predefined;

import base.Drawer;
import base.LSystem;

import java.awt.*;

public class DragonCurve extends Drawer {
    public DragonCurve() {
        super("F", new LSystem(
            new char[]{'F', 'G'},
            new String[]{"F+G", "F-G"}
        ));
    }

    @Override
    protected void paint(Graphics2D canvas) {
        canvas.translate(getWidth()/2, getHeight()/2);
        paintBasic(canvas, 'F', 'G');
    }
}
