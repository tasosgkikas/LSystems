import java.awt.*;

class SierpinskiArrowhead extends Drawer {
    public SierpinskiArrowhead() {
        super("A", new LSystem(
            new char[]{'A', 'B'},
            new String[]{"B-A-B", "A+B+A"}
        ));
    }

    @Override
    protected void paintComponent(Graphics2D canvas) {
        if (getITERATIONS() % 2 == 0) ANGLE = -ANGLE;

        canvas.translate(0, getHeight()-5);
        paintBasic(canvas, new char[]{'A', 'B'});
    }
}
