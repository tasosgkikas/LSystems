package base;

import javax.swing.JButton;
import java.awt.event.ActionEvent;

class RunButton extends JButton {
    RunButton(Drawer plotPanel) {
        super("Run");
        addActionListener((ActionEvent e) -> plotPanel.runBy(this));
    }
}
