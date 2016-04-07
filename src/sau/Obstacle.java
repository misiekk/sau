package sau;

import java.awt.*;
import java.util.ArrayList;

import static sau.Tile.statusKayak;
import static sau.Tile.statusObstacle;

/**
 * Represents single obstacle on the map
 */
public class Obstacle implements MapUpdater{
    private Map map;
    private ArrayList<Tile> tiles;

    Obstacle(Map _map){
        this.tiles = new ArrayList<Tile>();
        this.map = _map;
    }

    public void addTile(Tile t){
        this.tiles.add(t);
    }

    public void paint(Graphics g){
        for(Tile t : tiles){
            g.fillRect(t.getX(), t.getY(), Map.tileSize, Map.tileSize);
        }
    }

    @Override
    public void updatePosition() {
        Tile[][] tiles = map.getTileArray();
        updateCurrentPosition(tiles);
        updatePreviousPosition(tiles);
        calculateXY();
    }

    @Override
    public void updateCurrentPosition(Tile[][] tilesArray) {
        for(Tile t : tiles){
            tilesArray[t.getIndX()][t.getIndY()].setStatus(statusObstacle); // set new obstacle position in a global map
        }

    }

    @Override
    public void updatePreviousPosition(Tile[][] tilesArray) {
        //TODO OLD POSITION NEEDED
    }

    @Override
    public void calculateXY() {
        for(Tile t : tiles){
            t.setX(t.getIndX() * Map.tileSize);
            t.setY(t.getIndY() * Map.tileSize);
        }
    }
}
