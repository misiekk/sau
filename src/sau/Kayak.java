package sau;

public class Kayak extends Tile {

    private Map map;
    private int oldIndX, oldIndY;   // indexes of previous state

    Kayak(int _indX, int _indY, Map m) {
        this.map = m;

        this.oldIndX = _indX;
        this.oldIndY = _indY;

        setIndX(_indX);
        setIndY(_indY);
        calculateXY();

        setStatus(statusKayak);
        updateCurrentPosition(map.getTileArray());
    }

    private void calculateXY() {
        setX(this.getIndX() * Map.tileSize);
        setY(this.getIndY() * Map.tileSize);
    }

    public void updatePosition() {
        Tile[][] tiles = map.getTileArray();
        updateCurrentPosition(tiles);
        updatePreviousPosition(tiles);
        calculateXY();
    }

    private void updateCurrentPosition(Tile[][] tiles) {
        tiles[getIndX()][getIndY()].setStatus(statusKayak); // set new kayak position in a global map
    }

    private void updatePreviousPosition(Tile[][] tiles) {
        tiles[this.oldIndX][this.oldIndY].setStatus(statusFree);    // release previous kayak position in a global map TODO: status free?
    }

    public boolean moveRight() {
        if (this.getIndX() >= Map.xTilesCount - 1) {
            return false;
        }
        //new position
        this.oldIndX = this.getIndX();
        this.setIndX(this.getIndX() + 1);

        updatePosition();
        return true;
    }

    public boolean moveLeft() {
        if (this.getIndX() <= 0) {
            return false;
        }
        //new position
        this.oldIndX = this.getIndX();
        this.setIndX(this.getIndX() - 1);

        updatePosition();
        return true;
    }
}
