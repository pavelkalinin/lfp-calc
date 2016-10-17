package xyz.enhorse;

import javax.swing.*;

/**
 * Application
 * Created by PAK on 10.12.13.
 */

public class Application {
    public static void main(String[] arg) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException
                        | InstantiationException
                        | IllegalAccessException
                        | UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                new MainWindow();
            }
        });
    }
}

