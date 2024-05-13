package metrics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ResultWriter {

    BufferedWriter writer;

    public ResultWriter(String path) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(path));
        writer.write(ResultCompilationUnit.getHeader());
        writer.newLine();
    }

    public void writeResult(ResultCompilationUnit result) throws IOException {
        writer.write(result.toString());
        writer.newLine();
    }

    public void close(){
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}
