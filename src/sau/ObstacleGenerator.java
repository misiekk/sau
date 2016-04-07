package sau;

/**
 * Generates obstacles
 */
public class ObstacleGenerator {
    private Map map;

    ObstacleGenerator(Map _m){
        this.map = _m;
    }

    public void update(){
        // tu bedzie wolany generate
        // a w timerze update
    }

    public void generate(){
        // let's generate obstacles consisted of 3 tiles
        Obstacle o = new Obstacle(map);
        for(int i=0; i<3; ++i){
            Tile t = new Tile(Map.tileSize*i, Map.tileSize*(Map.yTilesCount-1), i, (Map.yTilesCount-1));
            t.setStatus(Tile.statusObstacle);
            o.addTile(t);
        }

        map.getObstaclesList().add(o);
    }

}
