package sau;

import java.util.Iterator;

/**
 * Generates obstacles
 */
public class ObstacleGenerator {
    private Map map;

    ObstacleGenerator(Map _m){
        this.map = _m;
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
        // let's generate obstacles consisted of 3 tiles
        if(!map.getObstaclesList().isEmpty()){
            return;
        }
        Obstacle o = new Obstacle(map);
        for(int i=0; i<3; ++i){
            Tile t = new Tile(Map.tileSize*i, Map.tileSize*(Map.yTilesCount-1), i, (Map.yTilesCount-1));
            t.setStatus(Tile.statusObstacle);
            o.addTile(t);
        }

        map.getObstaclesList().add(o);
    }
}
