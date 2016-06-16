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
     * @param fun Transfer function
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
     * Esegui la rete
     *
     * @param input input value
     * @return Valori di output restituiti dalla rete
     */
    public double[] getOutput(double[] input)
    {
        int i, j, k;
        double newValue;
        double output[] = new double[layers[layers.length - 1].size];

        // Put input into the first layer
        for(i = 0; i < layers[0].size; i++)
            layers[0].neurons[i].value = input[i];


        // Execute - hidden layers + output layers
        for(k = 1; k < layers.length; k++) {
            for (i = 0; i < layers[k].size; i++) {
                newValue = 0.0;
                for (j = 0; j < layers[k - 1].size; j++)
                    newValue += layers[k].neurons[i].weights[j] * layers[k - 1].neurons[j].value;

                newValue += layers[k].neurons[i].bias;
                layers[k].neurons[i].value = transferFunction.evaluate(newValue);
            }
        }


        // Get output values
        for(i = 0; i < layers[layers.length - 1].size; i++)
            output[i] = layers[layers.length - 1].neurons[i].value;

        return output;
    }



    /**
     * Backward propagation function threading
     * @param input input table
     * @param output output table
     * @param nthread number of threads
     * @return delta error between generated and expected output
     */
    public double backPropagateMultiThread(double[] input, double[] output, int nthread)
    {
        return 0.0;
    }



    /**
     * Backward propagation function. Adjusts the MLP's weights
     * @param input input table (values between 0 and 1)
     * @param output expected output table (values between 0 and 1)
     * @return delta error between generated and expected output
     */
    public double backPropagate(double[] input, double[] output) {
        int i, j, k;
        double new_output[] = getOutput(input);
        double error;

        //Calculate output error
        for(i = 0; i < layers[layers.length - 1].size; i++){
            error = output[i] - new_output[i];
            layers[layers.length - 1].neurons[i].delta = error * transferFunction.evaluateDerivative(new_output[i]);
        }


        for(k = layers.length - 2; k >= 0; k--){
            // Calculate the error of the current layer and recalculate delta
            for(i = 0; i < layers[k].size; i++){
                error = 0.0;
                for(j = 0; j < layers[k + 1].size; j++)
                    error += layers[k + 1].neurons[j].delta * layers[k + 1].neurons[j].weights[i];

                layers[k].neurons[i].delta = error * transferFunction.evaluateDerivative(layers[k].neurons[i].value);
            }

            // update the weights of the next layer
            for(i = 0; i < layers[k + 1].size; i++) {
                for(j = 0; j < layers[k].size; j++)
                    layers[k + 1].neurons[i].weights[j] += learningRate * layers[k + 1].neurons[i].delta *
                            layers[k].neurons[j].value;
                layers[k + 1].neurons[i].bias += learningRate * layers[k + 1].neurons[i].delta;
            }
        }

        error = 0.0;
        for(i = 0; i < output.length; i++){
            error += Math.abs(new_output[i] - output[i]);
            //System.out.println(output[i] + " " + new_output[i]);
        }

        error = error / output.length;
        return error;
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