package sau;

import sun.awt.windows.ThemeReader;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import static sau.Tile.STATUS_COLLISION;
import static sau.Tile.STATUS_KAYAK;
import static sau.Tile.STATUS_OBSTACLE;

public class Map extends JPanel{
    static final public int X_TILES_COUNT = 10;
    static final public int Y_TILES_COUNT = 20;
    static final public int TILE_SIZE = 20;  // tile = TILE_SIZE x TILE_SIZE px
    private int speed = 100;  // timer delay to set in ms

    private Thread simThread = null;
    private Timer timer;        // for updating GUI
   // private ArrayList<Tile> tileList;
    private Tile[][] tileArray; // 2D-array of tiles to quick access by index
    private Kayak kayak;        // kayak occupies only 1 tile
    private ArrayList<Obstacle> obstaclesList;
    private ObstacleGenerator generator;
    private boolean direction = true;   // TODO do testow zmian kierunku kajaka
    private Agent agent;
    private String collisionPlace="";
    private int counter = 0;
    private boolean isCollision = true;
    Map(Agent agent) {
        this.agent = agent;
//        setStartState();
        prepareTimer();
    }

    private void setStartState(){
        obstaclesList = new ArrayList<Obstacle>();
        tileArray = new Tile[X_TILES_COUNT][Y_TILES_COUNT];
        initTileList();
        //kayak = new Kayak(3, 0, this);  //TODO kayak initial position
        kayak = new Kayak(3,2, Tile.getStatusBoard(tileArray));
        kayak.setAgent(agent);
       // agent.setKayak(kayak);
        generator = new ObstacleGenerator(this);
        updateMap();
        repaint();

    }

    public void speedDown(){
        speed += 10;
        this.timer.setDelay(speed);
    }

    public void speedUp(){
        if(speed > 10){
            speed -= 10;
            this.timer.setDelay(speed);
        }
    }

    public void skipSteps(int steps) {
        //this.timer.stop();
        this.simThread.interrupt();
        System.out.println(simThread.isAlive());
        int i=0;
        while(i<steps){
            System.out.println(i);
            State currentState = new State(Tile.getStatusBoard(tileArray));
            Action action = agent.chooseAction(currentState);
            kayak.doAction(action);
            generator.update();
            updateMap();
            //repaint();

            //reinforcement learning:
            State nextState = new State(Tile.getStatusBoard(tileArray));
            if (collisionOccurred()) {
                agent.atTerminalState(currentState, action, nextState);
                //stopSimulation();
                isCollision = true;
                i++;
            }
            else
                agent.observeTransition(currentState, action, nextState);
        }
        System.out.println(simThread.isAlive());
        System.out.println("Starting timer");
        timer.start();
        //this.simThread.start();
    }


    private void prepareTimer() {
        this.timer = new Timer(speed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                State currentState = new State(Tile.getStatusBoard(tileArray));
                Action action = agent.chooseAction(currentState);
                kayak.doAction(action);
                generator.update();
                updateMap();
                repaint();

                //reinforcement learning:
                State nextState = new State(Tile.getStatusBoard(tileArray));
                if (collisionOccurred()) {
//                    counter++;
//                    updateMap();
                    agent.atTerminalState(currentState, action, nextState);
                    stopSimulation();
                    isCollision = true;
                } else
                    agent.observeTransition(currentState, action, nextState);

                //            else
                //                  agent.act(currentState);
                //          updateMap();
            }
        });
    }

    public void startSimulation() {
        simThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true && !simThread.isInterrupted()){
                    while(!timer.isRunning()){
                        if(isCollision) {
                            setStartState();
                            kayak.getAgent().startEpisode();
                            timer.start();
                            isCollision = false;
                            //Scanner scan = new Scanner(System.in);
                        }
                    }
                }
            }
        });
        simThread.start();
        //return t;

    }


    public void stopSimulation(){
        if(timer != null)
            this.timer.stop();
        //this.simThread.interrupt();
        //this.timer = null;

    }
    /* Returns true if kayak hit the obstacle */
    private boolean collisionOccurred(){
        for(int i=0; i<X_TILES_COUNT; ++i){
            for(int j = 0; j< Y_TILES_COUNT; ++j) {
                if(this.tileArray[i][j].getStatus() == STATUS_COLLISION){
                    if(collisionPlace.equals(""))
                        collisionPlace = "Collided at (" + Integer.toString(i) + ", " + Integer.toString(j) + ")";
                    return true;
                }
            }
        }
        return false;
    }

    /* Sets new status of every tile in the map */
    public void updateMap(){
        for(int i=0; i<X_TILES_COUNT; ++i){
            for(int j = 0; j< Y_TILES_COUNT; ++j) {
                tileArray[i][j].setStatus(Tile.STATUS_FREE);
            }
        }

        for(Tile t : kayak.getTiles()){
            this.tileArray[t.getIndX()][t.getIndY()].setStatus(STATUS_KAYAK);
        }

        for(Obstacle o : this.getObstaclesList()){
            for(Tile t : o.getTiles()){
                if(t.getIndY() < Y_TILES_COUNT)
                    if(tileArray[t.getIndX()][t.getIndY()].getStatus() == STATUS_KAYAK) {
                        this.tileArray[t.getIndX()][t.getIndY()].setStatus(STATUS_COLLISION);
                    }
                    else {
                        this.tileArray[t.getIndX()][t.getIndY()].setStatus(STATUS_OBSTACLE);
                    }
            }
        }
    }



    public ArrayList<Obstacle> getObstaclesList() { return this.obstaclesList; }
    public Tile[][] getTileArray(){ return this.tileArray; }
    private void paintKayak(Graphics g){
        g.setColor(Color.YELLOW);
        for(Tile t : kayak.getTiles()){
            g.fillRect(t.getX(), t.getY(), TILE_SIZE, TILE_SIZE);
           // g.setColor(Color.BLACK);
           // g.drawString(Integer.toString(t.getY()), t.getX(), t.getY() + TILE_SIZE);
        }

        g.setColor(Color.BLACK);
        int index = 0;
        for (double q : agent.qvalues){
            String text = Double.toString(q);
            Tile tile = kayak.getTiles().get(index);
            index += Kayak.KAYAK_WIDTH;
            g.drawString(text, tile.getX(), tile.getY() + TILE_SIZE);
        }
    }

    private void paintObstacles(Graphics g){
        g.setColor(Color.GRAY);
        for(Obstacle o : obstaclesList){
            o.paint(g);
        }
    }

    private void initTileList(){
        for(int i=0; i<X_TILES_COUNT; ++i){
            for(int j = 0; j< Y_TILES_COUNT; ++j) {
                int x = TILE_SIZE * i;
                int y = TILE_SIZE * j;
                Tile t = new Tile(x, y, i, j);
                tileArray[i][j] = t;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(tileArray != null) {
            for (int i = 0; i < X_TILES_COUNT; ++i) {
                for (int j = 0; j < Y_TILES_COUNT; ++j) {
                    g.drawRect(this.tileArray[i][j].getX(), this.tileArray[i][j].getY(), TILE_SIZE, TILE_SIZE);
                }
            }


        paintObstacles(g);

        g.setColor(Color.BLACK);

        for (int i = 0; i < X_TILES_COUNT; ++i) {
            for (int j = 0; j < Y_TILES_COUNT; ++j) {
                Tile tile = tileArray[i][j];
                g.drawString(Integer.toString(tile.getStatus()), tile.getX(), tile.getY() + TILE_SIZE);
            }
        }
        paintKayak(g);


        //info printing
        int x = X_TILES_COUNT * TILE_SIZE + 10;
        g.drawRect(x, 10, 300, 400);
        Agent agent = kayak.getAgent();
        int y = 30; int dy = 15;
        String txt = "Params:";
        g.drawString(txt, x + 10, y); y += dy;
        txt = "alpha = " + Double.toString(agent.alpha);
        g.drawString(txt, x + 10, y); y += dy;
        txt = "epsilon = " + Double.toString(agent.epsilon);
        g.drawString(txt, x + 10, y); y += dy;
        txt = "gamma = " + Double.toString(agent.gamma);
        g.drawString(txt, x + 10, y); y += dy + 10;

       /* txt = "Features:";
        g.drawString(txt, x + 10, y); y += dy;
        txt = "f0: distance to left shore = " + Integer.toString(kayak.distanceToLeftShore());
        g.drawString(txt, x + 10, y); y += dy;
        txt = "f1: distance to right shore = " + Integer.toString(kayak.distanceToRightShore());
        g.drawString(txt, x + 10, y); y += dy;
        txt = "f2: distance to left obstacle = " + Integer.toString(kayak.distanceToRockLeft());
        g.drawString(txt, x + 10, y); y += dy;
        txt = "f3: distance to right obstacle = " + Integer.toString(kayak.distanceToRockRight());
        g.drawString(txt, x + 10, y); y += dy;
        txt = "f4: distance to obstacle ahead = " + Integer.toString(kayak.distanceToRockAhead());
        g.drawString(txt, x + 10, y); y += dy + 10;*/

      /*  ArrayList<Float> weights = agent.getWeights();
        for (int i = 0; i <weights.size(); i++){
            txt = "w" + Integer.toString(i) + " = " + weights.get(i);
            g.drawString(txt, x + 10, y); y += dy;
        }*/

        /*txt = "Last action = " + agent.lastAction.print();
        g.drawString(txt, x + 10, y+5); y += dy+10;*/
        txt = "episodeRewards = " + Float.toString(agent.episodeRewards);
        g.drawString(txt, x + 10, y+5); y += dy+10;

        txt = "episodes so far = " + Integer.toString(agent.episodesSoFar);
        g.drawString(txt, x + 10, y); y += dy;
        txt = "total train rewards = " + Float.toString(agent.totalTrainRewards);
        g.drawString(txt, x + 10, y); y += dy;
        txt = "total test rewards = " + Float.toString(agent.totalTestRewards);
        g.drawString(txt, x + 10, y); y += dy;

        txt = "Kayak pos = (" + Integer.toString(kayak.getTiles().get(0).getIndX()) + ", " +
                Integer.toString(kayak.getTiles().get(0).getIndY()) + ")";
        g.drawString(txt, x + 10, y); y += dy;
        g.drawString(Integer.toString(counter) + ", " + collisionPlace, x + 10, y); y += dy;
        }
    }

    /* For debugging purposes */
    private void showMap(){
        for(int i=0; i<X_TILES_COUNT; ++i) {
            for (int j = 0; j < Y_TILES_COUNT; ++j) {
                String temp = Integer.toString(this.tileArray[i][j].getIndX()) + ":"
                        + Integer.toString(this.tileArray[i][j].getIndY()) + "\t"
                        + Integer.toString(this.tileArray[i][j].getX()) + ":"
                        + Integer.toString(this.tileArray[i][j].getY());
                System.out.println(temp);
            }
        }
    }

    /* For debugging purposes */
    private void showInfo(int status){
        for(int i=0; i<X_TILES_COUNT; ++i) {
            for (int j = 0; j < Y_TILES_COUNT; ++j) {
                if(this.tileArray[i][j].getStatus() != status){
                    continue;
                }
                String temp = Integer.toString(this.tileArray[i][j].getIndX()) + ":"
                        + Integer.toString(this.tileArray[i][j].getIndY()) + "\t"
                        + Integer.toString(this.tileArray[i][j].getX()) + ":"
                        + Integer.toString(this.tileArray[i][j].getY());
                System.out.println(temp);
            }
        }
        System.out.println("---");
    }

    public Kayak getKayak(){
        return kayak;
    }


}

