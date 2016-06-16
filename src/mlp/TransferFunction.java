package mlp;

public interface TransferFunction {

    /**
     * Transfer function
     * @param value input value
     * @return function output
     */
    public double evaluate(double value);


    /**
     * Calculates the derivative of a function
     * @param value input value
     * @return function output
     */
    public double evaluateDerivative(double value);
}
