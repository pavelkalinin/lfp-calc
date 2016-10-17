package xyz.enhorse;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Labeled edit field for numbers
 * Created by PAK on 11.12.13.
 */

public class HUDNumberField extends JPanel {
    public final JFormattedTextField field;
    public String defaultValue;

    public HUDNumberField(String labelBeforeEdit, String labelAfterEdit, String defaultValue) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);
        this.defaultValue = defaultValue;

        JLabel labelBefore = new JLabel(" " + labelBeforeEdit + " ");
        labelBefore.setHorizontalAlignment(JLabel.RIGHT);

        DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        unusualSymbols.setDecimalSeparator('.');
        unusualSymbols.setGroupingSeparator(' ');

        String strange = "#,##0.###";
        DecimalFormat weirdFormatter = new DecimalFormat(strange, unusualSymbols);
        weirdFormatter.setGroupingSize(999);

        field = new JFormattedTextField(weirdFormatter);
        field.setHorizontalAlignment(JFormattedTextField.RIGHT);
        JLabel labelAfter = new JLabel(" " + labelAfterEdit + " ");
        labelAfter.setHorizontalAlignment(JLabel.LEFT);

        add(labelBefore);
        add(field);
        field.setText(this.defaultValue);
        add(labelAfter);

        field.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
                validateInput(e);
            }

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }

            private void validateInput(KeyEvent e) {
                byte c = (byte) e.getKeyChar();
                if (((c > 47) && (c < 58)) || (c == 46) || (c == 44)) {
                    if (c == 44) {
                        e.setKeyChar('.');
                    }
                    return;
                }

                if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                    field.setText("0");
                    field.selectAll();
                }

                e.consume();
            }
        });
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




