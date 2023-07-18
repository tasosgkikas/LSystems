import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Ellipse2D;
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
            Parameter(String name, int min, int max, int value) {
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

        // PlotPanel: εμφάνιση του fractal
        JPanel plotPanel = new JPanel() {
//            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D)g;
                g2d.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                double width = getWidth(), height = getHeight();

                double x = 0, y = 0, N = numIters.slider.getValue() * 10000;
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

                    double panelX = y * scale, panelY = x * scale + height/2;

                    g2d.setPaint(Color.GREEN);
                    int strokeWidth = step.slider.getValue();
                    g2d.fill(new Ellipse2D.Double(panelX - 1, panelY - 1, strokeWidth, strokeWidth));
                }
            }
        };

        // δημιουργία run button
        JButton runButton = new JButton("Run") {{
            addActionListener( (ActionEvent e) -> {
                plotPanel.repaint();
            });
        }};

        // panel με controls παραμέτρων και run
        JPanel ctrlPanel = new JPanel(new BorderLayout()) {{
            // panel με controls παραμέτρων
            JPanel paramPanel = new JPanel(new BorderLayout()) {{
                add(new JPanel(new BorderLayout()) {{
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

        // Main frame: προσθήκη του control panel και του plot panel
        add(ctrlPanel, BorderLayout.PAGE_START);
        add(plotPanel, BorderLayout.CENTER);
    }
}
