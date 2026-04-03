package numbers;

public class NumbersCalculator {

    //napadlo mi doplnit switch, ktery by to pak jen rozrazoval
    public boolean isAmazingNumber(String property, long input) {
        switch (property) {
            case "happy":
                return isHappynumber(input);
            case "sad":
                //kontroluji jen happy na sad se mi vrati false
                return !isHappynumber(input);
            case "jumping":
                return isJumpingnumber(input);
            case "sunny":
                return isSunnyNumber(input);
            case "square":
                return isSquareNumber(input);
            case "spy":
                return isSpyNumber(input);
            case "gapful":
                return isGapfulNumber(input);
            case "palindromic":
                return isPalindromicNumber(input);
            case "duck":
                return isDuckNumber(input);
            case "buzz":
                return isBuzznumber(input);
            case "even":
                return isOddOrEven(input);
            case "odd":
                //kontroluju jen even, na odd se mi vrati false
                return !isOddOrEven(input);
            default:
                return false;
        }
    }

    private boolean isHappynumber(long input) {
        long sum = input;
        String inputString = String.valueOf(input);

        while (sum != 1 && sum != 4) {
            sum = 0;
            for (int i = 0; i < inputString.length(); i++) {
                int digit = Integer.parseInt(String.valueOf(inputString.charAt(i)));
                sum += digit * digit;
            }
            inputString = String.valueOf(sum);
        }
        return sum == 1;
    }

    //Jumping number
    private boolean isJumpingnumber(long input) {
        if  (input < 10) return true;
        String inputString = String.valueOf(input);
        for (int i = 0; i < inputString.length() - 1; i++) {
            int diff = Math.abs(inputString.charAt(i) - inputString.charAt(i + 1));
            if (diff != 1) return false;
        }
        return true;
    }

    //Sunny number
    private boolean isSunnyNumber(long input) {
        return isSquareNumber(input + 1) ;
    }

    //Square number
    private boolean isSquareNumber(long input) {
        return Math.sqrt(input) % 1 == 0;
    }

    //Spy number
    private boolean isSpyNumber(long input) {
        String inputString = String.valueOf(input);
        long sum = 0;
        long product = 1; //pri nasobeni nulou by to byla vzdy nula
        for (int i = 0; i < inputString.length(); i++) {
            //je treba to preparsovat z CHAR na long
            sum += Long.parseLong(String.valueOf(inputString.charAt(i)));
            product *= Long.parseLong(String.valueOf(inputString.charAt(i)));
        }
        if (sum == product) {
            return true;
        } else {
            return false;
        }
    }

    //Gapful number
    private boolean isGapfulNumber(long input) {
        String inputString = String.valueOf(input);
        if (inputString.length() < 3 ) {
            return false;
        } else {
            String[] array = inputString.split("");
            int newNumber = Integer.parseInt(array[0] + array[array.length - 1]);
            if (input % newNumber == 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    //Palindromic number
    private boolean isPalindromicNumber(long input) {
        StringBuilder inputString = new StringBuilder(String.valueOf(input));
        StringBuilder reversedString = new StringBuilder(inputString).reverse();
        return inputString.toString().contentEquals(reversedString);
    }

    //Duck number
    private boolean isDuckNumber(long input) {
        return String.valueOf(input).contains("0");
    }

    //Buzz number
    private boolean isBuzznumber(long input) {
        long lastNumber = input % 10;
        if (input % 7 == 0) {
            return true;
        } else if (lastNumber == 7) {
            return true;
        } else {
            return false;
        }
    }

    //Odd or even number
    private boolean isOddOrEven(long input) {
        if (input % 2 == 0) {
            return true; // Even
        } else {
            return false; // Odd
        }
    }

    /**
     * Explain - metoda z prvniho ukolu
     * @param input
     * @return
     */
    private String explain(long input) {
        boolean isDivisible = input % 7 == 0;
        boolean isEndsWith7 = input % 10 == 7;
        if (isDivisible && isEndsWith7) {
            return "It is divisible by 7 and ends with 7.";
        } else if (isDivisible) {
            return "It is divisible by 7.";
        } else if (isEndsWith7) {
            return "It ends with 7.";
        } else {
            return "It is neither divisible by 7 nor does it end with 7.";
        }
    }


}
