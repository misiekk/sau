package sau;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Map extends JPanel{
    static final public int X_TILES_COUNT = 30;
    static final public int Y_TILES_COUNT = 20;
    static final public int TILE_SIZE = 30;  // tile = TILE_SIZE x TILE_SIZE px
    static final private int TIMER_DELAY = 50;  // timer delay to set in ms

    //private Timer timer;        // for updating GUI
   // private ArrayList<Tile> tileList;
    private Tile[][] tileArray; // 2D-array of tiles to quick access by index
    private Kayak kayak;        // kayak occupies only 1 tile
    private ArrayList<Obstacle> obstaclesList;
    private ObstacleGenerator generator;

    Map() {
        obstaclesList = new ArrayList<Obstacle>();
        tileArray = new Tile[X_TILES_COUNT][Y_TILES_COUNT];
        initTileList();
        kayak = new Kayak(5, 5, this);  //TODO kayak initial position
        generator = new ObstacleGenerator(this);

        Timer timer = new Timer(TIMER_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //kayak.moveLeft();
                generator.update();
                repaint();
                //showInfo(Tile.STATUS_OBSTACLE);
            }
        });
        timer.start();
        generator.generate();

        //showMap();

    }
    public ArrayList<Obstacle> getObstaclesList() { return this.obstaclesList; }
    public Tile[][] getTileArray(){ return this.tileArray; }
    private void paintKayak(Graphics g){
        g.setColor(Color.YELLOW);
        g.fillRect(kayak.getX(), kayak.getY(), TILE_SIZE, 2* TILE_SIZE);
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
    }

}

