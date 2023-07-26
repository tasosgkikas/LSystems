import java.awt.Graphics2D;

public class SierpinskiTriangle extends Drawer{
    SierpinskiTriangle() {
        super("F-F-F", new LSystem(
            new String[]{"F", "G"},
            new String[]{"F-G+F+G-F", "GG"}
        ));
    }

    @Override
    protected void paintComponent(Graphics2D canvas) {
        canvas.translate(0, getHeight()-5);

        for (char c : PRODUCT.toCharArray())
            switch (c) {
                case 'F', 'G' -> { // draw forward
                    canvas.drawLine(0, 0, DX, 0);
                    canvas.translate(DX, 0);
                }
                case '+' -> // turn left
                    canvas.rotate(DA);
                case '-' -> // turn right
                    canvas.rotate(-DA);
            }
    }
}
