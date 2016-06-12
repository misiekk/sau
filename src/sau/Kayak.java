package sau;

import java.util.ArrayList;

import static sau.Map.TILE_SIZE;
import static sau.Map.X_TILES_COUNT;
import static sau.Map.Y_TILES_COUNT;

/**
* Kayak consists of 2 tiles. All kayak data are connected to the TOP tile.
* Kayak can move only in one axis (left-right).
*/

public class Kayak extends Tile implements MapUpdater{

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

/*
        setIndX(_indX);
        setIndY(_indY);
        setOldIndX(_indX);
        setOldIndY(_indY);
*/
        //calculateXY();

        //updateCurrentPosition(map.getTileArray());
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

    /* Method updates current and previous kayak's position in a global map */
    @Override
    public void updatePosition() {
        Tile[][] tiles = map.getTileArray();
        updatePreviousPosition(tiles);
        updateCurrentPosition(tiles);
        calculateXY();
    }

    @Override
    public void updateCurrentPosition(Tile[][] tilesArray) {
        for(int i=0; i<KAYAK_WIDTH; ++i){   // x
            for(int j=0; j<KAYAK_HEIGHT; ++j){  // y
                tilesArray[getIndX()+i][getIndY()+j].setStatus(STATUS_KAYAK);
                /*
                Tile tile = tilesArray[getIndX()+i][getIndY()+j];
                if(tile.getStatus() == STATUS_OBSTACLE)
                    tile.setStatus(STATUS_COLLISION);
                else
                    tile.setStatus(STATUS_KAYAK);*/
            }
        }
    }

    @Override
    public void updatePreviousPosition(Tile[][] tilesArray) {
        for(int i=0; i<X_TILES_COUNT; ++i){
            for (int j = 0; j < Y_TILES_COUNT; ++j) {
                if(tilesArray[i][j].getStatus() != STATUS_KAYAK){
                    continue;
                }
                tilesArray[i][j].setStatus(STATUS_FREE);
            }
        }
    }

    private int findMaxRightIndex(){
        int idx = 0;
        for(Tile t : tiles){
            if(t.getIndX() > idx){
                idx = t.getIndX();
            }
        }
        return idx;
    }

    private int findMaxLeftIndex(){
        int idx = X_TILES_COUNT;
        for(Tile t : tiles){
            if(t.getIndX() < idx){
                idx = t.getIndX();
            }
        }
        return idx;
    }

    /* Method checks if kayak can move one tile RIGHT, if yes updates kayak's position in a global map and returns true;
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

    /* Method checks if kayak can move one tile LEFT, if yes updates kayak's position in a global map and returns true;
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
