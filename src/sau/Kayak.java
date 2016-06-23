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
    //private Map map;
    private int rockLeft;
    private int rockRight;
    private int rockStraight;
    private Agent agent;
    public int[][] statusBoard;

    Kayak(int _indX, int _indY, int[][] statusBoard) {
        this.statusBoard = statusBoard;
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

    public void updateStatusBoard(){
        for (Tile t : tiles){
            statusBoard[t.getOldIndX()][t.getOldIndY()] = Tile.STATUS_FREE;
            if(statusBoard[t.getIndX()][t.getIndY()] == Tile.STATUS_OBSTACLE)
                statusBoard[t.getIndX()][t.getIndY()] = Tile.STATUS_COLLISION;
            else
                statusBoard[t.getIndX()][t.getIndY()] = Tile.STATUS_KAYAK;
        }
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
        updateStatusBoard();
        //map.updateMap();
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
        //map.updateMap();
        updateStatusBoard();
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

        //map.updateMap();
        return true;
    }

    //helper function simulation moving the kayak down the river
    public boolean moveDown() {
        //new position
        for (Tile t : tiles) {
            t.setOldIndY(t.getIndY());
            t.setIndY(t.getIndY() + 1);
        }
        //calculateXY();

        //update map
        //map.updateMap();
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
        switch(action.direction){
            case Action.LEFT:
                moveLeft();
                break;
            case Action.RIGHT:
                moveRight();
                break;
            default:
                break;
        }
        /*
        if (action.direction == Action.LEFT)
            moveLeft();
        else if (action.direction == Action.RIGHT)
            moveRight();*/
    }

    public int distanceToRightShore() {
        return X_TILES_COUNT - KAYAK_WIDTH - tiles.get(0).getIndX() + 1;
    }

    public int distanceToLeftShore() {
        return tiles.get(0).getIndX() + 1;
    }

   public void observeObstacles(int[][] statusBoard){
       this.statusBoard = statusBoard;
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

    public Agent getAgent(){
        return agent;
    }

    public void setAgent(Agent agent){
        this.agent = agent;
    }
}
