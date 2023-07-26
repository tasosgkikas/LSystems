import java.awt.Graphics2D;

public class SierpinskiTriangle extends Drawer{
    SierpinskiTriangle() {
        super("F-F-F", new LSystem(
            new String[]{"F", "G"},
            new String[]{"F−G+F+G−F", "GG"}
        ));
    }

    @Override
    protected void paintComponent(Graphics2D canvas) {

    }
}
