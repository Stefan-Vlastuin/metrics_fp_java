package metrics;

public record ResultCompilationUnit (String className, int SLOC, int complexity, int DIT, int NOC, int response, int lackOfCohesion, int coupling, int lambdas, int lambdaLines, double lambdaRatio, int lambdaFieldVariables, int lambdaSideEffects, int nrStreams, double paradigmScore) {
    
    public static String getHeader() {
        return "File;SLOC;Complexity;DIT;NOC;Response;LackOfCohesion;Coupling;LambdaCount;LambdaLinesCount;LambdaRatio;LambdaCountField;LambdaCountSideEffects;StreamsCount;ParadigmScore";
    }

    @Override
    public String toString() {
        return className + ";" + SLOC + ";" + complexity + ";" + DIT + ";" + NOC + ";" + response + ";" + lackOfCohesion + ";" + coupling + ";" + lambdas + ";" + lambdaLines + ";" + lambdaRatio + ";" + lambdaFieldVariables + ";" + lambdaSideEffects + ";" + nrStreams + ";" + paradigmScore;
    }
}
