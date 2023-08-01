package predefined;

import base.*;
import java.awt.Graphics2D;

public class DottedLine extends Drawer {
    public DottedLine() {
        super("F", new LSystem(
            new char[]{'F', 'X'},
            new String[]{"FXF", "XFX"}
        ));
    }

    @Override
    protected void paint(Graphics2D canvas) {
        canvas.translate(0, getHeight() / 2);
        double step = getWidth() / (Math.pow(3, getITERATIONS()));
        for (char c : PRODUCT.toCharArray()) {
            if (c == 'F')
                canvas.drawLine(0, 0, (int) step, 0);
            canvas.translate(step, 0);
        }
    }
}
