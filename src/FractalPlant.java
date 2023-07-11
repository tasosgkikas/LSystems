import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class FractalPlant extends JFrame {

    public static void main(String[] args) {
        EventQueue.invokeLater(FractalPlant::new);
    }

    FractalPlant() {
        super("Fractal Plant");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        add(createPanel());
//        pack();
        setSize(2*screenSize.width/3, 2*screenSize.height/3);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private static JPanel createPanel() {
        var mainPanel = new JPanel(new BorderLayout()); // το βασικό Panel
        var ctrlPanel = new JPanel(new BorderLayout(10, 0)); // panel με όλα τα controls
        var paramPanel = new JPanel(new BorderLayout()); // panel με τα controls των παραμέτρων
        var paramNamesPanel = new JPanel(new BorderLayout()); // panel με τα ονόματα των παραμέτρων
        var paramSlidersPanel = new JPanel(new BorderLayout()); // panel με τα sliders των παραμέτρων
        var paramValuesPanel = new JPanel(new BorderLayout()); // panel με τις τιμές των παραμέτρων

        var numIters = new JSlider(3, 10, 8);  // Slider για iterations
        var numItersLabel = new JLabel(String.valueOf(numIters.getValue()));

        var step = new JSlider(1, 10, 2);      // Slider για pixels / βήμα
        var stepLabel = new JLabel(String.valueOf(step.getValue()));

        paramNamesPanel.add(new JLabel("Iterations"), BorderLayout.PAGE_START);
        paramNamesPanel.add(new JLabel("Forward step (pixels)"), BorderLayout.PAGE_END);

        paramSlidersPanel.add(numIters, BorderLayout.PAGE_START);
        paramSlidersPanel.add(step, BorderLayout.PAGE_END);

        paramValuesPanel.add(numItersLabel, BorderLayout.PAGE_START);
        paramValuesPanel.add(stepLabel, BorderLayout.PAGE_END);

        paramPanel.add(paramNamesPanel, BorderLayout.LINE_START);
        paramPanel.add(paramSlidersPanel, BorderLayout.CENTER);
        paramPanel.add(paramValuesPanel, BorderLayout.LINE_END);

        class LabelChanger implements ChangeListener {
            @Override
            public void stateChanged(ChangeEvent e) {

            }
        }

        // change listeners για τους sliders
        numIters.addChangeListener(new LabelChanger());
        step.addChangeListener(new LabelChanger());

        var runButton = new JButton("Run");  // button για εκκίνηση

        ctrlPanel.add(paramPanel, BorderLayout.CENTER);
        ctrlPanel.add(runButton, BorderLayout.LINE_END);

        // Main panel: προσθήκη του control panel
        mainPanel.add(ctrlPanel, BorderLayout.PAGE_START);

        // PlotPanel: εμφάνιση του fractal
        var plotPanel = new JComponent() {
            /* ... */
        };

//        plotPanel.setPreferredSize(new Dimension(800, 800));
        // Main panel: προσθήκη του plot panel
        mainPanel.add(plotPanel, BorderLayout.CENTER);

        return mainPanel;
    }
}
