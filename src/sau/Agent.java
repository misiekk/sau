package sau;

import mlp.HyperbolicTangent;
import mlp.NeuralNetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
/**
 * Created by kasia on 11.06.16.
 */
public class Agent {
    protected NeuralNetwork NN;
    protected State lastState;
    protected Action lastAction;
    public double alpha, epsilon, gamma; //learning rate, epsilon, discount rate
    public int numTraining;
    public float episodeRewards = 0.0f;
    public float totalTrainRewards = 0.0f;
    public float totalTestRewards = 0.0f;
    public int episodesSoFar = 0;
    Kayak kayak;

    private int seed = 1; //for fixing random numbers for debugging purposes
    public ArrayList<Double> qvalues; //for debugging


    /**
     * Constructor
     * @param alpha learning rate for the neural network Q-approximator
     * @param epsilon greedy-epsilon rate used for exploration
     * @param gamma reward discount rate
     * @param numTraining number of training episodes
     */
    public Agent(double alpha, double epsilon, double gamma, int numTraining) {
        this.alpha = alpha;
        this.epsilon = epsilon;
        this.gamma = gamma;
        this.numTraining = numTraining;
        this.qvalues = new ArrayList<Double>();
        qvalues.add(0.0); //left
        qvalues.add(0.0); //right
        qvalues.add(0.0); //straight

        initNeuralNetwork(alpha);
    }

    /**
     * Initialize network. Here its layers can be configured
     * @param learningRate learning rate (alpha) of the neural network
     */
    protected void initNeuralNetwork(double learningRate){
        int inputLayerSize = Map.X_TILES_COUNT * Map.Y_TILES_COUNT + 1; //board size
        int hiddenLayerSize = Map.X_TILES_COUNT;
        int outputLayerSize = Action.ACTIONS_NUM; // QValue for each action available for a state
        int[] layers = new int[]{ inputLayerSize, hiddenLayerSize, outputLayerSize};
        NN = new NeuralNetwork(layers, learningRate, new HyperbolicTangent());
    }

    public void startEpisode(){
        lastState = null;
        lastAction = null;
        episodeRewards = 0.0f;
        seed = 1;
        if(episodesSoFar < 50)
            epsilon = 0.7;
        else
            epsilon = Math.pow(10.0/(episodesSoFar+1), 1);
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

    public Action chooseAction(State state){ //TODO choose not only randomly
        Random rand = new Random();
        double randomValue = rand.nextDouble();
        if(randomValue <= episodeRewards){
            //explore and pick a random action - epsilon greedy
            ArrayList<Action> legalActions = state.getLegalActions();
            int index = rand.nextInt(legalActions.size());
            return legalActions.get(index);
        }
        else
            return getBestAction(state);
        //Random rand = new Random(seed++);
    }


    protected void observeTransition(State currentState, Action action, State nextState){
        //lastState = currentState;
        //lastAction = action;
        int reward = nextState.getReward();
        episodeRewards += nextState.getReward();
        learn(currentState, nextState, reward);
    }

    protected void learn(State currentState, State nextState, int reward){
        double input[] =currentState.getFeatures(); //including 1 for bias
        double predictions[] = NN.getOutput(input);
        double targets[] = NN.getOutput(nextState.getFeatures());
        double maxQValue = max(targets);
        for (int i = 0; i < targets.length; i++)
            if (targets[i] == maxQValue)
                if(reward < 0)
                    targets[i] = reward;
                else
                targets[i] = reward + gamma * targets[i];
            else
                targets[i] = predictions[i];

        NN.backPropagate(targets, predictions);
    }

   /* protected double getMaxQValue(State state){
        double maxQValue = 0;
        //for each legal action, simulate it and calc QValue
        for (Action action : state.getLegalActions()){
            State nextState = StateSimulator.simulateAction(state, action);
            double qValue = NN.getOutput(nextState.getFeatures())[0];

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
    }*/
    protected Action getBestAction(State state){
        double[] actionValues = NN.getOutput(state.getFeatures());
        double maxActionVal = max(actionValues);
        int actionIndex = 0;
        for (int i = 0; i < actionValues.length; i++)
            if(actionValues[i] == maxActionVal)
                actionIndex = i;

        return new Action(actionIndex);
    }

    public void atTerminalState(State prevState, Action action, State state){
        observeTransition(prevState, action, state);
        stopEpisode();

        if(!isInTraining())
            System.out.println("Finished training");
    }

    protected boolean isInTraining(){
        return (episodesSoFar < numTraining);
    }

    protected double max(double[] table){
        double ret = - 10000;
        for (int i = 0; i < table.length; i++)
            if(table[i] > ret)
                ret = table[i];

        return ret;
    }

    public void setGamma (double gamma) { this.gamma = gamma; }
    public void setEpsilon (double epsilon) { this.epsilon = epsilon; }
    public void setAlpha (double alpha) {
        this.alpha = alpha;
        NN.setLearningRate(alpha);
    }
}
