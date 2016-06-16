package mlp;

public class Layer {
    public Neuron 	neurons[];
    public int 		size;

    /**
     * Layer of neurons
     *
     * @param size number of neurons in this layer
     * @param prevSize number of neurons in the previous layers
     */
    public Layer(int size, int prevSize)
    {
        this.size = size;
        neurons = new Neuron[size];

        for(int j = 0; j < size; j++)
            neurons[j] = new Neuron(prevSize);
    }
}