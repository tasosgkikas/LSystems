import java.awt.Graphics2D;

public class SierpinskiTriangle extends Drawer{
    SierpinskiTriangle() {
        super("F-F-F", new LSystem(
            new char[]{'F', 'G'},
            new String[]{"F-G+F+G-F", "GG"}
        ));
    }

    @Override
    protected void paintComponent(Graphics2D canvas) {
        canvas.translate(0, getHeight()-5);
        paintBasic(canvas, new char[]{'F', 'G'});
    }
}
