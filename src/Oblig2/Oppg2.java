package Oblig2;

public class Oppg2 {
    public static void main(String[] args) {

        double x=4;
        int n= 100000000;
        System.out.println("x= " + x + ", \nn= " + n);

        /**
         * Metode fra oppgave 2.1-1
         */

        System.out.println("\nMetode 1: \n2^10 = "+ metode1(2,10));
        System.out.println("3^14 = "+ metode1(3,14));

        double resultat1 =0;
        int antall1= 0;
        long start = System.currentTimeMillis();

        /*do {
            resultat1= metode1(x,n);
            antall1++;
        }while(System.currentTimeMillis() - start <1000);

        System.out.println("\nMetode 1 kjørte " +  antall1 + " ganger på 1000 millisekunder");


        /**
         * Metode fra oppgave 2.2-3
         */

        System.out.println("\nMetode 2: \n2^10 = "+metode2(2,10));
        System.out.println("3^14 = "+metode2(3,14));

        double resultat2 = 0;
        int antall2= 0;

        start = System.currentTimeMillis();
        do {
            resultat2=metode2(x,n);
            antall2++;
        }while(System.currentTimeMillis() -start <1000);

        System.out.println("\nMetode 2 " +  antall2 + " ganger på 1000 millisekunder");
        System.out.println((1000/antall2)*1000);
    }


    private static double metode1(double x, int n){
        if (n == 0){
            return 1;
        }else{
            return x* metode1(x,n-1);
        }
    }

    private static double metode2(double x, int n) {
        if (n == 0) {
            return 1;
        } else if (n % 2 == 0) {//if even number
            return metode2(x * x, n / 2);
        } else {//odd number
            return x * metode2(x * x, (n - 1) / 2);
        }
    }
}
