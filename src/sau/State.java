package sau;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by kasia on 11.06.16.
 */
public class State {
    public static final int NUM_FEATURES = 5;
    //private Tile[][] tiles;
    private Map map;
    private final int stayingAliveValue = 1;
    private final int dyingPenalty = - 1000;
    private final int SHORE_LEFT = 0;
    private final int SHORE_RIGHT = 1;
    private final int ROCK_LEFT = 2;
    private final int ROCK_RIGHT = 3;
    private final int ROCK_AHEAD = 4;
    private ArrayList<Integer> features;
    private ArrayList<Action> legalActions;

    public State(Kayak kayak){
        features = new ArrayList<Integer>();
        features.add(kayak.distanceToLeftShore());
        features.add(kayak.distanceToRightShore());
        features.add(kayak.distanceToRockLeft());
        features.add(kayak.distanceToRockRight());
        features.add(kayak.distanceToRockAhead());
        this.legalActions = kayak.getLegalActions();
    }

    public ArrayList<Action> getLegalActions(){
        return legalActions;
    }

    public int getReward(){
        if(features.get(ROCK_LEFT) == 0 || features.get(ROCK_RIGHT) == 0 || features.get(ROCK_AHEAD) ==0)
            return dyingPenalty;
        return stayingAliveValue;
    }

    public float getValue(ArrayList<Float> weights){
        float score = 0;
        for (int i = 0; i<weights.size(); i++)
            score+=weights.get(i)*features.get(i);

        return score;
    }

    public int getShoreLeft(){
        return features.get(SHORE_LEFT);
    }

    public int getShoreRight(){
        return features.get(SHORE_RIGHT);
    }

    public int getRockLeft(){
        return features.get(ROCK_LEFT);
    }

    public int getRockRight(){
        return features.get(ROCK_RIGHT);
    }
    public int getRockAhead(){
        return features.get(ROCK_AHEAD);
    }

    public ArrayList<Integer> getFeatures(){
        return  features;
    }

}

/* Features:
    shoreLeft - x distance from Kayak to the left shore (left on the screen, not left for the kayak)
    shoreRight - x distance from Kayak to the right shore
    rockLeft - x distance from Kayak to the closest rock to the left of the kayak (on the same "height" as kayak)
    rockRight - x distance from Kayak to the closest rock to the left of the kayak
    rockAhead - y distance from Kayak to the closest rock in front of it
 */