package sau;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends javax.swing.JFrame{
    //private JButton startSimulationButton;
    private JPanel mainPanel;
    private JButton buttonStart;
    private JButton buttonAlpha, buttonGamma, buttonEpsilon;
    private JPanel panelButton;
    private JPanel panelMap;
    private Map map;
    private Agent agent;
    static final float ALPHA = 0.2f;
    static final float GAMMA = 0.9f;
    static final float EPSILON = 0.3f;
    static final int NUM_TRAINING = 100;

    public GUI(){
        super("SAU - RiverKayak");

        agent = new Agent(ALPHA, EPSILON, GAMMA, NUM_TRAINING);
        map = new Map(agent);

        init();
    }

    private void prepareButtons(){
        buttonStart = new JButton("Start simulation");
        buttonAlpha = new JButton("Set alpha");
        buttonGamma = new JButton("Set gamma");
        buttonEpsilon = new JButton("Set epsilon");

        final JTextField txtAlpha = new JTextField();
        final JTextField txtGamma = new JTextField();
        final JTextField txtEpsilon = new JTextField(Double.toString(agent.epsilon));
        txtEpsilon.setPreferredSize(new Dimension(50, 15));

        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                map.startSimulation();
            }
        });
        buttonStart.setSize(new Dimension(150,50));

        buttonAlpha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                double newVal = Double.parseDouble(txtAlpha.getText());
                agent.setAlpha(newVal);
            }
        });

        buttonGamma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                double newVal = Double.parseDouble(txtGamma.getText());
                agent.setGamma(newVal);
            }
        });

        buttonAlpha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                double newVal = Double.parseDouble(txtEpsilon.getText());
                agent.setEpsilon(newVal);
            }
        });
    }
    private void init(){
        prepareButtons();
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        panelMap = new JPanel();
        panelMap.setLayout(new BoxLayout(panelMap, BoxLayout.Y_AXIS));

        panelButton = new JPanel();

        panelButton.add(buttonStart);
        panelButton.setLayout(new BoxLayout(panelButton, BoxLayout.PAGE_AXIS));
        panelButton.add(buttonEpsilon);

        panelMap.add(map);

        mainPanel.add(panelMap);
        mainPanel.add(panelButton);

        setContentPane(this.mainPanel);
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
