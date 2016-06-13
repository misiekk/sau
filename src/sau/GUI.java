package sau;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends javax.swing.JFrame{
    //private JButton startSimulationButton;
    private JPanel panel;
    private Map map;
    public GUI(){
        super("SAU - RiverKayak");
        init();
        map = new Map();
        JButton button = new JButton("Start simulation");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                map.startSimulation();
            }
        });
        panel.add(button);
        panel.add(map);
        /*for (int i = 0; i < 2; i++) {
            System.out.println("Starting sim no. " + i);
            map.startSimulation();
            System.out.println("Sim no. " + i + " done!");
        }*/
    }

    private void init(){
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        setContentPane(this.panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(Map.X_TILES_COUNT*Map.TILE_SIZE +2*Map.Y_TILES_COUNT + 300, Map.Y_TILES_COUNT *Map.TILE_SIZE +2*Map.X_TILES_COUNT + 100));
        pack();

/*        startSimulationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showConfirmDialog(GUI.this, "Clicked!");
            }
        });*/


        setVisible(true);
    }
}
