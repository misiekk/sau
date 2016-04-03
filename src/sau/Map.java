package sau;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Map extends JPanel{
    static final public int xTilesCount = 40;
    static final public int yTilesCount = 30;
    static final public int tileSize = 20;  // tile = tileSize x tileSize px
    static final private int timerDelay = 100;  // timer delay to set in ms

    //private Timer timer;        // for updating GUI
    private ArrayList<Tile> tileList;
    private Tile[][] tileArray; // 2D-array of tiles to quick access by index
    private Kayak kayak;        // kayak occupies only 1 tile


    Map() {
        tileList = new ArrayList<Tile>();
        tileArray = new Tile[xTilesCount][yTilesCount];
        initTileList();
        kayak = new Kayak(5*tileSize, 5*tileSize);  //TODO kayak initial position
        Timer timer = new Timer(timerDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //kayak.moveRight();
                repaint();
            }
        });
        timer.start();

        showMap();
    }

    private void paintKayak(Graphics g){
        //Graphics g = this.getGraphics();
        g.setColor(Color.YELLOW);
        g.fillRect(kayak.getX(), kayak.getY(), tileSize, tileSize);
    }


    private void initTileList(){
        for(int i=0; i<xTilesCount; ++i){
            for(int j=0; j<yTilesCount; ++j) {
                int x = tileSize * i;
                int y = tileSize * j;
                Tile t = new Tile(x, y, i, j);
                tileList.add(t);
                tileArray[i][j] = t;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(int i=0; i < this.tileList.size(); ++i) {
            g.drawRect(this.tileList.get(i).getX(), this.tileList.get(i).getY(), tileSize, tileSize);
        }

        paintKayak(g);
    }

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

}

