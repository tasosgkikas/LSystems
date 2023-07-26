import javax.swing.JPanel;
import java.util.EnumMap;

abstract class Drawer extends JPanel {
    abstract protected String produce(int iterations);
    abstract public void repaint(EnumMap<Parameter, Integer> paramValues);
}
