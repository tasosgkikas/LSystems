package predefined;

import base.Drawer;
import base.LSystem;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayDeque;
import java.util.Deque;

public class FractalPlant extends Drawer {
    public FractalPlant() {
        super("X", new LSystem(
            new char[]{'X', 'F'},
            new String[]{"F+[[X]-X]-F[-FX]+X", "FF"}
        ));
    }

    @Override
    protected void paint(Graphics2D canvas) {
        Deque<AffineTransform> transformStack = new ArrayDeque<>();
        canvas.setPaint(new Color(70, 100, 0));
        canvas.rotate(Math.PI/6);
        for (char c : PRODUCT.toCharArray())
            switch (c) {
                case 'F' -> { // draw forward
                    canvas.drawLine(0, 0, STEP, 0);
                    canvas.translate(STEP, 0);
                }
                case '+' -> // turn left
                    canvas.rotate(ANGLE);
                case '-' -> // turn right
                    canvas.rotate(-ANGLE);
                case '[' -> // save
                    transformStack.push(canvas.getTransform());
                case ']' -> // restore
                    canvas.setTransform(transformStack.pop());
            }
    }
}
