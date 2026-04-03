package numbers;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        NumbersCalculator numbersCalculator = new NumbersCalculator();
        NumbersService numbersService = new NumbersService();

        boolean isValidEndOfProgram = false;

        System.out.println("""
            Welcome to Amazing Numbers!
            
                Supported requests:
                - enter a natural number to know its properties;
                - enter two natural numbers to obtain the properties of the list:
                  * the first parameter represents a starting number;
                  * the second parameter shows how many consecutive numbers are to be printed;
                - two natural numbers and properties to search for;
                - a property preceded by minus must not be present in numbers;
                - separate the parameters with one space;
                - enter 0 to exit.
            """);

        while (!isValidEndOfProgram) {
            System.out.print("Enter a request: ");
            String[] inputString = scanner.nextLine().split(" ");
            isValidEndOfProgram = numbersService.validateInputString(inputString);
        };

        scanner.close();
    }

}