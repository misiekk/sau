package sau;

import javax.swing.*;

public class MainWindow {

    public static void main(String[] args) {
        GUI mainGUI = null;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GUI mainGUI = new GUI();
            }
        });

        System.out.print("Siema!");
    }
}