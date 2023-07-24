import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        tokens.replaceAll((String token) -> rule.getOrDefault(token, token));
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
        // dragon curve
        LSystem lSystem = new LSystem(
            new String[]{"F", "G"},
            new String[]{"F+G", "F-G"}
        );
        System.out.println(lSystem);

        String product = lSystem.produce("F", 3);
        System.out.println(product);
    }
}

class FractalPlant extends JPanel {
    static final LSystem lSystem = new LSystem(
        new String[]{"X", "F"},
        new String[]{"F+[[X]-X]-F[-FX]+X", "FF"}
    );
    private String product;
    private int step, angle;

    String produce(int iterations) {
        return lSystem.produce("X", iterations);
    }

    public void repaint(EnumMap<App.Parameter, Integer> paramValues) {
        this.product = produce(paramValues.get(App.Parameter.ITERATIONS));
        this.step = paramValues.get(App.Parameter.STEP);
        this.angle = paramValues.get(App.Parameter.ANGLE);
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        if (product == null) return;

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2d.drawRoundRect(
                getWidth()/3,
                getHeight()/3,
                getWidth()/3,
                getHeight()/3,
                100, 50
        );
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "FractalPlant{" +
                "step=" + step +
                ", angle=" + angle +
                '}';
    }
}

public class App extends JFrame {
    enum Parameter {
        ITERATIONS("Iterations", 1, 10),
        STEP("Forward step (pixels)", 1, 20, 1),
        ANGLE("Angle (degrees)", 10, 90);
        private final JLabel nameLabel;
        private JSlider slider;
        private JLabel valueLabel;
        Parameter(String name, int min, int max, int value) {
            nameLabel = new JLabel(name);
            slider = new JSlider(min, max, value) {{
                addChangeListener( (ChangeEvent e) ->
                    valueLabel.setText(String.valueOf(slider.getValue()))
                );
            }};
            valueLabel = new JLabel(String.valueOf(value));
        }
        Parameter(String name, int min, int max) {
            this(name, min, max, (min + max) / 2);
        }
        int getValue() {
            return slider.getValue();
        }

        @Override
        public String toString() {
            return "Parameter{" +
                    "nameLabel=" + nameLabel.getText() +
                    ", slider=" + slider.getModel() +
                    ", valueLabel=" + valueLabel.getText() +
                    '}';
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(App::new);
    }

    App() {
        super("Fractal Plant");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        buildFrame();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        addWindowStateListener(e -> {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            if (e.getNewState() == Frame.NORMAL)
                setSize(
                    2*screenSize.width/3,
                    2*screenSize.height/3
                );
        });
    }

    private void buildFrame() {
        var plotPanel = new FractalPlant();

        // run button creation
        JButton runButton = new JButton("Run") {{
            addActionListener( (ActionEvent e) -> {
                plotPanel.repaint(
                    new EnumMap<>(
                        Stream.of(Parameter.values())
                        .collect(Collectors.toMap(
                            UnaryOperator.identity(),
                            Parameter::getValue
                        ))
                    )
                );
            });
        }};

        // control panel in main frame
        add(new JPanel(new BorderLayout()) {{
            // parameters panel in control panel
            add(new JPanel(new BorderLayout()) {{
                // fields of class Parameter (ie nameLabel, slider...)
                Field[] fields = Stream.of(Parameter.class.getDeclaredFields())
                    .filter(field -> !field.isEnumConstant() && !field.isSynthetic())
                    .toArray(Field[]::new);

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
                    // field panel in parameters panel
                    add(new JPanel() {{
                        // vertical layout of the field panel
                        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
                        for (Parameter parameter : Parameter.values()) try {
                            // field of parameter in field panel
                            add((Component) field.get(parameter));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }}, position.get(field)); // field panel in parameters panel
            }}, BorderLayout.CENTER); // parameters panel in control panel

            // runButton in control panel
            add(runButton, BorderLayout.LINE_END);

        }}, BorderLayout.PAGE_END); // control panel in main frame

        // add plotPanel to main frame
        add(plotPanel, BorderLayout.CENTER);
    }
}
