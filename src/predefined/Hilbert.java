package predefined;

import base.Drawer;
import base.LSystem;

import java.awt.*;

public class Hilbert extends Drawer {
    public Hilbert() {
        super("A", new LSystem(
            new char[]{'A', 'B'},
            new String[]{"+BF-AFA-FB+", "-AF+BFB+FA-"}
        ));
    }

    @Override
    protected void paintComponent(Graphics2D canvas) {
        canvas.translate(2, 2);
        paintBasic(canvas);
    }
}
