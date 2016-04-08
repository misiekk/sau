package sau;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

import static sau.Tile.statusFree;
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
        for(Tile t : tiles) {
            tilesArray[t.getOldIndX()][t.getOldIndY()].setStatus(statusFree);
        }
    }

    @Override
    public void calculateXY() {
        for(Tile t : tiles){
            t.setX(t.getIndX() * Map.tileSize);
            t.setY(t.getIndY() * Map.tileSize);
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
                map.getTileArray()[t.getIndX()][t.getOldIndY()].setStatus(statusFree);
            }
        }

        tiles.removeAll(tilesToRemove);

        updatePosition();
    }

    /* Method checks if the obstacle should be removed from obstacle list */
    boolean toRemove(){
        if(this.tiles.isEmpty()){
            return true;
        }
        return false;
    }
}
