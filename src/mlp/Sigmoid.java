package mlp;

/**
 * Created by kasia on 17.06.16.
 */

public class Sigmoid implements TransferFunction{
    @Override
    public double evaluate(double value){
        return 1 / (1 + Math.pow(Math.E, - value));
    }

    @Override
    public double evaluateDerivative(double value){
        return (value - Math.pow(value, 2));
    }
}