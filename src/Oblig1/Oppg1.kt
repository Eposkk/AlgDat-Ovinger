package Oblig1

class Oppg1Kotlin {
    var startTime = 0L;
    var endTime = 0L;

    fun stockDataGenerator(numberOfDays: Int): IntArray {
        var stockChangePerDay = IntArray(numberOfDays);
        val max = 20;
        val min = -20;
        var randomInteger = 0;

        for (i in 0..numberOfDays){
            randomInteger = Math.floor(Math.random()*(max-min+1)+min).toInt();
            stockChangePerDay[i] = randomInteger;
        }
        return stockChangePerDay;
    }

    fun findMostProfit(){
        val main = Oppg1();
        val numberOfDays = 1000;
        var stockChangePerDay = intArrayOf(-1,3,-9,2,2,-1,2,-1,-5);
        var stockValuesPerDay = IntArray(9);

        var currentValue = 0;
        for(i in 0 .. stockChangePerDay.size-1){
            currentValue+=stockChangePerDay[i];
            stockValuesPerDay[i] = (currentValue);
        }
        startTime = System.currentTimeMillis();

        var maxdiff = 0;
        var stockI = 0; var stockJ=0;
        var index0=0; var index1=0;

        for(i in 0..stockValuesPerDay.size-1){
            for (j in 0 .. stockValuesPerDay.size-1){
                stockI = stockValuesPerDay[i];
                stockJ = stockValuesPerDay[j];

                if (i<j){
                    var difference = stockJ-stockI;
                    if (difference > maxdiff){
                        maxdiff=difference;
                        index0 = i+1;
                        index1 = j+1;
                    }
                }
            }
        }
        endTime = System.currentTimeMillis()-startTime;
        println("Kjøp på dag: "+index0+ "\nSelg på dag: " +index1+ "\nProfitt: "+maxdiff+"\nTid brukt: "+endTime+"ms");

    }
}

fun main(){
    val main = Oppg1Kotlin();
    main.findMostProfit();
}
