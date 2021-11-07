package Oblig8;

import java.io.*;
import java.util.*;

public class Huffman {

    private static final int MAX_BYTE_VALUE = 256;

    HuffmanNode[] byteArray;
    String[] traversalArray = new String[256];
    byte[] arrayOfOriginalBytes;
    int[] frequencies = new int[256];
    String fileExtension;
    HuffmanNode writeTop;


    public static void main(String[] args) throws IOException {
        Huffman n = new Huffman();
        n.readFile("src/Oblig8/diverse.txt");
        n.huffmanCoding();
        n.readEncodedFile("src/Oblig8/diverse.kuk","src/Oblig8/ut");
    }

    public void readFile(String file) throws IOException {
        fileExtension=file.substring(file.length()-3);

        var fil = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));

        arrayOfOriginalBytes = fil.readAllBytes();

        ArrayList<HuffmanNode> inbetweenArray = new ArrayList<>();

        for (int i = 0;i<arrayOfOriginalBytes.length;i++){
            var h = new HuffmanNode();
            h.data= (byte) (arrayOfOriginalBytes[i]);
            if (!inbetweenArray.contains(h)){
                h = new HuffmanNode(); h.data=arrayOfOriginalBytes[i];
                inbetweenArray.add(h);
            }
        }
        byteArray = new HuffmanNode[inbetweenArray.size()];
        for (int i = 0; i < inbetweenArray.size(); i++) {
            byteArray[i]=inbetweenArray.get(i);
        }
    }

    public void readEncodedFile(String file, String outputfil) throws IOException {
        int[] readFrequencies = new int[256];
        var fil = new DataInputStream((new FileInputStream(file)));
        byte firstByte = fil.readByte();
        String extension;
        switch (firstByte){
            case 1:
                extension = "txt";
                break;
            case 2:
                extension ="pdf";
                break;
            case 3:
                extension ="lz";
                break;
            default:
                throw new IllegalArgumentException("Ukjent type");
        }

        for (int i = 0; i < 256; i++) {
            int freq = fil.readInt();
            readFrequencies[i]=freq;
        }
        int lastByte = fil.readByte();
        System.out.println("h");

        ArrayList<Byte> out = new ArrayList<>();

        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>(256, new HuffmanComparator());

        byte[] readBytes = fil.readAllBytes();
        fil.close();


        int  numberOfFrequencies = 0;
        for (int i = 0; i < readFrequencies.length; i++) {
            if (frequencies[i]!=0){
                numberOfFrequencies+=1;
            }
        }
        HuffmanNode[] readNodes = new HuffmanNode[numberOfFrequencies];
        int j=0;
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i]!=0){
                var node = new HuffmanNode();
                node.data = (byte)i;
                readNodes[j] = node;
                j++;
            }
        }


        boolean array[]=new boolean[readBytes.length*8];
        for (j = 0; j < readBytes.length*8; j++) {
            if (getBit(readBytes,j)==1){
                array[j]=true;
            }
            else {
                array[j]=false;
            }
        }
        HuffmanNode top = createHuffmanTree(readNodes);
        HuffmanNode currentNode = top;



        for (int i = 0; i < array.length-lastByte; i++) {
            if (i>=array.length-8){
                if (array[i+lastByte]){
                    currentNode=currentNode.right;
                }
                else if (!array[i+lastByte]){
                    currentNode=currentNode.left;
                }
                if (currentNode.right==null && currentNode.right==null){
                    out.add(currentNode.data);
                    currentNode=top;
                }
            }else {
                if (array[i]){
                    currentNode=currentNode.right;
                }
                else if (!array[i]){
                    currentNode=currentNode.left;
                }
                if (currentNode.right==null && currentNode.right==null){
                    out.add(currentNode.data);
                    currentNode=top;
                }
            }
        }
        var writer = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputfil+"."+extension)));

        for (Byte aByte : out) {
            writer.writeByte(aByte);
        }
        writer.close();
    }

    private static ArrayList<HuffmanNode> makeNodeList(int[] frequensies) {
        ArrayList<HuffmanNode> nodeList = new ArrayList<>();
        for (int i = 0; i < frequensies.length; i++) {
            if (frequensies[i] != 0) {
                var node = new HuffmanNode();
                node.data= (byte) i; node.frequence=frequensies[i];
                nodeList.add(node);
            }
        }
        return nodeList;
    }

    public HuffmanNode createHuffmanTree(HuffmanNode[] byteArray){
        countFrequencies(byteArray);
        Arrays.sort(byteArray, new HuffmanDataComparator());

        PriorityQueue<HuffmanNode> queue = new PriorityQueue<HuffmanNode>(byteArray.length, new HuffmanComparator());

        for(int i= 0; i< byteArray.length; i++){
            queue.add(byteArray[i]);
        }

        HuffmanNode top = null;

        while(queue.size()>1){
            HuffmanNode left = queue.poll();
            HuffmanNode right = queue.poll();

            top = new HuffmanNode();
            top.frequence= left.frequence+right.frequence;

            top.left=left; top.right=right;

            queue.add(top);
        }
        return top;

    }

    public String getEncoding(HuffmanNode top, String traversal){

        if (top.left==null && top.right == null){
            traversalArray[top.data& 0xFF]= traversal;
            System.out.println(top.data+" "+ traversal);
            return traversal;
        }
        getEncoding(top.left,  traversal+"0");
        getEncoding(top.right, traversal+"1");

        return null;
    }

    public void huffmanCoding() throws IOException {
        HuffmanNode top = createHuffmanTree(byteArray);
        writeTop = top;

        String file="src/Oblig8/diverse.kuk";
        DataOutputStream fil = null;

        int lastByte = 0;

        try{
            fil = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            BitOutput output = new BitOutput(fil);

            System.out.println(fileExtension);
            switch (fileExtension){
                case "txt":
                    fil.writeByte(1);
                    break;
                case "pdf":
                    fil.writeByte(2);
                    break;
                case "lz":
                    fil.writeByte(3);
                    break;
                default:
                    throw new IllegalArgumentException("Not supported file type");
            }

            for (int i = 0; i < frequencies.length; i++) {
                fil.writeInt(frequencies[i]);
            }


            ArrayList<Boolean> allBits = new ArrayList<>();
            int count = 0;
            for (int i = 0; i < arrayOfOriginalBytes.length*8; i++) {
                if (getBit(arrayOfOriginalBytes,i) == 1){
                    allBits.add(true);
                }
                else allBits.add(false);
            }

            String traversal="";
            ArrayList<Boolean> compressedBits = new ArrayList<>();
            getEncoding(top,traversal);
            for (int i = 0; i < arrayOfOriginalBytes.length; i++) {

                System.out.println(arrayOfOriginalBytes[i]);

                traversal = traversalArray[arrayOfOriginalBytes[i]];

                for (int j = 0; j < traversal.length(); j++) {
                    if (traversal.charAt(j)=='1'){
                        compressedBits.add(true);
                    }else compressedBits.add(false);
                }
            }

            int currentByte = 0;
            int j = 0;
            int i = 0;
            ArrayList<Byte> outBytes = new ArrayList<>();
            while (j < compressedBits.size()) {
                if (compressedBits.get(j) == false)
                    currentByte = (currentByte << 1);
                else
                    currentByte = ((currentByte << 1)+1); // times 2 + 1

                ++j;
                ++i;

                if (i == 8) {
                    outBytes.add((byte) (currentByte));
                    i = 0;
                    currentByte = 0;
                }
            }

            lastByte = 8-i;
            if (i==0) lastByte = 0;
            fil.write(lastByte);
            if (currentByte !=0){
                outBytes.add((byte)currentByte);
            }
            for (Byte s : outBytes) {
                fil.writeByte(s);
            }

            System.out.println("H");

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (fil != null){
                fil.close();
            }
        }
    }

    public int getBit(byte[] b, int i){
        int posByte = i/8;
        int posBit = i%8;
        byte valByte = b[posByte];
        int valInt = valByte>>(8-(posBit+1)) & 0x0001;
        return valInt;
    }

    public void countFrequencies(HuffmanNode[] byteArray) {

        for (int i = 0; i < byteArray.length; i++) {
            byte b = byteArray[i].data;
            byteArray[i].data = b;
            for (int j = 0; j < (byteArray.length); j++) {
                if (b == byteArray[j].data) {
                    byteArray[i].frequence += 1;
                    frequencies[byteArray[i].data]+=1;
                }
            }
        }
    }
}

class BitOutput{
    private OutputStream out;
    boolean[] buffer = new boolean[8];
    int count = 0;

    public BitOutput (OutputStream out){
        this.out=out;
    }

    public void write(boolean bool) throws IOException {

        buffer[this.count] = bool;
        count++;
        if(count==8){
            int n = 0;
            for (int i = 0; i < 8; i++) {
                n = 2*n+(this.buffer[i]? 1:0);
            }
            out.write(n);
            count = 0;
        }
    }
}


class HuffmanNode implements Serializable {
    byte data;
    int frequence;

    HuffmanNode left;
    HuffmanNode right;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HuffmanNode that = (HuffmanNode) o;
        return data == that.data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}

class HuffmanComparator implements Comparator<HuffmanNode> {
    public int compare(HuffmanNode i, HuffmanNode j){
        return i.frequence - j.frequence;
    }
}

class HuffmanDataComparator implements Comparator<HuffmanNode>{
    public int compare(HuffmanNode i, HuffmanNode j){
        return i.data - j.data;
    }
}