package metrics.calculators;

import com.github.javaparser.ast.CompilationUnit;
import metrics.visitors.CouplingVisitor;

import java.util.*;

public class CouplingCalculator {
    private static CouplingCalculator instance;
    private final List<CompilationUnit> allUnits;
    private Map<CompilationUnit, Set<CompilationUnit>> couplings;

    private CouplingCalculator(List<CompilationUnit> allUnits){
        this.allUnits = allUnits;
    }

    public static CouplingCalculator getInstance(List<CompilationUnit> allUnits){
        if (instance == null){
            instance = new CouplingCalculator(allUnits);
        }
        return instance;
    }

    public int getCoupling(CompilationUnit cu){
        if (couplings == null){
            calculateCouplings();
        }

        return couplings.getOrDefault(cu, new HashSet<>()).size();
    }

    private void calculateCouplings(){
        couplings = new HashMap<>();
        for (CompilationUnit cu : allUnits){
            Set<CompilationUnit> usedClasses = findUsedClasses(cu);
            for (CompilationUnit usedClass : usedClasses){
                if (!cu.equals(usedClass)) {
                    // Add the coupling in both directions!
                    couplings.computeIfAbsent(cu, k -> new HashSet<>()).add(usedClass);
                    couplings.computeIfAbsent(usedClass, k -> new HashSet<>()).add(cu);
                }
            }
        }
    }

    private Set<CompilationUnit> findUsedClasses(CompilationUnit cu){
        CouplingVisitor v = new CouplingVisitor();
        v.visit(cu, null);
        return v.getUsedClasses();
    }

}
