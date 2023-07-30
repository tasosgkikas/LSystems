package predefined;

import base.Drawer;
import base.LSystem;

import java.awt.*;

public class Moore extends Drawer {
    public Moore() {
        super("LFL+F+LFL", new LSystem(
            new char[]{'L', 'R'},
            new String[]{"-RF+LFL+FR-", "+LF-RFR-FL+"}
        ));
    }

    @Override
    protected void paintComponent(Graphics2D canvas) {
        canvas.translate(getWidth()/2, getHeight()/2);
        paintBasic(canvas);
    }
}
