package metrics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ResultWriter {

    BufferedWriter writer;

    public ResultWriter(String path, List<String> names) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(path));
        writer.write(String.join(";", names));
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
