package sau;

/**
 * Created by kasia on 11.06.16.
 */
public class Action {
    static final Integer LEFT = -1;
    static final Integer RIGHT = 1;
    static final Integer STRAIGHT = 0;
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
}
