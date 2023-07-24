import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

enum Parameter {
    ITERATIONS("Iterations", 1, 10),
    STEP("Forward step (pixels)", 1, 20, 1),
    ANGLE("Angle (degrees)", 10, 90);
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
