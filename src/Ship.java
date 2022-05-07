import java.util.LinkedList;
import java.util.List;

public class Ship extends Storage {
    private int maxToxOrExp;
    private int maxHeavy;
    private int maxElect;
    private double maxWeight;

    private int currentToxOrExp = 0;
    private int currentHeavy = 0;
    private int currentElect = 0;
    private double currentWeight = 0;

    private int id;
    private static int counter = 0;
    public static final int argumentsToLoad = 10;
    private static List<Ship> allShips = new LinkedList<>();

    private String name;
    private String home;
    private String origin;
    private String destination;

    public Ship(int maxNumber, double maxWeight, int maxToxOrExp, int maxHeavy, int maxElect, String name, String home, String origin, String destination) {
        super(maxNumber);
        this.maxWeight = maxWeight;
        id = counter++;

        this.maxToxOrExp = maxToxOrExp;
        this.maxHeavy = maxHeavy;
        this.maxElect = maxElect;

        this.name = name;
        this.home = home;
        this.origin = origin;
        this.destination = destination;

        sortAddShip();
        setType(Type.SHIP);
    }
    public Ship(String[] ss){
        super(ss[5]);
        //name,id,home.origin,des,maxNumber,maxWeight,maxHeavy,maxElect
        name = ss[0];
        home = ss[2];
        origin = ss[3];
        destination = ss[4];

        int maxN;
        try{
            id = Integer.parseInt(ss[1]);
            maxWeight = Double.parseDouble(ss[6]);
            maxToxOrExp = Integer.parseInt(ss[7]);
            maxHeavy = Integer.parseInt(ss[8]);
            maxElect = Integer.parseInt(ss[9]);
        }
        catch (NumberFormatException e){
            Main.printWarning("Could not load some of the ship's data");
            return;
        }
        allShips.add(this);
        setType(Type.SHIP);
    }
    //region getters and setters
    public static List<Ship> getAllShips() {
        return allShips;
    }
    public int getId() {
        return id;
    }
    public static int getCounter() {
        return counter;
    }
    public static void setCounter(int counter) {
        Ship.counter = counter;
    }
    public static Ship getShip(int id) throws IndexOutOfBoundsException{
        for(Ship s: allShips)
            if(s.id == id) return s;
        throw new IndexOutOfBoundsException();
    }
    //endregion
    @Override
    public boolean load(Container c, List<Container> cc) {
        if(currentWeight + c.getGrossWeight() > maxWeight){
            Main.printWarning("containers are too heavy");
            return false;
        }
        switch (c.getType()) {
            case HEAVY -> {
                if (currentHeavy >= maxHeavy) {
                    System.out.println("too many heavy containers");
                    return false;
                } else {
                    if (super.load(c, cc)) {
                        currentWeight += c.getGrossWeight();
                        currentHeavy++;
                        return true;
                    }
                }
            }
            case REFRIGERATED -> {
                if (currentHeavy >= maxHeavy) {
                    System.out.println("too many heavy containers");
                    return false;
                } else if (currentElect >= maxElect) {
                    System.out.println("too many electric containers");
                    return false;
                } else {
                    if (super.load(c, cc)) {
                        currentWeight += c.getGrossWeight();
                        currentHeavy++;
                        currentElect++;
                        return true;
                    }
                }

            }
            case TOXIC_LIQUID, TOXIC_POWDER, EXPLOSIVE -> {
                if (currentToxOrExp >= maxToxOrExp) {
                    System.out.println("too many Toxic/Explosive containers");
                    return false;
                } else {
                    if (super.load(c, cc)) {
                        currentWeight += c.getGrossWeight();
                        currentToxOrExp++;
                        return true;
                    }
                }
            }
            default -> {
                if (super.load(c, cc)) {
                    currentWeight += c.getGrossWeight();
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean load(int shipId, Container c, List<Container> cc) {
        for(Ship s: allShips){
            if(s.id == shipId)
                return s.load(c,cc);
        }
        Main.printWarning("Ship not found");
        return false;
    }
    public void departure(){
        System.out.println(this + " departed from the seaport");
        getAllShips().remove(this);
    }
    //    public void unload(Storage storage) {
    //        storage.loadAll(getContainers());
    //    }
    @Override
    public void storageIsFull() {
        Main.printWarning("Too many containers on the ship");
    }
    @Override
    public String toString() {
        String returnMe = Color.BLUE + name + Color.RESET + " id: " + Color.BLUE + id + Color.RESET;
        return returnMe;
    }
    public String toStringDetails(){
        String returnMe = toString()+ "\n" +
                "made in " + home + "\n" +
                "from: " + origin + "\n" +
                "to: " + destination + "\n";
        returnMe += showContainers();

        returnMe += "Details: \n" +
                "- weight loaded: " + currentWeight + "kg / " + maxWeight + "kg\n" +
                "- Toxic/Explosive containers: " + currentToxOrExp + "/" + maxToxOrExp + "\n" +
                "- Heavy containers: " + currentHeavy + "/" + maxHeavy + "\n" +
                "- containers requiring electricity: " + currentElect + "/" + maxElect;
        return returnMe;
    }
    public static String toStringAllShips(){
        if(allShips.isEmpty())
            return "Currently there are no ships";
        else {
            String returnMe = "All Ships: \n";
            for(Ship s: allShips)
                returnMe += "- " + s + "\n";
            return returnMe;
        }
    }

    @Override
    public String saveAsString() {
        return  "Name: " + name + "\n" +
                "Id: " + id + "\n" +
                "Home: " + home + "\n" +
                "Origin: " + origin + "\n" +
                "Destination: " + destination + "\n" +
                "Max number of Containers: : " + getMaxNumber() + "\n" +
                "MaxWeight: " + maxWeight + "\n" +
                "MaxToxOrExp: " + maxToxOrExp + "\n" +
                "MaxHeavy: " + maxHeavy + "\n" +
                "MaxElect: " + maxElect + "\n" +
                "Containers: " + "\n" +
                super.saveAsString();
    }
    private void sortAddShip(){
        if(allShips.isEmpty()) {
            allShips.add(this);
            return;
        }
        for (int i = 0; i < allShips.size(); i++) {
            if(compareStrings(name,allShips.get(i).name)) {
                allShips.add(i, this);
                return;
            }
        }
        allShips.add(this);
    }
    private static boolean compareStrings(String s1, String s2){
        int i = 0;
        while (i < s1.length() && i < s2.length()) {
            if (s1.charAt(i) > s2.charAt(i))
                return true;
            i++;
        }
        return s1.length() > s2.length();
    }
}
