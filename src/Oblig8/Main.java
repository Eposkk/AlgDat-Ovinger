package Oblig8;

import java.io.IOException;
public class Main {

    public static void main(String[] args){
        LempelZiv lz = new LempelZiv();
        Huffman hf = new Huffman();

        if (args[0].equals("e")) {
            try {
                hf.compress(lz.compressFile(args[1]), args[2]);
                System.out.println("***Fil komprimert***");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if (args[0].equals("d")) {
            try {
                lz.deCompressFile(hf.decode(args[1]), args[2]);
                System.out.println("***Fil utpakket***");
            } catch (IOException e) {
                System.out.println("error");
                e.printStackTrace();
            }
        }
    }
}

