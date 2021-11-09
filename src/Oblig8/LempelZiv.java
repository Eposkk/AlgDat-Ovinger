package Oblig8;

import java.io.*;
import java.io.DataOutputStream;
import java.util.Arrays;

public class LempelZiv {

    private final int BUFFERSIZE = 127; //127 is the largest number one byte can hold
    private final int POINTERSIZE = 16;
    private final int MIN_POINTERSIZE = 3;//Smallest size for gaining on the compression

    private FileInputStream inputStream;
    private DataOutputStream dataOutputStream;

    /**
     *
     * @param filePath
     * @throws IOException
     */
    public ByteArrayOutputStream compressFile(String filePath) throws IOException {

        ByteArrayOutputStream compressesBytes= new ByteArrayOutputStream();

        inputStream = new FileInputStream(filePath); //Getting the file as a bit stream
        byte[] data = inputStream.readAllBytes();
        inputStream.close();

        ByteArrayOutputStream notCompressed = new ByteArrayOutputStream();

        int index = 0;

        while(index<data.length) {

            int max = index + POINTERSIZE; //Maximum index in the search word
            if (max > data.length - 1) max = data.length - 1;

            int min = index - BUFFERSIZE; //Minimum index of the sliding window
            if (min < 0) min = 0;

            byte[] buffer = Arrays.copyOfRange(data, min, index);

            int i = index + MIN_POINTERSIZE - 1;

            Pointer pointer = null;

            outer:
            while (i <= max) {
                byte[] sequence = Arrays.copyOfRange(data, index, i+1);
                int j = 0;
                while (sequence.length + j <= buffer.length) {
                    int k = sequence.length-1;
                    while (k >= 0 && sequence[k] == buffer[j+k]) {
                        k--;
                    }
                    if(k < 0){ //All characters in the search word matched the search buffer
                        pointer = new Pointer();
                        pointer.setOffset(buffer.length - j);
                        pointer.setLength(sequence.length);
                        i++;
                        continue outer;
                    }else {
                        int l = k-1; //Find last index of failed character from buffer in the search word if any
                        while(l >= 0 && (sequence[l] != buffer[j+k])) {
                            l--;
                        }
                        j += k-l; //Slide according to Boyer Moore
                    }
                }
                break;
            }

            if (pointer != null ){

                if (notCompressed.size() != 0) { //Write stored incompressible variables if any
                    writeNotCompressedBytes(compressesBytes, notCompressed);
                    notCompressed = new ByteArrayOutputStream();
                }

                compressesBytes.write(pointer.getOffset() * -1);
                compressesBytes.write(pointer.getLength());
                index += pointer.length;

            }else{//if no pointer
                notCompressed.write(data[index]);
                if (notCompressed.size() == 127) {
                    writeNotCompressedBytes(compressesBytes, notCompressed);
                    notCompressed = new ByteArrayOutputStream();
                }
                index+= 1;
            }
        }
        if (notCompressed.size() != 0) { //Remaining bytes
            writeNotCompressedBytes(compressesBytes, notCompressed);
        }
        compressesBytes.close();

        return compressesBytes;
    }


    private void writeNotCompressedBytes(ByteArrayOutputStream dataOutputStream, ByteArrayOutputStream notCompressed) throws IOException {
        dataOutputStream.write(notCompressed.size());

        for(byte b : notCompressed.toByteArray()) {
            dataOutputStream.write(b);
        }
        notCompressed.close();
    }

    /**

     * @throws IOException
     */
    public void deCompressFile(byte[] data, String outPutFileName) throws  IOException{

        ByteArrayOutputStream reWritten = new ByteArrayOutputStream();
        dataOutputStream = new DataOutputStream(new FileOutputStream(outPutFileName)); //Output file

        int i = 0;
        while(i<data.length-1){

            if(data[i] > 0){//the following data is uncompressed

                for(int l = 1; l <= data[i]; l++){
                    reWritten.write(data[i+l]);
                }
                i+= data[i]+1;
            }else{

                int offset = data[i];
                int length = data[i+1];

                if(length<0){
                    i=data.length;
                }

                byte[] w = reWritten.toByteArray();
                for(int l = 0; l<length; l++){
                    reWritten.write(w[(w.length+offset)+l]);
                }
                i+= 2;
            }
        }
        for(byte b: reWritten.toByteArray()){
            dataOutputStream.write(b);
        }
        dataOutputStream.close();
    }


    private class Pointer{
        private int length;
        private int offset;

        public Pointer() {
            this(-1, -1);
        }

        public Pointer(int length, int offset){
            super();
            this.length = length;
            this.offset = offset;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }
    }
}
