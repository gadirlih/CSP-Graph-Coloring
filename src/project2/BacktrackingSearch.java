package project2;

import java.util.*;

public class BacktrackingSearch {
    private final HashMap<Integer, ArrayList<Integer>> mAdjacencyList;
    // variable <-> set of values
    private final HashMap<Integer, HashSet<Integer>> mDomainList;
    private final int mColorCount;

    public BacktrackingSearch(Input input){
        mAdjacencyList = input.getAdjacencyList();
        mDomainList = new HashMap<>();
        initDomain();
        mColorCount = input.getColorCount();
    }

    private void initDomain(){
        for(Integer var : mAdjacencyList.keySet()){
            HashSet<Integer> tempSet = new HashSet<>();
            for(int i = 1; i <= mColorCount; i++){
                tempSet.add(i);
            }
            mDomainList.put(var, tempSet);
        }
    }

    public HashMap<Integer, Integer> startSearch(){
        return search(new HashMap<>());
    }

    // assignment, Vertex <-> Color Number
    private HashMap<Integer, Integer> search(HashMap<Integer, Integer> assignment){
        if(assignment.keySet().size() == mAdjacencyList.keySet().size()){
            return assignment;
        }

        int variable = choseVariable(assignment);

        for(int color : mDomainList.get(variable)){
            HashMap<Integer, Integer> copyAssignment = new HashMap<>(assignment);
            copyAssignment.put(variable, color);
//            if(consistent(copyAssignment)){ // can delete it since ac3 handles it
                AC3Result ac3Result = AC3(variable);
                if(ac3Result.isConsistent()){
                    // update domain list from deletedValues
                    for(Integer var : ac3Result.getDeletedValues().keySet()){
                        for(Integer value : ac3Result.getDeletedValues().get(var)){
                            mDomainList.get(var).remove(value);
                        }
                    }
                }else continue;
                HashMap<Integer, Integer> result = search(copyAssignment);
                if(!result.isEmpty()){
                    return result;
                }
//            }
            // add back deletedValues to domainList
            for(Integer var : ac3Result.getDeletedValues().keySet()) {
                for (Integer value : ac3Result.getDeletedValues().get(var)) {
                    mDomainList.get(var).add(value);
                }
            }
        }
        return new HashMap<>();
    }

    private int choseVariable(HashMap<Integer, Integer> assignment){
        for (int var : mAdjacencyList.keySet()) {
            if(!assignment.containsKey(var)){
                return var;
            }
        }
        return -1;
    }

    private boolean consistent(HashMap<Integer, Integer> assignment){
        // check if all neighbours of each variable is consistent
        for(Integer var : mAdjacencyList.keySet()){
            ArrayList<Integer> neighbours = mAdjacencyList.get(var);
            for(int neighbour : neighbours){
                if(assignment.containsKey(var) && assignment.containsKey(neighbour)){
                    if(assignment.get(var).equals(assignment.get(neighbour))){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private AC3Result AC3(int var){
        Queue<Pair> queue = new LinkedList<>();
        // deleted values from variables domain
        HashMap<Integer, ArrayList<Integer>> deletedValues = new HashMap<>();

        for(Integer neighbour : mAdjacencyList.get(var)){
            queue.add(new Pair(neighbour, var));
        }
        while(!queue.isEmpty()){
            Pair arc = queue.remove();
            if(revise(arc.getFirst(), arc.getSecond(), deletedValues)){
                if(mDomainList.get(arc.getFirst()).isEmpty()){
                    return new AC3Result(deletedValues, false);
                }
                // since First variable has changed we have to check its neighbours' consistency
                for(Integer neighbour : mAdjacencyList.get(arc.getFirst())){
                    queue.add(new Pair(neighbour, arc.getFirst()));
                }
            }
        }
        return new AC3Result(deletedValues, true);
    }

    // Checks consistency between from and to variables
    // direction is from "from" variable to "to" variable
    private boolean revise(int from, int to, HashMap<Integer, ArrayList<Integer>> deletedValues){
        boolean revised = false;
        boolean consistent = false;
        for(Integer xValue : mDomainList.get(from)){
            for(Integer yValue : mDomainList.get(to)){
                if (!xValue.equals(yValue)) {
                    consistent = true;
                    break;
                }
            }
            if(!consistent){
                mDomainList.get(from).remove(xValue);
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
