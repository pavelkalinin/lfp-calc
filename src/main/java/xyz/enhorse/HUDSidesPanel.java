package xyz.enhorse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel for selecting rectangle's sides
 * Created by PAK on 11.12.13.
 */
public class HUDSidesPanel extends JPanel {
    public boolean left = false;
    public boolean right = false;
    public boolean top = false;
    public boolean bottom = false;

    private final ClassLoader cl  = getClass().getClassLoader();

    private final JToggleButton tbLeft = new JToggleButton(new ImageIcon(cl.getResource("left.png")));
    private final JToggleButton tbRight = new JToggleButton(new ImageIcon(cl.getResource("right.png")));
    private final JToggleButton tbTop = new JToggleButton(new ImageIcon(cl.getResource("up.png")));
    private final JToggleButton tbBottom = new JToggleButton(new ImageIcon(cl.getResource("down.png")));
    private final JToggleButton tbAll = new JToggleButton(new ImageIcon(cl.getResource("center.png")));

    void setPerimeter() {
        left = tbLeft.isSelected();
        right = tbRight.isSelected();
        top = tbTop.isSelected();
        bottom = tbBottom.isSelected();
    }


    public HUDSidesPanel(String title) {


        super(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(title));

        add(BorderLayout.NORTH, tbTop);
        add(BorderLayout.WEST, tbLeft);
        add(BorderLayout.CENTER, tbAll);
        add(BorderLayout.EAST, tbRight);
        add(BorderLayout.SOUTH, tbBottom);

        class bPerimeterListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                tbLeft.setSelected(tbAll.isSelected());
                tbRight.setSelected(tbAll.isSelected());
                tbTop.setSelected(tbAll.isSelected());
                tbBottom.setSelected(tbAll.isSelected());
                setPerimeter();
            }
        }

        tbAll.addActionListener(new bPerimeterListener());

        class bButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                setPerimeter();

            }
        }

        tbLeft.addActionListener(new bButtonListener());
        tbRight.addActionListener(new bButtonListener());
        tbTop.addActionListener(new bButtonListener());
        tbBottom.addActionListener(new bButtonListener());
    }

    public void setEnabled(boolean state) {
        tbBottom.setEnabled(state);
        tbTop.setEnabled(state);
        tbLeft.setEnabled(state);
        tbRight.setEnabled(state);
        tbAll.setEnabled(state);
    }

    public void setSelected(boolean state) {
        tbBottom.setSelected(state);
        tbTop.setSelected(state);
        tbLeft.setSelected(state);
        tbRight.setSelected(state);
        tbAll.setSelected(state);
        setPerimeter();
    }

}
