package Oblig1;

public class Oppg1 {
    public long startTime=0;
    public long endTime=0;
    public Integer[] stockDataGenerator(int numberOfDays){
        Integer[] stockChangePerDay = new Integer[numberOfDays];
        int max=20;
        int min=-20;
        int randomInteger = 0;

        for (int i=0; i<numberOfDays;i++){
            randomInteger = (int)Math.floor(Math.random()*(max-min+1)+min);
            stockChangePerDay[i]=(randomInteger);
            //System.out.println(randomInteger);
        }
        return stockChangePerDay;
    }

    public void findMostProfit(){
        Oppg1 main = new Oppg1();
        int numberOfDays = 1000;
        //Integer[] stockChangePerDay=Oblig6.main.stockDataGenerator(numberOfDays);
        Integer[] stockChangePerDay = {-1,3,-9,2,2,-1,2,-1,-5};
        Integer[]stockValuesPerDay = new Integer[9];

        int currentValue=0;
        for (int i=0 ;i<stockChangePerDay.length;i++){
            stockValuesPerDay[i]=(currentValue+=stockChangePerDay[i]);
        }
        startTime = System.currentTimeMillis();

        int maxdiff = 0;
        int stockI=0;int stockJ=0;
        int indeks0=0; int indeks1=0;

        for (int i=0; i<stockValuesPerDay.length;i++){
            for (int j=0; j<stockValuesPerDay.length;j++){
                stockI=stockValuesPerDay[i];
                stockJ=stockValuesPerDay[j];

                if (i<j){
                    int difference = stockJ-stockI;
                    if(difference > maxdiff){
                        maxdiff = difference;
                        indeks0= i+1;
                        indeks1= j+1;
                    }
                }
            }
        }
        endTime=System.currentTimeMillis()-startTime;
        System.out.println("Kjøp på dag: "+indeks0+ "\nSelg på dag: " +indeks1+ "\nProfitt: "+maxdiff+"\nTid brukt: "+endTime+"ms");
    }
    public static void main(String[] args) {
        Oppg1 main = new Oppg1();
        main.findMostProfit();
    }
}
