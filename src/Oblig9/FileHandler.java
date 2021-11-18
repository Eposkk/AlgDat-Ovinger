package Oblig9;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class FileHandler {

    public FileHandler() {
    }

    public Node[] readNodeFile(String filePath) throws IOException {
        String userDirectory = System.getProperty("user.dir");
        File file = new File(userDirectory + "/"+ "/src/Oblig9/"+ filePath);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st = ((br.readLine()));

        int nodeCount = Integer.parseInt(st.trim());

        Node[] nodes = new Node[nodeCount];

        while ((st = br.readLine()) != null){

            st = st.trim();
            String[] numbers = hsplit(st, 3);
            int nodeNr = Integer.parseInt(numbers[0]);
            double latitude = Double.parseDouble(numbers[1]);
            double longitude = Double.parseDouble(numbers[2]);

            nodes[nodeNr] = new Node(nodeNr, latitude, longitude);
        }
        return nodes;
    }

    public void readEdgeFile(String filePath, Node[] nodes) throws IOException {

        String userDirectory = System.getProperty("user.dir");
        File file = new File(userDirectory + "/"+"/src/Oblig9/"+ filePath);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st = ((br.readLine()));

        int edgeCount = Integer.parseInt(st.trim());

        int i=0;
        while ((st = br.readLine()) != null){

            st = st.trim();
            String[] numbers = hsplit(st, 5);
            int startNode = Integer.parseInt(numbers[0]);
            int endNode = Integer.parseInt(numbers[1]);
            int weight = Integer.parseInt(numbers[2]);

            Edge edge = new Edge(startNode, endNode, weight);

            nodes[startNode].addEdge(edge);
        }
    }

    public void readEdgeFileReverse(String filePath, Node[] nodes) throws IOException {
        String userDirectory = System.getProperty("user.dir");
        File file = new File(userDirectory + "/"+"/src/Oblig9/"+ filePath);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st = ((br.readLine()));

        int edgeCount = Integer.parseInt(st.trim());

        while ((st = br.readLine()) != null){

            st = st.trim();
            String[] numbers = hsplit(st, 5);
            int startNode = Integer.parseInt(numbers[0]);
            int endNode = Integer.parseInt(numbers[1]);
            int weight = Integer.parseInt(numbers[2]);

            Edge edge = new Edge(endNode, startNode, weight);

            nodes[startNode].addEdge(edge);
        }
    }

    public HashMap<String, Integer> readPointsOfInterest(String filePath, Node[] nodes) throws IOException {
        String userDirectory = System.getProperty("user.dir");
        File file = new File(userDirectory + "/"+"/src/Oblig9/"+ filePath);

        HashMap<String, Integer> pointsOfInterest = new HashMap<>();

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st = ((br.readLine()));

        int count = Integer.parseInt(st.trim());

        while ((st = br.readLine()) != null){

            st = st.trim();
            String[] numbers = hsplit(st, 3);
            int nodeNr = Integer.parseInt(numbers[0]);
            int nodeType = Integer.parseInt(numbers[1]);
            String name = numbers[2];
            name = name.substring(1,name.length()-1);

            nodes[nodeNr].setNodeType(nodeType);
            nodes[nodeNr].setName(name);
            pointsOfInterest.put(name, nodeNr);
        }
        return pointsOfInterest;
    }

    String[] hsplit(String linje, int antall) {
        int j = 0;
        String[] felt = new String[antall];
        int lengde = linje.length();
        for (int i = 0; i < antall; ++i) {
            //Hopp over innledende blanke, finn starten på ordet
            while (linje.charAt(j) <= ' ') ++j;
            int ordstart = j;
            //Finn slutten på ordet, hopp over ikke-blanke
            while (j < lengde && linje.charAt(j) > ' ') ++j;
            felt[i] = linje.substring(ordstart, j);
        }
        return felt;
    }
}