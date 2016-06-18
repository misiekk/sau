package mlp;

/**
 * Created by kasia on 18.06.16.
 */
public class Linear implements TransferFunction {
    @Override
    public double evaluate(double value) {
        return value;
    }

    @Override
    public double evaluateDerivative(double value) {
        return 0;
    }
}
