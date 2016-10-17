package xyz.enhorse;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class HUDTextField extends JPanel {
    public final JTextField field;
    public String defaultValue;

    public HUDTextField(String labelBeforeEdit, String labelAfterEdit, String defaultValue) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);
        this.defaultValue = defaultValue;

        JLabel labelBefore = new JLabel(" " + labelBeforeEdit + " ");
        labelBefore.setHorizontalAlignment(JLabel.RIGHT);

        field = new JTextField();
        field.setHorizontalAlignment(JFormattedTextField.LEFT);
        JLabel labelAfter = new JLabel(" " + labelAfterEdit + " ");
        labelAfter.setHorizontalAlignment(JLabel.LEFT);

        add(labelBefore);
        add(field);
        field.setText(this.defaultValue);
        add(labelAfter);

        FocusListener fListener = new FocusListener() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        field.selectAll();
                    }
                });
            }

            public void focusLost(FocusEvent e) {
                String value = field.getText().replaceAll("\\.", "");
                if (value.isEmpty()) {
                    field.setText("0");
                }
                field.selectAll();

            }
        };
        field.addFocusListener(fListener);

    }
}