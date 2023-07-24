import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Ellipse2D;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

class LSystem {
    Map<String, String> rule;

    LSystem(String[] variables, String[] mappings) {
        if (variables.length != mappings.length)
            throw new IllegalArgumentException("Argument arrays must have the same length.");

        rule = new HashMap<>();
        for (int i = 0; i < variables.length; i++)
            rule.put(variables[i], mappings[i]);
    }

    String produce(String axiom, int iterations) {
        if (iterations == 0) return axiom;

        List<String> tokens = Arrays.asList(axiom.split(""));
        tokens.replaceAll((String token) -> {
            String substitute = rule.get(token);
            return substitute != null ? substitute : token;
        });
        axiom = String.join("", tokens);

        return produce(axiom, iterations - 1);
    }

    @Override
    public String toString() {
        return "LSystem{" +
                "rule=" + rule +
                '}';
    }

    public static void main(String[] args) {
        System.out.println((new LSystem( // dragon curve
                    new String[]{"F", "G"},
                    new String[]{"F+G", "F-G"}
            )).produce("F", 3)
        );
    }
}

abstract class Drawer extends JPanel {
    protected static class CommandEnum {
        private static final List<CommandEnum> values = new ArrayList<>();

        protected CommandEnum() {
            values.add(this);
        }

        public static List<CommandEnum> values() {
            return List.copyOf(values);
        }
    }

    protected LSystem lSystem;
    protected Map<String, CommandEnum> commandMap;

    protected Drawer(
            LSystem lSystem,
            String[] commandStrings,
            Class<? extends CommandEnum> Command
    ) throws IllegalArgumentException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {

        System.out.println(Command);
        if (lSystem == null)
            throw new IllegalArgumentException(
                "lSystem argument must not be null"
            );
        if (commandStrings == null || commandStrings.length == 0)
            throw new IllegalArgumentException(
                "commandStrings argument must not be null or empty"
            );

        Method valuesMethod = Command.getDeclaredMethod("values");
        CommandEnum[] commandValues = (CommandEnum[]) valuesMethod.invoke(null);
        if (commandValues.length != commandStrings.length)
            throw new IllegalArgumentException(
                "number of CommandEnum constants must be equal to length of commandStrings"
            );

        this.lSystem = lSystem;
        this.commandMap = new HashMap<>() {{
            for (int i = 0; i < commandStrings.length; i++)
                put(commandStrings[i], commandValues[i]);
        }};
    }

    @Override
    public String toString() {
        return "Drawer{" +
                "lSystem=" + lSystem +
                ", Command=" + CommandEnum.values().toString() +
                ", commandMap=" + commandMap +
                '}';
    }
}

class TestDrawer extends Drawer {
    static class Command extends CommandEnum {
        static final Command FORWARD = new Command();
        static final Command RIGHT = new Command();
        static final Command LEFT = new Command();
        static final Command SAVE = new Command();
        static final Command RESTORE = new Command();
    }

    protected TestDrawer()
            throws IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, IllegalAccessException {

        super(
                new LSystem(
                        new String[]{"X", "F"},
                        new String[]{"F+[[X]-X]-F[-FX]+X", "FF"}
                ),
                new String[] {"F", "-", "+", "[", "]"},
                Command.class
        );
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        System.out.println(this);
    }

    public static void main(String[] args)
            throws IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, IllegalAccessException {

        (new TestDrawer()).repaint();
    }
}

class FractalPlant {
    LSystem lSystem = new LSystem(
            new String[]{"X", "F"},
            new String[]{"F+[[X]-X]-F[-FX]+X", "FF"}
    );
    enum Command {FORWARD, RIGHT, LEFT, SAVE, RESTORE};
    Map<String, Command> cmd = Map.of(
        "F", Command.FORWARD,
        "-", Command.RIGHT,
        "+", Command.LEFT,
        "[", Command.SAVE,
        "]", Command.RESTORE
    );

    FractalPlant() {
    }


}

public class App extends JFrame {
    private static class Control { // controls παραμέτρων
        private final JLabel nameLabel;
        private JSlider slider;
        private JLabel valueLabel;
        Control(String name, int min, int max, int value) {
            nameLabel = new JLabel(name);
            slider = new JSlider(min, max, value) {{
                addChangeListener( (ChangeEvent e) ->
                    valueLabel.setText(String.valueOf(slider.getValue()))
                );
            }};
            valueLabel = new JLabel(String.valueOf(value));
        }
        Control(String name, int min, int max) {
            this(name, min, max, (min + max) / 2);
        }
        Control(String name) {
            this(name, 1, 10);
        }
        int getValue() {
            return slider.getValue();
        }

        @Override
        public String toString() {
            return "Control{" +
                    "nameLabel=" + nameLabel.getText() +
                    ", slider=" + slider.getAccessibleContext() +
                    ", valueLabel=" + valueLabel.getText() +
                    '}';
        }
    }

    class BarnsleyFern extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D)g;
            g2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            double x = 0, y = 0;
            double scale = 120;
            double panelX = y * scale, panelY = x * scale + getHeight()/2.0;

            g2d.setPaint(Color.GREEN);
            int strokeWidth = step.getValue();
            g2d.fill(new Ellipse2D.Double(
                    panelX - strokeWidth/2.0, panelY - strokeWidth/2.0,
                    strokeWidth, strokeWidth
            ));
        }
    }

    Control numIters, step;

    public static void main(String[] args) {
        EventQueue.invokeLater(App::new);
    }

    App() {
        super("Fractal Plant");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        buildFrame();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void buildFrame() {
        // BarnsleyFern: εμφάνιση του fractal
//        JPanel barnsleyFern = new BarnsleyFern();

        // controls creation
        Control[] controls = {
            new Control("Iterations", 1, 10),
            new Control("Forward step (pixels)", 1, 20, 1),
            new Control("Angle (degrees)", 10, 90)
        };

        // run button creation
        JButton runButton = new JButton("Run") {{
            addActionListener( (ActionEvent e) -> {
//                barnsleyFern.repaint();
                for (Control control: controls)
                    System.out.println(control);
            });
        }};

        // panel with controls and run button
        add(new JPanel(new BorderLayout()) {{
            // controls panel
            add(new JPanel(new BorderLayout()) {{
                // fields of class Control (ie nameLabel, slider...)
                Field[] fields = Control.class.getDeclaredFields();

                // field panel positions
                Object[] horizontalPositions = {
                        BorderLayout.LINE_START,
                        BorderLayout.CENTER,
                        BorderLayout.LINE_END
                };

                // (field: position) map (ie slider: CENTER)
                Map<Field, Object> position = new HashMap<>() {{
                    for (int i = 0; i < fields.length; i++)
                        put(fields[i], horizontalPositions[i]);
                }};

                for (Field field : fields)
                    // adding panel for this field of every control
                    add(new JPanel() {{
                        // vertical layout of the values of this field
                        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                        for (Control control : controls) try {
                            // adding vertically the field of this control
                            add((Component) field.get(control));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }}, position.get(field));
            }}, BorderLayout.CENTER);

            add(runButton, BorderLayout.LINE_END);

        }}, BorderLayout.PAGE_END);

        // Main frame: προσθήκη του control panel και του plot panel
//        add(barnsleyFern, BorderLayout.CENTER);
    }
}
