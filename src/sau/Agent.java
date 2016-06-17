package sau;

import mlp.HyperbolicTangent;
import mlp.NeuralNetwork;

import java.util.ArrayList;
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
        int inputLayerSize = Map.X_TILES_COUNT * Map.Y_TILES_COUNT + 1; //board size + action
        int hiddenLayerSize = Map.X_TILES_COUNT;
        int outputLayerSize = 1; // QValue(s,a)

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

    protected void act(State state){
        Action action = getAction(state);
        if(action != null) {
            State nextState = doAction(state, action);
            observeTransition(nextState, action);
        }
    }
    protected Action getAction(State state){ //TODO choose not only randomly
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

    protected State doAction(State state, Action action){
        lastState = state;
        lastAction = action;
        kayak.doAction(action);
        return new State(kayak);
        //return new State(state, action);
    }

    protected void observeTransition(State nextState, Action action){
        int reward = nextState.getReward();
        episodeRewards += reward;
        updateNN(nextState, action, reward);
    }

    protected void updateNN(State nextState, Action action, int reward){
        double input[] =lastState.getFeatures(action);
        double predictionVector[] = NN.getOutput(input);
        double target = reward + getMaxQValue(nextState);
        double error = Math.abs(target - predictionVector[0]);
        NN.backPropagate(input, predictionVector, error); //TODO square error?
    }
    protected double getMaxQValue(State state){
        double maxQValue = 0;
        //for each legal action, simulate it and calc QValue
        for (Action action : state.getLegalActions()){
            State nextState = new State(state, action); //simulate performing action
            double qValue = NN.getOutput(nextState.getFeatures(action))[0];

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
}
