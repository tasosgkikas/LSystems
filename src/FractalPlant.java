import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Line2D;

public class FractalPlant extends JFrame {
    public static void main(String[] args) {
        EventQueue.invokeLater(FractalPlant::new);
    }

    FractalPlant() {
        super("Fractal Plant");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(2*screenSize.width/3, 2*screenSize.height/3);

        buildFrame();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void buildFrame() {
        class Parameter { // controls παραμέτρων
            final JLabel nameLabel;
            JSlider slider;
            JLabel valueLabel;

            public Parameter(String name, int min, int max, int value) {
                nameLabel = new JLabel(name);
                slider = new JSlider(min, max, value) {{
                    addChangeListener( (ChangeEvent e) ->
                        valueLabel.setText(String.valueOf(slider.getValue()))
                    );
                }};
                valueLabel = new JLabel(String.valueOf(value));
            }
        }

        // δημιουργία παραμέτρων
        Parameter numIters = new Parameter("Iterations", 3, 10, 8);
        Parameter step = new Parameter("Forward step (pixels)", 1, 10, 2);

        // δημιουργία run button
        JButton runButton = new JButton("Run") {{
            addActionListener( (ActionEvent e) -> {});
        }};

        JPanel ctrlPanel = new JPanel(new BorderLayout()) {{ // panel με controls παραμέτρων και run
            JPanel paramPanel = new JPanel(new BorderLayout()) {{ // panel με controls παραμέτρων
                add(new JPanel(new BorderLayout()) {{ // panel με ονόματα παραμέτρων
                    add(numIters.nameLabel, BorderLayout.PAGE_START);
                    add(step.nameLabel, BorderLayout.PAGE_END);
                }}, BorderLayout.LINE_START);

                add(new JPanel(new BorderLayout()) {{
                    add(numIters.slider, BorderLayout.PAGE_START);
                    add(step.slider, BorderLayout.PAGE_END);
                }}, BorderLayout.CENTER);

                add(new JPanel(new BorderLayout()) {{
                    add(numIters.valueLabel, BorderLayout.PAGE_START);
                    add(step.valueLabel, BorderLayout.PAGE_END);
                }}, BorderLayout.LINE_END);
            }};

            JPanel runPanel = new JPanel() {{ add(runButton); }};

            add(paramPanel, BorderLayout.CENTER);
            add(runPanel, BorderLayout.LINE_END);
        }};

        // PlotPanel: εμφάνιση του fractal
        JPanel plotPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D)g;
                g2d.draw(new Line2D.Double(
                    0, 0, getWidth(), 0
                ));
            }
        };

        // Main frame: προσθήκη του control panel και του plot panel
        add(ctrlPanel, BorderLayout.PAGE_START);
        add(plotPanel, BorderLayout.CENTER);
    }
}
