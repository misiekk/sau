package sau;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends javax.swing.JFrame{
    //private JButton startSimulationButton;
    private JPanel mainPanel;
    private JButton buttonStart, buttonStop;
    private JButton buttonAlpha, buttonGamma, buttonEpsilon;
    private JButton buttonFaster, buttonSlower;
    private JPanel panelButton;
    private JPanel panelMap;
    private JTextField alphaTxt, gammaTxt, epsilonTxt;
    private Map map;
    private Agent agent;
    static final float ALPHA = 0.2f;
    static final float GAMMA = 0.9f;
    static final float EPSILON = 0.3f;
    static final int NUM_TRAINING = 100;
    private Thread t = null;

    public GUI(){
        super("SAU - RiverKayak");
        agent = new Agent(ALPHA, EPSILON, GAMMA, NUM_TRAINING);
        map = new Map(agent);

        init();
    }

    private void prepareButtons(){
        buttonStart = new JButton("Start");
        buttonStop = new JButton("Stop");
        buttonAlpha = new JButton("Set alpha");
        buttonGamma = new JButton("Set gamma");
        buttonEpsilon = new JButton("Set epsilon");
        buttonFaster = new JButton("Speed++");
        buttonSlower = new JButton("Speed--");

        alphaTxt = new JTextField();
        gammaTxt = new JTextField();
        epsilonTxt = new JTextField();

        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                map.startSimulation();
            }
        });
        buttonStart.setSize(new Dimension(150,50));

        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                map.stopSimulation();
            }
        });

        buttonAlpha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                double newVal = Double.parseDouble(alphaTxt.getText());
                agent.setAlpha(newVal);
            }
        });

        buttonGamma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                double newVal = Double.parseDouble(gammaTxt.getText());
                agent.setGamma(newVal);
            }
        });

        buttonEpsilon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                double newVal = Double.parseDouble(epsilonTxt.getText());
                agent.setEpsilon(newVal);
            }
        });

        buttonFaster.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                map.speedUp();
            }
        });

        buttonSlower.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                map.speedDown();
            }
        });

    }
    private void init(){
        prepareButtons();

        GridLayout gridLayout = new GridLayout(4,2);
        JPanel middlePanel = new JPanel(gridLayout);

        middlePanel.add(buttonStart);
        middlePanel.add(buttonStop);

        middlePanel.add(alphaTxt);
        middlePanel.add(buttonAlpha);

        middlePanel.add(gammaTxt);
        middlePanel.add(buttonGamma);

        middlePanel.add(epsilonTxt);
        middlePanel.add(buttonEpsilon);

        GridLayout gridLayout2 = new GridLayout(2,1);
        JPanel rightPanel = new JPanel(gridLayout2);

        rightPanel.add(buttonFaster);
        rightPanel.add(buttonSlower);
        buttonFaster.setSize(new Dimension(50,50));
        //middlePanel.add(buttonSlower);

        //GridLayout gridLayout = new GridLayout(2,1);
        //setLayout(gridLayout);
        //panelButton = new JPanel();
        //panelButton.setLayout(new BoxLayout(panelButton, BoxLayout.X_AXIS));
        //panelButton.add(map);
        //panelButton.add(buttonSlower);
        //panelButton.add(buttonEpsilon);
/*
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        mainPanel.add(buttonStart);
        mainPanel.add(buttonStop);
        mainPanel.add(buttonFaster);*/
//        setContentPane(this.mainPanel);
        //map.setVisible(true);
        //this.add(map);
        //this.add(mainPanel);
        this.add(map, BorderLayout.CENTER);
        this.add(rightPanel, BorderLayout.EAST);
        this.add(middlePanel, BorderLayout.SOUTH);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension((int)3.8*Map.X_TILES_COUNT * Map.TILE_SIZE,
                //+3*Map.Y_TILES_COUNT + 300,
                (int)2*Map.Y_TILES_COUNT * Map.TILE_SIZE));
                        //+3*Map.X_TILES_COUNT + 100));

        pack();
        setVisible(true);
    }
}
