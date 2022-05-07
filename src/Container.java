import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.List;

public abstract class Container {
    enum Type {
        STANDARD, HEAVY, REFRIGERATED, LIQUID, EXPLOSIVE, TOXIC_POWDER, TOXIC_LIQUID;
        public static Type getType(String s){
            Type type = null;
            switch (s){
                case "STANDARD" -> type = STANDARD;
                case "HEAVY" -> type = HEAVY;
                case "REFRIGERATED" -> type = REFRIGERATED;
                case "LIQUID" -> type = LIQUID;
                case "EXPLOSIVE" -> type = EXPLOSIVE;
                case "TOXIC_POWDER" -> type = TOXIC_POWDER;
                case "TOXIC_LIQUID" -> type = TOXIC_LIQUID;
            }
            return type;
        }
    }

    private Sender sender;
    private double tare;
    private double netWeight;
    private double grossWeight; //= net + tare
    private String certificate;
    private String security;
    private Type type;
    private int id;
    private static int count = 0;
    public final static int argumentsToLoad = 8;

    private static List<Container> notUsedContainers = new LinkedList<>();
    private Storage whereIsLoaded = null;

    private LocalDate arrivalDate;
    private LocalDate expireDate;

    public Container(Sender sender, double tare, double netWeight, String certificate, String security){
        this.sender = sender;
        this.tare = tare;
        this.netWeight = netWeight;
        this.grossWeight = netWeight + tare;
        this.certificate = certificate;
        this.security = security;
        id = count++;
        notUsedContainers.add(this);
    }
    public Container(String[] ss){
        try{
            id = Integer.parseInt(ss[0]);
            int sendersId = Integer.parseInt(ss[1]);
            for (Sender s: Sender.getSenders()){
                if(s.getId() == sendersId){
                    sender = s;
                    break;
                }
            }
            tare = Double.parseDouble(ss[2]);
            netWeight = Double.parseDouble(ss[3]);
            grossWeight = netWeight + tare;

            try{
                arrivalDate = LocalDate.parse(ss[6]);
                expireDate  = LocalDate.parse(ss[7]);
            }
            catch (DateTimeParseException ignored){}

        }catch (NumberFormatException e){
            Main.printWarning("Could not load some container's data");
            Main.printWarning("Make sure you formatted all files correctly");
            return;
        }
        certificate = ss[4];
        security = ss[5];
        notUsedContainers.add(this);
    }
    //region getters and setters
    public void setType(Type myType){ type = myType; }
    public Type getType() { return type; }
    public void setWhereIsLoaded(Storage whereIsLoaded) {this.whereIsLoaded = whereIsLoaded; }
    public Storage getWhereIsLoaded(){ return whereIsLoaded; }
    public static List<Container> getNotUsedContainers() { return notUsedContainers; }
    public int getId() { return id; }
    public static int getCount() {return count;}
    public static void setCount(int count) {Container.count = count;}
    public double getGrossWeight() { return grossWeight; }
    public Sender getSender() {return sender;}
    public void setArrivalDate(LocalDate arrivalDate) {this.arrivalDate = arrivalDate;}
    public void setExpireDate(LocalDate expireDate) {this.expireDate = expireDate;}
    public LocalDate getExpireDate() {return expireDate;}
    public LocalDate getArrivalDate() {return arrivalDate;}
    public static Container getContainer(int id) throws IndexOutOfBoundsException{
        for(Container c: notUsedContainers)
            if(c.id == id) return c;
        throw new IndexOutOfBoundsException();
    }
    public static Container findContainer(int id, Storage warehouse, Storage train ) throws IndexOutOfBoundsException{
        Container container = null;
        for(Container c: Container.getNotUsedContainers())
            if(id==c.getId()) container = c;
        if(container == null) for(Container c: warehouse.getContainers())
            if(id==c.getId()) container = c;
        if(container == null) for(Container c: train.getContainers())
            if(id==c.getId()) container = c;
        if(container == null) for(Ship s: Ship.getAllShips())
            for(Container c: s.getContainers()) if(id==c.getId()) container = c;
        if(container == null) throw new IndexOutOfBoundsException();
        else return container;
    }
    //endregion

    public void checkTime() throws IrresponsibleSenderWithDangerousGoods{
        LocalDate today = Calendar.getDate();
        if(expireDate != null && (today.isAfter(expireDate) || today.isEqual(expireDate))){
            //whereIsLoaded.getContainers().remove(this);
            String msg = "\n" +
                    "           Container " + this + " was disposed \n" +
                    "           Sender " + sender + " received a warning" + "\n" +
                    "           Arrive date: " + arrivalDate + "    Dispose date: " + expireDate;
            throw new IrresponsibleSenderWithDangerousGoods(this,msg);
        }
    }

    @Override
    public String toString() {
        String returnMe = Color.BLUE + type.toString() + Color.RESET;
        for (int i = type.toString().length(); i < 14; i++) {
            returnMe += " ";
        }
        returnMe += " id: " + Color.BLUE + id + Color.RESET;

        if(id<10) returnMe += " ";
        if(id<100) returnMe += " ";

        if(whereIsLoaded != null) {
            switch (whereIsLoaded.getType()) {
                case SHIP -> returnMe += " mass: " + grossWeight + "kg";
                case WAREHOUSE -> returnMe += " stored: " + arrivalDate;
                case TRAIN -> returnMe += " sender: " + sender;
            }
        } else returnMe += " mass: " + grossWeight + "kg";
        return returnMe;
    }
    public String toStringDetails(){
        String returnMe = toString() + "\n";
        if(whereIsLoaded == null)
            returnMe += "Not loaded anywhere" + "\n";
        else returnMe += "Loaded on " + whereIsLoaded + "\n";
        returnMe +=
                "Sender: " + sender + "\n" +
                "Tare: " + tare + "kg\n" +
                "Net weight: " + netWeight + "kg\n" +
                "Certificate: " + certificate + "\n" +
                "Security: " + security;

        return returnMe;
    }
    public static String toStringAllContainers(){
        if(notUsedContainers.isEmpty())
            return "Currently there are no containers not loaded anywhere.\n";
        else {
            String returnMe = "All containers not loaded anywhere: \n";
            for(Container c: notUsedContainers)
                returnMe += "- " + c + "\n";
            return returnMe;
        }
    }
    public String saveAsString(){
        String returnMe =
                "  Type: " + type + "\n" +
                "   Id: " + id + "\n" +
                "   Sender " + sender.getName() + " id: " +  sender.getId() + "\n" +
                "   Tare (kg): " + tare + "\n" +
                "   Net weight (kg): " + netWeight + "\n" +
                "   Certificate: " + certificate + "\n" +
                "   Security: " + security;

        //if(whereIsLoaded != null && whereIsLoaded.getType() == Storage.Type.WAREHOUSE){
            returnMe+="\n   arrivalDate: " + arrivalDate + "\n" +
                        "   expireDate: " + expireDate;


        return returnMe;
    }
}

//region All Containers

class StandardContainer extends Container{
    Color color;
    public final static int argumentsToLoad = Container.argumentsToLoad + 1;
    public StandardContainer(Sender sender, double tare, double netWeight, String certificate, String security, Color color){
        super(sender, tare, netWeight, certificate, security);
        this.color = color;
        setType(Type.STANDARD);
    }
    public StandardContainer(String[] ss){
        super(ss);
        color = Color.getColor(ss[argumentsToLoad-1]);
        setType(Type.STANDARD);
    }

    @Override
    public String toStringDetails() {
        return super.toStringDetails() + "\n" +
                "Color: " + color + color.getName() + Color.RESET;
    }
    @Override
    public String saveAsString() {
        return super.saveAsString() + "\n" +
                "   Color: " + color.getName();
    }
}
class HeavyContainer extends StandardContainer{
    String toughness; // certificate?
    public final static int argumentsToLoad = StandardContainer.argumentsToLoad + 1;
    public HeavyContainer(Sender sender, double tare, double netWeight, String certificate, String security, Color color, String toughness){
        super(sender, tare, netWeight, certificate, security, color);
        this.toughness = toughness;
        setType(Type.HEAVY);
    }
    public HeavyContainer(String[] ss){
        super(ss);
        toughness = ss[argumentsToLoad - 1];
        setType(Type.HEAVY);
    }

    @Override
    public String toStringDetails() {
        return super.toStringDetails() + "\n" +
                "Toughness: " + toughness;
    }
    @Override
    public String saveAsString() {
        return super.saveAsString() + "\n" +
                "   Toughness: " + toughness;
    }
}
class RefrigeratedContainer extends HeavyContainer{
    double power; //Wat
    public final static int argumentsToLoad = HeavyContainer.argumentsToLoad + 1;
    public RefrigeratedContainer(Sender sender, double tare, double netWeight, String certificate, String security, Color color, String toughness, double power){
        super(sender, tare, netWeight, certificate, security, color,toughness);
        this.power = power;
        setType(Type.REFRIGERATED);
    }
    public RefrigeratedContainer(String[] ss){
        super(ss);
        try {
            power = Double.parseDouble(ss[argumentsToLoad - 1]);
        } catch (NumberFormatException e){
            Main.printWarning("Could not load some data");
        }
        setType(Type.REFRIGERATED);
    }

    @Override
    public String toStringDetails() {
        return super.toStringDetails() + "\n" +
                "Power: " + power + "W";
    }
    @Override
    public String saveAsString() {
        return super.saveAsString() + "\n" +
                "   Power (W) : " + power;
    }
}
class LiquidContainer extends StandardContainer implements LiquidInterface{
    double maxPressure; //10^6 Pascal
    public final static int argumentsToLoad = StandardContainer.argumentsToLoad + 1;
    public LiquidContainer(Sender sender, double tare, double netWeight, String certificate, String security, Color color, double maxPressure){
        super(sender, tare, netWeight, certificate, security, color);
        this.maxPressure = maxPressure;
        setType(Type.LIQUID);
    }
    public LiquidContainer(String[] ss){
        super(ss);
        try {
            maxPressure = Double.parseDouble(ss[argumentsToLoad - 1]);
        } catch (NumberFormatException e){
            Main.printWarning("Could not load some data");
        }
        setType(Type.LIQUID);
    }

    @Override
    public String toStringDetails() {
        return super.toStringDetails() + "\n" +
                "Max pressure: " + maxPressure + "MPa";
    }
    @Override
    public String saveAsString() {
        return super.saveAsString() + "\n" +
                "   Max pressure (MPa) : " + maxPressure;
    }

    @Override
    public void printInfoAboutLiquid() {
        System.out.println("Current lvl of liquid may be found on the back of the container");
    }
}
class ExplosiveContainer extends StandardContainer{
    double shockAbsorption; //max Newton
    public final static int argumentsToLoad = StandardContainer.argumentsToLoad + 1;
    public ExplosiveContainer(Sender sender, double tare, double netWeight, String certificate, String security, Color color, double shockAbsorption){
        super(sender, tare, netWeight, certificate, security, color);
        this.shockAbsorption = shockAbsorption;
        setType(Type.EXPLOSIVE);
    }
    public ExplosiveContainer(String[] ss){
        super(ss);
        try {
            shockAbsorption = Double.parseDouble(ss[argumentsToLoad - 1]);
        } catch (NumberFormatException e){
            Main.printWarning("Could not load some data");
        }
        setType(Type.EXPLOSIVE);
    }

    @Override
    public String toStringDetails() {
        return super.toStringDetails() + "\n" +
                "Shock absorption: " + shockAbsorption + "N";
    }
    @Override
    public String saveAsString() {
        return super.saveAsString() + "\n" +
                "   Shock absorption (N) : " + shockAbsorption;
    }
}
abstract class ToxicContainer extends StandardContainer{
    int safetyLvl;
    public final static int argumentsToLoad = StandardContainer.argumentsToLoad + 1;
    public ToxicContainer(Sender sender, double tare, double netWeight, String certificate, String security, Color color, int safetyLvl){
        super(sender, tare, netWeight, certificate, security, color);
        this.safetyLvl = safetyLvl;
    }
    public ToxicContainer(String[] ss){
        super(ss);
        try {
            safetyLvl = Integer.parseInt(ss[argumentsToLoad - 1]);
        } catch (NumberFormatException e){
            Main.printWarning("Could not load some data");
        }
    }

    @Override
    public String toStringDetails() {
        return super.toStringDetails() + "\n" +
                "Safety level: " + safetyLvl;
    }
    @Override
    public String saveAsString() {
        return super.saveAsString() + "\n" +
                "   Safety level: " + safetyLvl;
    }
}
class ToxicPowderContainer extends ToxicContainer{
    double maxTemp; //K
    public final static int argumentsToLoad = ToxicContainer.argumentsToLoad + 1;
    public ToxicPowderContainer(Sender sender, double tare, double netWeight, String certificate, String security, Color color, int safetyLvl, double maxTemp){
        super(sender, tare, netWeight, certificate, security, color, safetyLvl);
        this.maxTemp = maxTemp;
        setType(Type.TOXIC_POWDER);
    }
    public ToxicPowderContainer(String[] ss){
        super(ss);
        try {
            maxTemp = Double.parseDouble(ss[argumentsToLoad - 1]);
        } catch (NumberFormatException e){
            Main.printWarning("Could not load some data");
        }
        setType(Type.TOXIC_POWDER);
    }

    @Override
    public String toStringDetails() {
        return super.toStringDetails() + "\n" +
                "Max Temperature: " + maxTemp + "K";
    }
    @Override
    public String saveAsString() {
        return super.saveAsString() + "\n" +
                "   Max Temperature (K): " + maxTemp;
    }
}
class ToxicLiquidContainer extends ToxicContainer implements LiquidInterface { //extends LiquidContainer ??
    double maxPH;
    public final static int argumentsToLoad = ToxicContainer.argumentsToLoad + 1;
    public ToxicLiquidContainer(Sender sender, double tare, double netWeight, String certificate, String security, Color color, int safetyLvl, double maxPH){
        super(sender, tare, netWeight, certificate, security, color, safetyLvl);
        this.maxPH = maxPH;
        setType(Type.TOXIC_LIQUID);
    }
    public ToxicLiquidContainer(String[] ss){
        super(ss);
        try {
            maxPH = Double.parseDouble(ss[argumentsToLoad - 1]);
        } catch (NumberFormatException e){
            Main.printWarning("Could not load some data");
        }
        setType(Type.TOXIC_LIQUID);
    }

    @Override
    public String toStringDetails() {
        return super.toStringDetails() + "\n" +
                "Max PH: " + maxPH;
    }
    @Override
    public String saveAsString() {
        return super.saveAsString() + "\n" +
                "   Max PH: " + maxPH;
    }

    @Override
    public void printInfoAboutLiquid() {
        System.out.println("Current lvl of liquid may be found on the back of the container");
    }
}
//endregion

interface LiquidInterface{
    abstract void printInfoAboutLiquid();
}

