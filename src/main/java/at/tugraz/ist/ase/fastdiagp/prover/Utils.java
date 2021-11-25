package at.tugraz.ist.ase.fastdiagp.prover;

import java.util.Iterator;
import java.util.Set;

public class Utils {
    public static void printConsistencyCheck(int counter, Set<String> constraints) {
        System.out.print(counter + ": {");
        for (Iterator<String> iterator = constraints.iterator(); iterator.hasNext(); ) {
            String constraint = iterator.next();
            System.out.print(constraint);

            if (iterator.hasNext()) {
                System.out.print(", ");
            }
        }
        System.out.println("}");
    }
}
