package sau;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by kasia on 11.06.16.
 */
public class State {
    public static final int numFeatures = 5;
    public State(){

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

/* Features:
    shoreLeft - x distance from Kayak to the left shore (left on the screen, not left for the kayak)
    shoreRight - x distance from Kayak to the right shore
    rockLeft - x distance from Kayak to the closest rock to the left of the kayak (on the same "height" as kayak)
    rockRight - x distance from Kayak to the closest rock to the left of the kayak
    rockStraight - y distance from Kayak to the closest rock in front of it
 */