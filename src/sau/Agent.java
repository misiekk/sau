package sau;

import java.util.ArrayList;

/**
 * Created by kasia on 11.06.16.
 */
public class Agent {
    float alpha, epsilon, gamma; //learning rate, epsilon, discount rate
    int numTraining;

    State lastState;
    Action lastAction;
    float episodeRewards = 0.0f;
    float totalTrainRewards = 0.0f;
    float totalTestRewards = 0.0f;
    int episodesSoFar = 0;
    ArrayList<Float> weights; // feature functions' weight for approximation
    public Agent(float alpha, float epsilon, float gamma, int numTraining){
        this.alpha = alpha;
        this.epsilon = epsilon;
        this.gamma = gamma;
        this.numTraining = numTraining;
    }

    private void initWeights(){
        for (int i =0 ; i < State.numFeatures; i++)
            weights.add(0.0f);
    }

    public void startEpisode(){
        lastState = null;
        lastAction = null;
        episodeRewards = 0.0f;
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

    public void observe(State state){
        if (lastState != null){
            float reward = state.getScore() - lastState.getScore();
            observeTransition(lastState, lastAction, state, reward);
        }
    }
    private void observeTransition(State state, Action action, State nextState, float deltaReward){
        episodeRewards += deltaReward;
        update(state, action, nextState, deltaReward);
    }

    //updates the approximation weights based on transition
    private void update(State state, Action action, State nextState, float deltaReward){

    }
    public void atTerminalState(State state){
        float deltaReward = state.getScore() - lastState.getScore();
        observeTransition(lastState, lastAction, state, deltaReward);
        stopEpisode();

        if(!isInTraining())
            System.out.println("Finished training");
    }
    public boolean isInTraining(){
        return (episodesSoFar < numTraining);
    }

    void doAction(State state, Action action){
        lastState = state;
        lastAction = action;
    }

    /*
    //the best action to take in the state given by the policy
    Action getPolicy(State state){
        Action action = new Action();
        return  action;
    }*/

    //choose an action and return it
    Action getAction(State state){
        Action action = getBestAction(state);
        return action;
    }

    /*return 0 if that state has never been seen
      returns w*feature vector otherwise
     */
    float getQValue(State state, Action action){
        float qValue = 0;
        return qValue;
    }

    /* returns max_action Q(state, action) over legal actions
        If there are no legal actions (e.g. at the terminal state, returns 0.0*/
    float getMaxQValue(State state){
       float value = 0;
       return value;
    }

    /* returns the best action to take in a state.
       returns null if there are no legal actions */
    Action getBestAction(State state){
        Action bestAction = new Action();
        ArrayList<Action> legalActions = state.getLegalActions();
        return bestAction;
    }

    //=============== getters and setters =======================
    ArrayList<Float> getWeights(){
        return weights;
    }
}
