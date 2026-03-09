package machine;

import java.util.Scanner;


public class CoffeeMachine {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        /**
         * Write how many ml of waterInside the coffee machine has:
         * > 300
         * Write how many ml of milkInside the coffee machine has:
         * > 65
         * Write how many grams of coffee beans the coffee machine has:
         * > 100
         * Write how many cups of coffee you will need:
         * > 1
         * Yes, I can make that amount of coffee
         */
        System.out.println("Write how many ml of water the coffee machine has:");
        int availableWater = scanner.nextInt();
        System.out.println("Write how many ml of milk the coffee machine has:");
        int availableMilk = scanner.nextInt();
        System.out.println("Write how many grams of coffee beans the coffee machine has:");
        int availableBeans = scanner.nextInt();
        System.out.println("Write how many cups of coffee you will need:");
        int requestedCups = scanner.nextInt();
        scanner.close();

        // Spotřeba na 1 šálek
        int waterPerCup = 200;
        int milkPerCup = 50;
        int beansPerCup = 15;

        // Kolik šálků vyrobíme z jednotlivých surovin?
        int possibleCupsByWater = availableWater / waterPerCup;
        int possibleCupsByMilk = availableMilk / milkPerCup;
        int possibleCupsByBeans = availableBeans / beansPerCup;

        // Skutečné maximum je to nejmenší z těchto tří čísel (úzké hrdlo)
        int maxCups = Math.min(possibleCupsByWater, Math.min(possibleCupsByMilk, possibleCupsByBeans));

        /**
         * "Yes, I can make that amount of coffee"
         * "Yes, I can make that amount of coffee (and even N more than that)"
         * "No, I can make only N cup(s) of coffee"
         */
        if (requestedCups == maxCups) {
            System.out.println("Yes, I can make that amount of coffee");
        } else if (requestedCups < maxCups) {
            int extraCups = maxCups - requestedCups;
            System.out.println("Yes, I can make that amount of coffee (and even " + extraCups + " more than that)");
        } else { // (requestedCups > maxCups)
            System.out.println("No, I can make only " + maxCups + " cup(s) of coffee");
        }


    }
}
