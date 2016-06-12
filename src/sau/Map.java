package sau;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static sau.Tile.STATUS_COLLISION;
import static sau.Tile.STATUS_KAYAK;
import static sau.Tile.STATUS_OBSTACLE;

public class Map extends JPanel{
    static final public int X_TILES_COUNT = 20;
    static final public int Y_TILES_COUNT = 40;
    static final public int TILE_SIZE = 20;  // tile = TILE_SIZE x TILE_SIZE px
    static final private int TIMER_DELAY = 200;  // timer delay to set in ms

    private Timer timer;        // for updating GUI
   // private ArrayList<Tile> tileList;
    private Tile[][] tileArray; // 2D-array of tiles to quick access by index
    private Kayak kayak;        // kayak occupies only 1 tile
    private ArrayList<Obstacle> obstaclesList;
    private ObstacleGenerator generator;
    private Info infoLabel;
    private boolean direction = true;   // TODO do testow zmian kierunku kajaka
    Map(Info info) {
        obstaclesList = new ArrayList<Obstacle>();
        tileArray = new Tile[X_TILES_COUNT][Y_TILES_COUNT];
        initTileList();
        kayak = new Kayak(0, 0, this);  //TODO kayak initial position
        generator = new ObstacleGenerator(this);
        this.infoLabel = info;
        //showMap();

    }

    public void startSimulation(){
        this.timer = new Timer(TIMER_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(direction){          // Wahadelko
                    if(!kayak.moveRight()){
                        direction = false;
                    }
                }
                else{
                    if(!kayak.moveLeft()){
                        direction = true;
                    }
                }

                generator.update();
                infoLabel.update();
                updateMap();
                repaint();

                if(isCollision()){
                    stopSimulation();
                }

            }
        });
        timer.start();
    }

    public void stopSimulation(){
        this.timer.stop();
        this.timer = null;

    }
    /* Returns true if kayak hit the obstacle */
    private boolean isCollision(){
        for(int i=0; i<X_TILES_COUNT; ++i){
            for(int j = 0; j< Y_TILES_COUNT; ++j) {
                if(this.tileArray[i][j].getStatus() == STATUS_COLLISION){
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

        for(int i=0; i<X_TILES_COUNT; ++i) {
            for (int j = 0; j < Y_TILES_COUNT; ++j) {
                g.drawRect(this.tileArray[i][j].getX(), this.tileArray[i][j].getY(), TILE_SIZE, TILE_SIZE);
            }
        }

        paintKayak(g);
        paintObstacles(g);
        g.setColor(Color.BLACK);
        for(int i=0; i<X_TILES_COUNT; ++i) {
            for (int j = 0; j < Y_TILES_COUNT; ++j) {
                Tile tile = tileArray[i][j];
                g.drawString(Integer.toString(tile.getStatus()), tile.getX(), tile.getY()+TILE_SIZE);
            }
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

