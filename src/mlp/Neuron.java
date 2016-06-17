package mlp;

public class Neuron {
    public double		value; //output value of the neuron
    public double[]		weights;
    //public double		bias;
    public double		delta; //error of this neuron during backpropagation

    public Neuron(int prevLayerSize)
    {
        weights = new double[prevLayerSize];
        //bias = Math.random() / 10000000000000.0;
        delta = Math.random() / 10000000000000.0;
        value = Math.random() / 10000000000000.0;

        for(int i = 0; i < weights.length; i++)
            weights[i] = Math.random() / 10000000000000.0;
    }
}