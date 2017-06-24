package dev.berezinmv.numericalmethods;

import java.util.List;

public class Answer {

    private final List<Double> results;

    private final List<Double> values;

    public Answer(List<Double> results, List<Double> values) {
        this.results = results;
        this.values = values;
    }

    public List<Double> getResults() {
        return results;
    }

    public Integer getIterations() {
        return results.size();
    }

    public void print() {
        System.out.println("-------------------------------------------");
        System.out.println("|   # |          Result |           Value |");
        System.out.println("-------------------------------------------");
        for (int iter = 0; iter < getIterations(); iter++) {
            Double result = results.get(iter);
            Double value = values.get(iter);
            System.out.format("| %3d | %15.12f | %15.12f |\n", iter, result, value);
            System.out.println("-------------------------------------------");
        }
    }
}
