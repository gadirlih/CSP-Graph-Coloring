package project2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class InputReader {
    public static Input read(String filePath){
        HashMap<Integer, ArrayList<Integer>> adjacencyList = new HashMap<>();
        int colorCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {

                if(line.isEmpty() || line.charAt(0) == '#') continue;

                if(line.charAt(0) == 'c'){
                    String[] tokens = line.split(" ");
                    colorCount = Integer.parseInt(tokens[2]);
                }else {

                    String[] args = line.split(",");
                    int from = Integer.parseInt(args[0]);
                    int to = Integer.parseInt(args[1]);

                    // initialize lists if absent
                    adjacencyList.computeIfAbsent(from, ArrayList::new);
                    adjacencyList.computeIfAbsent(to, ArrayList::new);

                    // It is undirected graph so add it to both of them
                    adjacencyList.get(from).add(to);
                    adjacencyList.get(to).add(from);
                }
            }
        } catch (Exception e) {
            System.out.println("Your input file format is not correct!");
            e.printStackTrace();
            System.exit(1);
        }

        return new Input(adjacencyList, colorCount);
    }
}