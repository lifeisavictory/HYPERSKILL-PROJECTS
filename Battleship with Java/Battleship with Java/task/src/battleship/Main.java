package battleship;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class Main {


    //TODO - prejmenovat promenne a metody na anglicke nazvy !!!


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Service service = new Service();
        //GAMER, KTERY VLASTNI LODE A HERNI MATICE
        Gamer gamer = new Gamer("Tomas");

        /**
         * ZACATEK HERNIHO CYKLU
         */
        service.naplnMatici(gamer.getGameMatrix());
        service.naplnMatici(gamer.getAttackMatrix());
        service.printGameField(gamer.getGameMatrix());

        /**
         * Pridani 1. lodi - Aircraft Carrier
         */
        System.out.println("Enter the coordinates of the Aircraft Carrier (5 cells):");
        service.zadejPoziciLodi(gamer.getGameMatrix(), gamer.getAircraftCarrier());
        service.printGameField(gamer.getGameMatrix());
        /**
         * Pridani 2. lodi - Battleship
         */
        System.out.println();
        System.out.println("Enter the coordinates of the Battleship (4 cells):");
        service.zadejPoziciLodi(gamer.getGameMatrix(), gamer.getBattleship());
        service.printGameField(gamer.getGameMatrix());
        /**
         * Pridani 3. lodi - Submarine
         */
        System.out.println();
        System.out.println("Enter the coordinates of the Submarine (3 cells):");
        service.zadejPoziciLodi(gamer.getGameMatrix(), gamer.getSubmarine());
        service.printGameField(gamer.getGameMatrix());
        /**
         * Pridani 4. lodi - Cruiser
         */
        System.out.println();
        System.out.println("Enter the coordinates of the Cruiser (3 cells):");
        service.zadejPoziciLodi(gamer.getGameMatrix(), gamer.getCruiser());
        service.printGameField(gamer.getGameMatrix());
        /**
         * Pridani 5. lodi - Destroyer
         */
        System.out.println();
        System.out.println("Enter the coordinates of the Destroyer (2 cells):");
        service.zadejPoziciLodi(gamer.getGameMatrix(), gamer.getDestroyer());
        service.printGameField(gamer.getGameMatrix());

        /**
         * START HRY - TASK 3
         */
        System.out.println("The game starts!");
        service.naplnMatici(gamer.getAttackMatrix());
        service.printGameField(gamer.getAttackMatrix());
        System.out.println("Take a shot!");
        service.zautoc(gamer.getGameMatrix(), gamer.getAttackMatrix(), gamer);



    }
}


class Service {

    private Scanner sc = new Scanner(System.in);
    private Set<String> obsazenePozice = new HashSet<>();
    private Set<String> zakazanePozice = new HashSet<>(); //vedle jiz stoji jina lod
    private Set<String> hitnutePozice = new HashSet<>(); //zasahnute pozice
    private Set<String> minutePozice = new HashSet<>(); //minul jsem

    public Service() {}

    /**
     * UTOK NA JEDNO POLICKO V MATICI
     *
     * @param matrix
     * @param attackMatrix
     */
    public void zautoc(String[][] matrix, String[][] attackMatrix, Gamer gamer) {

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
            //PROJDU LODE A PODIVAM SE KTERA JE ZASAZENA
            //pokud je v obsazenePozice, ale neni u zadne lodi, uz se na policko strilelo
            Ship zasahnutaLod = null;
            if (obsazenePozice.contains("" + vystrelChar + vystrelInt)) {
                //najdeme lod, kterou jsme zasahli
                if (gamer.getAircraftCarrier().getPositions().contains("" + vystrelChar + vystrelInt)) {
                    zasahnutaLod = gamer.getAircraftCarrier();
                } else if (gamer.getBattleship().getPositions().contains("" + vystrelChar + vystrelInt)) {
                    zasahnutaLod = gamer.getBattleship();
                } else if (gamer.getSubmarine().getPositions().contains("" + vystrelChar + vystrelInt)) {
                    zasahnutaLod = gamer.getSubmarine();
                } else if (gamer.getCruiser().getPositions().contains("" + vystrelChar + vystrelInt)) {
                    zasahnutaLod = gamer.getCruiser();
                } else if (gamer.getDestroyer().getPositions().contains("" + vystrelChar + vystrelInt)) {
                    zasahnutaLod = gamer.getDestroyer();
                }
            }
            //NASLEDUJE POKUD JSI MINUL - NIKDY TAM LOD NEBYLA
            if (!obsazenePozice.contains("" + vystrelChar + vystrelInt)) {
                vystrel = 'M';
                setVystrelDoMatice(attackMatrix, vystrel, vystrelChar, vystrelInt);
                setVystrelDoMatice(matrix, vystrel, vystrelChar, vystrelInt); //nastaveni "M" do matice vystrelu
                minutePozice.add("" + vystrelChar + vystrelInt); //pridam do pozic ktere jsem minul
                printGameField(attackMatrix);
                System.out.println("You missed. Try again:");
            } else if (zasahnutaLod != null) {
                //HIT - LOD TAM JE A ZATIM NEBYLO ZASAZENO
                vystrel = 'X';
                setVystrelDoMatice(attackMatrix, vystrel, vystrelChar, vystrelInt); //nastaveni X do matice vystrelu
                setVystrelDoMatice(matrix, vystrel, vystrelChar, vystrelInt); //nastaveni X do matice zasahu
                zasahnutaLod.deletePosition("" + vystrelChar + vystrelInt); //odstraneni pozice z lode
                hitnutePozice.add("" + vystrelChar + vystrelInt); //pridam do pozic, ktere jsem hitnul
                printGameField(attackMatrix);
                if (gamer.getAircraftCarrier().getPositions().isEmpty() &&
                        gamer.getBattleship().getPositions().isEmpty() &&
                        gamer.getSubmarine().getPositions().isEmpty() &&
                        gamer.getCruiser().getPositions().isEmpty() &&
                        gamer.getDestroyer().getPositions().isEmpty()) {
                    System.out.println("You sank the last ship. You won. Congratulations!");
                    isInputValid = true;
                } else if (zasahnutaLod.getPositions().isEmpty()) {
                    System.out.println("You sank a ship! Specify a new target:");
                } else {
                    System.out.println("You hit a ship! Try again:");
                }
            } else {
                //HIT - LOD TAM JE POLICKO JIZ BYLO ZASAZENO
                vystrel = 'X';
                setVystrelDoMatice(attackMatrix, vystrel, vystrelChar, vystrelInt); //nastaveni X do matice vystrelu
                setVystrelDoMatice(matrix, vystrel, vystrelChar, vystrelInt);
                printGameField(attackMatrix);//nastaveni X do matice zasahu
                System.out.println("You hit a ship! Try again:");
            }
        } while (!isInputValid);
    }


    /**
     * NASTAVENI VYSTRELU DO HERNI MATICE - MATRIX
     *
     * @param matrix
     * @param vystrel
     * @param vystrelChar
     * @param vystrelInt
     */
    private void setVystrelDoMatice(String[][] matrix, char vystrel, char vystrelChar, int vystrelInt) {
        matrix[vystrelChar - 'A'][vystrelInt - 1] = Character.toString(vystrel);
    }


    /**
     * ZADANI POLOHY LODI V HERNI MATICI - pro gameMatrix
     *
     * @param matrix
     */
    public void zadejPoziciLodi(String[][] matrix, Ship ship) {

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
            if (!isValidDelkaLodi(vypoctenaDelkaLodi, ship.getName(), ship.getLength())) {
                continue;
            }
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
            //NASTAVENI SOURADNIC V SETU KONKRETNI LODI


            isInputValid = true;
        } while (!isInputValid);
        setLodDoMatice(matrix, ship,  vypoctenaDelkaLodi, startChar, startInt, endChar, endInt);
    }


    /**
     * VYPIS MATICE
     *
     * @param matrix
     */
    public void printGameField(String[][] matrix) {
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
     */
    public void naplnMatici(String[][] matrix) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                matrix[i][j] = "~";
            }
        }
    }


    /**
     *
     * ODDIL PRO PRIVATNI METODY
     *
     */


    /**
     * VALIDACE SOURADNIC VLOZENE LODE
     *
     * @param startChar
     * @param startInt
     * @param endChar
     * @param endInt
     * @return
     */
    private boolean isZadaneValidniSouradnice(char startChar, int startInt, char endChar, int endInt) {
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
     * VLOZENIO LODI DO HERNIHO POLE - pro gameMatrix
     * VLOZENI SOURADNIC KE KONKRETNI LODI
     * VLOZENI DO SETU OBSAZENE POZICE
     *
     * @param matrix
     * @param delkaLodi
     * @param startChar
     * @param startInt
     * @param endChar
     * @param endInt
     */
    private void setLodDoMatice(String[][] matrix, Ship ship, int delkaLodi, char startChar, int startInt, char endChar, int endInt) {
        //ZADANI LODI DO POLE
        if (startChar == endChar) {
            //lod je v radku
            for (int i = 0; i < delkaLodi; i++) {
                //aby to bylo univerzalni pro radek i sloupec
                matrix[startChar - 'A'][startInt - 1 + i] = "O";
                ship.addPosition("" + startChar + (startInt + i));
                obsazenePozice.add("" + startChar + (startInt + i));
            }
        } else {
            // lod je ve sloupci
            for (int i = 0; i < delkaLodi; i++) {
                matrix[startChar - 'A' + i][startInt - 1] = "O";
                ship.addPosition("" + (char) (startChar + i) + startInt);
                obsazenePozice.add("" + (char) (startChar + i) + startInt);
            }
        }
    }


    /**
     * VALIDACE DELKY LODI
     *
     * @param vypoctenaDelkaLodi
     * @param shipName
     * @param defaultSizeOfShip
     * @return
     */
    private boolean isValidDelkaLodi(int vypoctenaDelkaLodi, String shipName, int defaultSizeOfShip) {
        if (vypoctenaDelkaLodi != defaultSizeOfShip) {
            System.out.println("Error! Wrong length of the " + shipName + "! Try again:");
            return false;
        }
        return true;
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
    private boolean nedotykaSeJineLodi(char startChar, char endChar, int startInt, int endInt) {
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
    private void setZakazanePoziceProUmisteniDalsiLodi(char startChar, char endChar, int startInt, int vypoctenaDelkaLodi) {
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


}


class Gamer {

    private String name;
    //HRAC MA NASLEDUJICI MATICE
    private String[][] gameMatrix = new String[10][10];
    private String[][] attackMatrix = new String[10][10];
    //HRAC MA NASLEDUJICI LODE
    private Ship aircraftCarrier = new Ship("Aircraft Carrier", 5);
    private Ship battleship = new Ship("Battleship", 4);
    private Ship submarine = new Ship("Submarine", 3);
    private Ship cruiser = new Ship("Cruiser", 3);
    private Ship destroyer = new Ship("Destroyer", 2);

    public Gamer(String name) {
        this.name = name;
    }

    public String[][] getGameMatrix() {
        return gameMatrix;
    }

    public String[][] getAttackMatrix() {
        return attackMatrix;
    }

    public String getName() {
        return name;
    }

    public Ship getAircraftCarrier() {
        return aircraftCarrier;
    }

    public Ship getBattleship() {
        return battleship;
    }
    public Ship getSubmarine() {
        return submarine;
    }
    public Ship getCruiser() {
        return cruiser;
    }
    public Ship getDestroyer() {
        return destroyer;
    }


}


class Ship {

    private String name;
    private int length;
    private Set<String> positions = new HashSet<>();

    public Ship(String name, int length) {
        this.name = name;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public Set<String> getPositions() {
        return positions;
    }

    public void addPosition(String position) {
        this.positions.add(position);
    }

    public void deletePosition(String position) {
        this.positions.remove(position);
    }

}

