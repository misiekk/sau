package sau;

import java.awt.*;
import java.util.ArrayList;

import static sau.Tile.STATUS_FREE;
import static sau.Tile.STATUS_OBSTACLE;

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
            if(t.getIndY() <  Map.Y_TILES_COUNT) {    // prevention from going out of bound of global map indexes for obstacles > 1 tiles high
                g.fillRect(t.getX(), t.getY(), Map.TILE_SIZE, Map.TILE_SIZE);
            }
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
            if(t.getIndY() < Map.Y_TILES_COUNT) {     // prevention from going out of bound of global map indexes for obstacles > 1 tiles high
                tilesArray[t.getIndX()][t.getIndY()].setStatus(STATUS_OBSTACLE); // set new obstacle position in a global map
            }
        }

    }

    @Override
    public void updatePreviousPosition(Tile[][] tilesArray) {
        for(Tile t : tiles) {
            if(t.getOldIndY() < Map.Y_TILES_COUNT) {      // prevention from going out of bound of global map indexes for obstacles > 1 tiles high
                tilesArray[t.getOldIndX()][t.getOldIndY()].setStatus(STATUS_FREE);
            }
        }
    }

    @Override
    public void calculateXY() {
        for(Tile t : tiles){
            t.setX(t.getIndX() * Map.TILE_SIZE);
            t.setY(t.getIndY() * Map.TILE_SIZE);
        }
    }

    /* Method moves the obstacle up; if (the part of) the obstacle is out of the map, it removes proper tiles from obstacle
    * and frees these tiles in the map */
    public void moveUp(){
        ArrayList<Tile> tilesToRemove = new ArrayList<>();   // list with tiles of obstacle to remove
        for(Tile t : tiles) {
            t.setOldIndY(t.getIndY());
            t.setIndY(t.getIndY() - 1);

            if(t.getIndY() < 0){
                tilesToRemove.add(t);
                map.getTileArray()[t.getIndX()][t.getOldIndY()].setStatus(STATUS_FREE);
            }
        }

        tiles.removeAll(tilesToRemove);

        updatePosition();
    }

    /* Method checks if the obstacle should be removed from obstacle list */
    boolean toRemove(){
        return this.tiles.isEmpty();
    }
}
