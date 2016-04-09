package sau;

/**
* Kayak consists of 2 tiles. All kayak data are connected to the TOP tile.
* Kayak can move only in one axis (left-right).
*/

public class Kayak extends Tile implements MapUpdater{

    private static int SECOND_TILE_IDX = 1;   // constant value in order to prevent generating errors, i guess
    private Map map;

    Kayak(int _indX, int _indY, Map m) {
        this.map = m;

        setIndX(_indX);
        setIndY(_indY);
        setOldIndX(_indX);
        setOldIndY(_indY);

        calculateXY();

        setStatus(STATUS_KAYAK);
        updateCurrentPosition(map.getTileArray());
    }

    /* Method sets current coordinates of kayak.
    *  XY cords are used for painting. */
    public void calculateXY() {
        setX(this.getIndX() * Map.TILE_SIZE);
        setY(this.getIndY() * Map.TILE_SIZE);
    }

    /* Method updates current and previous kayak's position in a global map */
    @Override
    public void updatePosition() {
        Tile[][] tiles = map.getTileArray();
        updateCurrentPosition(tiles);
        updatePreviousPosition(tiles);
        calculateXY();
    }

    @Override
    public void updateCurrentPosition(Tile[][] tilesArray) {
        tilesArray[getIndX()][getIndY()].setStatus(STATUS_KAYAK); // set new kayak position in a global map - top tile
        tilesArray[getIndX()][getIndY()+ SECOND_TILE_IDX].setStatus(STATUS_KAYAK);   // bottom tile
    }

    @Override
    public void updatePreviousPosition(Tile[][] tilesArray) {
        //tilesArray[this.oldIndX][this.oldIndY].setStatus(STATUS_FREE);    // release previous kayak position in a global map TODO: status free?
        //tilesArray[this.oldIndX][this.oldIndY+SECOND_TILE_IDX].setStatus(STATUS_FREE);  // bottom tile
        tilesArray[this.getOldIndX()][this.getOldIndY()].setStatus(STATUS_FREE);    // release previous kayak position in a global map TODO: status free?
        tilesArray[this.getOldIndX()][this.getOldIndY()+ SECOND_TILE_IDX].setStatus(STATUS_FREE);  // bottom tile
    }

    /* Method checks if kayak can move one tile RIGHT, if yes updates kayak's position in a global map and returns true;
     * else returns false and does nothing */
    public boolean moveRight() { // TODO check if no obstacle?
        if (this.getIndX() >= Map.X_TILES_COUNT - 1) {
            return false;
        }
        //new position
        setOldIndX(this.getIndX());
        this.setIndX(this.getIndX() + 1);

        updatePosition();
        return true;
    }

    /* Method checks if kayak can move one tile LEFT, if yes updates kayak's position in a global map and returns true;
     * else returns false and does nothing */
    public boolean moveLeft() { // TODO check if no obstacle?
        if (this.getIndX() <= 0) {
            return false;
        }
        //new position
        setOldIndX(this.getIndX());
        this.setIndX(this.getIndX() - 1);

        updatePosition();
        return true;
    }
}
