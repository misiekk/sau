package mlp;

import java.util.ArrayList;

public class Layer {
    public ArrayList<Neuron> 	neurons;
    //public TransferFunction     transferFunction;
    public int 		            size;

    /**
     * Layer of neurons
     *
     * @param size number of neurons in this layer
     * @param prevSize number of neurons in the previous layers
     */
    public Layer(int size, int prevSize){
        //this.transferFunction = transferFunction;
        this.size = size;
        this.neurons = new ArrayList<Neuron>();

        for(int j = 0; j < size; j++)
            neurons.add(new Neuron(prevSize));

        addBias(prevSize);
    }

    private void addBias(int prevSize){
        this.size++;
        neurons.add(new Neuron(prevSize));
    }
}