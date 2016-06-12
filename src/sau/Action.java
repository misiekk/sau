package sau;

/**
 * Created by kasia on 11.06.16.
 */
public class Action {
    static final Integer LEFT = -1;
    static final Integer RIGHT = 1;
    static final Integer STRAIGHT = 0;
    public Integer direction;
    Action(Integer direction){
        this.direction = direction;
    }

}
