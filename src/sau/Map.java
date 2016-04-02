package sau;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Map extends JPanel{
    static final public int xTilesCount = 10;
    static final public int yTilesCount = 10;
    static final public int tileSize = 50;  // tile = 50x50 px

    private ArrayList<Tile> tileList;
    Map() {
        tileList = new ArrayList<Tile>();
        initTileList();
    }

    private void initTileList(){
        for(int i=0; i<xTilesCount; ++i){
            for(int j=0; j<yTilesCount; ++j) {
                int x = tileSize * i;
                int y = tileSize * j;
                Tile t = new Tile(x, y);
                tileList.add(t);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(int i=0; i< this.tileList.size(); ++i) {
            g.drawRect(this.tileList.get(i).getX(), this.tileList.get(i).getY(), tileSize, tileSize);
        }

    }
}
