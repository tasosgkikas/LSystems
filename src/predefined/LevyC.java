package predefined;

import base.Drawer;
import base.LSystem;

import java.awt.*;

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
