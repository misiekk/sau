package sau;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Map extends JPanel{
    static final public int xTilesCount = 20;
    static final public int yTilesCount = 20;
    static final public int tileSize = 30;  // tile = tileSize x tileSize px
    static final private int timerDelay = 250;  // timer delay to set in ms

    //private Timer timer;        // for updating GUI
   // private ArrayList<Tile> tileList;
    private Tile[][] tileArray; // 2D-array of tiles to quick access by index
    private Kayak kayak;        // kayak occupies only 1 tile
    private ArrayList<Obstacle> obstaclesList;
    private ObstacleGenerator generator;

    Map() {
        obstaclesList = new ArrayList<Obstacle>();
        tileArray = new Tile[xTilesCount][yTilesCount];
        initTileList();
        kayak = new Kayak(5, 5, this);  //TODO kayak initial position
        generator = new ObstacleGenerator(this);

        Timer timer = new Timer(timerDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                kayak.moveLeft();
                generator.update();
                repaint();
                showInfo(Tile.statusObstacle);

            }
        });
        timer.start();
        generator.generate();

        //showMap();

    }
    public ArrayList<Obstacle> getObstaclesList() { return this.obstaclesList; }
    public Tile[][] getTileArray(){ return this.tileArray; };
    private void paintKayak(Graphics g){
        //Graphics g = this.getGraphics();
        g.setColor(Color.YELLOW);
        g.fillRect(kayak.getX(), kayak.getY(), tileSize, 2*tileSize);
    }

    private void paintObstacles(Graphics g){
        //Graphics g = this.getGraphics();
        g.setColor(Color.GRAY);
        for(Obstacle o : obstaclesList){
            o.paint(g);
        }
    }

    private void initTileList(){
        for(int i=0; i<xTilesCount; ++i){
            for(int j=0; j<yTilesCount; ++j) {
                int x = tileSize * i;
                int y = tileSize * j;
                Tile t = new Tile(x, y, i, j);
                //tileList.add(t);
                tileArray[i][j] = t;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(int i=0; i<xTilesCount; ++i) {
            for (int j = 0; j < yTilesCount; ++j) {
                g.drawRect(this.tileArray[i][j].getX(), this.tileArray[i][j].getY(), tileSize, tileSize);
            }
        }
        /*
        for(int i=0; i < this.tileList.size(); ++i) {
            g.drawRect(this.tileList.get(i).getX(), this.tileList.get(i).getY(), tileSize, tileSize);
        }*/

        paintKayak(g);
        paintObstacles(g);
    }

    /* For debugging purposes */
    private void showMap(){
        for(int i=0; i<xTilesCount; ++i) {
            for (int j = 0; j < yTilesCount; ++j) {
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
        for(int i=0; i<xTilesCount; ++i) {
            for (int j = 0; j < yTilesCount; ++j) {
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

