package base;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;

public enum Parameter {
    ITERATIONS("Iterations", 0, 10, 5),
    STEP("Forward step (pixels)", 1, 256, 3),
    ANGLE("Angle (degrees)", 0, 180, 90);

    final JLabel nameLabel;
    JSlider slider;
    JTextField valueField;

    Parameter(String name, int min, int max, int value) {
        nameLabel = new JLabel(name);

        slider = new JSlider(min, max, value) {{
            addChangeListener( (ChangeEvent e) ->
                valueField.setText(String.valueOf(slider.getValue()))
            );
        }};

        String text = String.valueOf(value);
        int columns = String.valueOf(slider.getMaximum()).length() - 1;
        valueField = new JTextField(text, columns) {{
            addActionListener( (ActionEvent e) ->
                slider.setValue(Integer.parseInt(valueField.getText()))
            );
        }};
    }

    int getValue() {
        return slider.getValue();
    }
}
