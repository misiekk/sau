package sau;

import java.util.ArrayList;

import static sau.Map.TILE_SIZE;
import static sau.Map.X_TILES_COUNT;

/**
* Kayak consists of N tiles.
* Kayak can move only in one axis (left-right).
*/

public class Kayak extends Tile{

    public static int KAYAK_WIDTH = 2;
    public static int KAYAK_HEIGHT = 5;
    private ArrayList<Tile> tiles;      // list with tiles that define whole kayak
    private Map map;

    Kayak(int _indX, int _indY, Map m) {
        this.map = m;
        this.tiles = new ArrayList<>();
        for(int i=0; i<KAYAK_WIDTH; ++i) {   // x
            for (int j = 0; j < KAYAK_HEIGHT; ++j) {
                Tile t = new Tile(
                        Map.TILE_SIZE * (i + _indX),                   // x
                        Map.TILE_SIZE * (j + _indY),           // y
                        i + _indX,                                    // x index
                        j + _indY);                         // y index
                t.setStatus(Tile.STATUS_KAYAK);
                this.tiles.add(t);
            }
        }
    }

    public ArrayList<Tile> getTiles(){
        return this.tiles;
    }

    /* Method sets current coordinates of kayak.
    *  XY cords are used for painting.
    *  Y cord doesn't change! */
    public void calculateXY() {
        for(Tile t : tiles){
            t.setX(t.getIndX()*TILE_SIZE);
        }
    }

    /* Method returns maximum right index of tile in an obstacle */
    private int findMaxRightIndex(){
        int idx = 0;
        for(Tile t : tiles){
            if(t.getIndX() > idx){
                idx = t.getIndX();
            }
        }
        return idx;
    }
    /* Method returns maximum left index of tile in an obstacle */
    private int findMaxLeftIndex(){
        int idx = X_TILES_COUNT;
        for(Tile t : tiles){
            if(t.getIndX() < idx){
                idx = t.getIndX();
            }
        }
        return idx;
    }

    /* Method checks if kayak can move one tile RIGHT, if yes updates all tiles' indexes
     * else returns false and does nothing */
    public boolean moveRight() {
        if (findMaxRightIndex() >= X_TILES_COUNT - KAYAK_WIDTH+1) {
            return false;
        }
        //new position
        for(Tile t : tiles){
            t.setOldIndX(t.getIndX());
            t.setIndX(t.getIndX()+1);
        }
        calculateXY();

        return true;
    }

    /* Method checks if kayak can move one tile LEFT, if yes updates all tiles' indexes
     * else returns false and does nothing */
    public boolean moveLeft() {
        if (findMaxLeftIndex() <= 0) {
            return false;
        }
        //new position
        for(Tile t : tiles){
            t.setOldIndX(t.getIndX());
            t.setIndX(t.getIndX()-1);
        }
        calculateXY();
        return true;
    }
}
