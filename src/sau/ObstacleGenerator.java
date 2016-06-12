package sau;

import java.util.Iterator;
import java.util.Random;

/**
 * Generates obstacles
 */
public class ObstacleGenerator {
    private static int MAX_OBSTACLE_LENGTH = 1;
    private static int MAX_OBSTACLE_HEIGHT = 10;
    private static int FREQ_COUNTER = 10; // how many tiles will be free after generating an obstacle before the next one
    private int freq;       // parameter to set frequency of generating obstacles -> number of free tiles (in y axis) between 2 obstacles

    private Map map;

    ObstacleGenerator(Map _m){
        this.map = _m;
        this.freq = 0;
    }

    /* Method moves up all the obstacles and removes elements which are out of the map */
    public void update(){
        for (Obstacle o : map.getObstaclesList()){
            o.moveUp();
        }

        Iterator<Obstacle> it = map.getObstaclesList().iterator();
        while(it.hasNext()){
            if(it.next().toRemove()){
                it.remove();
            }
        }

        generate();
    }

    // TODO
    public void generate(){
        /*if(!map.getObstaclesList().isEmpty()){  // only 1 obstacle allowed on the map - at least for now
            return;
        }*/

        if((freq++) < FREQ_COUNTER+MAX_OBSTACLE_HEIGHT){
            return;
        }

        this.freq = 0;
        Random rand = new Random();
        int obstacleLength = rand.nextInt(MAX_OBSTACLE_LENGTH) + 1;     // from 1 to maxObstacleLength
        int obstacleHeight = rand.nextInt(MAX_OBSTACLE_HEIGHT) + 5;     // from 1 to maxObstacleHeight
        int initialXIdx = rand.nextInt(Map.X_TILES_COUNT - obstacleLength);   // x index of obstacle from 0 to max (according to its generated length)
        //System.out.println(initialXIdx + ": " + obstacleLength + "x" + obstacleHeight);

        Obstacle o = new Obstacle(map);
        for(int h = 0; h < obstacleHeight; ++h) {
            for (int i = 0; i < obstacleLength; ++i) {
                Tile t = new Tile(
                        Map.TILE_SIZE * (i + initialXIdx),                   // x
                        Map.TILE_SIZE * (Map.Y_TILES_COUNT - 1 + h),           // y
                        i + initialXIdx,                                    // x index
                        (Map.Y_TILES_COUNT - 1 + h));                         // y index
                t.setStatus(Tile.STATUS_OBSTACLE);
                o.addTile(t);
            }
        }
        map.getObstaclesList().add(o);
    }

    /* Method checks if kayak position is equal to an obstacle position
    * if true: sets status COLLISION in global map */
    public boolean updateCollision(){
        for (Obstacle o : map.getObstaclesList()){
            if(o.checkCollision(map.getTileArray())){
                return true;
            }
        }
        return false;
    }
}
