package machine;

import java.util.Scanner;

public class CoffeeMachine {
    //Supplies
    public int availableWater;
    public int availableMilk;
    public int availableCoffeeBeans;
    public int availableCups;
    public int availableMoney;
    //The count that determines when the coffee machine needs cleaning
    public int numberOfCups;

    public CoffeeMachine(int availableWater, int availableMilk, int availableCoffeeBeans, int availableCups, int availableMoney){
        this.availableWater = availableWater;
        this.availableMilk = availableMilk;
        this.availableCoffeeBeans = availableCoffeeBeans;
        this.availableCups = availableCups;
        this.availableMoney = availableMoney;
        this.numberOfCups = 0;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        CoffeeMachine coffeeMachine = new CoffeeMachine(400, 540, 120, 9, 550);

        boolean isRunning = true;
        String action = "";

        do {
            System.out.println("Write action (buy, fill, take, clean, remaining, exit):");
            action = scanner.nextLine();

            if (coffeeMachine.numberOfCups >= 10 && "buy".equals(action)) {
                System.out.println("I need cleaning!");
            } else {
                switch (action) {
                    case "buy":
                        buyCoffee(scanner, coffeeMachine);
                        break;
                    case "fill":
                        refillCoffeeMachine(scanner, coffeeMachine);
                        break;
                    case "take":
                        System.out.println("I gave you $" + coffeeMachine.availableMoney);
                        coffeeMachine.availableMoney = 0;
                        break;
                    case "clean":
                        if (coffeeMachine.numberOfCups < 10) {
                            break;
                        } else {
                            coffeeMachine.numberOfCups = 0;
                            System.out.println("I have been cleaned!");
                        }
                        break;
                    case "remaining":
                        showSupplies(coffeeMachine);
                        break;
                    case "exit":
                        isRunning = false;
                        break;
                }
            }

        } while (isRunning);
        scanner.close();
    }


    static void showSupplies(CoffeeMachine coffeeMachine){
        System.out.print("The coffee machine has:\n"
        + coffeeMachine.availableWater + " ml of water\n"
        + coffeeMachine.availableMilk + " ml of milk\n"
        + coffeeMachine.availableCoffeeBeans + " g of coffee beans\n"
        + coffeeMachine.availableCups + " disposable cups\n"
        + "$" + coffeeMachine.availableMoney + " of money\n");
    }

    static void refillCoffeeMachine(Scanner scanner, CoffeeMachine coffeeMachine){
        System.out.println("Write how many ml of water you want to add: ");
        coffeeMachine.availableWater += scanner.nextInt();
        System.out.println("Write how many ml of milk you want to add: ");
        coffeeMachine.availableMilk += scanner.nextInt();
        System.out.println("Write how many grams of coffee beans you want to add: ");
        coffeeMachine.availableCoffeeBeans += scanner.nextInt();
        System.out.println("Write how many disposable cups you want to add: ");
        coffeeMachine.availableCups += scanner.nextInt();
        scanner.nextLine();
    }

    static void buyCoffee(Scanner scanner, CoffeeMachine coffeeMachine){
        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
        String input = scanner.nextLine();
        switch (input) {
            case "1":
                checkSupplies(new Coffee(250,   0, 16, 4), coffeeMachine);
                break;
            case "2":
                checkSupplies(new Coffee(350,  75, 20, 7), coffeeMachine);
                break;
            case "3":
                checkSupplies(new Coffee(200, 100, 12, 6), coffeeMachine);
                break;
            case "back":
                break;
        }
    }

    static void checkSupplies(Coffee coffee, CoffeeMachine coffeeMachine){
        //nerika, ze dosly 2 ingredience, ale prvni ktera chybi - to je trochu nelogicky, ale zadani je zadani
        // reports only the first missing ingredient, not all — as per task requirements
        if (coffeeMachine.availableWater < coffee.water){
            System.out.println("Sorry, not enough water!");
        } else if (coffeeMachine.availableMilk < coffee.milk){
            System.out.println("Sorry, not enough milk!");
        } else if (coffeeMachine.availableCoffeeBeans < coffee.coffeeBeans){
            System.out.println("Sorry, not enough coffee beans!");
        } else if (coffeeMachine.availableCups < 1){
            System.out.println("Sorry, not enough disposable cups!");
        }  else {
            coffeeMachine.availableWater -= coffee.water;
            coffeeMachine.availableMilk -= coffee.milk;
            coffeeMachine.availableCoffeeBeans -= coffee.coffeeBeans;
            coffeeMachine.availableCups -= 1;
            coffeeMachine.availableMoney += coffee.price;
            coffeeMachine.numberOfCups++ ;
            System.out.println("I have enough resources, making you a coffee!");
        }

    }
}


class Coffee {
    public int water;
    public int milk;
    public int coffeeBeans;
    public int price;
    public Coffee(int water, int milk, int coffeeBeans, int price){
        this.water = water;
        this.milk = milk;
        this.coffeeBeans = coffeeBeans;
        this.price = price;
    }
}