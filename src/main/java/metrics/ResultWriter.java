package metrics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ResultWriter {

    BufferedWriter writer;

    public ResultWriter(String path){
        try {
            this.writer = new BufferedWriter(new FileWriter(path));
            writer.write("File;SLOC;Complexity;DIT;NOC;Response;LackOfCohesion;Coupling;LambdaCount;LambdaLinesCount;LambdaRatio;LambdaCountField;LambdaCountSideEffects;StreamsCount;ParadigmScore\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeResult(Results result){
        try {
            writer.write(result.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
