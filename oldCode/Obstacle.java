package sau;

import java.awt.*;
import java.util.ArrayList;

import static sau.Map.TILE_SIZE;
import static sau.Map.X_TILES_COUNT;
import static sau.Map.Y_TILES_COUNT;
import static sau.Tile.*;

/**
 * Represents single obstacle on the map
 */
public class Obstacle{
    private Map map;
    private ArrayList<Tile> tiles;

    Obstacle(Map _map){
        this.tiles = new ArrayList<Tile>();
        this.map = _map;
    }

    public ArrayList<Tile> getTiles(){
        return this.tiles;
    }
    public void addTile(Tile t){
        this.tiles.add(t);
    }

    public void paint(Graphics g){
        for(Tile t : tiles){
            if(t.getIndY() <  Y_TILES_COUNT) {    // prevention from going out of bound of global map indexes for obstacles > 1 tiles high
                g.fillRect(t.getX(), t.getY(), Map.TILE_SIZE, Map.TILE_SIZE);
            }
        }
    }

    public void calculateXY() {
        for(Tile t : tiles){
            t.setX(t.getIndX() * Map.TILE_SIZE);
            t.setY(t.getIndY() * Map.TILE_SIZE);
        }
    }

    /* Method moves the obstacle up and recalculates XY cords of an obstacle
     * if (the part of) the obstacle is out of the map, it removes proper tiles from obstacle */
    public void moveUp(){
        ArrayList<Tile> tilesToRemove = new ArrayList<>();   // list with tiles of obstacle to remove
        for(Tile t : tiles) {
            t.setOldIndY(t.getIndY());
            t.setIndY(t.getIndY() - 1);

            if(t.getIndY() < 0){
                tilesToRemove.add(t);
            }
        }
        tiles.removeAll(tilesToRemove);

        calculateXY();
    }

    /* Method checks if the obstacle should be removed from obstacle list */
    boolean toRemove(){
        return this.tiles.isEmpty();
    }

}
