package battleship;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Service service = new Service();
        //GAMER, KTERY VLASTNI LODE A HERNI MATICE
        Player player1 = new Player("Hansel");
        Player player2 = new Player("Gretel");
        Set<Player> players = new HashSet<>(
                Set.of(player1, player2)
        );

        /**
         * ZACATEK HERNIHO CYKLU
         */
        int count = 0;
        for (Player player : players) {
            count++;
            System.out.println("Player " + count + ", place your ships on the game field");
            service.fillMatrix(player.getGameMatrix());
            service.fillMatrix(player.getAttackMatrix());
            service.printGameField(player.getGameMatrix());
            /**
             * Pridani 1. lodi - Aircraft Carrier
             */
            System.out.println("Enter the coordinates of the Aircraft Carrier (5 cells):");
            service.placeShip(player.getGameMatrix(), player.getAircraftCarrier(), player);
            service.printGameField(player.getGameMatrix());
            /**
             * Pridani 2. lodi - Battleship
             */
            System.out.println();
            System.out.println("Enter the coordinates of the Battleship (4 cells):");
            service.placeShip(player.getGameMatrix(), player.getBattleship(), player);
            service.printGameField(player.getGameMatrix());
            /**
             * Pridani 3. lodi - Submarine
             */
            System.out.println();
            System.out.println("Enter the coordinates of the Submarine (3 cells):");
            service.placeShip(player.getGameMatrix(), player.getSubmarine(), player);
            service.printGameField(player.getGameMatrix());
            /**
             * Pridani 4. lodi - Cruiser
             */
            System.out.println();
            System.out.println("Enter the coordinates of the Cruiser (3 cells):");
            service.placeShip(player.getGameMatrix(), player.getCruiser(), player);
            service.printGameField(player.getGameMatrix());
            /**
             * Pridani 5. lodi - Destroyer
             */
            System.out.println();
            System.out.println("Enter the coordinates of the Destroyer (2 cells):");
            service.placeShip(player.getGameMatrix(), player.getDestroyer(), player);
            service.printGameField(player.getGameMatrix());
            //PROMAZANI KONZOLE PRO DALSIHO HRACE
            System.out.println("Press Enter and pass the move to another player");
            clearConsole();
        }

        /**
         * START HRY - TASK 6
         * GAME LOOP
         */
        int turnCount = 0;
        boolean isGameOver = false;
        Player currentPlayer = null;
        Player opponentPlayer = null;
        int playerNumber = 0;

        do {
            if (turnCount % 2 == 0) {
                currentPlayer = player1;
                opponentPlayer = player2;
                playerNumber = 1;
            } else {
                currentPlayer = player2;
                opponentPlayer = player1;
                playerNumber = 2;
            }
            //jsou videt moje utoky na souperovu matici (moje attackMatrix)
            service.printGameField(currentPlayer.getAttackMatrix());
            System.out.println("---------------------");
            //jsou videt jeho utoky na moji matici (moje gameMatrix)
            service.printGameField(currentPlayer.getGameMatrix());
            System.out.println("\nPlayer " + playerNumber + ", it's your turn:\n");
            isGameOver = service.shoot(opponentPlayer.getGameMatrix(), currentPlayer.getAttackMatrix(), opponentPlayer, currentPlayer);
            //PROMAZANI KONZOLE PRO DALSIHO HRACE
            if (!isGameOver) {
                System.out.println("Press Enter and pass the move to another player");
                clearConsole();
                turnCount++;
            }
        } while (!isGameOver);
   }


    /**
     * PRIVATNI METODA NA MAZANI OBRAZOVKY
     * jinak nez takto jsem obrazovku nedokazal "smazat"
     * tohle jsem si nechal poradit AI, protoze mi to nechtelo projit testy na hyperskill
     */
    public static void clearConsole() {
        try {
            System.in.read(); // Jen počkáme, až uživatel cokoliv zmáčkne (Enter) a jdeme dál
        } catch (Exception e) {
            // Ignorujeme
        }
    }


}


class Service {

    private final Scanner sc = new Scanner(System.in);

    public Service() {}

    /**
     * UTOK NA JEDNO POLICKO V MATICI
     *
     * @param opponentMatrix
     * @param currentPlayerAttackMatrix
     */
    public boolean shoot(String[][] opponentMatrix, String[][] currentPlayerAttackMatrix, Player opponentPlayer, Player currentPlayer) {

        String[] input;
        boolean isInputValid = false;
        boolean isGameOver = false;
        char shotChar = 'A';
        char shotResult = '\0';
        int shotInt = 0;

        do {
            input = sc.nextLine().trim().split(" ");
            //Rozklad UTOKU na PISMENO a CISLO
            String cell = input[0];
            shotChar = cell.charAt(0);
            shotInt = Integer.parseInt(cell.substring(1));
            //VALIDACE SOURADNIC VLOZENYCH UZIVATELEM PODLE VELIKOSTI HERNI MATICE
            if (shotChar < 'A' || shotChar > 'J' || shotInt < 1 || shotInt > 10 || input.length > 1) {
                System.out.println("Error! You entered wrong coordinates! Try again:");
                continue;
            }
            //PROJDU LODE A PODIVAM SE KTERA JE ZASAZENA
            //pokud je v obsazenePozice, ale neni u zadne lodi, uz se na policko strilelo
            Ship hitShip = null;
            if (opponentPlayer.getOccupiedPositions().contains("" + shotChar + shotInt)) {
                //najdeme lod, kterou jsme zasahli
                if (opponentPlayer.getAircraftCarrier().getPositions().contains("" + shotChar + shotInt)) {
                    hitShip = opponentPlayer.getAircraftCarrier();
                } else if (opponentPlayer.getBattleship().getPositions().contains("" + shotChar + shotInt)) {
                    hitShip = opponentPlayer.getBattleship();
                } else if (opponentPlayer.getSubmarine().getPositions().contains("" + shotChar + shotInt)) {
                    hitShip = opponentPlayer.getSubmarine();
                } else if (opponentPlayer.getCruiser().getPositions().contains("" + shotChar + shotInt)) {
                    hitShip = opponentPlayer.getCruiser();
                } else if (opponentPlayer.getDestroyer().getPositions().contains("" + shotChar + shotInt)) {
                    hitShip = opponentPlayer.getDestroyer();
                }
            }
            //NASLEDUJE POKUD JSI MINUL - NIKDY TAM LOD NEBYLA
            if (!opponentPlayer.getOccupiedPositions().contains("" + shotChar + shotInt)) {
                shotResult = 'M';
                setShot(currentPlayer.getAttackMatrix(), shotResult, shotChar, shotInt);
                setShot(opponentMatrix, shotResult, shotChar, shotInt); //nastaveni "M" do matice vystrelu
                System.out.println("You missed.");
                isInputValid = true;
            } else if (hitShip != null) {
                //HIT - LOD TAM JE A ZATIM NEBYLO ZASAZENO
                shotResult = 'X';
                setShot(currentPlayerAttackMatrix, shotResult, shotChar, shotInt); //nastaveni X do matice vystrelu
                setShot(opponentMatrix, shotResult, shotChar, shotInt); //nastaveni X do matice zasahu
                hitShip.deletePosition("" + shotChar + shotInt); //odstraneni pozice z lode
                if (opponentPlayer.getAircraftCarrier().getPositions().isEmpty() &&
                        opponentPlayer.getBattleship().getPositions().isEmpty() &&
                        opponentPlayer.getSubmarine().getPositions().isEmpty() &&
                        opponentPlayer.getCruiser().getPositions().isEmpty() &&
                        opponentPlayer.getDestroyer().getPositions().isEmpty()) {
                    System.out.println("You sank the last ship. You won. Congratulations!");
                    isInputValid = true;
                    isGameOver = true;
                } else if (hitShip.getPositions().isEmpty()) {
                    System.out.println("You sank a ship!");
                    isInputValid = true;
                } else {
                    System.out.println("You hit a ship!");
                    isInputValid = true;
                }
            } else {
                //HIT - LOD TAM JE POLICKO JIZ BYLO ZASAZENO
                shotResult = 'X';
                setShot(currentPlayerAttackMatrix, shotResult, shotChar, shotInt); //nastaveni X do matice vystrelu
                setShot(opponentMatrix, shotResult, shotChar, shotInt); //nastaveni X do matice zasahu
                System.out.println("You hit a ship!");
                isInputValid = true;
            }
        } while (!isInputValid);
        return isGameOver; //vraci true, pokud hrac uhadl posledni policko
    }


    /**
     * NASTAVENI VYSTRELU DO HERNI MATICE - MATRIX
     *
     * @param matrix
     * @param shotResult
     * @param shotChar
     * @param shotInt
     */
    private void setShot(String[][] matrix, char shotResult, char shotChar, int shotInt) {
        matrix[shotChar - 'A'][shotInt - 1] = Character.toString(shotResult);
    }


    /**
     * ZADANI POLOHY LODI V HERNI MATICI - pro gameMatrix
     *
     * @param matrix
     */
    public void placeShip(String[][] matrix, Ship ship, Player player) {

        String[] input;
        boolean isInputValid = false;
        int calculatedShipLength = 0;
        char startChar = 'A';
        int startInt = 0;
        char endChar = 'A';
        int endInt = 0;

        do {
            input = sc.nextLine().trim().split(" ");
            //PRVNI BUNKA - rozklad na PISMENO a CISLO
            String firstCell = input[0];
            startChar = firstCell.charAt(0);
            startInt = Integer.parseInt(firstCell.substring(1));
            //DRUHA BUNKA - rozklad na PISMENO a CISLO
            String secondCell = input[1];
            endChar = secondCell.charAt(0);
            endInt = Integer.parseInt(secondCell.substring(1));
            //PREHOZENI CISEL V TABULCE POKUD JE PRVNI MENSI - D5 A5
            if (startInt > endInt) {
                int temp = startInt;
                startInt = endInt;
                endInt = temp;
            }
            if (startChar > endChar) {
                char temp = startChar;
                startChar = endChar;
                endChar = temp;
            }
            //VYPOCET DELKY LODI A VALIDACE VZHLEDEM K PRIJATEMU DRUHU LODI [BATTLESHIP = 5, SUBMARINE = 2] atd
            //musim ji umet spocitat pro horizont i vertikal - do lodi se pocita i posledni pozice (nelze jen tupe odcitat)
            calculatedShipLength = (startChar == endChar)
                    ? (Math.abs(startInt - endInt) + 1)
                    : (Math.abs(startChar - endChar) + 1);
            if (!isValidShipLength(calculatedShipLength, ship.getName(), ship.getLength())) {
                continue;
            }
            //VALIDACE SOURADNIC VLOZENYCH UZIVATELEM PODLE VELIKOSTI HERNI MATICE
            if (!isValidCoordinates(startChar, startInt, endChar, endInt)) {
                continue;
            }
            //VALIDACE ZDA NOVA LOD NENI V ZAKAZANEM POLI
            if (!isNotTouchingOtherShip(startChar, endChar, startInt, endInt, player)) {
                continue;
            }
            //NASTAVENI OBSAZENYCH POLI PRO DALSI ZADANI
            setForbiddenPositions(startChar, endChar, startInt, calculatedShipLength, player);
            //NASTAVENI SOURADNIC V SETU KONKRETNI LODI


            isInputValid = true;
        } while (!isInputValid);
        setShipToMatrix(matrix, ship,  calculatedShipLength, startChar, startInt, endChar, endInt, player);
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
    public void fillMatrix(String[][] matrix) {
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
    private boolean isValidCoordinates(char startChar, int startInt, char endChar, int endInt) {
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
     * @param shipLength
     * @param startChar
     * @param startInt
     * @param endChar
     * @param endInt
     */
    private void setShipToMatrix(String[][] matrix, Ship ship, int shipLength, char startChar, int startInt, char endChar, int endInt, Player player) {
        //ZADANI LODI DO POLE
        if (startChar == endChar) {
            //lod je v radku
            for (int i = 0; i < shipLength; i++) {
                //aby to bylo univerzalni pro radek i sloupec
                matrix[startChar - 'A'][startInt - 1 + i] = "O";
                ship.addPosition("" + startChar + (startInt + i));
                player.getOccupiedPositions().add("" + startChar + (startInt + i));
            }
        } else {
            // lod je ve sloupci
            for (int i = 0; i < shipLength; i++) {
                matrix[startChar - 'A' + i][startInt - 1] = "O";
                ship.addPosition("" + (char) (startChar + i) + startInt);
                player.getOccupiedPositions().add("" + (char) (startChar + i) + startInt);
            }
        }
    }


    /**
     * VALIDACE DELKY LODI
     *
     * @param calculatedShipLength
     * @param shipName
     * @param defaultSizeOfShip
     * @return
     */
    private boolean isValidShipLength(int calculatedShipLength, String shipName, int defaultSizeOfShip) {
        if (calculatedShipLength != defaultSizeOfShip) {
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
    private boolean isNotTouchingOtherShip(char startChar, char endChar, int startInt, int endInt, Player player) {
        if (startChar == endChar) {
            for (int i = startInt; i <= endInt; i++) {
                if (player.getForbiddenPositions().contains("" + startChar + i)) {
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    return false;
                }
            }
        } else {
            for (char i = startChar; i <= endChar; i++) {
                if (player.getForbiddenPositions().contains("" + i + startInt)) {
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
     * @param calculatedShiplength
     */
    private void setForbiddenPositions(char startChar, char endChar, int startInt, int calculatedShiplength, Player player) {
        char newStartChar = (char) (startChar - 1);
        int newStartInt = startInt - 1;
        if (startChar == endChar) {
            for (char row = (char) (startChar - 1); row <= (char) (startChar + 1); row++) {
                for (int col = newStartInt; col <= startInt + calculatedShiplength; col++) {
                    player.getForbiddenPositions().add("" + row + col);
                }
            }
        } else {
            for (int col = startInt - 1; col <= startInt + 1; col++) {
                for (char row = newStartChar; row <= (char) (startChar + calculatedShiplength); row++) {
                    player.getForbiddenPositions().add("" + row + col);
                }
            }
        }
    }


}


class Player {

    private String name;
    //HRAC MA NASLEDUJICI MATICE
    private String[][] gameMatrix = new String[10][10];
    private String[][] attackMatrix = new String[10][10];
    //HASHSETY PRO KONTROLU HIT A MINUL
    private Set<String> occupiedPositions = new HashSet<>(); //obsazene pozice - stoji na nich lode
    private Set<String> forbiddenPositions = new HashSet<>(); //pozice kolem lodi kam se nesmi umistit dalsi lod
    //HRAC MA NASLEDUJICI LODE
    private Ship aircraftCarrier = new Ship("Aircraft Carrier", 5);
    private Ship battleship = new Ship("Battleship", 4);
    private Ship submarine = new Ship("Submarine", 3);
    private Ship cruiser = new Ship("Cruiser", 3);
    private Ship destroyer = new Ship("Destroyer", 2);

    public Player(String name) {
        this.name = name;
    }

    public String[][] getGameMatrix() {
        return gameMatrix;
    }

    public String[][] getAttackMatrix() {
        return attackMatrix;
    }

    public Set<String> getOccupiedPositions() {
        return occupiedPositions;
    }

    public Set<String> getForbiddenPositions() {
        return forbiddenPositions;
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

