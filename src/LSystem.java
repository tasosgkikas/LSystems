import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Ellipse2D;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;

class Producer {
    Map<String, String> rule;

    Producer(String[] variables, String[] mappings) {
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

    public static void main(String[] args) {
        System.out.println(
            (new Producer( // dragon curve
                           new String[]{"F", "G"},
                           new String[]{"F+G", "F-G"}
            )).produce("F", 3)
        );
    }
}

class Drawer extends Producer {
    enum Command {FORWARD, RIGHT, LEFT, SAVE, RESTORE};
    Map<String, Command> cmd = Map.of(
        "F", Command.FORWARD,
        "-", Command.RIGHT,
        "+", Command.LEFT,
        "[", Command.SAVE,
        "]", Command.RESTORE
    );
    double angleRads;


    Drawer() {
        super(
            new String[]{"X", "F"},
            new String[]{"F+[[X]-X]-F[-FX]+X", "FF"}
        );
    }


}

public class LSystem extends JFrame {
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
        public int getValue() {
            return slider.getValue();
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

            double x = 0, y = 0, N = Math.pow(10, numIters.getValue());
            for (int n = 0; n < N; n++) {
                double r = Math.random();
                if (r < 0.1) {
                    x = 0.0;
                    y = 0.16 * y;
                }
                else if (r < 0.86) {
                    x = 0.85 * x + 0.04 * y;
                    y = -0.04 * x + 0.85 * y + 1.6;
                }
                else if (r < 0.93) {
                    x = 0.2 * x - 0.26 * y;
                    y = 0.23 * x + 0.22 * y + 1.6;
                }
                else {
                    x = -0.15 * x + 0.28 * y;
                    y = 0.26 * x + 0.24 * y + 0.44;
                }

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
    }

    Control numIters, step;

    public static void main(String[] args) {
        EventQueue.invokeLater(LSystem::new);
    }

    LSystem() {
        super("Fractal Plant");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        buildFrame();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void buildFrame() {
        // BarnsleyFern: εμφάνιση του fractal
        JPanel barnsleyFern = new BarnsleyFern();

        // δημιουργία controls
        Control[] controls = {
            new Control("Iterations", 1, 10),
            new Control("Forward step (pixels)", 1, 20, 5),
            new Control("Angle (degrees)", 10, 90)
        };

        // δημιουργία run button
        JButton runButton = new JButton("Run") {{
            addActionListener( (ActionEvent e) -> {
                barnsleyFern.repaint();
            });
        }};

        // panel με controls παραμέτρων και run
        JPanel ctrlPanel = new JPanel(new BorderLayout()) {{
            Field[] fields = Control.class.getDeclaredFields();

            Object[] horizontalConstraints = {
                    BorderLayout.LINE_START,
                    BorderLayout.CENTER,
                    BorderLayout.LINE_END
            };

            Map<Field, Object> position = new HashMap<>() {{
                for (int i = 0; i < fields.length; i++)
                    put(fields[i], horizontalConstraints[i]);
            }};

            JPanel ctrl = new JPanel(new BorderLayout()) {{
                for (Field field : fields)
                    // panel με controls παραμέτρων
                    add(new JPanel(new GridLayout(fields.length, 1)) {{
                        for (Control control : controls) try {
                            add((Component) field.get(control));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }}, position.get(field));
            }};

            add(ctrl, BorderLayout.CENTER);
            add(runButton, BorderLayout.LINE_END);
        }};

        // Main frame: προσθήκη του control panel και του plot panel
//        add(barnsleyFern, BorderLayout.CENTER);
        add(ctrlPanel, BorderLayout.PAGE_END);
    }
}
