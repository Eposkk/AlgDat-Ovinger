package Oblig6;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Graph {

    public static void main(String[] args) {
        int startNode = 5;
        Graph graf1 = new Graph("src/Oblig6/L7g1.txt");
        System.out.println("*** Breddesøk på Graf 1, startnode " + startNode +" ***");
        graf1.bfs(startNode);
        graf1.printNodeInfo(startNode);

        System.out.println("\n");
        Graph graf2 = new Graph("src/Oblig6/L7g5.txt");
        System.out.println("*** Toplogisk søk på Graf 5 ***");
        graf2.printTopoListe();
    }

    int N, E;
    Node[] node;

    public Graph(String filepath){
        try {
            newGraphFromFile(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializePredecessor(Node s){
        for(int i = N; i --> 0 ;){
            node[i].d = new Predecessor();
        }
        ((Predecessor) s.d).distance = 0;
    }

    public void bfs(int i){
        Node s = node[i];

        initializePredecessor(s);

        Queue queue = new Queue(N-1);

        queue.addInQueue(s);
        while(!queue.isEmpty()){
            Node n = queue.nextInQueue();
            for(Edge e = n.edge1; e != null; e = e.next){
                Predecessor p = (Predecessor) e.to.d;
                if(p.distance == p.infinite){
                    p.distance = ((Predecessor) n.d).distance +1;
                    p.predecessor = n;
                    queue.addInQueue(e.to);
                }
            }
        }
    }

    /**
     * Depth first search for topsort
     * @param n starting node
     * @param l end node
     * @return
     */
    public Node df_topo(Node n, Node l){
        Topo_lst nd= (Topo_lst) n.d;
        if(nd.funnet) return l;
        nd.funnet = true;
        for (Edge k = n.edge1; k!=null;k=k.next){
            l=df_topo(k.to,l);
        }
        nd.neste = l;
        return n;
    }

    public Node topoSearch(){
        Node l = null;
        for (int i = N; i-->0;){
            node[i].d = new Topo_lst(i);
        }
        for (int i = N; i-->0;){
            l=df_topo(node[i],l);
        }
        return l;
    }

    public void newGraphFromFile(String filePath) throws IOException {
        File file = new File(filePath);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st = ((br.readLine()));

        String[] numbers = st.split("\\s");

        N = Integer.parseInt(numbers[0]);
        node = new Node[N];

        for(int i = 0; i<N; i++)node[i] = new Node();

        E = Integer.parseInt(numbers[1]);

        while ((st = br.readLine()) != null){
            numbers = st.split("\\s");
            int from = Integer.parseInt(numbers[0]);
            int to = Integer.parseInt(numbers[1]);
            Edge e = new Edge(node[to], node[from].edge1, from);
            node[from].edge1 = e;
        }
    }

    public void printNodeInfo(int i){
        System.out.println("N  F  D");

        for (int j = 0; j<node.length; j++){
            Predecessor pd = (Predecessor) node[j].d;
            var n = "-";
            if (!((((Predecessor) node[j].d).predecessor) ==null)){
                n=Integer.toString(((Predecessor) node[j].d).predecessor.edge1.pekerFra);

            }
            System.out.println(j + "  "+ n +"  " + pd.distance);
        }
    }


    public void printTopoListe(){
        Node liste = topoSearch();
        Node currentNode = liste;

        for (int i = 0; i<N;i++){
            System.out.print(((Topo_lst)currentNode.d).getIndex());
            if(i!=N-1){
                System.out.print(" - ");
            }
            currentNode = ((Topo_lst)currentNode.d).neste;
        }
    }

}

class Queue{
    private Node[] list;
    private int start = 0;
    private int end = 0;
    private int count = 0;

    public Queue(int size){
        list = new Node[size];
    }

    public boolean isEmpty(){
        return count == 0;
    }

    public boolean isFull(){
        return count == list.length;
    }

    public void addInQueue(Node n){
        if(isFull()) return;

        list[end] = n;

        end = (end+1)%list.length;
        count++;
    }

    public Node nextInQueue(){
        if(!isEmpty()){
            Node n = list[start];
            start = (start+1)%list.length;
            count--;
            return n;
        } else{
            return null;
        }
    }
}
class Predecessor{
    int distance;
    Node predecessor;

    static int infinite = 100000000;

    public Predecessor(){
        distance = infinite;
    }

    public int getDistance(){
        return distance;
    }

    public Node getPredecessor(){
        return predecessor;
    }
}

class Node{
    Edge edge1;
    Object d;
}

class Edge{
    Edge next;
    Node to;
    int pekerFra;

    public Edge(Node n, Edge next, int pekerFra){
        to = n;
        this.next = next;
        this.pekerFra = pekerFra;
    }
}

class Topo_lst {
    boolean funnet;
    int index;
    Node neste;
    public  Topo_lst(int index){
        this.index=index;
    }
    public int getIndex() {
        return index;
    }
}

