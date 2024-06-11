package metrics.metrics.baseline;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import java.io.File;

public class Setup {

    public static void setupSymbolSolver(String path){
        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        ParserConfiguration parserConfiguration = new ParserConfiguration()
                .setSymbolResolver(new JavaSymbolSolver(typeSolver)).setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_18);
        typeSolver.add(new JavaParserTypeSolver(new File(path), parserConfiguration));
        typeSolver.add(new ReflectionTypeSolver(false));
        StaticJavaParser.setConfiguration(parserConfiguration);
    }
}
