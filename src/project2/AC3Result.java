package project2;

import java.util.ArrayList;
import java.util.HashMap;

public class AC3Result {
    private final HashMap<Integer, ArrayList<Integer>> mDeletedValues;
    private final boolean mConsistent;

    public AC3Result(HashMap<Integer, ArrayList<Integer>> deletedValues, boolean consistent){
        this.mDeletedValues = deletedValues;
        this.mConsistent = consistent;
    }

    public HashMap<Integer, ArrayList<Integer>> getDeletedValues(){
        return mDeletedValues;
    }

    public boolean isConsistent(){
        return mConsistent;
    }
}
