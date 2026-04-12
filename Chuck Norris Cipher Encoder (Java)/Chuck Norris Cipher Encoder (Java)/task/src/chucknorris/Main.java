package chucknorris;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //TODO refactoring

        do {
            System.out.println("Please input operation (encode/decode/exit):");
            String operation = sc.nextLine();
            if (operation.equals("exit")) {
                System.out.println("Bye!");
                break;
            }
            //zakodovani musi byt Unicode
            if (operation.equals("encode")) {
                System.out.println("Input string:");
                String input = sc.nextLine();
                System.out.println("Encoded string:");
                System.out.println(Service.encodeBinaryToChuckNorris(Service.encodeUnicodeToBinary(input)));
                System.out.println();
            }
            //dekodovani musi byt Chuck Norris Code
            if (operation.equals("decode")) {
                System.out.println("Input encoded string:");
                String input = sc.nextLine();
                if (Service.isValidChuckNorrisCode(input)) {
                    System.out.println("Decoded string:");
                    System.out.println(Service.decodeBinaryToUnicode(Service.decodeChuckNorrisToBinary(input)));
                    System.out.println();
                }
            }
            if (!operation.equals("encode") && !operation.equals("decode") && !operation.equals("exit")){
                System.out.println("There is no '" + operation + "' operation");
            }
        } while (true);



        sc.close();
    }
}


class Service {

    /**
     * Validace vstupu Chuck Norris Code
     * @param input
     * @return
     */
    public static boolean isValidChuckNorrisCode(String input) {
        String[] blocks = input.split(" ");
        //chyba pokud je pocet v poli lichy
        if (blocks.length % 2 != 0) {
            System.out.println("Encoded string is not valid.");
            return false;
        }
        //chyba pokud jsou v input jine znaky nez 0
        if (!input.matches("[0 ]+")){
            System.out.println("Encoded string is not valid.");
            return false;
        }
        //chyba pokud jsou v poli jinak nez 0 a 00 (kazdy sudy index)
        for (int i = 0; i < blocks.length; i += 2) {
            if (!blocks[i].equals("0") && !blocks[i].equals("00")) {
                System.out.println("Encoded string is not valid.");
                return false;
            }
        }
        //Pokud dekoduju Z Chuck Norris Code na Binary, musi byt vysledek % 7 == 0
        if ( (decodeChuckNorrisToBinary(input).length()) % 7 != 0) {
            System.out.println("Encoded string is not valid.");
            return false;
        }
        return true;
    }


    /**
     * Prevod z binary code na Unicode
     * @param input
     * @return
     */
    public static String decodeBinaryToUnicode(String input) {
        String output = "";
        for (int i = 0; i < input.length(); i += 7) {
            String binaryCode = input.substring(i, i + 7);
            int decimalCode = Integer.parseInt(binaryCode, 2);
            output += (char) decimalCode;
        }
        return output;
    }

    /**
     * Prevod Chuck Norris Code na binarni
     * @param input
     * @return
     */
    public static String decodeChuckNorrisToBinary(String input) {
        String[] chuckNorrisDigits = input.split(" ");
        StringBuilder finalBinaryCode = new StringBuilder();
        int count = 1; //bude rozhodovat o pozici bunky (pozici v poli)

        if (chuckNorrisDigits[0].equals("0")) { //pokud je na prvni pozici 1
            //index 1 - zmenim na 1 / index 3 - zustanou nuly / index 5 - zmena na jednicky
            count = 1; //na tyhle pozici zaciname
            for (int i = 1; i < chuckNorrisDigits.length; i = i + 2) {
                if (count % 2 == 1) {
                    finalBinaryCode.append(chuckNorrisDigits[i].replace("0", "1"));
                    count++;
                } else if (count % 2 == 0) {
                    finalBinaryCode.append(chuckNorrisDigits[i]);
                    count++;
                }
            }
        } else if (chuckNorrisDigits[0].equals("00")) {//pokud je na prvni pozici 0
            for (int i = 1; i < chuckNorrisDigits.length; i = i + 2) {
                if (count % 2 == 1) {
                    finalBinaryCode.append(chuckNorrisDigits[i]);
                    count++;
                } else if (count % 2 == 0) {
                    finalBinaryCode.append(chuckNorrisDigits[i].replace("0", "1"));
                    count++;
                }
            }
        }
        return finalBinaryCode.toString();
    }


    /**
     * Prevod na binarni format
     * @param input
     * @return
     */
    public static String encodeUnicodeToBinary(String input) {
        String[] inputToBinaryDigits = new String[input.length()];
        for (int i = 0; i < input.length(); i++) {
            String binaryNumber = Integer.toBinaryString(input.charAt(i)); //prevod na binarni format
            inputToBinaryDigits[i] = String.format("%7s", binaryNumber).replaceAll(" ", "0"); //doplneni na 7bitu (7 mist) pokud ma prevod jen 6
        }
        //Prevod na jeden dlouhy String - to chce zadani
        String binaryString = "";
        for (int i = 0; i < inputToBinaryDigits.length; i++) {
            binaryString = binaryString.concat(inputToBinaryDigits[i]);
        }
        return binaryString;
    }


    /**
     * Prevod binarniho kodu na CHUCK NORRIS CODE
     * @param input
     * @return
     */
    public static String encodeBinaryToChuckNorris(String input) {
        StringBuilder resultChuckNorris = new StringBuilder();
        char digit;
        char lastDigit = '\0';
        String typeOfDigit = "";
        int countOfDigits = 0;

        for (int j = 0; j < input.length(); j++) {
            digit = input.charAt(j);
            //rozhodnuti pro prvni znak
            if (j == 0) {
                lastDigit = digit;
                typeOfDigit = (digit == '0') ? "00" : "0";
                countOfDigits = 1;
            }
            //rozhodnuti pokud se posledni a novy znak lisi (musi probehnout zapis)
            if (lastDigit != digit) {
                if (resultChuckNorris.length() > 0) {
                    resultChuckNorris.append(" ");
                }
                resultChuckNorris.append(typeOfDigit).append(" ").repeat("0", countOfDigits);
                countOfDigits = 0;//nulovani poctu
                lastDigit = digit;//nastaveni posledniho znaku
                typeOfDigit = (digit == '0') ? "00" : "0";//nastaveni noveho typu
                countOfDigits++;
                continue;
            }
            //rozhodnuti pro jakykoliv dalsi znak
            if (j > 0 && lastDigit == digit) {
                countOfDigits++;
                continue;
            }
            if (j > 0 && lastDigit != digit) {
                //vzit typ a pocet a pridat do do StringBuilder chuckNorrisDigits
                if (resultChuckNorris.length() > 0) {
                    resultChuckNorris.append(" ");
                }
                resultChuckNorris.append(typeOfDigit).append(" ").repeat("0", countOfDigits);
                typeOfDigit = "";//nulovani typu
                countOfDigits = 0;//nulovani postu
                continue;
            }
        }
        //konec for cyklu
        if (resultChuckNorris.length() > 0) {
            resultChuckNorris.append(" ");
        }
        resultChuckNorris.append(typeOfDigit).append(" ").repeat("0", countOfDigits);
        return resultChuckNorris.toString();
    }


}