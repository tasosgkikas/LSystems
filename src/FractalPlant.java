import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Ellipse2D;

public class FractalPlant extends JFrame {
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
        public int getValue() { return slider.getValue(); }
    }

    class PlotPanel extends JPanel {
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
        EventQueue.invokeLater(FractalPlant::new);
    }

    FractalPlant() {
        super("Fractal Plant");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        buildFrame();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void buildFrame() {
        // δημιουργία controls
        numIters = new Control("Iterations (10^n)", 0, 9);
        step = new Control("Point diameter (pixels)", 1, 10);

        // PlotPanel: εμφάνιση του fractal
        JPanel plotPanel = new PlotPanel();

        // δημιουργία run button
        JButton runButton = new JButton("Run") {{
            addActionListener( (ActionEvent e) -> {
                plotPanel.repaint();
            });
        }};

        // panel με controls παραμέτρων και run
        JPanel ctrlPanel = new JPanel(new FlowLayout()) {{
            // panel με controls παραμέτρων
            add(new JPanel(new GridLayout(2, 1)) {{
                add(numIters.nameLabel);
                add(step.nameLabel);
            }});

            add(new JPanel(new GridLayout(2, 1)) {{
                add(numIters.slider);
                add(step.slider);
            }});

            add(new JPanel(new GridLayout(2, 1)) {{
                add(numIters.valueLabel);
                add(step.valueLabel);
            }});

            add(new JPanel() {{ add(runButton); }});
        }};

        // Main frame: προσθήκη του control panel και του plot panel
        add(ctrlPanel, BorderLayout.PAGE_START);
        add(plotPanel, BorderLayout.CENTER);
    }
}
