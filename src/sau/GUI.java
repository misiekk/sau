package sau;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends javax.swing.JFrame{
    //private JButton startSimulationButton;
    private JPanel panel;
    private Map map;
    private Agent agent;
    static final float ALPHA = 0.2f;
    static final float GAMMA = 0.9f;
    static final float EPSILON = 0.3f;
    static final int NUM_TRAINING = 100;

    public GUI(){
        super("SAU - RiverKayak");
        agent = new Agent(ALPHA, EPSILON, GAMMA, NUM_TRAINING);
        init();
        map = new Map(agent);
       JButton button = new JButton("Start simulation");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                map.startSimulation();
            }
        });
        panel.add(button);

        JButton btnAlpha = new JButton("Set alpha");
        JButton btnGamma = new JButton("Set gamma");
        JButton btnEpsilon = new JButton("Set epsilon");
        final JTextField txtAlpha = new JTextField();
        final JTextField txtGamma = new JTextField();
        final JTextField txtEpsilon = new JTextField(Double.toString(agent.epsilon));
        txtEpsilon.setPreferredSize(new Dimension(50, 15));

        btnAlpha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                double newVal = Double.parseDouble(txtAlpha.getText());
                agent.setAlpha(newVal);
            }
        });

        btnGamma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                double newVal = Double.parseDouble(txtGamma.getText());
                agent.setGamma(newVal);
            }
        });
        btnAlpha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                double newVal = Double.parseDouble(txtEpsilon.getText());
                agent.setEpsilon(newVal);
            }
        });

        panel.add(btnEpsilon);
      //  panel.add(txtEpsilon);
        panel.add(map);

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
