package Oblig2;

import java.util.Date;

public class Oppg1 {

    public static void main(String[] args) {
        Date start = new Date();
        double x=2;
        int n=10000;
        int antall= 0;
        Date slutt;
        double r =0;

        System.out.println(exponential(2,10));
        System.out.println(exponential(3,14));

        do {
            r=Math.pow(x,n);
            slutt = new Date();
            antall++;
        }while(slutt.getTime()-start.getTime() <1000);

        System.out.println("Number of times program has run (1000 milliseconds): "+antall);
    }

    private static double exponential(double x, int n){
        if (n == 0){
            return 1;
        }else{
            return x* exponential(x,n-1);
        }
    }
}
