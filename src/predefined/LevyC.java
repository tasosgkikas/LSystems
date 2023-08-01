package predefined;

import base.*;
import java.awt.Graphics2D;

public class LevyC extends Drawer {
    public LevyC() {
        super("F", new LSystem(
            new char[]{'F'},
            new String[]{"+F--F+"}
        ));
    }

    @Override
    protected void paint(Graphics2D canvas) {
        canvas.translate(getWidth()/4, getHeight()/4);
        paintBasic(canvas);
    }
}
