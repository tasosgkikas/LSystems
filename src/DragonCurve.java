import java.awt.*;

class DragonCurve extends Drawer {
    DragonCurve() {
        super("F", new LSystem(
            new char[]{'F', 'G'},
            new String[]{"F+G", "F-G"}
        ));
    }

    @Override
    protected void paintComponent(Graphics2D canvas) {
        canvas.translate(getWidth()/2, getHeight()/2);
        paintBasic(canvas, new char[]{'F', 'G'});
    }
}
