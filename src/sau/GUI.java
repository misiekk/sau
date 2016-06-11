package sau;

import javax.swing.*;
import java.awt.*;

public class GUI extends javax.swing.JFrame{
    //private JButton startSimulationButton;
    private JPanel panel;
    private Map map;
    public GUI(){
        super("SAU - RiverKayak");
        init();
        map = new Map();
        panel.add(map);
        map.startSimulation();
    }

    private void init(){
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        setContentPane(this.panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(Map.X_TILES_COUNT*Map.TILE_SIZE +2*Map.Y_TILES_COUNT, Map.Y_TILES_COUNT *Map.TILE_SIZE +2*Map.X_TILES_COUNT));
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
