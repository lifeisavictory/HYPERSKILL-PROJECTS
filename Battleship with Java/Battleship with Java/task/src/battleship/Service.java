package battleship;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Service {


    static Scanner sc = new Scanner(System.in);
    static Set<String> zakazanePozice = new HashSet<>(); //vedle jiz stoji jina lod
    static Set<String> obsazenePozice = new HashSet<>(); //na techto pozicich stoji lod
    static Set<String> hitnutePozice = new HashSet<>(); //zasahnute pozice
    static Set<String> minutePozice = new HashSet<>(); //minul jsem

    /**
     * UTOK NA JEDNO POLICKO V MATICI
     *
     * @param matrix
     * @param attackMatrix
     * @return
     */
    public static String[][] zautoc(String[][] matrix, String[][] attackMatrix) {

        String[] input;
        boolean isInputValid = false;
        char vystrelChar = 'A';
        int vystrelInt = 0;
        char vystrel = '\0';

        do {
            input = sc.nextLine().trim().split(" ");
            //Rozklad UTOKU na PISMENO a CISLO
            String prvniBunka = input[0];
            vystrelChar = prvniBunka.charAt(0);
            vystrelInt = Integer.parseInt(prvniBunka.substring(1));

            //VALIDACE SOURADNIC VLOZENYCH UZIVATELEM PODLE VELIKOSTI HERNI MATICE
            if (vystrelChar < 'A' || vystrelChar > 'J' || vystrelInt < 1 || vystrelInt > 10 || input.length > 1) {
                System.out.println("Error! You entered wrong coordinates! Try again:");
                continue;
            }
            //POKUD JSI MINUL
            if (!obsazenePozice.contains("" + vystrelChar + vystrelInt)) {
                vystrel = 'M';
                setVystrelDoMatice(attackMatrix, vystrel, vystrelChar, vystrelInt);
                setVystrelDoMatice(matrix, vystrel, vystrelChar, vystrelInt);
                Service.printGameField(attackMatrix); //nastaveni "M" do matice vystrelu
                System.out.println("You missed!");
                Service.printGameField(matrix);
                minutePozice.add("" + vystrelChar + vystrelInt); //pridam do pozic ktere jsem minul
                isInputValid = true;
            }
            //POKUD JSI HITNUL
            if (obsazenePozice.contains("" + vystrelChar + vystrelInt)) {
                vystrel = 'X';
                setVystrelDoMatice(attackMatrix, vystrel, vystrelChar, vystrelInt); //nastaveni X do matice vystrelu
                setVystrelDoMatice(matrix, vystrel, vystrelChar, vystrelInt);  //nastaveni X do matice zasahu
                Service.printGameField(attackMatrix);
                System.out.println("You hit a ship!");
                Service.printGameField(matrix);
                obsazenePozice.remove("" + vystrelChar + vystrelInt); //odstranim z obsazene pozice
                hitnutePozice.add("" + vystrelChar + vystrelInt); //pridam do pozic, ktere jsem hitnul
                isInputValid = true;
            }

        } while (!isInputValid);
        return matrix;
    }


    /**
     * NASTAVENI VYSTRELU DO HERNI MATICE - MATRIX
     *
     * @param matrix
     * @param vystrel
     * @param vystrelChar
     * @param vystrelInt
     * @return
     */
    public static String[][] setVystrelDoMatice(String[][] matrix, char vystrel, char vystrelChar, int vystrelInt) {
        matrix[vystrelChar - 'A'][vystrelInt - 1] = Character.toString(vystrel);
        return matrix;
    }


    /**
     * ZADANI POLOHY LODI V HERNI MATICI - pro gameMatrix
     *
     * @param matrix
     * @return
     */
    public static String[][] zadejPolohuLodi(String[][] matrix, String ship, int sizeOfShip) {

        String[] input;
        boolean isInputValid = false;
        int vypoctenaDelkaLodi = 0;
        char startChar = 'A';
        int startInt = 0;
        char endChar = 'A';
        int endInt = 0;

        do {
            input = sc.nextLine().trim().split(" ");
            //PRVNI BUNKA - rozklad na PISMENO a CISLO
            String prvniBunka = input[0];
            startChar = prvniBunka.charAt(0);
            startInt = Integer.parseInt(prvniBunka.substring(1));
            //DRUHA BUNKA - rozklad na PISMENO a CISLO
            String druhaBunka = input[1];
            endChar = druhaBunka.charAt(0);
            endInt = Integer.parseInt(druhaBunka.substring(1));
            //PREHOZENI CISEL V TABULCE POKUD JE PRVNI MENSI - D5 A5
            if (startInt > endInt) {
                int preklopeni = startInt;
                startInt = endInt;
                endInt = preklopeni;
            }
            if (startChar > endChar) {
                char preklopeni = startChar;
                startChar = endChar;
                endChar = preklopeni;
            }
            //VYPOCET DELKY LODI A VALIDACE VZHLEDEM K PRIJATEMU DRUHU LODI [BATTLESHIP = 5, SUBMARINE = 2] atd
            //musim ji umet spocitat pro horizont i vertikal - do lodi se pocita i posledni pozice (nelze jen tupe odcitat)
            vypoctenaDelkaLodi = (startChar == endChar)
                    ? (Math.abs(startInt - endInt) + 1)
                    : (Math.abs(startChar - endChar) + 1);
            if (!isValidDelkaLodi(vypoctenaDelkaLodi, ship, sizeOfShip)) {
                continue;
            }
            ;
            //VALIDACE SOURADNIC VLOZENYCH UZIVATELEM PODLE VELIKOSTI HERNI MATICE
            if (!isZadaneValidniSouradnice(startChar, startInt, endChar, endInt)) {
                continue;
            }
            //VALIDACE ZDA NOVA LOD NENI V ZAKAZANEM POLI
            if (!nedotykaSeJineLodi(startChar, endChar, startInt, endInt)) {
                continue;
            }
            //NASTAVENI OBSAZENYCH POLI PRO DALSI ZADANI
            setZakazanePoziceProUmisteniDalsiLodi(startChar, endChar, startInt, vypoctenaDelkaLodi);

            isInputValid = true;
        } while (!isInputValid);
        return setLodDoMatice(matrix, vypoctenaDelkaLodi, startChar, startInt, endChar, endInt);
    }


    /**
     * NASTAVENI ZAKAZANYCH POZIC PRO UMISTENI DALSI LODI
     * HASHSET - RYCHLY BEZ DUPLICIT
     * OBSAZENE POZICE SE VLOZI DO HASHSET PRO POROVNANI PRI ZADANI NOVE LODI
     * NOVA LOD SE NESMI DOTYKAT STAVAJICI (VYMEZUJEME VETSI OBSAZENOST, NEZ SKUTECNE JE - TO JE MUJ NAPAD :)
     * 0 - je lod,  X jsou pole kde nesmi nic stat
     * XXXXXXX
     * X00000X
     * XXXXXXX
     *
     * @param startChar
     * @param endChar
     * @param startInt
     * @param vypoctenaDelkaLodi
     */
    public static void setZakazanePoziceProUmisteniDalsiLodi(char startChar, char endChar, int startInt, int vypoctenaDelkaLodi) {
        char newStartChar = (char) (startChar - 1);
        int newStartInt = startInt - 1;
        if (startChar == endChar) {
            for (char radek = (char) (startChar - 1); radek <= (char) (startChar + 1); radek++) {
                for (int sloupec = newStartInt; sloupec <= startInt + vypoctenaDelkaLodi; sloupec++) {
                    zakazanePozice.add("" + radek + sloupec);
                }
            }
        } else {
            for (int sloupec = startInt - 1; sloupec <= startInt + 1; sloupec++) {
                for (char radek = newStartChar; radek <= (char) (startChar + vypoctenaDelkaLodi); radek++) {
                    zakazanePozice.add("" + radek + sloupec);
                }
            }
        }
    }

    /**
     * OVERENI ZE SE NOVA LOD NEDOTYKAT STAVAJICI LODI
     *
     * @param startChar
     * @param endChar
     * @param startInt
     * @param endInt
     * @return
     */
    public static boolean nedotykaSeJineLodi(char startChar, char endChar, int startInt, int endInt) {
        // TODO tohel je pekny gulas, jeste si to znovu projdi
        if (startChar == endChar) {
            for (int i = startInt; i <= endInt; i++) {
                if (zakazanePozice.contains("" + startChar + i)) {
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    return false;
                }
            }
        } else {
            for (char i = startChar; i <= endChar; i++) {
                if (zakazanePozice.contains("" + i + startInt)) {
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * VALIDACE DELKY LODI
     *
     * @param vypoctenaDelkaLodi
     * @param ship
     * @param defaultSizeOfShip
     * @return
     */
    public static boolean isValidDelkaLodi(int vypoctenaDelkaLodi, String ship, int defaultSizeOfShip) {
        if (vypoctenaDelkaLodi != defaultSizeOfShip) {
            System.out.println("Error! Wrong length of the " + ship + "! Try again:");
            return false;
        }
        return true;
    }

    /**
     * VLOZENIO LODI DO HERNIHO POLE - pro gameMatrix
     *
     * @param matrix
     * @param delkaLodi
     * @param startChar
     * @param startInt
     * @param endChar
     * @param endInt
     * @return
     */
    public static String[][] setLodDoMatice(String[][] matrix, int delkaLodi, char startChar, int startInt, char endChar, int endInt) {
        //ZADANI LODI DO POLE
        if (startChar == endChar) {
            //lod je v radku
            for (int i = 0; i < delkaLodi; i++) {
                //aby to bylo univerzalni pro radek i sloupec
                matrix[startChar - 'A'][startInt - 1 + i] = "O";
                obsazenePozice.add("" + startChar + (startInt + i));
            }
        } else {
            // lod je ve sloupci
            for (int i = 0; i < delkaLodi; i++) {
                matrix[startChar - 'A' + i][startInt - 1] = "O";
                obsazenePozice.add("" + (char) (startChar + i) + startInt);
            }
        }
        return matrix;
    }


    /**
     * VALIDACE SOURADNIC VLOZENE LODE
     *
     * @param startChar
     * @param startInt
     * @param endChar
     * @param endInt
     * @return
     */
    public static boolean isZadaneValidniSouradnice(char startChar, int startInt, char endChar, int endInt) {
        //VALIDACE - POKUD NEJSOU PISMENA V RADKU NEBO SLOUPCI, NEPOKRACUJE SE
        if (startChar != endChar && startInt != endInt) {
            System.out.println("Error!");
            return false;
        } else if (startInt == 0) {
            System.out.println("Error! Wrong ship location! Try again:");
            return false;
        } else if (startInt < 0 || endInt > 10) {
            System.out.println("Error! Wrong ship location! Try again:");
            return false;
        } else if (startChar < 'A' || startChar > 'J') {
            System.out.println("Error! Wrong ship location! Try again:");
            return false;
        } else if (endChar < 'A' || endChar > 'J') {
            System.out.println("Error! Wrong ship location! Try again:");
            return false;
        }
        return true;
    }


    /**
     * VYPIS MATICE
     *
     * @param matrix
     */
    public static void printGameField(String[][] matrix) {
        String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

        System.out.print(" ");
        for (int i = 1; i <= matrix.length; i++) {
            System.out.print(" " + numbers[i - 1]);
        }
        System.out.println();
        for (int i = 0; i < matrix.length; i++) {
            System.out.print(letters[i]);
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(" " + matrix[i][j]);
            }
            System.out.println();
        }
    }

    /**
     * NAPLNENI MATICE DEFAULTNIMI ZNAKY
     *
     * @param matrix
     * @return
     */
    public static void naplnMatici(String[][] matrix) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                matrix[i][j] = "~";
            }
        }
    }


}