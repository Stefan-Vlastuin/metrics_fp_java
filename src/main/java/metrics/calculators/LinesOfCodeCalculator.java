package metrics.calculators;

import com.github.javaparser.ast.Node;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public final class LinesOfCodeCalculator {

    public static int countSLOC(Node n){
        LexicalPreservingPrinter.setup(n);
        return LinesOfCodeCalculator.countSLOC(LexicalPreservingPrinter.print(n));
    }

    private static int countSLOC(String code){
        BufferedReader reader = new BufferedReader(new StringReader(code));
        int count = 0;
        boolean multiLineComment = false;
        String line;
        try {
            while ((line = reader.readLine()) != null){
                line = line.trim();
                if (multiLineComment){
                    if (line.endsWith("*/")){ // End of multiline comment
                        multiLineComment = false;
                    }
                } else {
                    if (!line.isEmpty() && !line.startsWith("//")){ // Skip empty lines and comment lines
                        if (line.startsWith("/*")){ // Start of multiline comment
                            if (!line.endsWith("*/")){ // Check that the comment does not end on the same line
                                multiLineComment = true;
                            }
                        } else {
                            count++;
                        }
                    }
                }
            }
        } catch (IOException e) {
            return 0;
        }
        return count;
    }
}
