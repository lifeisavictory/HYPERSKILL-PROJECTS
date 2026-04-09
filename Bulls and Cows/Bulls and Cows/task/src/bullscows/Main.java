package bullscows;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        BullsAndCowsService bullsAndCowsService = new BullsAndCowsService();
        String lengthOfSecretCodeString = "";
        String countOfSymbolsString = "";
        int lengthOfSecretCode = 0;
        int countOfSymbols = 0;
        boolean isInputValid = false;

        //Validace vstupu od uzivatele
        //delka kodu a pocet symbolu ze kterych se bude vybirat
        do {
            //pro pripad dalsiho pruchodu nastavujeme isInputValid na false
            isInputValid = false;

            /**
             * Zadani delky kodu
             */
            System.out.println("Input the length of the secret code:");
            lengthOfSecretCodeString = scanner.nextLine();
            //ERROR #1 - lengthOfSecretCodeString neobsahuje pouze cisla
            try {
                lengthOfSecretCode = Integer.parseInt(lengthOfSecretCodeString);
            } catch (NumberFormatException e) {
                System.out.println("Error: \"" + lengthOfSecretCodeString + "\" isn't a valid number.");
                return;
            }
            //ERROR #2 - lengthOfSecretCode je 0
            if (lengthOfSecretCode <= 0) {
                System.out.println("Error: \"" + lengthOfSecretCodeString + "\" isn't a valid number.");
                return;
            }

            /**
             * Zadání pocitu symbolu ze kterych se bude vybirat
             */
            System.out.println("Input the number of possible symbols in the code:");
            countOfSymbolsString = scanner.nextLine();
            //ERROR #3 - countOfSymbolsString neobsahuje pouze cisla
            try {
                countOfSymbols = Integer.parseInt(countOfSymbolsString);
            } catch (NumberFormatException e) {
                System.out.println("Error: \"" + countOfSymbolsString + "\" isn't a valid number.");
                return;
            }
            //ERROR #4 - lengthOfSecretCode je 0
            if (countOfSymbols <= 0) {
                System.out.println("Error: \"" + countOfSymbolsString + "\" isn't a valid number.");
                return;
            }
            //ERROR #5 - countOfSymbols presahne 36
            if (countOfSymbols > 36) {
                System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
                return;
            }
            //ERROR #6 - lengthOfSecretCode presahne pocet symbolu
            if (lengthOfSecretCode > countOfSymbols) {
                System.out.println("Error: it's not possible to generate a code with a length of " + lengthOfSecretCode + " with " + countOfSymbols + " unique symbols.");
                return;
            }

            /**
             * Pripad, kdyje vse zadano dobre
             */
            if (lengthOfSecretCode <= countOfSymbols && countOfSymbols <= 36 && lengthOfSecretCode > 0) {
                isInputValid = true;
            }
        } while (!isInputValid);

        //vygenerovani secret key
        bullsAndCowsService.generateSecretCode(lengthOfSecretCode, countOfSymbols);
        System.out.println("Okay, let's start a game!");

        //herni smycka
        boolean isGameFinished = false;
        int iterationCount = 1;
        do {
            System.out.println("Turn " + iterationCount + ":");
            String input = scanner.nextLine();
            bullsAndCowsService.checkUserInput(input);
            iterationCount++;
            if (lengthOfSecretCode == bullsAndCowsService.getBulls()) {
                System.out.println("Congratulations! You guessed the secret code.");
                isGameFinished = true;
            }
        } while (!isGameFinished);
        scanner.close();
    }


}