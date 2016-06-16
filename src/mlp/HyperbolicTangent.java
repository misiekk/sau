package mlp;

public class HyperbolicTangent implements TransferFunction
{

    @Override
    public double evaluate(double value){
        return Math.tanh(value);
    }

    @Override
    public double evaluateDerivative(double value){
        return 1 - Math.pow(value, 2);
    }

}
