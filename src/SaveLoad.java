import java.io.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class SaveLoad {
    public static void saveAll(Storage train, Storage warehouse){
        save(train);
        save(warehouse);
        try {
            //region saving ships
            BufferedWriter writer = new BufferedWriter(new FileWriter("Ship.txt"));
            for (Ship s: Ship.getAllShips()) {
                writer.write(s.saveAsString());
                writer.write(": ////////////////////////////////////\n");
            }
            writer.close();
            //endregion
            //region saving containers
            writer = new BufferedWriter(new FileWriter("Containers.txt"));
            for (Container c: Container.getNotUsedContainers()) {
                writer.write("-" + c.saveAsString() + "\n");
            }
            writer.close();
            //endregion
            //region saving important
            writer = new BufferedWriter(new FileWriter("Important.txt"));
            writer.write("!!! DON'T MODIFY THIS FILE !!!\n");
            writer.write("Ships counter: " + Ship.getCounter() + "\n");
            writer.write("Container counter: " + Container.getCount() + "\n");
            writer.write("Date: " + Calendar.getDate());
            writer.close();
            //endregion
        } catch (IOException e) {
            e.printStackTrace();
        }

        Main.printWarning("Saved all data");
    }
    private static void save(Storage storage){
        String path = "";
        switch (storage.getType()){
            case WAREHOUSE -> path = "Warehouse";
            case TRAIN -> path = "Train";
            default -> path = "Other";
        }
        path += ".txt";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(storage.saveAsString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Storage loadTrain(){
        Storage train = new Train();
        String[] ss = load("Train.txt");
        loadContainers(train, ss);
        return train;
    }
    public static Storage loadWarehouse() {
        String[] ss = load("Warehouse.txt");
        Warehouse warehouse = new Warehouse(10);
        if (ss.length > 1) {
            warehouse = new Warehouse(ss[0]);
            loadContainers(warehouse, subArr(ss, 1));
        }
        return warehouse;
    }
    public static void loadShips(){
        String[] ss = load("Ship.txt");

        for (int i = 0; i + Ship.argumentsToLoad < ss.length;) {
            Ship ship = new Ship(subArr(ss,i,i+Ship.argumentsToLoad));

            i+=Ship.argumentsToLoad + 1;
            int j = i;
            while(!ss[j].contains("////////")) j++;
            loadContainers(ship,subArr(ss,i,j));
            i=j+1;
        }
    }
    public static void loadImportant(){
        String[] ss = load("Important.txt");
        if(ss.length > 0) {
            try {
                Ship.setCounter(Integer.parseInt(ss[1]));
                Container.setCount(Integer.parseInt(ss[2]));
                Calendar.setDate(LocalDate.parse(ss[3]));
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                Main.printWarning("!!! ATTENTION !!!");
                Main.printWarning("Some important data were modified");
                Main.printWarning("Application will not work correctly\n");
            }
        }
    }

    public static void loadContainers(){
        String[] ss = load("Containers.txt");
        getContainers(ss);
    }
    public static void loadContainers(Storage storage, String[] ss){
        for(Container c: getContainers(ss))
            storage.load(c);
    }
    private static List<Container> getContainers(String[] ss){
        List<Container> cc = new LinkedList<>();
        for (int i = 0; i < ss.length;) {
            if(ss[i] == null){
                i++;
                continue;
            }
            Container.Type type = Container.Type.getType(ss[i]);
            i++;
            if(type == null) {
                Main.printWarning("Could not load TYPE of a container");
                return cc;
            }
            switch (type){
                case STANDARD -> {
                    if(ss.length<i+StandardContainer.argumentsToLoad) {
                        Main.printWarning("Some data of the container " + type + " are missing");
                        Main.printWarning("Container was not loaded");
                        return cc;
                    }
                    cc.add(new StandardContainer(subArr(ss,i,i+StandardContainer.argumentsToLoad)));
                    i+=StandardContainer.argumentsToLoad;
                }
                case HEAVY -> {
                    if(ss.length<i+HeavyContainer.argumentsToLoad) {
                        Main.printWarning("Some data of the container " + type + " are missing");
                        Main.printWarning("Container was not loaded");
                        return cc;
                    }
                    cc.add(new HeavyContainer(subArr(ss,i,i+HeavyContainer.argumentsToLoad)));
                    i+=HeavyContainer.argumentsToLoad;
                }
                case REFRIGERATED -> {
                    if(ss.length<i+RefrigeratedContainer.argumentsToLoad) {
                        Main.printWarning("Some data of the container " + type + " are missing");
                        Main.printWarning("Container was not loaded");
                        return cc;
                    }
                    cc.add(new RefrigeratedContainer(subArr(ss,i,i+RefrigeratedContainer.argumentsToLoad)));
                    i+=RefrigeratedContainer.argumentsToLoad;
                }
                case LIQUID -> {
                    if(ss.length<i+LiquidContainer.argumentsToLoad) {
                        Main.printWarning("Some data of the container " + type + " are missing");
                        Main.printWarning("Container was not loaded");
                        return cc;
                    }
                    cc.add(new LiquidContainer(subArr(ss,i,i+LiquidContainer.argumentsToLoad)));
                    i+=LiquidContainer.argumentsToLoad;
                }
                case EXPLOSIVE -> {
                    if(ss.length<i+ExplosiveContainer.argumentsToLoad) {
                        Main.printWarning("Some data of the container " + type + " are missing");
                        Main.printWarning("Container was not loaded");
                        return cc;
                    }
                    cc.add(new ExplosiveContainer(subArr(ss,i,i+ExplosiveContainer.argumentsToLoad)));
                    i+=ExplosiveContainer.argumentsToLoad;
                }
                case TOXIC_POWDER -> {
                    if(ss.length<i+ToxicPowderContainer.argumentsToLoad) {
                        Main.printWarning("Some data of the container " + type + " are missing");
                        Main.printWarning("Container was not loaded");
                        return cc;
                    }
                    cc.add(new ToxicPowderContainer(subArr(ss,i,i+ToxicPowderContainer.argumentsToLoad)));
                    i+=ToxicPowderContainer.argumentsToLoad;
                }
                case TOXIC_LIQUID -> {
                    if(ss.length<i+ToxicLiquidContainer.argumentsToLoad) {
                        Main.printWarning("Some data of the container " + type + " are missing");
                        Main.printWarning("Container was not loaded");
                        return cc;
                    }
                    cc.add(new ToxicLiquidContainer(subArr(ss,i,i+ToxicLiquidContainer.argumentsToLoad)));
                    i+=ToxicLiquidContainer.argumentsToLoad;
                }
            }
        }
        return cc;
    }

    private static String[] load(String myPath){
        List<String> list = new LinkedList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(myPath));
            String line;
            while((line = reader.readLine()) !=null){
                list.add(line);
            }
            reader.close();
        } catch (IOException e) {
            //e.printStackTrace();
            Main.printWarning("Could not find " + myPath + " file");
            Main.printWarning("Data " + myPath + " was not loaded");
        }
        //change list to array
        String[] ss = new String[list.size()];
        ss = list.toArray(ss);

        for (int i = 0; i < ss.length; i++) {
            if(ss[i].contains(": "))
                ss[i] = ss[i].substring(ss[i].lastIndexOf(": ") + 2);
            else ss[i] = null;
        }

        return ss;
    }

    //region helping functions
    private static String[] subArr(String[] ss, int from, int to){
        String[] newArr = new String[Math.max(0,to-from)];
        for (int i = 0; i < newArr.length; i++) {
            newArr[i] = ss[i+from];
        }
        return newArr;
    }
    private static String[] subArr(String[] ss, int from){
        return subArr(ss,from,ss.length);
    }
    static void printArr(String[] ss){
        for (int i = 0; i < ss.length; i++) {
            System.out.print(ss[i] + " ");
        }
        System.out.println();
    }
    //endregion
}
