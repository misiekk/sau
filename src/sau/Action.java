package sau;

/**
 * Created by kasia on 11.06.16.
 */
public class Action {
    public static final Integer LEFT = 1;
    public static final Integer RIGHT = 2;
    public static final Integer STRAIGHT = 3;
    public static final int ACTIONS_NUM = 3;
    public Integer direction;
    public Action(Integer direction){
        this.direction = direction;
    }
    public String print(){
        if(direction == LEFT)
            return "left";
        if(direction == RIGHT)
            return "right";
        return "straight";
    }

    public int getMapDirection(){
        if(direction == LEFT)
            return -1;
        if(direction == RIGHT)
            return 1;
        return 0;
    }
}
