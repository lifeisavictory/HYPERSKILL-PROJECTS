package numbers;

import java.util.ArrayList;
import java.util.Arrays;


public class NumbersService {

    NumbersCalculator numbersCalculator = new NumbersCalculator();

    /**
     * Metoda pro zadani cisla
     *
     * @return int
     */
    public boolean validateInputString(String[] inputString) {
        long firstNumber = 0;
        long secondNumber = 0;
        ArrayList<String> inputProperties = new ArrayList<>();
        //overeni prvniho cisla
        if (inputString.length >= 1) {
            firstNumber = Long.parseLong(inputString[0]);
            boolean isValid = isNaturalWithFirstNumber(firstNumber);
            if (!isValid) return false;
            if (firstNumber == 0) return true;
        }
        //overeni druheho cisla
        if (inputString.length >= 2) {
            secondNumber = Long.parseLong(inputString[1]);
            boolean isValid = isNaturalWithSecondNumber(secondNumber);
            if (!isValid) return false;
        }
        //overeni vsech vlastnosti
        if (inputString.length >= 3) {
            for (int i = 2; i < inputString.length; i++) {
                inputProperties.add(inputString[i].toUpperCase());
            }
            //proveri mutuallne exlusivni vlastnosti
            if (!isMutuallyExclusive(inputProperties)) return false;
            //proveri vlatnosti
            boolean isValid = isProperty(inputProperties);
            if (!isValid) return false;
        }
        //odtud jsou definovany vypisy
        if (inputString.length == 1) {
            printResume(firstNumber);
            return false;
        } else if (inputString.length == 2) {
            printResume(firstNumber, secondNumber);
            return false;
        } else if (inputString.length >= 3) {
            printResume(firstNumber, secondNumber, inputProperties);
            return false;
        }
        return false;    }


    /**
     *
     * @param firstNumber
     * @param secondNumber
     * @param inputProperties
     */
    private void printResume(long firstNumber, long secondNumber, ArrayList<String> inputProperties) {
        long count = 0;
        long i = firstNumber;

        while (count < secondNumber) {
            boolean hasAllProperties = true;
            //kontrola vlastnosti
            for (String property : inputProperties) {
                if (property.startsWith("-")) {
                    if (numbersCalculator.isAmazingNumber(property.substring(1).toLowerCase(), i)) {
                        //pokud obsahuje "-" a cislo ma danou vlastnost, tak se odebere
                        hasAllProperties = false;
                        break;
                    }
                } else if (!numbersCalculator.isAmazingNumber(property.toLowerCase(), i)) {
                    //pokud nema cislo danou vlastnost, tak se odebere
                    hasAllProperties = false;
                    break;
                }
            }
            //pokud ma vsechny pozadovane vlastnosti, vypise cislo i s vlastnostmi
            if (hasAllProperties) {
                ArrayList<String> properties = new ArrayList<>();
                properties = getAllProperties(i, properties);
                System.out.println(i + " is " + String.join(", ", properties));
                count++;
            }
            i++;
        }



    }

    /**
     *
     * @param firstNumber
     * @param secondNumber
     */
    private void printResume(long firstNumber, long secondNumber) {
        for (long i = firstNumber; i < firstNumber + secondNumber; i++) {
            ArrayList<String> properties = new ArrayList<>();
            properties = getAllProperties(i, properties);
            System.out.println(i + " is " + String.join(", ", properties));
        }
    }

    /**
     *
     * @param input
     */
    private void printResume(long input) {
        boolean isHappy = numbersCalculator.isAmazingNumber("happy", input);
        boolean isJumping = numbersCalculator.isAmazingNumber("jumping", input);
        boolean isSquare = numbersCalculator.isAmazingNumber("square", input);
        boolean isSunny = numbersCalculator.isAmazingNumber("sunny", input);
        boolean isEven = numbersCalculator.isAmazingNumber("even", input);
        boolean isBuzz = numbersCalculator.isAmazingNumber("buzz", input);
        boolean isDuck = numbersCalculator.isAmazingNumber("duck", input);
        boolean isPalindromic = numbersCalculator.isAmazingNumber("palindromic", input);
        boolean isGapful = numbersCalculator.isAmazingNumber("gapful", input);
        boolean isSpy = numbersCalculator.isAmazingNumber("spy", input);
        System.out.println("Properties of " + input);
        System.out.println("\t" + "buzz: " + isBuzz);
        System.out.println("\t" + "duck: " + isDuck);
        System.out.println("\t" + "palindromic: " + isPalindromic);
        System.out.println("\t" + "even: " + isEven);
        System.out.println("\t" + "odd: " + !isEven);
        System.out.println("\t" + "gapful: " + isGapful);
        System.out.println("\t" + "spy: " + isSpy);
        System.out.println("\t" + "square: " + isSquare);
        System.out.println("\t" + "sunny: " + isSunny);
        System.out.println("\t" + "jumping: " + isJumping);
        System.out.println("\t" + "happy: " + isHappy);
        System.out.println("\t" + "sad: " + !isHappy);
    }

    private ArrayList<String> getAllProperties(long i, ArrayList<String> properties) {
        if (!numbersCalculator.isAmazingNumber("happy", i)) properties.add("sad");
        if (numbersCalculator.isAmazingNumber("happy", i)) properties.add("happy");
        if (numbersCalculator.isAmazingNumber("jumping", i)) properties.add("jumping");
        if (numbersCalculator.isAmazingNumber("square", i)) properties.add("square");
        if (numbersCalculator.isAmazingNumber("sunny", i)) properties.add("sunny");
        if (numbersCalculator.isAmazingNumber("even", i)) properties.add("even");
        if (!numbersCalculator.isAmazingNumber("even", i)) properties.add("odd");
        if (numbersCalculator.isAmazingNumber("buzz", i)) properties.add("buzz");
        if (numbersCalculator.isAmazingNumber("duck", i)) properties.add("duck");
        if (numbersCalculator.isAmazingNumber("palindromic", i)) properties.add("palindromic");
        if (numbersCalculator.isAmazingNumber("gapful", i)) properties.add("gapful");
        if (numbersCalculator.isAmazingNumber("spy", i)) properties.add("spy");
        return properties;
    }

    /**
     * Metoda pro kontrolu vstupu vlastnosti
     * @param inputProperties
     * @return
     */
    private boolean isProperty (ArrayList<String> inputProperties) {
        ArrayList<String> wrongProperty = new ArrayList<>();
        for (String property : inputProperties) {
            //nasledujici radek si upravi slova jako "-buzz", aby odpovidala ENUMu
            String transformProperty = property.startsWith("-") ? property.substring(1) : property;
            if (!Arrays.stream(Property.values()).anyMatch(p -> p.name().equals(transformProperty))) {
                wrongProperty.add(property);
            }
        }
        if (wrongProperty.isEmpty()) return true;
        if (wrongProperty.size() == 1) {
            System.out.println("The property [" + wrongProperty.get(0) + "] is wrong.");
        } else {
            System.out.println("The properties [" + String.join(", ", wrongProperty) + "] are wrong.");
        }
        System.out.println("Available properties: " + Arrays.toString(Property.values()));
        return false;
    }

    /**
     * Metoda pro kontrolu vlastnostich, ktere jsou mutuallne exlusivni
     * Nasledujici vlastnosti se navzajem vylucuji
     * 1. SUNNY a SQUARE
     * 2. SPY a DUCK
     * 3. EVEN a ODD
     * @param properties
     * @return
     */
    private boolean isMutuallyExclusive(ArrayList<String> properties) {
        // buzz a -buzz nemuze byt soucasne - nasledujici podminka to resi obecne pro vsechny vlastnosti
        for (String property : properties) {
            if (property.startsWith("-") && properties.contains(property.substring(1))) {
                System.out.println("The request contains mutually exclusive properties: " + "[" + property + ", " + property.substring(1) + "]");
                System.out.println("There are no numbers with these properties.");
                return false;
            }
        }
        //nasleduji pevne stanovene vlastnosti
        if (properties.contains("SUNNY") && properties.contains("SQUARE")) {
            System.out.println("The request contains mutually exclusive properties: " + "[SUNNY, SQUARE]");
            System.out.println("There are no numbers with these properties.");
            return false;
        }
        if (properties.contains("-SUNNY") && properties.contains("-SQUARE")) {
            System.out.println("The request contains mutually exclusive properties: " + "[-SUNNY, -SQUARE]");
            System.out.println("There are no numbers with these properties.");
            return false;
        }
        if (properties.contains("SPY") && properties.contains("DUCK")) {
            System.out.println("The request contains mutually exclusive properties: " + "[SPY, DUCK]");
            System.out.println("There are no numbers with these properties.");
            return false;
        }
        if (properties.contains("-SPY") && properties.contains("-DUCK")) {
            System.out.println("The request contains mutually exclusive properties: " + "[-SPY, -DUCK]");
            System.out.println("There are no numbers with these properties.");
            return false;
        }
        if (properties.contains("EVEN") && properties.contains("ODD")) {
            System.out.println("The request contains mutually exclusive properties: " + "[EVEN, ODD]");
            System.out.println("There are no numbers with these properties.");
            return false;
        }
        if (properties.contains("-EVEN") && properties.contains("-ODD")) {
            System.out.println("The request contains mutually exclusive properties: " + "[-EVEN, -ODD]");
            System.out.println("There are no numbers with these properties.");
            return false;
        }
        if (properties.contains("SAD") && properties.contains("HAPPY")) {
            System.out.println("The request contains mutually exclusive properties: " + "[SAD, HAPPY]");
            System.out.println("There are no numbers with these properties.");
            return false;
        }
        if (properties.contains("-SAD") && properties.contains("-HAPPY")) {
            System.out.println("The request contains mutually exclusive properties: " + "[-SAD, -HAPPY]");
            System.out.println("There are no numbers with these properties.");
            return false;
        }
        return true;
    }

    /**
     * Metoda pro kontrolu vstupu prvniho cisla
     * @param input
     * @return
     */
    private boolean isNaturalWithFirstNumber(long input) {
        if (input > 0) {
            return true;
        } else if (input == 0) {
            System.out.println("Goodbye!");
            return true;
        } else {
            System.out.println("The first parameter should be a natural number or zero.");
            return false;
        }
    }

    /**
     * Metoda pro kontrolu vstupu druheho cisla
     * @param input
     * @return
     */
    private boolean isNaturalWithSecondNumber(long input) {
        if (input > 0) {
            return true;
        } else {
            System.out.println("The second parameter should be a natural number.");
            return false;
        }
    }

}