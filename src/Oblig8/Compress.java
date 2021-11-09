package Oblig8;

import java.io.IOException;

public class Compress {

    public static void main(String[] args){
        LempelZiv lz = new LempelZiv();
        Huffman hf = new Huffman();

        if(args[0].equals("help")){
            System.out.println("*** HELP ***");
            System.out.println("1. Compress file - command: 'c'" +
                    "\n java Main e inputfile outputfile");
            System.out.println("\n");
            System.out.println("2. Decompress file - command: 'd'" +
                    "\n java Main d inputfile outputfile");
        }else if (args[0].equals("c")) {
            try {
                hf.compress(lz.compressFile(args[1]), args[2]);
                System.out.println("***File compressed***");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (args[0].equals("d")) {
            try {
                lz.deCompressFile(hf.decode(args[1]), args[2]);
                System.out.println("***File decompressed***");
            } catch (IOException e) {
                System.out.println("error");
                e.printStackTrace();
            }
        }else{
            System.out.println("Unkown command, write 'help' to see commands");
        }
    }
}
