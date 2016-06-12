package sau;

import java.util.ArrayList;

import static sau.Map.TILE_SIZE;
import static sau.Map.X_TILES_COUNT;

/**
* Kayak consists of N tiles.
* Kayak can move only in one axis (left-right).
*/

public class Kayak extends Tile {

    public static int KAYAK_WIDTH = 2;
    public static int KAYAK_HEIGHT = 5;
    private ArrayList<Tile> tiles;      // list with tiles that define whole kayak
    private Map map;
    private int rockLeft;
    private int rockRight;
    private int rockStraight;

    Kayak(int _indX, int _indY, Map m) {
        this.map = m;
        this.tiles = new ArrayList<>();
        for (int i = 0; i < KAYAK_WIDTH; ++i) {   // x
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

    public ArrayList<Tile> getTiles() {
        return this.tiles;
    }

    /* Method sets current coordinates of kayak.
    *  XY cords are used for painting.
    *  Y cord doesn't change! */
    public void calculateXY() {
        for (Tile t : tiles) {
            t.setX(t.getIndX() * TILE_SIZE);
        }
    }

    /* Method returns maximum right index of tile in an obstacle */
    public int findMaxRightIndex() {
        int idx = 0;
        for (Tile t : tiles) {
            if (t.getIndX() > idx) {
                idx = t.getIndX();
            }
        }
        return idx;
    }

    /* Method returns maximum left index of tile in an obstacle */
    public int findMaxLeftIndex() {
        int idx = X_TILES_COUNT;
        for (Tile t : tiles) {
            if (t.getIndX() < idx) {
                idx = t.getIndX();
            }
        }
        return idx;
    }

    /* Method checks if kayak can move one tile RIGHT, if yes updates all tiles' indexes
     * else returns false and does nothing */
    public boolean moveRight() {
        if (!canGoRight()) {
            return false;
        }
        //new position
        for (Tile t : tiles) {
            t.setOldIndX(t.getIndX());
            t.setIndX(t.getIndX() + 1);
        }
        calculateXY();

        return true;
    }

    /* Method checks if kayak can move one tile LEFT, if yes updates all tiles' indexes
     * else returns false and does nothing */
    public boolean moveLeft() {
        if (!canGoLeft())
            return false;
        //new position
        for (Tile t : tiles) {
            t.setOldIndX(t.getIndX());
            t.setIndX(t.getIndX() - 1);
        }
        calculateXY();
        return true;
    }

    //helper function simulation moving the kayak up the river
    public boolean moveUp() {
        //new position
        for (Tile t : tiles) {
            t.setOldIndY(t.getIndY());
            t.setIndY(t.getIndY() - 1);
        }
        calculateXY();

        //update map
        map.updateMap();
        return true;
    }

    //helper function simulation moving the kayak down the river
    public boolean moveDown() {
        //new position
        for (Tile t : tiles) {
            t.setOldIndY(t.getIndY());
            t.setIndY(t.getIndY() + 1);
        }
        calculateXY();

        //update map
        map.updateMap();
        return true;
    }

    private boolean canGoLeft() {
        if (findMaxLeftIndex() <= 0)
            return false;
        return true;
    }

    private boolean canGoRight() {
        if (findMaxRightIndex() >= X_TILES_COUNT - KAYAK_WIDTH + 1)
            return false;
        return true;
    }

    public ArrayList<Action> getLegalActions() {
        ArrayList<Action> legalActions = new ArrayList<Action>();
        legalActions.add(new Action(Action.STRAIGHT));
        if (canGoRight())
            legalActions.add(new Action(Action.RIGHT));
        if (canGoLeft())
            legalActions.add(new Action(Action.LEFT));

        return legalActions;
    }

    public void doAction(Action action) {
        if (action.direction == Action.LEFT)
            moveLeft();
        else if (action.direction == Action.RIGHT)
            moveRight();
    }

    public int distanceToRightShore() {
        return X_TILES_COUNT - KAYAK_WIDTH - tiles.get(0).getIndX() + 1;
    }

    public int distanceToLeftShore() {
        return tiles.get(0).getIndX() + 1;
    }

    public void observereObstacles() {
        rockStraight = Integer.MAX_VALUE;
        rockLeft = Integer.MAX_VALUE;
        rockRight = Integer.MAX_VALUE;
        Tile originTile = tiles.get(0);

        //check straight ahead
        for (int indY = originTile.getIndY() + KAYAK_HEIGHT; indY < Map.Y_TILES_COUNT; indY++) {
            boolean foundObstacle = false;
            for (int indX = originTile.getIndX(); indX < originTile.getIndX() + KAYAK_WIDTH; indX++) {
                if (map.getTileArray()[indX][indY].getStatus() == STATUS_OBSTACLE) {
                    rockStraight = indY - (originTile.getIndY() + KAYAK_HEIGHT);
                    foundObstacle = true;
                    break;
                }
                if (map.getTileArray()[indX][indY].getStatus() == STATUS_COLLISION) {
                    rockStraight = 0;
                    break;
                }
            }
            if (foundObstacle)
                break;
        }

        //check left
        for (int indX = originTile.getIndX(); indX >= 0; indX--) {
            boolean foundObstacle = false;
            for (int indY = originTile.getIndY(); indY <= KAYAK_HEIGHT; indY++) {
                if (map.getTileArray()[indX][indY].getStatus() == STATUS_OBSTACLE) {
                    rockLeft = originTile.getIndX() - indX;
                    foundObstacle = true;
                    break;
                }
                if (map.getTileArray()[indX][indY].getStatus() == STATUS_COLLISION) {
                    rockLeft = 0;
                    foundObstacle = true;
                    break;
                }
            }
            if (foundObstacle)
                break;
        }

        //check right
        for (int indX = originTile.getIndX() + KAYAK_WIDTH; indX < Map.X_TILES_COUNT; indX++) {
            boolean foundObstacle = false;
            for (int indY = originTile.getIndY(); indY <= KAYAK_HEIGHT; indY++) {
                if (map.getTileArray()[indX][indY].getStatus() == STATUS_OBSTACLE) {
                    rockRight = indX - (originTile.getIndX() + KAYAK_WIDTH) + 1;
                    foundObstacle = true;
                    break;
                }
                if (map.getTileArray()[indX][indY].getStatus() == STATUS_COLLISION) {
                    rockRight = 0;
                    foundObstacle = true;
                    break;
                }
            }
            if (foundObstacle)
                break;
        }
    }

    public int distanceToRockAhead() {
        return rockStraight;
    }

    public int distanceToRockLeft(){
        return rockLeft;
    }

    public  int distanceToRockRight(){
        return rockRight;
    }
}
