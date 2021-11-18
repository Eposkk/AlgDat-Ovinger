package Oblig9;

import java.io.*;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws IOException {

        /** Setup of program **/

        FileHandler fileHandler = new FileHandler();

        Node[] nodes = fileHandler.readNodeFile("noder.txt");

        HashMap<String, Integer> pointsOfInterest = fileHandler.readPointsOfInterest("interessepkt.txt", nodes);

        fileHandler.readEdgeFile("kanter.txt", nodes);

        ShortestPath shortestPath = new ShortestPath();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter start point: ");
        String startPoint = br.readLine();
        System.out.print("Enter end point: ");
        String endPoint = br.readLine();

        int startNr = pointsOfInterest.get(startPoint);
        int endNr = pointsOfInterest.get(endPoint);

        /** Regular Dijkstra's search **/

        System.out.println("\n**** Dijkstra's Algorithm ****");

        long timeStart = System.currentTimeMillis();
        int processedNodes = shortestPath.dijkstra(nodes, startNr, endNr);
        long time = System.currentTimeMillis()-timeStart;

        FileWriter writer = new FileWriter("dijkstraRoute.txt");
        if(processedNodes > -1){
            System.out.println("Time used: " + time + " milliseconds");
            System.out.println("Nodes checked: " + processedNodes);
            int totalSecs = nodes[endNr].getDistanceFromStart()/100;
            int hours = Math.round(totalSecs / 3600);
            int minutes = (totalSecs % 3600) / 60;
            int seconds = totalSecs % 60;

            String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            System.out.println("Distance: " + timeString);
            Node n = nodes[endNr];
            while(n!= null){
                writer.write(n.getLatitude() + ", " + n.getLongitude() + "\n");
                if(n.getPredecessor()>-1){
                    n = nodes[n.getPredecessor()];
                }else {
                    n=null;
                }
            }
            System.out.println("Route printed to dijkstraRoute.txt");
        }
        writer.close();

        /** ALT algorithm search **/

        System.out.println("\n**** ALT Algorithm ****");

        nodes = fileHandler.readNodeFile("noder.txt");
        Node[] reversedNodes = nodes.clone();

        fileHandler.readEdgeFileReverse("kanter.txt", reversedNodes);

        pointsOfInterest = fileHandler.readPointsOfInterest("interessepkt.txt", nodes);

        fileHandler.readEdgeFile("kanter.txt", nodes);

        String[] landmarks = {"Nordkapp", "SÃ¸nderborg", "MÃ¥lÃ¸y", "Tulppio", "Vaalimaa"};

        int[] landmarkNumbers = new int[landmarks.length];
        for(int i = 0; i<landmarks.length; i++) {
            landmarkNumbers[i] = pointsOfInterest.get(landmarks[i]);
        }

        PreproccesedArray preproccessedArrays = new PreproccesedArray(new int[landmarks.length][nodes.length],new int[landmarks.length][nodes.length]);

        /** Preprocessing of Arrays **/

        File fil = new File("preprosseserteLister.bt");
        if (fil.exists()){
            DataInputStream in = null;
            try{
                in = new DataInputStream(new FileInputStream("preprosseserteLister.bt"));
                for (int i = 0; i < landmarks.length; i++) {
                    for (int j = 0; j < nodes.length; j++) {

                        preproccessedArrays.fromLandmark[i][j] = in.readInt();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                in.close();
            }
        }else {
            int[][] fromLandmark = new int[landmarks.length][nodes.length];

            for (int l = 0; l < landmarks.length; l++) {
                System.out.print("Preprocessing fromArray for "+landmarks[l]+ "\r");
                fromLandmark[l] = shortestPath.dijkstraPreProcess(nodes, landmarkNumbers[l]);
            }

            int[][] toLandmark = new int[landmarks.length][reversedNodes.length];

            for (int l = 0; l < landmarks.length; l++) {
                System.out.print("Preprocessing toArray for "+landmarks[l]+ "\r");
                toLandmark[l] = shortestPath.dijkstraPreProcess(reversedNodes, landmarkNumbers[l]);
            }
            System.out.print("Writing preprocessing to file\r");

            preproccessedArrays = new PreproccesedArray(fromLandmark, toLandmark);

            DataOutputStream ut = null;
            try {
                ut = new DataOutputStream(new FileOutputStream("preprosseserteLister.bt"));
                for (int i = 0; i < preproccessedArrays.fromLandmark.length; i++) {
                    int[] midlertidigArray = preproccessedArrays.fromLandmark[i];
                    for (int j = 0; j < nodes.length; j++) {
                        ut.writeInt(midlertidigArray[j]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                ut.close();
            }
            System.out.print("\r");
        }

        timeStart = System.currentTimeMillis();
        processedNodes = shortestPath.alt(nodes, startNr, endNr, preproccessedArrays);
        time = System.currentTimeMillis()-timeStart;

        writer = new FileWriter("altRoute.txt");
        if(processedNodes > -1){
            System.out.println("Time used: " + time + " milliseconds");
            System.out.println("Nodes checked: " + processedNodes);

            int totalSecs = nodes[endNr].getDistanceFromStart()/100;
            int hours = Math.round(totalSecs / 3600);
            int minutes = (totalSecs % 3600) / 60;
            int seconds = totalSecs % 60;

            String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            System.out.println("Distance: " + timeString);
            Node n = nodes[endNr];
            while(n!= null){
                writer.write(n.getLatitude() + ", " + n.getLongitude() + "\n");
                if(n.getPredecessor()>-1){
                    n = nodes[n.getPredecessor()];
                }else {
                    n = null;
                }
            }
            System.out.println("Route printed to altRoute.txt");
        }
        writer.close();
    }
}