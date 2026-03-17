package lastpencil;

import java.util.Random;
import java.util.Scanner;


public class Main {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int numberOfPencils = 0;
        String whoIsPlaying = "";
        StringBuilder pencils = new StringBuilder();

        numberOfPencils = inputNumberOfPencils(scanner);
        whoIsPlaying = inputName(scanner);

        for (int i = 1; i <= numberOfPencils; i++) {
            pencils.append("|");
        }

        /**
         * MAIN GAME LOOP
         */
        do {
            System.out.println(pencils);
            if ("John".equals(whoIsPlaying)) { //John
                System.out.println(whoIsPlaying + "'s turn!");
                whoIsPlaying = playJohn(scanner, whoIsPlaying, pencils);

            } else if ("Jack".equals(whoIsPlaying)) { //Jack
                System.out.println(whoIsPlaying + "'s turn!");
                whoIsPlaying = playJack(whoIsPlaying, pencils);
            }
        }
        while (pencils.length() > 0);
        String winner = "John".equals(whoIsPlaying) ? "John" : "Jack";
        System.out.println(winner + " won!");
        scanner.close();
    }

    /**
     * HELPER METHODS
     */
    static String playJack(String whoIsPlaying, StringBuilder pencils) {
        Random random = new Random();
        int input = 0;
        if (pencils.length() == 1) {
            input = 1;
        } else if (pencils.length() % 4 == 1) { //5,9,13,17
            input = random.nextInt(3) + 1;
        } else if (pencils.length() % 4 == 0) { //4,8,12,16
            input = 3;
        } else if (pencils.length() % 4 == 3) { //3,7,11,15
            input = 2;
        } else if (pencils.length() % 4 == 2) { //2,6,10,14
            input = 1;
        } else {
            input = random.nextInt(3) + 1;
        }
        System.out.println(input);
        pencils.delete(0, input);
        whoIsPlaying = "Jack".equals(whoIsPlaying) ? "John" : "Jack";
        return whoIsPlaying;
    }

    static String playJohn(Scanner scanner, String whoIsPlaying, StringBuilder pencils) {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input > 3 || input < 1) {
                    System.out.println("Possible values: '1', '2' or '3'");
                } else if (input > pencils.length()) {
                    System.out.println("Too many pencils were taken");
                } else {
                    pencils.delete(0, input);
                    whoIsPlaying = "Jack".equals(whoIsPlaying) ? "John" : "Jack";
                    return whoIsPlaying;
                }
            } catch (Exception e) {
                System.out.println("Possible values: '1', '2' or '3'");
            }
        }
    }

    static int inputNumberOfPencils(Scanner scanner) {
        boolean validInput = false;
        int numberOfPencils = 0;
        do {
            try {
                System.out.println("How many pencils would you like to use:");
                numberOfPencils = Integer.parseInt(scanner.nextLine());
                if (numberOfPencils <= 0) {
                    System.out.println("The number of pencils should be positive");
                } else {
                    validInput = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("The number of pencils should be numeric");
            }
        }
        while (!validInput);
        return numberOfPencils;
    }

    static String inputName(Scanner scanner) {
        boolean validInput = false;
        String whoIsPlaying = "";
        do {
            System.out.println("Who will be the first (John, Jack):");
            whoIsPlaying = scanner.nextLine().trim();
            if (!"John".equals(whoIsPlaying) && !"Jack".equals(whoIsPlaying)) {
                System.out.println("Choose between 'John' and 'Jack'");
            } else {
                validInput = true;
            }
        }
        while (!validInput);
        return whoIsPlaying;
    }


}
