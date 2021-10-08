package Oblig2;

public class Oppg3 {
    public static void main(String[] args) {
        double x=2;
        int n=10000;
        System.out.println("x= " + x + ", \nn= " + n);

        /**
         * Metode 2.1-1
         */

        System.out.println("\nMetode 1: \n2^10 = "+exponential(2,10));
        System.out.println("3^14 = "+exponential(3,14));

        double resultat1 =0;
        int antall1= 0;
        long start = System.currentTimeMillis();

        do {
            resultat1=exponential(x,n);
            antall1++;
        }while(System.currentTimeMillis() - start <1000);

        System.out.println("\nOppgave 2.1-1 kjørte " +  antall1 + " ganger på 1000 millisekunder");


        /**
         * Metode 2.2-3
         */

        System.out.println("\nMetode 2: \n2^10 = "+exponentialType2(2,10));
        System.out.println("3^14 = "+exponentialType2(3,14));

        double resultat2 = 0;
        int antall2= 0;

        start = System.currentTimeMillis();
        do {
            resultat2=exponentialType2(x,n);
            antall2++;
        }while(System.currentTimeMillis() -start <1000);

        System.out.println("\nOppgave 2.2-3 kjørte " +  antall2 + " ganger på 1000 millisekunder");
    }


    private static double exponential(double x, int n){
        if (n == 0){
            return 1;
        }else{
            return x* exponential(x,n-1);
        }
    }

    private static double exponentialType2(double x, int n) {
        if (n == 0) {
            return 1;
        } else if (n % 2 == 0) {//if even number
            return exponentialType2(x * x, n / 2);
        } else {//odd number
            return x * exponentialType2(x * x, (n - 1) / 2);
        }
    }
}
