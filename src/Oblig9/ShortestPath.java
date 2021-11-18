package Oblig9;

import java.util.PriorityQueue;

public class ShortestPath {

    public ShortestPath() {
    }

    /**
     * Method for finding distance between two nodes given their coordinates
     *
     * @param one The first node
     * @param two the second node
     * @return the distance between the nodes in metres
     */
    public double distanceBetweenNode(Node one, Node two){

        final int R = 6371;

        double l1 = one.getLatitude() * Math.PI/180;
        double b1 = one.getLongitude() * Math.PI/180;

        double l2 = two.getLatitude() * Math.PI/180;
        double b2 = two.getLongitude() * Math.PI/180;


        double a = Math.pow(Math.sin((b1-b2)/2), 2) + Math.cos(b1)*Math.cos(b2) * Math.pow(Math.sin((l1-l2)/2), 2);

        return 2*R *Math.asin(Math.sqrt(a));
    }

    /**
     * Method using dijkstras algorithm to find the shortest path between two nodes
     *
     * @param nodes graph to search in
     * @param startNode the node to start the search from
     * @param goalNode the goal node of the search
     * @return the number of nodes it searched
     */
    public int dijkstra(Node[] nodes, int startNode, int goalNode){

        nodes[startNode].setDistanceFromStart(0);

        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(new NodeDistanceComparator());

        priorityQueue.add(nodes[startNode]);//adding the start node to the priority queue

        int count = 0;
        while(!priorityQueue.isEmpty()){
            count++;
            Node min = priorityQueue.poll();// taking the first node from the queue

            if(min.getNumber() == goalNode){
                return count;
            }
            for(Edge e : min.getEdges()){// Iterating through the edges of the node

                int alt = min.getDistanceFromStart() + e.getWeight(); //getting the current distance from the
                // start node and adding the weight of the edge

                if(alt < nodes[e.getEndNode()].getDistanceFromStart()){ //if the newly found distance is smaller than the previous
                    nodes[e.getEndNode()].setDistanceFromStart(alt); // updating distance
                    nodes[e.getEndNode()].setPredecessor(min.getNumber()); //adding the predecessor, so the route can be found

                    priorityQueue.add(nodes[e.getEndNode()]);//adding the node to the queue so to continue the search
                }
            }
        }
        return -1;
    }

    /**
     * Method for finding the 10 closest of a given type from a given startnode
     *
     * @param nodes the graph to search in
     * @param startNode The starting node
     * @param goalType the type of node we are looking for
     * @return array of 10 closest of given type
     */
    public int[] dijkstraTenClosest(Node[] nodes, int startNode, int goalType){

        for(int i = 0; i < nodes.length; i++){ //setting all distances to infinity
            nodes[i].setDistanceFromStart(1000000000);
        }

        nodes[startNode].setDistanceFromStart(0);

        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(new NodeDistanceComparator());

        priorityQueue.add(nodes[startNode]);//adding the start node to the priority queue

        int[] correctType = new int[10];
        int count = 0;
        while(!priorityQueue.isEmpty()){

            Node min = priorityQueue.poll();// taking the first node from the queue

            if(min.getNodeType() == goalType){
                correctType[count]= min.getNumber();
                count++;
                if(count == 10){
                    return correctType;
                }
            }
            for(Edge e : min.getEdges()){// Iterating through the edges of the node

                int alt = min.getDistanceFromStart() + e.getWeight(); //getting the current distance from the
                // start node and adding the weight of the edge

                if(alt < nodes[e.getEndNode()].getDistanceFromStart()){ //if the newly found distance is smaller than the previous

                    nodes[e.getEndNode()].setDistanceFromStart(alt); // updating distance
                    nodes[e.getEndNode()].setPredecessor(min.getNumber()); //adding the predecessor, so the route can be found

                    priorityQueue.add(nodes[e.getEndNode()]);//adding the node to the queue so to continue the search
                }
            }
        }
        return null;
    }

    /**
     * Method for preprocessing the distances from all nodes to a given start node
     *
     * @param nodes the nodes of hte graph
     * @param startNode the node to start the search from
     * @return a list of the shortest distance for each node to the start node
     */
    public int[] dijkstraPreProcess(Node[] nodes, int startNode){
        int n = nodes.length;

        int[] distances = new int[nodes.length];

        for(int i = 0; i < n; i++){ //setting all distances to infinity
            distances[i] = 1000000000;
            nodes[i].setDistanceFromStart(1000000000);
        }

        distances[startNode] = 0;//setting the start node distance to 0
        nodes[startNode].setDistanceFromStart(0);

        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(new NodeDistanceComparator());

        priorityQueue.add(nodes[startNode]);//adding the start node to the priority queue

        while(!priorityQueue.isEmpty()){
            Node min = priorityQueue.poll();// taking the first node from the queue

            for(Edge e : min.getEdges()){// Iterating thru the edges of the node

                int alt = min.getDistanceFromStart() + e.getWeight(); //getting the current distance from the
                // start node and adding the weight of the edge

                if(alt < nodes[e.getEndNode()].getDistanceFromStart()){ //if the newly found distance is smaller than the previous
                    nodes[e.getEndNode()].setDistanceFromStart(alt); // updating distance
                    nodes[e.getEndNode()].setPredecessor(min.getNumber()); //adding the predecessor, so the route can be found
                    priorityQueue.add(nodes[e.getEndNode()]);//adding the node to the queue to continue the search
                    distances[e.getEndNode()] = alt;
                }
            }
        }
        return distances;
    }

    /**
     * Method using the enhanced dijkstras algorithm, the ALT Algorithm to find the shortest path between two nodes.
     * This method will find the same route as the dijkstra algorithm, but in a much smaller search
     *
     * @param nodes the graph to search in
     * @param startNode the starting point of the search
     * @param goalNode the goal node of the search
     * @param array Preprocessed data containg distances to and from all nodes and landmarks
     * @return the amount of nodes searched
     */

    public int alt(Node[] nodes, int startNode, int goalNode, PreproccesedArray array){

        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(new NodeLandmarkDistanceComparator());

        for(int i = 0; i < nodes.length; i++){ //setting all distances to infinity
            nodes[i].setDistanceFromStart(1000000000);
            nodes[i].setPredecessor(-1);
        }

        nodes[startNode].setDistanceFromGoal(generateDistanceForAlt(nodes[startNode], array, goalNode));

        nodes[startNode].setDistanceFromStart(0);

        priorityQueue.add(nodes[startNode]);//adding the start node to the priority queue

        int count = 0;
        while(!priorityQueue.isEmpty()){
            count++;
            Node min = priorityQueue.poll();// taking the first node from the queue

            if(min.getNumber() == goalNode){
                return count;
            }
            for(Edge e : min.getEdges()){// Iterating through the edges of the node

                int alt = min.getDistanceFromStart() + e.getWeight() + generateDistanceForAlt(nodes[e.getEndNode()], array, goalNode); //getting the current distance from the
                // start node and adding the weight of the edge

                if(alt < nodes[e.getEndNode()].getDistanceFromStart()+ generateDistanceForAlt(nodes[e.getEndNode()], array, goalNode)){ //if the newly found distance is smaller than the previous

                    if(nodes[e.getEndNode()].getDistanceFromStart() < 1000000000){
                        priorityQueue.remove(nodes[e.getEndNode()]);
                        nodes[e.getEndNode()].setDistanceFromStart(min.getDistanceFromStart() + e.getWeight());
                        priorityQueue.add(nodes[e.getEndNode()]);
                    }else{
                        nodes[e.getEndNode()].setDistanceFromStart(min.getDistanceFromStart() + e.getWeight());
                    }
                    // updating distance
                    nodes[e.getEndNode()].setPredecessor(min.getNumber()); //adding the predecessor, so the route can be found

                    nodes[e.getEndNode()].setDistanceFromGoal(generateDistanceForAlt(nodes[e.getEndNode()], array, goalNode));
                    priorityQueue.add(nodes[e.getEndNode()]);//adding the node to the queue to continue the search
                }
            }
        }
        return -1;
    }

    /**
     * Method for generating the best possible distance estimate for use in the ALT algorithm
     * @param node Node that is processed by the algorithm
     * @param array Preprocessed arrays
     * @param goalNode The goalnode
     * @return Returns the same node, just that its goal distance is updated
     */

    public int generateDistanceForAlt(Node node, PreproccesedArray array, int goalNode){
        int[][] fromLandmarkArray = array.getFromLandmark();
        int[][] toLandmarkArray = array.getToLandmark();

        int distanceFromLandmarkToGoal = 0;
        int distanceFromLandmarkToN = 0;
        int firstEstimate = 0;

        int distanceFromNToLandmark = 0;
        int distanceFromGoalToLandmark = 0;
        int secondEstimate = 0;

        int prevoiusEstimate = 0;
        int finalEstimate = 0;


        //Looper over alle elemeneter i landmark arrayen
        for (int i = 0; i < fromLandmarkArray.length; i++) {

            //Finner avstand fra hvert landemerke til målnoden og node n
            distanceFromLandmarkToGoal = fromLandmarkArray[i][goalNode];
            distanceFromLandmarkToN = fromLandmarkArray[i][node.getNumber()];

            //Subrtaherer de to
            firstEstimate = 0;
            if (distanceFromLandmarkToGoal-distanceFromLandmarkToN>0) firstEstimate = distanceFromLandmarkToGoal-distanceFromLandmarkToN;

            //Finne avstand fra n og målnoden til landemerker
            distanceFromNToLandmark = toLandmarkArray[i][node.getNumber()];
            distanceFromGoalToLandmark = toLandmarkArray[i][goalNode];

            //Subtraherer de to
            secondEstimate = distanceFromNToLandmark - distanceFromGoalToLandmark;

            //Sjekker hvilken som er størst
            if (firstEstimate>secondEstimate) finalEstimate = firstEstimate;
            else finalEstimate = secondEstimate;

            //Sjekker om estimatet er større enn det tidligere estimater
            if (finalEstimate>prevoiusEstimate) prevoiusEstimate = finalEstimate;

        }

        //Setter avstanden fra goal som estimatet
        return prevoiusEstimate;
    }
}