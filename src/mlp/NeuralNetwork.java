package mlp;

import java.io.FileInputStream;
        import java.io.FileOutputStream;
        import java.io.ObjectInputStream;
        import java.io.ObjectOutputStream;

public class NeuralNetwork implements Cloneable {
    protected double			learningRate = 0.6;
    protected Layer[]			layers;
    protected TransferFunction 	transferFunction;


    /**
     * Creates a Multilayered perceptron
     *
     * @param layers table with neuron numbers for each layer
     * @param learningRate learning rate (alpha)
     * @param fun transfer (activation) function
     */
    public NeuralNetwork(int[] layers, double learningRate, TransferFunction fun)   {
        this.learningRate = learningRate;
        this.transferFunction = fun;
        this.layers = new Layer[layers.length];

        for(int i = 0; i < layers.length; i++)
            if(i != 0)
                this.layers[i] = new Layer(layers[i], layers[i - 1]);
            else
                this.layers[i] = new Layer(layers[i], 0);

    }



    /**
     * Calculate output
     * @param input input value
     * @return table of values generated by the output layer
     */
    public double[] getOutput(double[] input)
    {
        int i, j, k;
        double newValue;
        int outputSize = layers[layers.length - 1].size;
        double output[] = new double[outputSize];

        // Put input into the first layer
        for(i = 0; i < layers[0].size; i++)
            layers[0].neurons.get(i).value = input[i];


        // Approximate: hidden layers + output layers
        for(k = 1; k < layers.length; k++) {
            for (i = 0; i < layers[k].size; i++) {
                newValue = 0.0;
                for (j = 0; j < layers[k - 1].size; j++)
                    newValue += layers[k].neurons.get(i).weights[j] * layers[k - 1].neurons.get(j).value;

                newValue += layers[k].neurons.get(i).bias;
                layers[k].neurons.get(i).value = transferFunction.evaluate(newValue);
            }
        }

        // Get output values
        for(i = 0; i < outputSize; i++)
            output[i] = layers[layers.length - 1].neurons.get(i).value;

        return output;
    }


    /**
     * Backward propagation function. Adjusts the MLP's weights
     * @param target
     * @param prediction
     * @return 0
     */
    public double backPropagate(double[] target, double[] prediction) {
        int i, j, k;
        double error;
        double globalError[] = new double[3];
        for (i = 0; i < prediction.length; i++)
            globalError[i] = Math.pow(target[i] - prediction[i], 2) * 0.5;
        double localError = 0.0;
        //double new_output[] = getOutput(input);

        //Calculate output error
        for(i = 0; i < layers[layers.length - 1].size; i++){
          //error = Math.pow(target[i] - prediction[i], 2) * 0.5;
            error = target[i] - prediction[i];
            layers[layers.length - 1].neurons.get(i).delta = error * transferFunction.evaluateDerivative(prediction[i]);
        }


        for(k = layers.length - 2; k >= 0; k--){
            // Calculate the error of the current layer and recalculate delta
            for(i = 0; i < layers[k].size; i++){
                error = 0.0;
                for(j = 0; j < layers[k + 1].size; j++)
                    error += layers[k + 1].neurons.get(j).delta * layers[k + 1].neurons.get(j).weights[i];

                layers[k].neurons.get(i).delta = error * transferFunction.evaluateDerivative(layers[k].neurons.get(i).value);
            }

            // update the weights of the next layer
            for(i = 0; i < layers[k + 1].size; i++) {
                for(j = 0; j < layers[k].size; j++)
                    layers[k + 1].neurons.get(i).weights[j] += learningRate * layers[k + 1].neurons.get(i).delta *
                            layers[k].neurons.get(j).value;
                layers[k + 1].neurons.get(i).bias += learningRate * layers[k + 1].neurons.get(i).delta;
            }
        }

        return 0; // TODO return value needed?
    }

    /**
     * Backward propagation function. Adjusts the MLP's weights
     * @param input input table (values between 0 and 1????????????????????? Sure?)
     * @param output output generated by the NN before (values between 0 and 1 ????????????? Sure?)
     * @param globalError error value (difference between expected value and the one returned by the NN)
     * @return globalError to output size ratio
     */
    public double backPropagate(double[] input, double[] output, double globalError) {
        int i, j, k;
        double localError = 0.0;
        //double new_output[] = getOutput(input);

        //Calculate output error
        for(i = 0; i < layers[layers.length - 1].size; i++){
            // error = output[i] - new_output[i];
            layers[layers.length - 1].neurons.get(i).delta = globalError * transferFunction.evaluateDerivative(output[i]); //this was newOutput before
        }


        for(k = layers.length - 2; k >= 0; k--){
            // Calculate the error of the current layer and recalculate delta
            for(i = 0; i < layers[k].size; i++){
                localError = 0.0;
                for(j = 0; j < layers[k + 1].size; j++)
                    localError += layers[k + 1].neurons.get(j).delta * layers[k + 1].neurons.get(j).weights[i];

                layers[k].neurons.get(i).delta = localError * transferFunction.evaluateDerivative(layers[k].neurons.get(i).value);
            }

            // update the weights of the next layer
            for(i = 0; i < layers[k + 1].size; i++) {
                for(j = 0; j < layers[k].size; j++)
                    layers[k + 1].neurons.get(i).weights[j] += learningRate * layers[k + 1].neurons.get(i).delta *
                            layers[k].neurons.get(j).value;
                //layers[k + 1].neurons.get(i).bias += learningRate * layers[k + 1].neurons.get(i).delta;
            }
        }

        return globalError/output.length; // TODO return value needed?
    }


    /**
     * Save the MLP network to file
     *
     * @param path where to save the MLP
     * @return true if writing succeeded
     */
    public boolean save(String path){
        try{
            FileOutputStream fout = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(this);
            oos.close();
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * Load an MLP from file
     * @param path path to saved MLP
     * @return MLP network or null
     */
    public static NeuralNetwork load(String path){
        try {
            NeuralNetwork net;
            FileInputStream fin = new FileInputStream(path);
            ObjectInputStream oos = new ObjectInputStream(fin);
            net = (NeuralNetwork) oos.readObject();
            oos.close();
            return net;
        }
        catch (Exception e){
            return null;
        }
    }

    /**
     * @return Learning rate
     */
    public double getLearningRate()
    {
        return learningRate;
    }

    /**
     *
     * @param rate
     */
    public void	setLearningRate(double rate)
    {
        learningRate = rate;
    }

    /**
     * Sets new transfer function
     * @param fun a transfer function
     */
    public void setTransferFunction(TransferFunction fun)
    {
        transferFunction = fun;
    }


    /**
     * @return Number of neurons in the input layer
     */
    public int getInputLayerSize()
    {
        return layers[0].size;
    }


    /**
     * @return Number of neurons in the output layer
     */
    public int getOutputLayerSize()
    {
        return layers[layers.length - 1].size;
    }
}