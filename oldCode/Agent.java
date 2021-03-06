package sau;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by kasia on 11.06.16.
 */
public class Agent {
    public float alpha, epsilon, gamma; //learning rate, epsilon, discount rate
    public int numTraining;

    int seed = 1;
    State lastState;
    Action lastAction;
    public float episodeRewards = 0.0f;
    public float totalTrainRewards = 0.0f;
    public float totalTestRewards = 0.0f;
    public int episodesSoFar = 0;
    public ArrayList<Float> qvalues; //for debugging
    Kayak kayak;
    ArrayList<Float> weights; // feature functions' weight for approximation
    public Agent(float alpha, float epsilon, float gamma, int numTraining){
        this.alpha = alpha;
        this.epsilon = epsilon;
        this.gamma = gamma;
        this.numTraining = numTraining;
        this.qvalues = new ArrayList<Float>();
        qvalues.add(0.0f);  qvalues.add(0.0f);  qvalues.add(0.0f);
        initWeights();
    }

    public void setKayak(Kayak kayak){
        this.kayak = kayak;
    }
    private void initWeights(){
        weights = new ArrayList<Float>();
        for (int i =0 ; i < State.NUM_FEATURES; i++)
            weights.add(0.0f);
    }

    public void startEpisode(){
        lastState = null;
        lastAction = null;
        episodeRewards = 0.0f;
        seed = 1;
    }

    public void stopEpisode(){
        episodesSoFar += 1;
        if(episodesSoFar < numTraining)
            totalTrainRewards += episodeRewards;
        else
            totalTestRewards += episodeRewards;

        if (episodesSoFar >= numTraining){
            //stop training
            epsilon = 0;    //no exploration
            alpha = 0;      //no learning
        }
    }

    public void act(State state){
        Action action = getAction(state);
        if(action != null) {
            State nextState = doAction(state, action);
            observe(nextState);
        }
    }

    protected void observe(State state){
        int debug;
        if (lastState != null){
            float reward = state.getReward();// - lastState.getReward();
            if(reward < 0)
                debug = 1;
            observeTransition(lastState, lastAction, state, reward);
        }
    }
    protected void observeTransition(State state, Action action, State nextState, float deltaReward){
        episodeRewards += deltaReward;
        update(state, action, nextState, deltaReward);
    }

    //updates the approximation weights based on transition
    protected void update(State state, Action action, State nextState, float deltaReward){
        float maxNext = getMaxQValue(nextState);
        float currentQ = getQValue(state, action);
        float error = deltaReward + gamma * maxNext - currentQ;
        int temp = 0;
        for (int i = 0; i < weights.size(); i++){
            Float w = weights.get(i);
            int f = nextState.getFeatures().get(i);
            weights.set(i, w + alpha * error * f);
        }
    }

    public void atTerminalState(State state){
        //float deltaReward = state.getReward() - lastState.getReward();
        float deltaReward = state.getReward();
        observeTransition(lastState, lastAction, state, deltaReward);
        stopEpisode();

        if(!isInTraining())
            System.out.println("Finished training");
    }
    public boolean isInTraining(){
        return (episodesSoFar < numTraining);
    }

    private State doAction(State state, Action action){
        lastState = state;
        lastAction = action;
        kayak.doAction(action);
        return new State(state, action);
        //return new State(kayak);
    }

    //the best action to take in the state given by the policy
    protected Action getPolicy(State state, ArrayList<Action> legalActions){
        Action action = null;
        return  action;
    }

    //choose an action and return it
    protected Action getAction(State state){
        ArrayList<Action> legalActions = state.getLegalActions();
        Action action;
        //get random numer
        //if bigger than epsilon
        //then choose argmax Q(s,a');
        //action = getBestAction(state);
        //otherwise explore and pick a random action

        Random rand = new Random();
//        Random rand = new Random(seed++);
        int index = rand.nextInt(legalActions.size());
        return legalActions.get(index);

    }

    /*return 0 if that state is terminal
      returns w*feature vector otherwise */
    protected float getQValue(State state, Action action){
        //simulate performing action and going from state to nextState
      /*  kayak.doAction(action);
        kayak.moveDown(); //simulate obstacles getting closer
        kayak.observeObstacles();
        State nextState = new State(kayak);
        kayak.moveUp(); //undo simulation
        kayak.observeObstacles();
        if (nextState.getReward() < 0)
            return 0;
        float qValue = nextState.getValue(weights);
        return qValue;*/
        State nextState = new State(state, action);
        return nextState.getValue(weights);
    }

    /* returns max_action Q(state, action) over legal actions
        If there are no legal actions (e.g. at the terminal state, returns 0.0*/
    float getMaxQValue(State state){
        if (state.getReward() < 0) //terminal state: died
            return 0;
        float maxQValue = 0;
        //for each legal action, take it and calc QValue
        for (Action action : state.getLegalActions()){
            float qValue = getQValue(state, action);

            //==========debugging================
            int index = 0;
            if(action.direction == Action.STRAIGHT)
                index = 2;
            else if (action.direction == Action.RIGHT)
                index = 1;
            qvalues.set(index, qValue);
            //==========end of debugging===========

            if (qValue > maxQValue) {
                maxQValue = qValue;
            }
        }
       return maxQValue;
    }

    /* returns the best action to take in a state.
       returns null if there are no legal actions */
    Action getBestAction(State state) {
        if (state.getReward() < 0) //terminal state: died
            return null; //TODO check this??
        Action bestAction = new Action(Action.STRAIGHT);
        ArrayList<Action> legalActions = state.getLegalActions();
        float maxQValue = 0;
        for (Action action : state.getLegalActions()) {
            float qValue = getQValue(state, action);
            if (qValue > maxQValue) {
                maxQValue = qValue;
                bestAction.direction = action.direction;
            }
        }
        return bestAction;
    }

    //=============== getters and setters =======================
    ArrayList<Float> getWeights(){
        return weights;
    }
}
