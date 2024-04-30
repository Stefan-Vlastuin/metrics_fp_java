package metrics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ResultWriter {

    BufferedWriter writer;

    public ResultWriter(String path){
        try {
            this.writer = new BufferedWriter(new FileWriter(path));
            writer.write(ResultCompilationUnit.getHeader());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeResult(ResultCompilationUnit result){
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
