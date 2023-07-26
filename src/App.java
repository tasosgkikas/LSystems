import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App extends JFrame {
    private static class RunButton extends JButton {
        public RunButton(Drawer plotPanel) {
            super("Run");
            addActionListener( (ActionEvent e) -> plotPanel.repaint(
                new EnumMap<>(
                    Stream.of(Parameter.values())
                    .collect(Collectors.toMap(
                        UnaryOperator.identity(),
                        Parameter::getValue
                    ))
                )
            ));
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(App::new);
    }

    private App() {
        super("L-System");
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
        Drawer plotPanel = new DragonCurve();
        RunButton runButton = new RunButton(plotPanel);

        // control panel in main frame
        add(new JPanel(new BorderLayout()) {{
            // parameters panel in control panel
            add(new JPanel(new BorderLayout()) {{
                // instance fields of class Parameter (ie nameLabel)
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
