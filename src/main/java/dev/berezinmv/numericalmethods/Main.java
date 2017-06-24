package dev.berezinmv.numericalmethods;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Main {

    public static void main(String[] args) {
        Double accuracy = 0.0001;

        Function<Double, Double> function = x -> 3 * Math.pow(x, 4) + 4 * Math.pow(x, 3) - 12 * Math.pow(x, 2) - 5;
        Function<Double, Double> function$ = x -> 12 * Math.pow(x, 3) + 12 * Math.pow(x, 2) - 24 * x;
        Function<Double, Double> function$$ = x -> 36 * Math.pow(x, 2) + 24 * x - 24;

        Double leftBound = 1d;
        Double rightBound = 2d;

        Answer chordsAnswer = chordsMethod(function, function$$, leftBound, rightBound, accuracy);
        chordsAnswer.print();

        Answer divideAnswer = divideMethod(function, leftBound, rightBound, accuracy);
        divideAnswer.print();

        Answer tangentAnswer = tangentMethod(function, function$, function$$, leftBound, rightBound, accuracy);
        tangentAnswer.print();
    }

    /**
     * Calculate using 'divide by half' method
     * @param function      - Function
     * @param leftBound     - Left bound
     * @param rightBound    - Right bound
     * @param accuracy      - Accuracy
     * @return
     */
    public static Answer divideMethod(Function<Double, Double> function,
                                      Double leftBound,
                                      Double rightBound,
                                      Double accuracy) {
        Double leftValue = function.apply(leftBound);

        Double diff = Double.MAX_VALUE;
        Double xn;
        List<Double> results = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        while (Math.abs(diff) > accuracy) {
            xn = (leftBound + rightBound) / 2;
            diff = function.apply(xn);
            results.add(xn);
            values.add(diff);
            if (leftValue * diff < 0) {
                rightBound = xn;
            } else {
                leftBound = xn;
                leftValue = diff;
            }
        }
        return new Answer(results, values);
    }

    /**
     * Calculate result with chords method
     * @param function      - Function
     * @param function$$    - Second derivative of function
     * @param leftBound     - Left bound
     * @param rightBound    - Right bound
     * @param accuracy      - Accuracy
     * @return
     */
    public static Answer chordsMethod(Function<Double, Double> function,
                                      Function<Double, Double> function$$,
                                      Double leftBound,
                                      Double rightBound,
                                      Double accuracy) {
        Double result;
        Function<Double, Double> reducer;
        if (function.apply(leftBound) * function$$.apply(leftBound) > 0) {
            result = rightBound;
            reducer = (prev) -> leftBound - function.apply(leftBound) / (function.apply(prev) - function.apply(leftBound)) * (prev - leftBound);
        } else if (function.apply(rightBound) * function$$.apply(rightBound) > 0) {
            result = leftBound;
            reducer = (prev) -> prev - function.apply(prev) / (function.apply(rightBound) - function.apply(prev)) * (rightBound - prev);
        } else {
            throw new RuntimeException("Something gone terribly wrong");
        }

        return calculateRecurrently(function, reducer, result, accuracy);
    }

    /**
     * Calculate using tangent method
     * @param function      - Function
     * @param function$     - First derivative of function
     * @param function$$    - Second derivative of function
     * @param leftBound     - Left bound
     * @param rightBound    - Right bound
     * @param accuracy      - Accuracy
     * @return
     */
    public static Answer tangentMethod(Function<Double, Double> function,
                                       Function<Double, Double> function$,
                                       Function<Double, Double> function$$,
                                       Double leftBound,
                                       Double rightBound,
                                       Double accuracy) {
        Double result;
        Function<Double, Double> reducer = (xn) -> xn - function.apply(xn) / function$.apply(xn);
        if (function.apply(leftBound) * function$$.apply(leftBound) > 0) {
            result = leftBound;
        } else if (function.apply(rightBound) * function$$.apply(rightBound) > 0) {
            result = rightBound;
        } else {
            throw new RuntimeException("Something gone terribly wrong");
        }
        return calculateRecurrently(function, reducer, result, accuracy);
    }

    public static Answer chordTangentMethod(Function<Double, Double> function,
                                     Function<Double, Double> function$,
                                     Function<Double, Double> function$$,
                                     Double leftBound,
                                     Double rightBound,
                                     Double accuracy) {
        throw new RuntimeException("Unimplemented");
    }

    private static Answer calculateRecurrently(Function<Double, Double> function,
                                               Function<Double, Double> reducer,
                                               Double result,
                                               Double accuracy) {
        Double diff = Double.MAX_VALUE;
        List<Double> results = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        while (diff > accuracy) {
            Double xn = reducer.apply(result);
            diff = Math.max(Math.abs(result - xn), Math.abs(function.apply(result) - function.apply(xn)));
            result = xn;
            results.add(result);
            values.add(function.apply(result));
        }
        return new Answer(results, values);
    }
}
