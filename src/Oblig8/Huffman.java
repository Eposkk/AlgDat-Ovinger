package Oblig8;

import java.io.*;
import java.util.*;

class Huffman {

    HuffmanNode[] byteArray;
    String[] traversalArray = new String[256];
    byte[] arrayOfOriginalBytes;
    int[] frequencies = new int[256];
    HuffmanNode writeTop;

    public void compress(ByteArrayOutputStream inputBytes, String a) throws IOException {

        arrayOfOriginalBytes = inputBytes.toByteArray() ;

        ArrayList<HuffmanNode> inbetweenArray = new ArrayList<>();

        for (int i = 0;i<arrayOfOriginalBytes.length;i++){
            var h = new HuffmanNode();
            h.data= (arrayOfOriginalBytes[i]);
            if (!inbetweenArray.contains(h)){
                h = new HuffmanNode(); h.data=arrayOfOriginalBytes[i];
                inbetweenArray.add(h);
            }
        }
        byteArray = new HuffmanNode[inbetweenArray.size()];
        for (int i = 0; i < inbetweenArray.size(); i++) {
            byteArray[i]=inbetweenArray.get(i);
        }
        encode(a);
    }

    public void encode(String file) throws IOException {
        HuffmanNode top = createHuffmanTree(byteArray);
        writeTop = top;
        DataOutputStream fil = null;

        int lastByte = 0;

        try{
            fil = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            for (int i = 0; i < frequencies.length; i++) {
                fil.writeInt(frequencies[i]);
            }

            ArrayList<Boolean> allBits = new ArrayList<>();
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
                traversal = traversalArray[arrayOfOriginalBytes[i]+128];

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


        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (fil != null){
                fil.close();
            }
        }
    }

    public byte[] decode(String file) throws IOException {
        int[] readFrequencies = new int[256];
        var fil = new DataInputStream((new FileInputStream(file)));

        for (int i = 0; i < 256; i++) {
            int freq = fil.readInt();
            readFrequencies[i]=freq;
        }
        int lastByte = fil.readByte();

        ArrayList<Byte> out = new ArrayList<>();

        byte[] readBytes = fil.readAllBytes();
        fil.close();

        int  numberOfFrequencies = 0;
        for (int i = 0; i < readFrequencies.length; i++) {
            if (readFrequencies[i]!=0){
                numberOfFrequencies+=1;
            }
        }
        HuffmanNode[] readNodes = new HuffmanNode[numberOfFrequencies];
        int j=0;
        for (int i = 0; i < readFrequencies.length; i++) {
            if (readFrequencies[i]!=0){
                var node = new HuffmanNode();
                node.data = (byte)(i+128);
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
        Arrays.sort(readNodes, new HuffmanDataComparator());
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
        byte[] writer = new byte[out.size()];

        for (int i = 0; i<out.size(); i++) {
            writer[i] = out.get(i);
        }

        return writer;
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
            traversalArray[top.data+128]= traversal;
            return traversal;
        }
        getEncoding(top.left,  traversal+"0");
        getEncoding(top.right, traversal+"1");

        return null;
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
                    frequencies[byteArray[i].data+128]+=1;
                }
            }
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