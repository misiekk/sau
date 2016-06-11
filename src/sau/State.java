package sau;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by kasia on 11.06.16.
 */
public class State {
    State(){

    }

    public ArrayList<Action> getLegalActions(){
        ArrayList<Action> legalActions = new ArrayList<Action>();
        return legalActions;
    }

    public float getScore(){
        float score = 0;
        return  score;
    }
}
