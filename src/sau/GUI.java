package sau;

import javax.swing.*;
import java.awt.*;

public class GUI extends javax.swing.JFrame{
    //private JButton startSimulationButton;
    private JPanel panel;
    private Map map;
    private Info info;
    public GUI(){
        super("SAU - RiverKayak");
        init();
        info = new Info();
        map = new Map(info);
        panel.add(map);
        info.setMap(map);
        //info.update();
        //setupInfo(map);
        panel.add(info);
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
  /*  private void setupInfo(Map map){
        //String text = "X index = " + Integer.toString(map.getKayak().getIndX()) + " Y index = " + Integer.toString(map.getKayak().getIndY());
//        int kayakStatus = map.getTileArray()[5][7].getStatus();
        String text = "Status of (5,7) = " + Integer.toString(map.getTileArray()[5][7].getStatus()); //3 kayak, 4 collision, 2 obstacle, 1 free
        info = new JLabel(text);
    }*/
}
