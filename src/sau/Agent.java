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
        int inputLayerSize = Map.X_TILES_COUNT * Map.Y_TILES_COUNT; //board size
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
        ArrayList<Action> legalActions = state.getLegalActions();
        Action action;
        //get random numer
        //if bigger than epsilon
        //then choose argmax Q(s,a');
        //action = getBestAction(state);
        //otherwise explore and pick a random action
        Random rand = new Random();
        //Random rand = new Random(seed++);
        int index = rand.nextInt(legalActions.size());
        return legalActions.get(index);
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
        ArrayList<Double> targetsList = new ArrayList(Arrays.asList(targets));
        double maxQValue = Collections.max(targetsList);
        for (int i = 0; i < targets.length; i++)
            if (targets[i] == maxQValue)
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

    public void atTerminalState(State prevState, Action action, State state){
        observeTransition(prevState, action, state);
        stopEpisode();

        if(!isInTraining())
            System.out.println("Finished training");
    }

    protected boolean isInTraining(){
        return (episodesSoFar < numTraining);
    }
}
