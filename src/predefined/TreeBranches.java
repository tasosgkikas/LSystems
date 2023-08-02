package predefined;

import base.Drawer;
import base.LSystem;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayDeque;
import java.util.Deque;

public class TreeBranches extends Drawer {
    public TreeBranches() {
        super("X", new LSystem(
            new char[]{'X', 'F'},
            new String[]{"F+++[X]--[X]--[X]--X", "FF"}
        ));
    }

    @Override
    protected void paint(Graphics2D canvas) {
        canvas.translate(getWidth()/2, 0);
        canvas.setPaint(new Color(70, 100, 0));
        Deque<AffineTransform> transformStack = new ArrayDeque<>();
        for (char c : PRODUCT.toCharArray())
            switch (c) {
                case 'F' -> { // draw forward
                    canvas.drawLine(0, 0, 0, STEP);
                    canvas.translate(0, STEP);
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
