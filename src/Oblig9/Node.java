package Oblig9;

import java.util.ArrayList;
import java.util.Comparator;

public class Node {
    private int number;
    private double latitude;
    private double longitude;

    private int nodeType;
    private String name;

    private ArrayList<Edge> edges;

    private int distanceFromStart;
    private int distanceFromGoal;

    private int predecessor;

    public Node(int number, double latitude, double longitude){
        this.number = number;
        this.latitude = latitude;
        this.longitude = longitude;
        edges= new ArrayList<>();
        distanceFromStart = 1000000000;
        distanceFromGoal = 1000000000;
        predecessor = -1;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getNodeType() {
        return nodeType;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public int getDistanceFromStart() {
        return distanceFromStart;
    }

    public void setDistanceFromStart(int distanceFromStart) {
        this.distanceFromStart = distanceFromStart;
    }

    public int getDistanceFromGoal() {
        return distanceFromGoal;
    }

    public void setDistanceFromGoal(int distanceFromGoal) {
        this.distanceFromGoal = distanceFromGoal;
    }

    public int getDistanceSum(){
        return distanceFromStart + distanceFromGoal;
    }

    public void addEdge(Edge e){
        edges.add(e);
    }

    public int getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(int predecessor) {
        this.predecessor = predecessor;
    }

    @Override
    public String toString() {
        return "Node{" +
                "number=" + number +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", nodeType=" + nodeType +
                ", name='" + name + '\'' +
                ", edges=" + edges +
                ", distanceFromStart=" + distanceFromStart +
                ", distanceFromGoal=" + distanceFromGoal +
                ", predecessor=" + predecessor +
                '}';
    }
}

class NodeDistanceComparator implements Comparator<Node>{

    @Override
    public int compare(Node o1, Node o2) {
        return Double.compare(o1.getDistanceFromStart(), o2.getDistanceFromStart());
    }
}

class NodeLandmarkDistanceComparator implements Comparator<Node>{
    @Override
    public int compare(Node o1, Node o2) {
        return Double.compare(o1.getDistanceSum(), o2.getDistanceSum());
    }
}
