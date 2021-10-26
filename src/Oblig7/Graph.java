package Oblig7;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Graph {

    int NumberOfNodes;
    int NumberOfEdges;
    Node[] node;

    /**
     * Konstruktør
     * @param filePath
     */
    public Graph(String filePath) {
        try {
            newGraphFromFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String filnavn = "src/Oblig7/flytgraf1.txt";
        Graph graph1= new Graph(filnavn);
        System.out.println("\n*** Kjører på graf "+ filnavn + " ***");
        graph1.edmondsKarp(0,7);

        filnavn ="src/Oblig7/flytgraf2.txt";
        System.out.println("\n*** Kjører på graf "+ filnavn + " ***");
        graph1= new Graph(filnavn);
        graph1.edmondsKarp(0,15);

        filnavn= "src/Oblig7/flytgraf3.txt";
        System.out.println("\n*** Kjører på graf "+ filnavn+ " ***");
        graph1= new Graph(filnavn);
        graph1.edmondsKarp(0,15);
    }

    /**
     * Edmonds-Karp maximum flow algorithm
     * @param startNode the source
     * @param endNode the sink
     * @return maximum flow
     */

    public int edmondsKarp(int startNode, int endNode){
        System.out.println("\n�kning : Flyt�kende vei");
        int maxFlow = 0;
        while(true) {
            WeightedEdge[] parents = new WeightedEdge[NumberOfNodes];
            ArrayList <Node> queue = new ArrayList<>();
            queue.add(node[startNode]);

            //BFS
            while (!queue.isEmpty()) {
                Node current = queue.remove(0);
                for (WeightedEdge e : current.edges){
                    if (parents[e.to] == null && e.to != startNode && e.weight > e.flow){//Normal BFS except we also check if the capacity/weight is more than the current flow
                        parents[e.to] = e;
                        queue.add(node[e.to]);
                    }
                }
            }

            //When the end node is not reached, the loop breaks and prints out the maximum flow.
            if (parents[endNode]==null){
                break;
            }

            //Delta Flow, finds the delta flow///
            int df = 10000000;
            for (WeightedEdge wEdge = parents[endNode]; wEdge!=null; wEdge=parents[wEdge.from]){
                df = Math.min(df, wEdge.weight-wEdge.flow);
            }
            //Adds the flow of the found delta flow to the edge and subtracts the found flow from the reverse edge
            System.out.print(df + " ");
            for (WeightedEdge wEdge = parents[endNode]; wEdge!=null; wEdge = parents[wEdge.from]){
                System.out.print(wEdge.reversedEdge.from + "-");
                wEdge.flow+=df;
                wEdge.reversedEdge.flow -=df;
            }
            System.out.print(startNode+"\n");

            //Updates the maximum flow
            maxFlow+=df;
        }
        System.out.println("Max Flow: " + maxFlow);

        return maxFlow;
    }

    public void newGraphFromFile(String filePath) throws IOException {

        String userDirectory = System.getProperty("user.dir");
        File file = new File(userDirectory + "/"+ filePath);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st = ((br.readLine()));

        String[] numbers = st.split("\\s");

        NumberOfNodes = Integer.parseInt(numbers[0]);
        node = new Node[NumberOfNodes];

        for(int i = 0; i<NumberOfNodes; i++)node[i] = new Node();

        NumberOfEdges = Integer.parseInt(numbers[1]);

        while ((st = br.readLine()) != null){

            st = st.trim();
            numbers = st.split("\\s+",3);
            int from = Integer.parseInt(numbers[0].trim());
            int to = Integer.parseInt(numbers[1].trim());
            int weight = Integer.parseInt(numbers[2].trim());
            WeightedEdge e = new WeightedEdge(from, to, 0, weight);
            WeightedEdge e1 = new WeightedEdge(to, from, 0, 0);
            e.setReversedEdge(e1);
            e1.setReversedEdge(e);
            node[from].edges.add(e);
            node[to].edges.add(e1);
        }
    }
}

class WeightedEdge{
    int from;
    int to;
    int flow;
    int weight;

    WeightedEdge reversedEdge;

    public WeightedEdge(int u, int v, int flow, int weight){
        this.from = u;
        this.to = v;
        this.flow = flow;
        this.weight = weight;
    }

    public void setReversedEdge(WeightedEdge reversedEdge) {
        this.reversedEdge = reversedEdge;
    }
}

class Node {
    ArrayList<WeightedEdge> edges = new ArrayList<>();
}