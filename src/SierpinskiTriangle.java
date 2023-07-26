import java.awt.*;

public class SierpinskiTriangle extends Drawer{
    static final LSystem lSystem = new LSystem(
        new String[]{"F", "G"},
        new String[]{"F−G+F+G−F", "GG"}
    );

    @Override
    protected String produce(int iterations) {
        return lSystem.produce("F-F-F", iterations);
    }

    @Override
    protected void paintComponent(Graphics2D canvas) {

    }
}
