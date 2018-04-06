import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * LZW Decompression decompresses a LZW file back to a text file.
 * <p>
 * Author: Bryce Sulin 4/8/18
 */
public class LZWDecompression {

    public static void main(String[] args) throws IOException {

        Scanner keyboard = new Scanner(System.in);
        int code;
        int dictionarySize = 256;
        String newString, str, inputFileDecoder;
        String outputFile = "";
        int counter = 0;

        String binary = "";

        System.out.println("Enter .lzw input file name: ");
        inputFileDecoder = keyboard.nextLine();

        if (inputFileDecoder.lastIndexOf(".") > 0)
            outputFile = inputFileDecoder.substring(0, inputFileDecoder.lastIndexOf("."));

        // Create dictionary
        Map<Integer, String> dictionary = new HashMap<Integer, String>();

        for (int i = 0; i < 256; i++) {
            dictionary.put(i, "" + (char) i);
        }

        OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(outputFile));

        // Read contents of .lzw file
        File file = new File(inputFileDecoder);
        FileInputStream fis = new FileInputStream(file);

        byte[] bytes = new byte[(int) file.length()];
        fis.read(bytes, 0, (int) file.length());
        int[] intArray = new int[bytes.length];

        for (int i = 0; i < bytes.length; i++) {
            intArray[i] = bytes[i];
        }

        for (int i = 0; i < 2; i++) {
            binary = binary + convertToBinary(intArray[i]);
        }

        int in = convertToInteger(binary);
        str = dictionary.get(in);
        writer.write(str);

        binary = "";

        // Convert integer array to binary string
        int i = 2;
        while (i < intArray.length) {
            binary = binary + convertToBinary(intArray[i]);
            counter++;
            if (counter == 2) {
                code = convertToInteger(binary);
                if (!dictionary.containsKey(code)) {
                    newString = str + str.substring(0, 1);
                } else {
                    newString = dictionary.get(code);
                }
                writer.write(newString);
                if (dictionary.size() < dictionarySize) {
                    dictionary.put(dictionary.size(), str + newString.substring(0, 1));
                }
                str = newString;
                binary = "";
                counter = 0;
            }
            i += 1;
        }
        writer.close();
        fis.close();
    }

    // Converts binary string to integer
    private static int convertToInteger(String binary) {
        int integer = 0, value, power = 15;
        for (char c : binary.toCharArray()) {
            value = Integer.parseInt("" + c);
            integer += value * Math.pow(2.0, power);
            power -= 1;
        }
        return integer;
    }

    // Converts integer to its binary value
    private static String convertToBinary(int value) {
        String finalValue = "";
        finalValue = Integer.toBinaryString(value);
        int length = finalValue.length();
        for (int i = 0; i < (8 - length); i++) {
            finalValue = "0" + finalValue;
        }
        return finalValue;
    }
}