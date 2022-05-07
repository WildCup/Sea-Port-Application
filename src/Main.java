import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;

enum Color{
    RESET("\u001B[0m"),
    BLACK("\u001B[30m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[37m"),
    CLEAR("\033[H\033[2J");
    String name;
    Color(String name) {this.name = name;}

    @Override
    public String toString() {
        return name;
    }
    public String getName() { return super.toString(); }
    public static Color getColor(String s){
        Color c = null;
        switch (s){
            case "BLACK" -> c = BLACK;
            case "RED" -> c = RED;
            case "GREEN" -> c = GREEN;
            case "YELLOW" -> c = YELLOW;
            case "BLUE" -> c = BLUE;
            case "PURPLE" -> c = PURPLE;
            case "CYAN" -> c = CYAN;
            case "WHITE" -> c = WHITE;
            case "RESET" -> c = RESET;
        }
        return c;
    }
}
public class Main {
    private static Storage train;
    public static void main(String[] args) {
        //region create some objects
        Sender sender1 = new Sender("Hubert", "Hagel", "01302906492", "Kielpin");
        train = new Train();
        Storage warehouse = new Warehouse(15);
//        Ship ship1 = new Ship(5, 3000, 2, 2, 2,"Titanic", "China", "USA", "Poland");
//        Ship ship2 = new Ship(5, 1000, 0, 1, 0,"Black Pearl", "Caribbean", "Cuba", "Poland");
//        Ship ship3 = new Ship(10, 10000, 4, 4, 4,"Queen Anne's Revenge", "England", "England", "Poland");
//
//        Container c0 = new StandardContainer    (sender1, 100, 500, "AAA", "BBB", Color.CYAN);
//        Container c1 = new HeavyContainer       (sender1, 100, 500, "AAA", "BBB", Color.CYAN, "CCC");
//        Container c2 = new RefrigeratedContainer(sender1, 100, 500, "AAA", "BBB", Color.CYAN, "CCC", 120);
//        Container c3 = new LiquidContainer      (sender1, 100, 500, "AAA", "BBB", Color.CYAN, 1200);
//        Container c4 = new ExplosiveContainer   (sender1, 100, 500, "AAA", "BBB", Color.CYAN, 5000);
//        Container c5 = new ToxicPowderContainer (sender1, 100, 500, "AAA", "BBB", Color.CYAN, 3, 300);
//        Container c6 = new ToxicLiquidContainer (sender1, 100, 500, "AAA", "BBB", Color.CYAN, 3, 7);
//        Container c7 = new RefrigeratedContainer(sender1, 100, 500, "AAA", "BBB", Color.CYAN, "CCC", 120);
//        Container c8 = new LiquidContainer      (sender1, 100, 500, "AAA", "BBB", Color.CYAN, 1200);
//        Container c9 = new HeavyContainer       (sender1, 100, 500, "AAA", "BBB", Color.CYAN, "CCC");
//        Container c10 = new HeavyContainer       (sender1, 100, 500, "AAA", "BBB", Color.CYAN, "CCC");
//        Container c11 = new StandardContainer   (sender1, 100, 500, "AAA", "BBB", Color.CYAN);
//        Container c12 = new HeavyContainer       (sender1, 100, 500, "AAA", "BBB", Color.CYAN, "CCC");
//        Container c13 = new ExplosiveContainer   (sender1, 100, 500, "AAA", "BBB", Color.CYAN, 5000);
//        Container c14 = new ExplosiveContainer   (sender1, 100, 500, "AAA", "BBB", Color.CYAN, 5000);
//        Container c15 = new RefrigeratedContainer(sender1, 100, 500, "AAA", "BBB", Color.CYAN, "CCC", 120);
//        Container c16 = new RefrigeratedContainer(sender1, 100, 500, "AAA", "BBB", Color.CYAN, "CCC", 120);
//        Container c17 = new RefrigeratedContainer(sender1, 100, 500, "AAA", "BBB", Color.CYAN, "CCC", 120);
//        Container c18 = new LiquidContainer      (sender1, 100, 500, "AAA", "BBB", Color.CYAN, 1200);
//        Container c19 = new ToxicPowderContainer (sender1, 100, 500, "AAA", "BBB", Color.CYAN, 3, 300);
//        Container c20 = new ToxicLiquidContainer (sender1, 100, 500, "AAA", "BBB", Color.CYAN, 3, 7);
        //endregion

        //region load saved DATA
        SaveLoad.loadImportant();
        train = SaveLoad.loadTrain();
        warehouse = SaveLoad.loadWarehouse();
        SaveLoad.loadShips();
        SaveLoad.loadContainers();
        //endregion

        //region load containers
//        ship1.load(c0);
//        ship1.load(c1);
//        ship1.load(c2);
//        ship1.load(c3);
//        ship1.load(c4);
//
//        ship3.load(c5);
//        ship3.load(c6);
//        ship3.load(c7);
//
//        train.load(c8);
//        train.load(c9);
//        train.load(c10);
//        train.load(c11);
//        train.load(c12);
//        train.load(c13);
//
//        warehouse.load(c15);
//        warehouse.load(c16);
        //endregion

        Thread calendar = new Calendar((Warehouse) warehouse);
        calendar.start();
        
        Scanner scan = new Scanner(System.in);
        String command;

        //region MENU

        printMAIN_MENU();
        while (true) {
            System.out.print("/");
            command = scan.nextLine().toUpperCase();

            //region SHOW commands
            if (command.equals("SHOW") || command.equals("SHOW "))
                printSHOW_MENU();
            else if (command.equals("SHOW SHIPS"))
                System.out.print(Ship.toStringAllShips());
            else if (command.length() > 10 && command.startsWith("SHOW SHIP ")) { //("SHOW SHIP ").length() = 10
                try {
                    int id = Integer.parseInt(command.substring(10));
                    System.out.println(Ship.getShip(id).toStringDetails());
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    System.out.println("Incorrect ID number");
                }
            }
            else if (command.equals("SHOW CONTAINERS"))
                System.out.print(Container.toStringAllContainers());
            else if (command.length() > 15 && command.startsWith("SHOW CONTAINER ")) { //("SHOW CONTAINER ").length() = 15
                try {
                    int id = Integer.parseInt(command.substring(15));
                    System.out.println(Container.findContainer(id,warehouse,train).toStringDetails());
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    System.out.println("Incorrect ID number");
                }
            }
            else if (command.equals("SHOW WAREHOUSE")) {
                System.out.println(warehouse.toString());
                System.out.print(warehouse.showContainers());
            }
            else if (command.equals("SHOW TRAIN")) {
                System.out.println(train.toString());
                System.out.print(train.showContainers());
            }
            //endregion

            //region SHIP commands
            else if (command.equals("SHIP")) {
                printSHIP_MENU();
                //int maxNumber, double maxWeight, int maxToxOrExp, int maxHeavy, int maxElect, String name, String home, String origin, String destination
                int[] arr1 = new int[5];
                String[] arr2 = new String[4];
                double maxWeight = 0;

                String[] instructions = {
                        "Maximum number of all containers: ",
                        "Maximum lifting capacity of the ship (kg): ",
                        "Maximum number of Toxic/Explosive containers: ",
                        "Maximum number of Heavy containers: ",
                        "Maximum number of containers requiring electric connection: ",
                        "Name of the Ship: ",
                        "Home port: ",
                        "Transport origin: ",
                        "Destination: "
                };

                int i = 0;
                while (true) {
                    System.out.println(instructions[i]);

                    System.out.print("/");
                    command = scan.nextLine().toUpperCase();

                    if (command.equals("EXIT")) {
                        //consoleClear();
                        System.out.println("Ship was not saved");
                        printMAIN_MENU();
                        break;
                    }
                    else if (i < arr1.length){
                        try{
                            if(i==1) maxWeight = Double.parseDouble(command);
                            else arr1[i] = Integer.parseInt(command);
                        } catch (NumberFormatException e) {
                            System.out.println("Incorrect number");
                            i--;
                        }
                    } else {
                        arr2[i - arr1.length] = command;
                    }
                    i++;
                    if(i >= instructions.length){
                        Ship ship = new Ship(arr1[0],maxWeight,arr1[2],arr1[3],arr1[4],arr2[0],arr2[1],arr2[2],arr2[3]);
                        System.out.println("Ship of id: " + Color.BLUE + ship.getId() + Color.RESET + " was created!");
                        System.out.println("Type: " + Color.YELLOW + "SHOW SHIP " + ship.getId() + Color.RESET + " to show it's parameters");
                        break;
                    }
                }
            }
            //endregion

            //region CONTAINER commands
            else if (command.equals("CONTAINER")){
                printCONTAINER_MENU();

                Sender sender = null;
                double tare = 0;
                double netWeight = 0;
                String certificate = null;
                String security = null;
                Container.Type type = null;

                Color color = null;
                int int1 = 0;
                double double1 = 0;
                String string1 = null;

                //                String[] instructions = {
//                        "Choose a sender\n"+Sender.allSendersToString() + "Sender's id: ",
//                        "Tare (kg): ",
//                        "Net weight: ",
//                        "Certificate: ",
//                        "Security: ",
//                        "Choose a type (STANDARD, HEAVY, REFRIGERATED, LIQUID, EXPLOSIVE, TOXIC_POWDER, TOXIC_LIQUID)\n" +
//                        "Type: "
//                };
                List<String> instructions = new LinkedList<>();
                instructions.add("Choose a sender\n"+Sender.allSendersToString() + "Sender's id: ");
                instructions.add("Tare (kg): ");
                instructions.add("Net weight: ");
                instructions.add("Certificate: ");
                instructions.add("Security: ");
                instructions.add("Choose a type (STANDARD, HEAVY, REFRIGERATED, LIQUID, EXPLOSIVE, TOXIC_POWDER, TOXIC_LIQUID)\nType: ");

                int i = 0;
                while (true) {
                    System.out.println(instructions.get(i));

                    System.out.print("/");
                    command = scan.nextLine().toUpperCase();

                    if (command.equals("EXIT")) {
                        //consoleClear();
                        System.out.println("Container was not saved");
                        printMAIN_MENU();
                        break;
                    }
                    try{
                        switch (i){
                            case 0 -> sender = Sender.getSenders().get(Integer.parseInt(command));
                            case 1 -> tare = Double.parseDouble(command);
                            case 2 -> netWeight = Double.parseDouble(command);
                            case 3 -> certificate = command;
                            case 4 -> security = command;
                            case 5 -> {
                                type = Container.Type.getType(command);
                                if(type == null) throw new NumberFormatException();

                                instructions.add("Choose a color (BLACK, RED, GREEN, YELLOW, BLUE, PURPLE, CYAN, WHITE)\nColor: ");
                                switch (type){
                                    case REFRIGERATED: instructions.add("Power (Wat): ");
                                    case HEAVY : instructions.add("Toughness (certificate): ");
                                        break;
                                    case LIQUID: instructions.add("Max pressure (10^6 Pascal): ");
                                        break;
                                    case EXPLOSIVE: instructions.add("Shock absorption (Newton): ");
                                        break;
                                    case TOXIC_POWDER:
                                        instructions.add("Safety lvl: ");
                                        instructions.add("Max temp (K): ");
                                        break;
                                    case TOXIC_LIQUID:
                                        instructions.add("Safety lvl: ");
                                        instructions.add("Max PH: ");
                                        break;
                                }
                            }
                            case 6 -> {
                                color = Color.getColor(command);
                                if (color == null) throw new NumberFormatException();
                            }
                            default ->{
                                if(i == 7 && type == Container.Type.REFRIGERATED)
                                    double1 = Double.parseDouble(command);
                                if(i == 7 && type == Container.Type.HEAVY || i == 8 && type == Container.Type.REFRIGERATED)
                                    string1 = command;
                                if(i == 7 && type == Container.Type.LIQUID)
                                    double1 = Double.parseDouble(command);
                                if(i == 7 && type == Container.Type.EXPLOSIVE)
                                    double1 = Double.parseDouble(command);
                                if(i == 7 && type == Container.Type.TOXIC_POWDER || type == Container.Type.TOXIC_LIQUID)
                                    int1 = Integer.parseInt(command);
                                if(i == 8 && type == Container.Type.TOXIC_POWDER || type == Container.Type.TOXIC_LIQUID)
                                    double1 = Integer.parseInt(command);
                            }
                        }
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        System.out.println("Incorrect number");
                        i--;
                    }

                    i++;
                    if(i >= instructions.size()){
                        if(type != null) {
                            Container container = null;
                            switch (type) {
                                case STANDARD -> container = new StandardContainer(sender, tare, netWeight, certificate, security, color);
                                case HEAVY -> container = new HeavyContainer(sender, tare, netWeight, certificate, security, color, string1);
                                case REFRIGERATED -> container = new RefrigeratedContainer(sender, tare, netWeight, certificate, security, color, string1, double1);
                                case LIQUID -> container = new LiquidContainer(sender, tare, netWeight, certificate, security, color, double1);
                                case EXPLOSIVE -> container = new ExplosiveContainer(sender, tare, netWeight, certificate, security, color, double1);
                                case TOXIC_POWDER -> container = new ToxicPowderContainer(sender, tare, netWeight, certificate, security, color, int1, double1);
                                case TOXIC_LIQUID -> container = new ToxicLiquidContainer(sender, tare, netWeight, certificate, security, color, int1, double1);
                            }
                            if(container != null) {
                                System.out.println("Container of id: " + Color.BLUE + container.getId() + Color.RESET + " was created!");
                                System.out.println("Type: " + Color.YELLOW + "SHOW CONTAINER " + container.getId() + Color.RESET + " to show it's parameters");
                            }
                        }
                        break;
                    }
                }
            }
            //endregion

            //region LOAD commands
            else if (command.equals("LOAD") || command.equals("LOAD "))
                printLOAD_MENU();
            else if (command.startsWith("LOAD ") && command.length() > 5) { //("LOAD ").length() = 5
                try {
                    int containerId = Integer.parseInt(command.substring(5, command.indexOf(" ", 5)));
                    Container container = Container.findContainer(containerId,warehouse,train);
                    //we found a container now try to load it

                    command = command.substring(command.indexOf(" ", 6) + 1);
                    if (command.equals("WAREHOUSE"))
                        warehouse.load(container);
                    else if (command.equals("TRAIN"))
                        train.load(container);
                    else if (command.startsWith("SHIP ") && command.length() > 5) {
                        int shipId = Integer.parseInt(command.substring(5));
                        Ship.getShip(shipId).load(container);
                    }
                    else {
                        System.out.println("Incorrect command");
                        continue;
                    }
                    Storage s = container.getWhereIsLoaded();
                    if(s != null)
                        System.out.println(container + " was loaded to " + s);
                    else System.out.println("Container couldn't be loaded");
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    System.out.println("Incorrect ID number");
                }
            }
            //endregion

            //region OTHER commands
            else if (command.startsWith("DEPARTURE ") && command.length() > 10) { //("DEPARTURE ").length() = 10
                try{
                    int id = Integer.parseInt(command.substring(10));
                    Ship.getShip(id).departure();
                }
                catch (NumberFormatException | IndexOutOfBoundsException e){
                    System.out.println("Incorrect ID number");
                }
            }
            else if (command.equals("SAVE")) {
                SaveLoad.saveAll(train,warehouse);
                printWarning("Program was saved");
            }
            else if (command.equals("EXIT")) {
                calendar.interrupt();
                break;
            }
            else if (command.equals("HELP"))
                printMAIN_MENU();
            else
                System.out.println("Command not found");
            //endregion
        }
        //endregion
    }

    static void printMAIN_MENU() {
        System.out.println("Type a command: \n" +
                Color.YELLOW + "SHOW" + Color.RESET + " - display SHOW MENU\n" +
                Color.YELLOW + "SHIP" + Color.RESET + " - create new Ship\n" +
                Color.YELLOW + "CONTAINER" + Color.RESET + " - create new Container\n" +
                Color.YELLOW + "LOAD" + Color.RESET + " - display LOAD MENU\n" +
                Color.YELLOW + "DEPARTURE" +Color.BLUE +" ID"+ Color.RESET + " - departure ship with given "+Color.BLUE+"ID"+Color.RESET+" number\n" +
                Color.YELLOW + "SAVE" + Color.RESET + " - save the program\n" +
                Color.YELLOW + "EXIT" + Color.RESET + " - exit the program\n" +
                Color.YELLOW + "HELP" + Color.RESET + " - show THIS MENU");
    }
    static void printSHOW_MENU() {
        System.out.println("Type a command: \n" +
                Color.YELLOW + "SHOW SHIPS" + Color.RESET + " - display all ships\n" +
                Color.YELLOW + "SHOW SHIP" + Color.BLUE + " ID" + Color.RESET +
                " - show ship with given " + Color.BLUE + "ID" + Color.RESET + " number\n" +
                Color.YELLOW + "SHOW CONTAINERS" + Color.RESET + " - display all containers not loaded anywhere\n" +
                Color.YELLOW + "SHOW CONTAINER" + Color.BLUE + " ID" + Color.RESET +
                " - show container with given " + Color.BLUE + "ID" + Color.RESET + " number\n" +
                Color.YELLOW + "SHOW WAREHOUSE" + Color.RESET + " - display all information about the Warehouse\n" +
                Color.YELLOW + "SHOW TRAIN" + Color.RESET + " - display all information about the Train");
    }
    static void printSHIP_MENU() {
        System.out.println("You are creating a new Ship \n" +
                "Type command: \n" +
                //Color.RED + "RESET" + Color.RESET + " - start creating a ship again \n" +
                Color.YELLOW + "EXIT" + Color.RESET + " - don't create a ship. Go back to main menu \n" +
                "Type appropriate parameters: ");
    }
    static void printCONTAINER_MENU() {
        System.out.println("You are creating a new Container \n" +
                "Type command: \n" +
                //Color.RED + "RESET" + Color.RESET + " - start creating a ship again \n" +
                Color.YELLOW + "EXIT" + Color.RESET + " - don't create a ship. Go back to main menu \n" +
                "Type appropriate parameters: ");
    }
    static void printLOAD_MENU() {
        System.out.println("Type a command: \n" +
                Color.YELLOW + "LOAD" +Color.BLUE +" ID "+ Color.YELLOW + "WAREHOUSE" + Color.RESET +
                " - load container with given"+Color.BLUE+" ID "+Color.RESET+"number to the Warehouse\n" +
                Color.YELLOW + "LOAD" +Color.BLUE +" ID "+ Color.YELLOW + "TRAIN" + Color.RESET +
                " - load container with given"+Color.BLUE+" ID "+Color.RESET+"number on the Train\n" +
                Color.YELLOW + "LOAD" +Color.BLUE +" ID "+ Color.YELLOW + "SHIP" + Color.BLUE +" ID"+ Color.RESET +
                " - load container with given"+Color.BLUE+" ID "+Color.RESET+"number on the Ship with given "+Color.BLUE+"ID"+Color.RESET+" number");
    }

    static void consoleClear() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
    static void printWarning(String s){
        System.out.println(Color.RED + s + Color.RESET);
    }
    static void printMessage(String s){
        System.out.println(Color.PURPLE + s + Color.RESET);
    }
    static void arriveNewTrain(){
        train = new Train();
        printWarning("New Train has arrived");
    }
}
