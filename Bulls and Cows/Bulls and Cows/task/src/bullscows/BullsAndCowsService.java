package bullscows;

import java.util.Random;

public class BullsAndCowsService {

    //nas secret key (pojmenovani promenne je podle zadani)
    private StringBuilder pseudoRandomNumber = new StringBuilder();
    private StringBuilder secretKey;
    private int bulls = 0;
    private int cows = 0;

    public int getBulls() {
        return bulls;
    }

    /**
     * Generovani nahodneho cisla (0-9 a 'a'-'z')
     *
     * @param lengthOfSecretCode
     * @param countOfSymbols
     */
    public void generateSecretCode(int lengthOfSecretCode, int countOfSymbols) {
        Random random = new Random();
        boolean isPseudoRandomNumberGenerated = false;
        char[] allSymbols = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                'u', 'v', 'w', 'x', 'y', 'z'
        };

        do {
            int randomSymbol = random.nextInt(countOfSymbols);
            char symbol = allSymbols[randomSymbol];
            if (!pseudoRandomNumber.toString().contains(Character.toString(symbol))) {
                pseudoRandomNumber.append(symbol);
            }
            if (pseudoRandomNumber.length() == lengthOfSecretCode) {
                isPseudoRandomNumberGenerated = true;
            }
        } while (!isPseudoRandomNumberGenerated);
        secretKey = pseudoRandomNumber;

        String symbolRange;
        if (countOfSymbols <= 10) {
            symbolRange = "0-" + (countOfSymbols - 1);
            System.out.println("The secret is prepared: " + "*".repeat(lengthOfSecretCode) + " (" + symbolRange + ").");
        } else {
            symbolRange = "a-" + allSymbols[countOfSymbols - 1];
            System.out.println("The secret is prepared: " + "*".repeat(lengthOfSecretCode) + " (0-9, " + symbolRange + ").");
        }
    }


    /**
     * Pocitani bulls a cows
     *
     * @param input
     */
    public void checkUserInput(String input) {
        bulls = 0;
        cows = 0;

        String secretKeyString = String.valueOf(secretKey);
        String inputString = input;
        //tady se pocitaji bulls a cows
        for (int i = 0; i < secretKeyString.length(); i++) {
            if (secretKeyString.charAt(i) == inputString.charAt(i)) {
                //nalezeni bulls podle pozice v retezci
                bulls++;
            } else if (secretKeyString.contains(String.valueOf(inputString.charAt(i)))) {
                //nalezeni cows podle pozice v retezci (pozor, nesmi pocitat cislo z bulls pokud tam uz je)
                cows++;
            }
        }
        //tady pokracuje vypis
        if (bulls > 0 && cows > 0) {
            System.out.println("Grade: " + bulls + (bulls == 1 ? " bull" : " bulls") + " and " + cows + (cows == 1 ? " cow" : " cows"));
        } else if (bulls > 0) {
            System.out.println("Grade: " + bulls + (bulls == 1 ? " bull" : " bulls"));
        } else if (cows > 0) {
            System.out.println("Grade: " + cows + (cows == 1 ? " cow" : " cows"));
        } else {
            System.out.println("Grade: None");
        }
    }


}
