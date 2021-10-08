package Oblig5;

import java.util.Random;

public class Oppg2 {
    private static int tall = 10000019;
    //private static int tall = 50051;

    public static void main(String[] args) {
        System.out.println("**RUNNING TESTS**\n");

        int n= 1;
        while(n<=5){
            switch (n){
                case 1:
                    runTest(0.5);
                    break;
                case 2:
                    runTest(0.8);
                    break;
                case 3:
                    runTest(0.9);
                    break;
                case 4:
                    runTest(0.99);
                    break;
                case 5:
                    runTest(1);
                    break;
                default:
                    System.out.println("Something unexcpected happened");
                    break;

            }
            n++;
        }
        System.out.println("**TESTS FINISHED**");
    }

    private static void runTest(double p){
        System.out.println("Prosent: "+p*100 + "%\n");
        HashTable hashTableDouble = new HashTableDouble(tall, p);
        hashTableDouble.printData();
        HashTable hashTableLinear = new HashTableLinear(tall, p);
        hashTableLinear.printData();
        HashTable hashTableSquared = new HashTableSquared(tall, p);
        hashTableSquared.printData();
    }
}

abstract class HashTable {

    protected int[] array;
    protected int arraySize;
    protected int[] generatedArray;

    protected int collision = 0;
    protected int numberOfNames = 0;

    public HashTable(int m, double prosent){
        arraySize = m;
        array = new int[m];
        generatedArray = generateRandomNumbers(prosent);
    }

    public abstract void printData();


    /**
     *
     * @param k integer based on the string
     * @return an index for the string
     */
    protected int hash(int k){
        return k % arraySize;
    }

    protected abstract int addValue ( int value);

    /**
     * Method for reading navn.txt and adding the values to the list.
     */
    private int[] generateRandomNumbers(double prosent){
        int[] originalArray = new int[(int)Math.round(arraySize*prosent)];
        int tall= 0;
        Random rand = new Random();
        for(int i = 0; i<originalArray.length; i++){
            tall+= 1 + rand.nextInt(200);
            originalArray[i] = tall;
        }
        for (int i = 0; i < originalArray.length; i++) {
            int randomIndexToSwap = rand.nextInt(originalArray.length);
            int temp = originalArray[randomIndexToSwap];
            originalArray[randomIndexToSwap] = originalArray[i];
            originalArray[i] = temp;
        }
        return originalArray;
    }

    /**
     *
     * @param array array to populate hashTable
     * @return time used to fill the hashTable
     */
    public long fillHashTable(int[] array){

        long start = System.currentTimeMillis();
        for (int i = 0; i<array.length; i++){
            if (array[i] <1){
                System.out.println(array[i]);
            }
            addValue(array[i]);
        }
        long end = System.currentTimeMillis();
        return end-start;
    }
    public double calculateLoadFactor(){
        return (double) numberOfNames/arraySize;
    }
}
class HashTableDouble extends HashTable {

    public HashTableDouble(int m, double prosent) {
        super(m, prosent);
    }

    @Override
    protected int addValue(int value) {
        int h1 = hashOne(value);
        int h2;
        int pos = h1;

        if(array[pos] == 0){
            array[pos] = value;
            return pos;
        }else{
            h2 = hashTwo(value);
            while(array[pos] != 0){
                collision++;
                pos = probe(pos, h2);
                if(array[pos] == 0){
                    array[pos] = value;
                    return pos;
                }
            }
        }
        return -1; //Full
    }

    protected int probe(int pos, int h2) {
        return (pos+h2) % arraySize;
    }

    private int hashOne(int value){
        return value % arraySize;
    }

    private int hashTwo(int value){
        return (value % (arraySize-1)) + 1;
    }

    @Override
    public void printData(){
        long time = fillHashTable(generatedArray);
        System.out.println("**Double Hash**\n Collisions: " +  collision);
        System.out.println("Milliseconds: " + time + "\n");
    }
}

class HashTableLinear extends HashTable {
    public HashTableLinear(int m, double n) {
        super(m,n);
    }

    /**
     * Add a value to the hashTable
     * @param value the String value to be added
     */
    protected int addValue(int value){
        int h = hash(value);
        for (int i = 0; i<arraySize;i++){
            int j = probe(h,i);
            if(array[j] == 0){
                array[j] = value;
                return j;
            }else{
                collision++;
            }
        }
        return -1; //Full
    }

    protected int probe(int hash, int i) {
        return (hash+i)%arraySize;
    }

    @Override
    public void printData(){
        long time = fillHashTable(generatedArray);
        System.out.println("**Linear Probing**\n Collisions: " +  collision);
        System.out.println("Milliseconds: " + time + "\n");
    }
}
class HashTableSquared extends HashTable {
    public HashTableSquared(int m, double n) {
        super(m,n);
    }

    /**
     * Add a value to the hashTable
     * @param value the String value to be added
     */
    protected int addValue(int value){
        int h = hash(value);
        for (int i = 0; i<arraySize;++i){
            int j = probe(h,i);
            if(array[j] == 0){
                array[j] = value;
                return j;
            }else{
                collision++;
            }
        }
        return -1; //Full
    }

    private int probe(int hash, int i) {
        int k1 = 7;
        int k2 = 9;
        return (int) ((hash+k1*i+k2*Math.pow(i,2))%arraySize);
    }

    @Override
    public void printData(){
        long time = fillHashTable(generatedArray);
        System.out.println("**Squared Probing**\n Collisions: " +  collision);
        System.out.println("Milliseconds: " + time + "\n");
    }
}

