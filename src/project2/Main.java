package project2;

import java.util.HashMap;

public class Main {
    public static void main(String[] args){
        // check if input file exists
        if(args.length != 1){
            System.out.println("There must be exactly one argument(input file path) passed to the program!");
            return;
        }
        // read input
        Input input = InputReader.read(args[0]);
        // start search
        BacktrackingSearch backtrackingSearch = new BacktrackingSearch(input);
        HashMap<Integer, Integer> result = backtrackingSearch.startSearch();
        // print final assignments
        System.out.println("Variable -> Color");
        for(Integer var : result.keySet()){
            System.out.println(var + " -> " + result.get(var));
        }
    }
}
