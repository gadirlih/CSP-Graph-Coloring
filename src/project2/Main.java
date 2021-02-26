package project2;

import java.util.HashMap;

public class Main {
    public static void main(String[] args){
        // read input
        Input input = InputReader.read("test1.txt");
        // start search
        BacktrackingSearch backtrackingSearch = new BacktrackingSearch(input);
        HashMap<Integer, Integer> result = backtrackingSearch.startSearch();
        // print final assignments
        System.out.println(result);
    }
}
