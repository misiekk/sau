package sau;

import mlp.HyperbolicTangent;
import mlp.NeuralNetwork;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by kasia on 11.06.16.
 */
public class Agent {
    public double alpha, epsilon, gamma; //learning rate, epsilon, discount rate
    public int numTraining;
    public float episodeRewards = 0.0f;
    public float totalTrainRewards = 0.0f;
    public float totalTestRewards = 0.0f;
    public int episodesSoFar = 0;
    public ArrayList<Float> qvalues; //for debugging
    Kayak kayak;


    public Agent(double alpha, double epsilon, double gamma, int numTraining) {
        this.alpha = alpha;
        this.epsilon = epsilon;
        this.gamma = gamma;
        this.numTraining = numTraining;
        this.qvalues = new ArrayList<Float>();
        qvalues.add(0.0f); //left
        qvalues.add(0.0f); //right
        qvalues.add(0.0f); //straight

        initNeuralNetwork();
    }
    private void initNeuralNetwork(){
        int inputLayerSize = Map.X_TILES_COUNT * Map.Y_TILES_COUNT + 1 + 1; //board size + action + polarization
        int hiddenLayerSize = Map.X_TILES_COUNT;
        int outputLayerSize = 1; // QValue(s,a)

        int[] layers = new int[]{ inputLayerSize, hiddenLayerSize, outputLayerSize};
        NeuralNetwork NN = new NeuralNetwork(layers, alpha, new HyperbolicTangent());

    }

}
