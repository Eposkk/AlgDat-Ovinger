package Oblig9;

import java.io.Serializable;

public class PreproccesedArray implements Serializable {
    int[][] fromLandmark;
    int[][] toLandmark;


    public PreproccesedArray(int[][] fromLandmark, int[][] toLandmark){
        this.fromLandmark = fromLandmark;
        this.toLandmark = toLandmark;
    }

    public int[][] getFromLandmark() {
        return fromLandmark;
    }

    public int[][] getToLandmark() {
        return toLandmark;
    }
}
