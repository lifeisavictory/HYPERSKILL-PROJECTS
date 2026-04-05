package cinema;

import java.util.Scanner;

public class Cinema {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isValid = true;
        int rows, seats, myRow = 0, mySeat = 0;
        int currentIncome = 0;


        System.out.println("Enter the number of rows:");
        rows = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter the number of seats in each row:");
        seats = Integer.parseInt(scanner.nextLine());
        //Pole urcuje sal a obsazenost
        String[][] hall = new String[rows][seats];
        //Naplneni pole Stringem "S"
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < seats; j++) {
                hall[i][j] = "S";
            }
        }
        System.out.println();

        while (isValid) {
            System.out.println("1. Show the seats");
            System.out.println("2. Buy a ticket");
            System.out.println("3. Statistics");
            System.out.println("0. Exit");
            int choice = Integer.parseInt(scanner.nextLine().trim());
            switch (choice) {
                case 1:
                    printSeats(rows, seats, hall);
                    System.out.println();
                    break;
                case 2:
                    while (true) {
                        System.out.println("Enter a row number:");
                        myRow = Integer.parseInt(scanner.nextLine());
                        System.out.println("Enter a seat number in that row:");
                        mySeat = Integer.parseInt(scanner.nextLine());

                        //validace obsazenosti sedadla
                        boolean isSeatFree = isFree(hall, myRow, mySeat);
                        System.out.println();

                        if (isSeatFree) {
                            //zobrazi cenu sedadla a upravi currentIncome
                            hall[myRow - 1][mySeat - 1] = "B";
                            currentIncome = ticketPrice(rows, seats, myRow, currentIncome);
                            break;
                        }
                    }
                    break;
                case 3:
                    System.out.println();
                    printStatistics(hall, currentIncome);
                    System.out.println();
                    break;
                case 0:
                    isValid = false;
                    break;
            }
        }


    }

    private static boolean isFree(String[][] hall, int myRow, int mySeat) {
        boolean isSeatFree = true;
        if (hall.length < myRow || hall[0].length < mySeat || myRow < 1 || mySeat < 1) {
            System.out.println("Wrong input!");
            isSeatFree = false;
        } else if (hall[myRow - 1][mySeat - 1].equals("B")) {
            System.out.println("That ticket has already been purchased!");
            isSeatFree = false;
        }
        return isSeatFree;
    }


    /**
     * Vypis statistik
     * @param hall
     * @param currentIncome
     */
    private static void printStatistics(String[][] hall, int currentIncome) {
        int countB = 0;
        //Number of purchased tickets
        for (int i = 0; i < hall.length; i++) {
            for (int j = 0; j < hall[i].length; j++) {
                if (hall[i][j].equals("B")) {
                    countB++;
                }
            }
        }
        System.out.println("Number of purchased tickets: " + countB);
        //Procentualni podil zakoupenych sedadel k celkovemu poctu sedadel
        double percent = ((double)countB / (hall.length * hall[0].length)) * 100;
        System.out.println("Percentage: " + String.format("%.2f", percent) + "%");
        //Aktualni prijem - je jiz pocitany pri zakoupeni sedadla
        System.out.println("Current income: $" + currentIncome);
        //Maximalni mozny prijem - spocitame tady podle zadani nekde ve druhem TASKU (rozdeleni ceny sedadel)
        int totalIncome = 0;
        int totalSeats = hall.length * hall[0].length;
        double result = 0;
        if (totalSeats <= 60) {
            System.out.println("Total income: $" + totalSeats * 10);
        } else {
            if (hall.length % 2 == 0) {
                result = ( ((totalSeats / 2) * 10) + ((totalSeats / 2) * 8) );
                System.out.println("Total income: $" + (int)result);
            } else {
                double diff = (double)hall.length / 2;
                result = Math.floor(diff) * hall[0].length * 10 + Math.ceil(diff) * hall[0].length * 8;
                System.out.println("Total income: $" + (int)result);
            }
        }

    }


    /**
     * Vypis ceny sedadla
     * @param rows
     * @param seats
     * @param myRow
     */
    private static int ticketPrice(int rows, int seats, int myRow, int currentIncome) {
        int totalSeats = rows * seats;
        if (totalSeats <= 60) {
            System.out.println("Ticket price: $10");
            currentIncome += 10;
        } else {
            if (myRow <= rows / 2) {
                System.out.println("Ticket price: $10");
                currentIncome += 10;
            } else {
                System.out.println("Ticket price: $8");
                currentIncome += 8;
            }
        }
        return currentIncome;
    }


    /**
     * Vypis vsech sedadel (obsazene i volne)
     * @param rows
     * @param seats
     * @param hall
     */
    private static void printSeats(int rows, int seats, String[][] hall) {
        System.out.println("Cinema:");
        System.out.print(" ");
        for (int i = 1; i <= seats; i++) {
            System.out.print(" " + i);
        }
        System.out.println();
        //vypsani celeho poleni pole
        for (int i = 0; i < rows; i++) {
            System.out.print(i + 1 + " ");
            for (int j = 0; j < seats; j++) {
                System.out.print(hall[i][j] + " ");
            }
            System.out.println();
        }
    }


}