package battleship;

import java.security.spec.RSAOtherPrimeInfo;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class Main {


    //TODO - prejmenovat promenne a metody na anglicke nazvy !!!


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String[][] gameMatrix = new String[10][10];
        String[][] attackMatrix = new String[10][10];


        //DEFINOVANI LODI PRO HRU
        String[] ships = {"Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"};
        int[] defaultSizeOfShips = {5, 4, 3, 3, 2};

        //LOGIKA HERNIHO CYKLU
        Service.naplnMatici(gameMatrix);
        Service.naplnMatici(attackMatrix);
        Service.printGameField(gameMatrix);

        /**
         * Pridani 1. lodi - Aircraft Carrier
         */
        System.out.println("Enter the coordinates of the Aircraft Carrier (5 cells):");
        gameMatrix = Service.zadejPolohuLodi(gameMatrix, ships[0], defaultSizeOfShips[0] );
        Service.printGameField(gameMatrix);
        /**
         * Pridani 2. lodi - Battleship
         */
        System.out.println();
        System.out.println("Enter the coordinates of the Battleship (4 cells):");
        gameMatrix = Service.zadejPolohuLodi(gameMatrix, ships[1], defaultSizeOfShips[1] );
        Service.printGameField(gameMatrix);
        /**
         * Pridani 3. lodi - Submarine
         */
        System.out.println();
        System.out.println("Enter the coordinates of the Submarine (3 cells):");
        gameMatrix = Service.zadejPolohuLodi(gameMatrix, ships[2], defaultSizeOfShips[2] );
        Service.printGameField(gameMatrix);
        /**
         * Pridani 4. lodi - Cruiser
         */
        System.out.println();
        System.out.println("Enter the coordinates of the Cruiser (3 cells):");
        gameMatrix = Service.zadejPolohuLodi(gameMatrix, ships[3], defaultSizeOfShips[3] );
        Service.printGameField(gameMatrix);
        /**
         * Pridani 5. lodi - Destroyer
         */
        System.out.println();
        System.out.println("Enter the coordinates of the Destroyer (2 cells):");
        gameMatrix = Service.zadejPolohuLodi(gameMatrix, ships[4], defaultSizeOfShips[4] );
        Service.printGameField(gameMatrix);
        /**
         * START HRY - TASK 3
         */

        System.out.println("The game starts!");
        Service.naplnMatici(attackMatrix);
        Service.printGameField(attackMatrix);
        System.out.println("Take a shot!");
        Service.zautoc(gameMatrix, attackMatrix);
        Service.naplnMatici(gameMatrix);

    }
}