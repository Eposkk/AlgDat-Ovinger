package Oblig9;

import java.io.FileWriter;
import java.io.IOException;

public class Bensinstasjoner {

    public static void main(String[] args) throws IOException {

        FileHandler fileHandler = new FileHandler();

        Node[] nodes = fileHandler.readNodeFile("noder.txt");

        fileHandler.readPointsOfInterest("interessepkt.txt", nodes);
        fileHandler.readEdgeFile("kanter.txt", nodes);

        ShortestPath shortestPath = new ShortestPath();

        Node[] nodecopy = nodes.clone();
        Node[] nodecopy1 = nodes.clone();

        int[] bensinstasjonerVærnes = shortestPath.dijkstraTenClosest(nodecopy1, 6590451, 2);
        int[] ladestasjonerRøros = shortestPath.dijkstraTenClosest(nodecopy, 1419364, 4);


        FileWriter writer = new FileWriter("bensinstasjoner.txt");

        for(int b: bensinstasjonerVærnes){
            Node n = nodes[b];
            writer.write(n.getLatitude() + ", " + n.getLongitude() + "\n");
        }
        writer.close();

        FileWriter writer2 = new FileWriter("ladestasjoner.txt");
        for(int b: ladestasjonerRøros){
            Node n = nodes[b];
            writer2.write(n.getLatitude() + ", " + n.getLongitude() + "\n");
        }

        System.out.println("Routes printed to files");
        writer2.close();
    }
}