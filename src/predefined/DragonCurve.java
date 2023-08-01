package predefined;

import base.*;
import java.awt.Graphics2D;

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
