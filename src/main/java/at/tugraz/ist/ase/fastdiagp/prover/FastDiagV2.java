package at.tugraz.ist.ase.fastdiagp.prover;

import lombok.Getter;
import org.apache.commons.collections4.SetUtils;

import java.util.*;

import static at.tugraz.ist.ase.fastdiagp.prover.Utils.printConsistencyCheck;

/**
 * Implementation of the FastDiag algorithm.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class FastDiagV2 {

    static int incrementCounter;

    @Getter
    private List<Set<String>> CCList;

//    private final ChocoConsistencyChecker checker;

//    public FastDiagV3(ChocoConsistencyChecker checker) {
//        this.checker = checker;
//    }

    public FastDiagV2() {
        CCList = new ArrayList<>();
    }

    public void printConsistencyChecks(Set<String> C) {
        incrementCounter = 1;
        System.out.println("Consistency checks needed by FastDiag2: ");
//        printConsistencyCheck(incrementCounter++, C);
//        CCList.add(C);

        List<Set<String>> Δ = new ArrayList<>();
        List<Set<String>> AC = new ArrayList<>();
        Δ.add(Collections.emptySet());
        AC.add(C);

        fd(Δ, C, AC);
    }

    /**
     * This function will activate FastDiag algorithm if there exists at least one constraint,
     * which induces an inconsistency in B. Otherwise, it returns an empty set.
     *
     * // Func FastDiag(C, B) : Δ
     * // if isEmpty(C) or consistent(B U C) return Φ
     * // else return C \ FD(C, B, Φ)
     *
     * @param C a consideration set of constraints. Need to inverse the order of the possibly faulty constraint set.
     * @param AC a background knowledge
     * @return a diagnosis or an empty set
     */
//    public Set<String> findDiagnosis(Set<String> C, Set<String> B) {
//        Set<String> BwithC = SetUtils.union(B, C);
//
//        // if isEmpty(C) or consistent(B U C) return Φ
//        if (C.isEmpty()
//                || checker.isConsistent(BwithC)) {
//            return Collections.emptySet();
//        } else { // else return C \ FD(C, B, Φ)
//            incrementCounter(COUNTER_FASTDIAG_CALLS);
//            start(TIMER_FIRST);
//            Set<String> mss = fd(Collections.emptySet(), C, B);
//            stop(TIMER_FIRST);
//
//            incrementCounter(COUNTER_DIFFERENT_OPERATOR);
//            return SetUtils.difference(C, mss);
//        }
//    }

    private List<Set<String>> fd(List<Set<String>> Δ, Set<String> C, List<Set<String>> AC) {
        List<Set<String>> D = new ArrayList<>();

//        System.out.println("Δ: " + Δ.size() + " AC: " + AC.size());

        // if Δ != Φ and consistent(B U C) return C;
//        if( !Δ.get(0).isEmpty() ) {
        if (!Δ.isEmpty() ) {
            for (int i = 0; i < AC.size(); i++) {
//            for (Set<String> b : B) {
                if( !Δ.get(i).isEmpty() ) {
                    Set<String> ac = AC.get(i);
//                    Set<String> BwithC = SetUtils.union(ac, C);

                    // print BwithC
                    printConsistencyCheck(incrementCounter++, ac);
                    CCList.add(ac);

//                if (checker.isConsistent(BwithC)) {
//                    return C;
//                }
                }
            }

        }

        // if singleton(C) return Φ;
        int n = C.size();
        if (n == 1) {
            // add empty set to D
            D.add(C);
            D.add(Collections.emptySet());
            return D;
//            return Collections.emptySet();
        } else {

            int k = n / 2;  // k = n/2;
            // C1 = {c1..ck}; C2 = {ck+1..cn};
            List<String> firstSubList = new ArrayList<>(C).subList(0, k);
            List<String> secondSubList = new ArrayList<>(C).subList(k, n);
            Set<String> C1 = new LinkedHashSet<>(firstSubList);
            Set<String> C2 = new LinkedHashSet<>(secondSubList);

            List<Set<String>> newAC = new ArrayList<>();
            for (Set<String> stringSet1 : AC) {
                newAC.add(SetUtils.difference(stringSet1, C2));
            }

            List<Set<String>> newC2 = new ArrayList<>();
            for (int i = 0; i < AC.size(); i++) {
                newC2.add(new LinkedHashSet<>(secondSubList));
            }

            // Δ2 = FD(C2, C1, B);
            List<Set<String>> Δ2 = fd(newC2, C1, newAC);

            // Δ1 = FD(C1 - Δ2, C2, B U Δ2);
            newAC = new ArrayList<>();
            for (Set<String> stringSet1 : AC) {
                for (Set<String> stringSet2 : Δ2) {
                    newAC.add(SetUtils.difference(stringSet1, stringSet2));
                }
            }

            List<Set<String>> newΔ2 = new ArrayList<>();
            for (int i = 0; i < AC.size(); i++) {
                for (Set<String> stringSet1 : Δ2) {
                    newΔ2.add(stringSet1);
                }
            }

//            Set<String> BwithΔ2 = SetUtils.union(stringSet, B);
//            Set<String> C1withoutΔ2 = SetUtils.difference(C1, stringSet);
//            List<Set<String>> Δ1 = fd(Δ2, C2.get(0), newB);
            List<Set<String>> Δ1 = fd(newΔ2, C2, newAC);

            for (Set<String> stringSet1 : Δ1) {
                for (Set<String> stringSet2 : Δ2) {
                    D.add(SetUtils.union(stringSet2, stringSet1));
                }
            }
            return D;
        }
    }
}