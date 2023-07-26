import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

enum Parameter {
    ITERATIONS("Iterations", 1, 10, 5),
    STEP("Forward step (pixels)", 1, 20, 5),
    ANGLE("Angle (degrees)", 10, 90, 25);
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
