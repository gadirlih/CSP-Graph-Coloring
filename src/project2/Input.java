package project2;

import java.util.ArrayList;
import java.util.HashMap;

public class Input {
    private final HashMap<Integer, ArrayList<Integer>> mAdjacencyList;
    private final int mColorCount;


    public Input(HashMap<Integer, ArrayList<Integer>> adjacencyList, int colorCount) {
        this.mAdjacencyList = adjacencyList;
        this.mColorCount = colorCount;
    }

    public HashMap<Integer, ArrayList<Integer>> getAdjacencyList() {
        return mAdjacencyList;
    }

    public int getColorCount() {
        return mColorCount;
    }
}