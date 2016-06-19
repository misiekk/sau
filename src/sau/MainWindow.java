package sau;

import javax.swing.*;

public class MainWindow {

    public static void main(String[] args) {
        GUI mainGUI = null;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                /*JFrame frame = new JFrame("SAU");
                frame.setContentPane(new NewGUI().mainPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);*/
                new GUI();

            }
        });

    }
}