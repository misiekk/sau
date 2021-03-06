package sau;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Created by kasia on 11.06.16.
 */
public class State {
    private final int stayingAliveValue = 1;
    private final int dyingPenalty = -1000;
   // private ArrayList<Double> environment;
    public ArrayList<Integer> kayakPositions;
    public int[][]statusBoard;
    public Map map;
    public boolean collision = false;
    //public boolean isTerminal = false;

    public double[] getFeatures() {
        /*
        int i;
        double features[] = new double[environment.size() + 2]; //2 = action + polarization
        for (i = 0; i < environment.size(); i++)
            features[i] = environment.get(i);
        features[i] = action.direction * 1.0 / Action.ACTIONS_NUM; //normalize action*/
        int index = 1;
        double features[] = new double[statusBoard.length * statusBoard[0].length + 1]; //1 for bias
        for(int i = 0; i < statusBoard.length; i++)
            for (int j = 0; j < statusBoard[0].length; j++) {
                features[i * Map.X_TILES_COUNT + j] = statusBoard[i][j] * 1.0 / Tile.STATUS_NUM;
            }

        features[statusBoard.length] = 1; // bias
        return features;
    }

    public State(int[][] statusBoard) {
        this.statusBoard = statusBoard;
       /* environment = new ArrayList<>(Map.X_TILES_COUNT * Map.Y_TILES_COUNT);
        //prepare normalized values of the board
        for (int i = 0; i < map.getTileArray().length; i++)
            for (int j = 0; j < map.getTileArray()[0].length; j++)
                environment.add(1.0 * map.getTileArray()[i][j].getStatus() / Tile.STATUS_NUM);*/
        //setKayakPositions(environment); // not needed here?
        setKayakPositions();
        collision = collisionOccurred(statusBoard);
    }

    private  void setKayakPositions(){
        kayakPositions = new ArrayList<>();
        for(int i = 0; i < statusBoard.length; i++)
            for(int j = 0; j < statusBoard[0].length; j++)
                if(statusBoard[i][j] == Tile.STATUS_KAYAK)
                    kayakPositions.add(i * Map.X_TILES_COUNT + j);
    }

    private boolean collisionOccurred(int[][] statusBoard){
        for(int i = 0; i < statusBoard.length; i++)
            for(int j = 0; j < statusBoard[0].length; j++)
                if(statusBoard[i][j] == Tile.STATUS_COLLISION)
                    return true;
        return false;
    }

    public int getReward(){
        if(collision)
            return dyingPenalty;
        return  stayingAliveValue;
    }

    public ArrayList<Action> getLegalActions(){
        ArrayList<Action> legalActions = new ArrayList<>();
        int columnPos = kayakPositions.get(0) % Map.X_TILES_COUNT;

        if(columnPos != 0)
            legalActions.add(new Action(Action.LEFT));
        if(columnPos != Map.X_TILES_COUNT - Kayak.KAYAK_WIDTH)
            legalActions.add(new Action(Action.RIGHT));
        legalActions.add(new Action(Action.STRAIGHT));

        return legalActions;
    }
    /*public State(State state, Action action){
        setKayakPositions(state.environment);
        environment = new ArrayList<>(state.environment.size());
        int rowLength = Map.X_TILES_COUNT;
        //TODO simulate performing action in State state and calculate environment
        //move kayak to left or right
        int dx = action.getMapDirection();
        if(dx < 0) {
            if (kayakPositions.get(0) / rowLength > 0) { //if kayak is not next to the left shore

            }
        }
        for(int i = rowLength; i < state.environment.size(); i++){
            int indX = i/Map.X_TILES_COUNT;
            int indY = i - indX * Map.X_TILES_COUNT;
            //move everything one up
            environment.set(i - rowLength, environment.get(i));
        }
}
    private void setKayakPositions(ArrayList<Double> environment){
        kayakPositions = new ArrayList<>();
        for(int i = 0; i < environment.size(); i++)
            if(environment.get(i) * Tile.STATUS_NUM * 1.0 == Tile.STATUS_KAYAK)
                kayakPositions.add(i);
    }

    */





}
    //old logic
/*
    public static final int NUM_FEATURES = 6;
    //private Tile[][] tiles;
    private Map map;
    private final int SHORE_LEFT = 0;
    private final int SHORE_RIGHT = 1;
    private final int ROCK_LEFT = 2;
    private final int ROCK_RIGHT = 3;
    private final int ROCK_AHEAD = 4;
    private final int POLARIZATION = 5;
    // private ArrayList<Integer> features;
    private ArrayList<Float> normalizedFeatures = new ArrayList<>(Collections.nCopies(6, 0.0f));
    private ArrayList<Action> legalActions;

    public State(Kayak kayak){
        features = new ArrayList<Integer>();
        features.add(kayak.distanceToLeftShore());
        features.add(kayak.distanceToRightShore());
        features.add(kayak.distanceToRockLeft());
        features.add(kayak.distanceToRockRight());
        features.add(kayak.distanceToRockAhead());
        features.add(1);
        normalizeFeatures();
        //this.legalActions = kayak.getLegalActions();
    }

    public State(State lastState, Action action){
        features = new ArrayList<Integer>();
        ArrayList<Integer> mask = new ArrayList<>(Collections.nCopies(5, 0));
        if(lastState.getFeatures().get(ROCK_AHEAD) < Map.Y_TILES_COUNT)
            mask.set(ROCK_AHEAD, -1);
        else
            mask.set(ROCK_AHEAD, 0);

        if(action.direction == Action.LEFT){
            mask.set(SHORE_LEFT, -1);
            mask.set(SHORE_RIGHT, 1);
            if(lastState.getRockLeft() > 0)
                mask.set(ROCK_LEFT, -1);
            else
                mask.set(ROCK_LEFT, -0);
            mask.set(ROCK_RIGHT, 1);
        }
        else if(action.direction == Action.RIGHT){
            mask.set(SHORE_LEFT, 1);
            mask.set(SHORE_RIGHT, -1);

            mask.set(ROCK_LEFT, 1);
            if (lastState.getRockLeft() > 0)
                mask.set(ROCK_RIGHT, -1);
            else
                mask.set(ROCK_RIGHT, 0);

        }

        features.add(lastState.getShoreLeft()+mask.get(SHORE_LEFT));
        features.add(lastState.getShoreRight()+mask.get(SHORE_RIGHT));
        features.add(lastState.getRockLeft() + mask.get(ROCK_LEFT));
        features.add(lastState.getRockRight() + mask.get(ROCK_RIGHT));
        features.add(lastState.getRockAhead() + mask.get(ROCK_AHEAD));
        features.add(1);//polarization
        normalizeFeatures();
    }

    private void normalizeFeatures(){
        for (int i = 0; i < 4; i++){
            float value = features.get(i)/(Map.X_TILES_COUNT - Kayak.KAYAK_WIDTH + 1);
            normalizedFeatures.set(i, value);
        }
        float rockAheadNormalized = features.get(ROCK_AHEAD)/ (Map.Y_TILES_COUNT - Kayak.KAYAK_HEIGHT + 1);
        normalizedFeatures.set(ROCK_AHEAD, rockAheadNormalized);
        normalizedFeatures.set(POLARIZATION, 1.0f);
    }

    public ArrayList<Action> getLegalActions(){
        ArrayList<Action> actions = new ArrayList<>();
        if(features.get(SHORE_LEFT) > 0)
            actions.add(new Action(Action.LEFT));
        if(features.get(SHORE_RIGHT) > 0)
            actions.add(new Action(Action.RIGHT));
        actions.add(new Action(Action.STRAIGHT));
        return actions;
        //return legalActions;
    }

    public int getReward(){
        if(features.get(ROCK_LEFT) == 0 || features.get(ROCK_RIGHT) == 0 || features.get(ROCK_AHEAD) ==0)
            return dyingPenalty;
        return stayingAliveValue;
    }

    public float getValue(ArrayList<Float> weights){
        float score = 0;
        for (int i = 0; i<weights.size(); i++)
            score+=weights.get(i)*normalizedFeatures.get(i);

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


}*/

/* Features:
    shoreLeft - x distance from Kayak to the left shore (left on the screen, not left for the kayak)
    shoreRight - x distance from Kayak to the right shore
    rockLeft - x distance from Kayak to the closest rock to the left of the kayak (on the same "height" as kayak)
    rockRight - x distance from Kayak to the closest rock to the left of the kayak
    rockAhead - y distance from Kayak to the closest rock in front of it
 */