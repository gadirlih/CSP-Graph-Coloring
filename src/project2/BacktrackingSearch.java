package project2;

import java.util.*;

public class BacktrackingSearch {
    private final HashMap<Integer, ArrayList<Integer>> mAdjacencyList;
    // variable <-> set of values
    private final HashMap<Integer, HashSet<Integer>> mDomainList;
    private final int mColorCount;

    public BacktrackingSearch(Input input) {
        mAdjacencyList = input.getAdjacencyList();
        mDomainList = new HashMap<>();
        mColorCount = input.getColorCount();
        initDomain();
    }

    // initialize domain of each variable with all values
    private void initDomain() {
        for (Integer var : mAdjacencyList.keySet()) {
            HashSet<Integer> tempSet = new HashSet<>();
            for (int i = 1; i <= mColorCount; i++) {
                tempSet.add(i);
            }
            mDomainList.put(var, tempSet);
        }
    }

    // calls backtracking search method
    public HashMap<Integer, Integer> startSearch() {
        return search(new HashMap<>());
    }

    // backtracking search
    // assignment, Vertex <-> Color Number
    private HashMap<Integer, Integer> search(HashMap<Integer, Integer> assignment) {
        if (assignment.keySet().size() == mAdjacencyList.keySet().size()) {
            return assignment;
        }

        int variable = choseVariable(assignment);

        ArrayList<Integer> orderedDomain = orderDomain(variable);

        for (int color : orderedDomain) {
            HashMap<Integer, Integer> copyAssignment = new HashMap<>(assignment);
            copyAssignment.put(variable, color);

            // after assignment apply AC3
            AC3Result ac3Result = AC3(variable, color);

            // If consistent then apply changes returned by AC3 to domain list, otherwise continue to the next assignment
            if (ac3Result.isConsistent()) {
                // update domain list from deletedValues
                for (Integer var : ac3Result.getDeletedValues().keySet()) {
                    for (Integer value : ac3Result.getDeletedValues().get(var)) {
                        mDomainList.get(var).remove(value);
                    }
                }
            } else continue;

            // call search recursively
            HashMap<Integer, Integer> result = search(copyAssignment);
            if (!result.isEmpty()) {
                return result;
            }
            // add back deletedValues to domainList
            for (Integer var : ac3Result.getDeletedValues().keySet()) {
                for (Integer value : ac3Result.getDeletedValues().get(var)) {
                    mDomainList.get(var).add(value);
                }
            }
        }
        return new HashMap<>();
    }

    // Apply MRV
    // Chose variable which has minimum remaining values
    // If there are more than one variable with the same amount of minimum values
    // Then chose based on the degree of these remaining variables
    // Chose the one with the highest degree
    private int choseVariable(HashMap<Integer, Integer> assignment) {
        ArrayList<Integer> minimums = new ArrayList<>();
        int minSize = Integer.MAX_VALUE;
        for (Integer var : mDomainList.keySet()) {
            if (!assignment.containsKey(var)) {
                int domainSize = mDomainList.get(var).size();
                if (domainSize < minSize) {
                    minSize = domainSize;
                    minimums.clear();
                    minimums.add(var);
                } else if (domainSize == minSize) {
                    minimums.add(var);
                }
            }
        }
        int maxDegreeVar = minimums.get(0);
        if (minimums.size() > 1) {
            int maxDegree = Integer.MIN_VALUE;
            for (Integer var : minimums) {
                int degree = mAdjacencyList.get(var).size();
                if (degree > maxDegree) {
                    maxDegree = degree;
                    maxDegreeVar = var;
                }
            }
            return maxDegreeVar;
        }
        return minimums.get(0);
    }

    // Apply LCV
    // Check domains of neighbours and determine if the chosen value constrain their domain
    // Sort the values based on how much they constrain neighbours in ascending order
    private ArrayList<Integer> orderDomain(int var) {
        ArrayList<Integer> orderedDomain = new ArrayList<>();
        ArrayList<Pair> orderedPairs = new ArrayList<>();
        for (Integer val : mDomainList.get(var)) {
            int count = 0;
            for (Integer neighbour : mAdjacencyList.get(var)) {
                if (mDomainList.get(neighbour).contains(val)) count++;
            }
            orderedPairs.add(new Pair(val, count));
        }
        orderedPairs.sort(Comparator.comparing(Pair::getSecond));
        for (Pair pair : orderedPairs) {
            orderedDomain.add(pair.getFirst());
        }
        return orderedDomain;
    }

    // Constraint propagation
    private AC3Result AC3(int var, int color) {
        Queue<Pair> queue = new LinkedList<>();
        // deleted values from variables domain
        HashMap<Integer, ArrayList<Integer>> deletedValues = new HashMap<>();

        // decrease domain of assigned variable to the only value chosen
        // 1. add deleted values to the deletedValues list
        ArrayList<Integer> valsDeletedFromDomain = new ArrayList<>();
        for (Integer val : mDomainList.get(var)) {
            if (color != val) {
                // initialize list if absent and add value
                deletedValues.computeIfAbsent(var, ArrayList::new);
                deletedValues.get(var).add(val);
                valsDeletedFromDomain.add(val);
            }
        }
        // 2. delete them from domainList
        for (Integer val : valsDeletedFromDomain) {
            mDomainList.get(var).remove(val);
        }

        // add all arcs from neighbours to var to the queue
        for (Integer neighbour : mAdjacencyList.get(var)) {
            queue.add(new Pair(neighbour, var));
        }
        while (!queue.isEmpty()) {
            Pair arc = queue.remove();
            if (revise(arc.getFirst(), arc.getSecond(), deletedValues)) {
                // if domain of a variable becomes empty then return false
                if (mDomainList.get(arc.getFirst()).isEmpty()) {
                    return new AC3Result(deletedValues, false);
                }
                // since First variable has changed we have to check its neighbours' consistency
                for (Integer neighbour : mAdjacencyList.get(arc.getFirst())) {
                    queue.add(new Pair(neighbour, arc.getFirst()));
                }
            }
        }
        return new AC3Result(deletedValues, true);
    }

    // Checks consistency between from and to variables
    // direction is from "from" variable to "to" variable
    private boolean revise(int from, int to, HashMap<Integer, ArrayList<Integer>> deletedValues) {
        boolean revised = false;
        boolean consistent = false;
        for (Iterator<Integer> i = mDomainList.get(from).iterator(); i.hasNext(); ) {
            int xValue = i.next();
            for (int yValue : mDomainList.get(to)) {
                if (xValue != yValue) {
                    consistent = true;
                    break;
                }
            }
            if (!consistent) {
                i.remove();
                // initialize list if absent and add value
                deletedValues.computeIfAbsent(from, ArrayList::new);
                deletedValues.get(from).add(xValue);
                revised = true;
            }
            consistent = false;
        }
        return revised;
    }
}
