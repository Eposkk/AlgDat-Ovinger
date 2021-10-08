package Oblig3;

import java.util.Random;

public class Oppg2 {

    /**
     *
     * Klasse for å eksperimenter med quickSort og countingSort
     *
     */

    public static void main(String[] args) {

        //størrelsen på listene som skal sorteres
        int storrelse = 1_000_000;

        //antall forskjellige lister vi skal teste
        int antKjoringer = 100;

        //Listetype vi skal teste med
        String listetype = "REPETERENDE_TALL";
        //String listetype = "3_GANGEN";
        //String listetype = "UNIKE_TALL";

        System.out.println("\n***Tidtaking***");
        System.out.println("Tester algoritmene på på de samme "+ antKjoringer +" tilfeldig genererte listene, Listene har en størrelse på: " + storrelse);
        System.out.println("Listetype: " + listetype);
        System.out.println(
                "Dersom en av sorteringene feiler ved å enten endre totalsummen eller å" +
                "\nha sortert feil avbrytes kjøringen og hvilken metode som feilet blir printet\n"
        );

        //Variabler for å lagre totaltid brukt av hver algoritme på sorteringen
        int samletTid1 = 0;
        int samletTid2 = 0;

        //Lister for å lage kopier av listen så begge metoden får kjørt på identiske lister¢
        int[] kopi1 = new int[storrelse];
        int[] kopi2 = new int[storrelse];

        //Loop som kjører antall ganger definert i antKjoringer
        for(int a = 0; a<antKjoringer; a++){

            //Opretter ny liste for sortering
            /**
             ******* Endre listetype her *******
             */
            int[] liste;

            switch (listetype){
                case "REPETERENDE_TALL":
                    liste = genererTilfeldigListeFlereTall(storrelse);
                    break;
                case "3_GANGEN":
                    liste = genererListe3Gangen(storrelse);
                    break;
                case "UNIKE_TALL":
                    liste = genererListeMedUnikeTall(storrelse);
                    break;
                default:
                    throw new IllegalStateException("Listeype kan ikke være: " + listetype + "\n du kan velge mellom: VANLIG, 3_GANGEN, UNIKE_TALL");
            }

            //Finner totalsummen av listen for å kunne teste senere
            long s = sjekkSum(liste);

            //kopierer listen til to separate lister
            for(int i=0 ; i<liste.length;i++){
                kopi2[i] = liste[i];
                kopi1[i] = liste[i];
            }

            //Tar tiden på og kjører quicksort algoritmen
            long startTime1=System.currentTimeMillis();
            quickSort(kopi1, 0, storrelse-1);
            long sluttTime1=System.currentTimeMillis();

            samletTid1 += (sluttTime1-startTime1);

            //Dersom algoritmen har gjort en fril, avbryt kjøringen
            if(!sjekkSortering(kopi1) || sjekkSum(kopi1)!= s){
                System.out.println("feil med vanlig");
                break;
            }

            //Tar tiden på og kjører quickSort med countingSort algoritmen
            long startTime2=System.currentTimeMillis();
            quickSortMedCountingSort(kopi2, 0, storrelse-1);
            long sluttTime2=System.currentTimeMillis();

            samletTid2 += (sluttTime2-startTime2);

            //Dersom algoritmen har gjort en feil, avbryt kjøringen
            if(!sjekkSortering(kopi2)|| sjekkSum(kopi2)!= s){
                System.out.println("feil med tellesort");
                break;
            }





            //Viser i prosent hvor langt i kjøringen vi har kommet
            System.out.print("Progress: " + a/(0.01*antKjoringer) + "%\r");
        }

        //Finner gjennomsnittstiden på hver runde for hver av algoritmene
        System.out.println("QuickSort: "+(samletTid1/antKjoringer) + " Millisekunder");
        System.out.println("QuickSort med countingSort: "+(samletTid2/antKjoringer) + " Millisekunder");
    }


    /**
     * Metode for å generere en randomisrt liste med tilfeldige tall
     *
     * @param storrelse størrelsen på listen man vil få returnert
     * @return den genererte listen med tall
     */

    private static int[] genererTilfeldigListeFlereTall(int storrelse){
        int[] liste = new int[storrelse];

        Random r = new Random();

        for (int i = 0; i<liste.length;i++){
            liste[i] = r.nextInt(storrelse/10);
        }
        return liste;
    }

    /**
     * Metode for å genere en randomisert liste med tall i 3-gangen
     *
     * @param storrelse størrelsen på listen man vil få returnert
     * @return den genererte listen
     */
    private static int[] genererListe3Gangen(int storrelse){
        int[] liste = new int[storrelse];
        for (int i = 0; i<storrelse;i++){
            liste[i]= i*3;
        }

        Random r = new Random();

        for(int i = storrelse-1; i >0; i--){
            int j = r.nextInt(i);

            int temp = liste[i];
            liste[i] = liste[j];
            liste[j] = temp;
        }
        return liste;
    }

    /**
     * Metode for å generere en liste med alle mulige positive heltall basert på lengden av lista
     *
     * @param storrelse størrelsen på lista man vil generere
     * @return den genererte listen
     */
    private static int[] genererListeMedUnikeTall(int storrelse){

        int[] liste = new int[storrelse];

        //generer liste på gitt størrelse
        for(int i = 0; i<storrelse; i++) {
            liste[i] = i;
        }

        Random r = new Random();

        //randomiser listen
        for(int i = storrelse-1; i >0; i--){
            int j = r.nextInt(i);

            int temp = liste[i];
            liste[i] = liste[j];
            liste[j] = temp;
        }

        return liste;
    }

    /**
     * Metode for å sjekke at en liste med tall er sortert korrekt i stigende rekkefølge
     *
     * @param liste listen som skal sjekkes
     * @return true hvis sortert korrekt, false hvis ikke
     */
    private static boolean sjekkSortering(int[] liste){
        for (int i = 1; i<liste.length;i++){
            if (!(liste[i]>=liste[i-1])){
                System.out.println("feil ved"+liste[i]);
                return false;
            }
        }
        return true;
    }

    /**
     * Sorteringsalgoritme som anvender bode metoden quickSort og countingSort for bedre ytelse enn vanlig quickSort
     *
     * @param liste listen som skal sorteres
     * @param venstreEndepunkt venstre endepunkt av listen
     * @param hoyreEndepunkt høyre endepunkt av listen
     */
    private static void quickSortMedCountingSort(int[] liste, int venstreEndepunkt, int hoyreEndepunkt) {

        if(venstreEndepunkt < hoyreEndepunkt ){
            int storste = 0;
            int minste = liste[venstreEndepunkt];

            //Dersom deltabellen ikke er på ytterkantene av tabellen, setter vi minste
            // og største basert på endepuktene
            if(venstreEndepunkt == 0 && hoyreEndepunkt == liste.length-1){
                minste = venstreEndepunkt;
                storste = hoyreEndepunkt;
            }else{
                //Dersom del tabellen er på ytterkantene av tabellen kjører vi
                // en løkke for å finne minste og største verdi
                for(int i = venstreEndepunkt; i<= hoyreEndepunkt; i++) {

                    if (liste[i] > storste) storste = liste[i];
                    if (liste[i] < minste) minste = liste[i];

                    //Dersom løkken ser at variasjonsbredden er større enn intervallet,
                    // så avbryter den for å spare tid
                    if (storste - minste > hoyreEndepunkt - venstreEndepunkt) {
                        break;
                    }
                }
            }
            //Dersom variasjonsbredden er mindre enn intervallet, gjør countingSort
            if (storste - minste < hoyreEndepunkt - venstreEndepunkt){
                countingSort(liste, venstreEndepunkt, hoyreEndepunkt, storste, minste);
            }else { //Dersom variasjonsbredden er større enn intervallet, gjør quickSort
                int delePos = splitt(liste, venstreEndepunkt, hoyreEndepunkt);
                quickSortMedCountingSort(liste, venstreEndepunkt, delePos - 1);
                quickSortMedCountingSort(liste, delePos + 1, hoyreEndepunkt);
            }
        }
    }

    /**
     * Sorteringsalgoritme som anvende metoden quickSort
     *
     * @param liste listen som skal sorteres
     * @param venstreEndepunkt venstre endepunkt av listen
     * @param hoyreEndepunkt høyre endepunkt av listen
     */
    private static void quickSort(int[] liste, int venstreEndepunkt, int hoyreEndepunkt){
        if (venstreEndepunkt < hoyreEndepunkt) {
            int delePos = splitt(liste, venstreEndepunkt, hoyreEndepunkt);
            quickSort(liste, venstreEndepunkt, delePos - 1);
            quickSort(liste, delePos + 1, hoyreEndepunkt);
        }
    }

    /**
     * Hjelpemetode for quicksort algoritmen for å finne del-lister som skal sorteres
     *
     * @param liste
     * @param venstreEndepunkt
     * @param hoyreEndepunkt
     * @return
     */
    private static int splitt(int[] liste, int venstreEndepunkt, int hoyreEndepunkt) {

        bytt(liste, hoyreEndepunkt, Math.round((hoyreEndepunkt+venstreEndepunkt)/2));
        int pivot = liste[hoyreEndepunkt];

        int i = (venstreEndepunkt-1);


        for (int j = venstreEndepunkt; j < hoyreEndepunkt; j++) {
            if (liste[j] < pivot) {
                i++;

                bytt(liste, i,j);
            }
        }

        bytt(liste, i+1, hoyreEndepunkt);

        return i+1;
    }

    /**
     * CountingSort algoritme skrevet for å brukes med quickSort.
     * Brukes for å sortere deltabeller der variansen er mindre enn intervallet.
     *
     * @param liste listen som skal sorteres
     * @param venstreEndepunkt venstre endepunkt av deltabellen
     * @param hoyreEndepunkt høyre endepunkt av deltabellen
     * @param storste største verdi i deltabellen
     * @param minste minste verdi i deltabellen
     */

    public static void countingSort(int[] liste, int venstreEndepunkt, int hoyreEndepunkt, int storste, int  minste){
        int i;
        int n = hoyreEndepunkt-venstreEndepunkt;
        int v = storste - minste;

        int[] output = new int[n+1];
        int[] count = new int[v+1];

        // Initialiserer count arrayen med bare null
        for (i = 0; i < v; ++i) {
            count[i] = 0;
        }
        //Lagrer antallet av hver verdi
        for (i = venstreEndepunkt; i <= hoyreEndepunkt; i++) {
            count[liste[i]-minste]++;
        }
        // Lagrer det kumulative antallet av hver
        for ( i = 1; i <= v; i++) {
            // System.out.println(i);
            count[i] += count[i - 1];
        }
        // Finner indeksen til hvert element i den originale listen i count listen,
        // og plasserer de i en sortert ouput liste
        for ( i = hoyreEndepunkt ; i >= venstreEndepunkt; i--) {
            output[count[liste[i]-minste]-1] = liste[i];
            count[liste[i]-minste]--;
        }
        // Kopierer over den sorterte listen til den egentlige listen
        for ( i = venstreEndepunkt; i <= hoyreEndepunkt; i++) {
            liste[i] = output[i-venstreEndepunkt];
        }
    }

    /**
     * Summerer summen av en liste
     *
     * @param liste listen å summere
     * @return summen til listen
     */
    private static long sjekkSum(int[] liste){
        long sum = 0;
        for(Integer i: liste){
            sum += i;
        }
        return sum;
    }

    /**
     * Metode for å bytte plassering på to verdier i en liste
     *
     * @param liste listen det skal utføres en endring i
     * @param i plassering 1
     * @param j plassering 2
     */

    private static void bytt(int[] liste, int i, int j) {
        int k = liste[i];
        liste[i] = liste[j];
        liste[j] = k;
    }
}