package mlp;

/**
 * Created by kasia on 18.06.16.
 */
public class Softplus implements TransferFunction {

    @Override
    public double evaluate(double value) {
        return Math.log(1 + Math.exp(value));
    }

    @Override
    public double evaluateDerivative(double value) {
        return 1/(1 + Math.exp(-value));
    }
}
