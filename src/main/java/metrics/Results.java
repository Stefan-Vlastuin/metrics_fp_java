package metrics;

public record Results (String className, int SLOC, int complexity, int DIT, int NOC, int response, int lackOfCohesion, int coupling, int lambdas, int lambdaLines, double lambdaRatio, int lambdaFieldVariables, int lambdaSideEffects, int nrStreams, double paradigmScore) {
    
    @Override
    public final String toString() {
        return className + ";" + SLOC + ";" + complexity + ";" + DIT + ";" + NOC + ";" + response + ";" + lackOfCohesion + ";" + coupling + ";" + lambdas + ";" + lambdaLines + ";" + lambdaRatio + ";" + lambdaFieldVariables + ";" + lambdaSideEffects + ";" + nrStreams + ";" + paradigmScore;
    }
}
