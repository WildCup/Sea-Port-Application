import java.time.LocalDate;
import java.util.List;

public class Warehouse extends Storage{

    public Warehouse(int maxNumber){
        super(maxNumber);
        setType(Type.WAREHOUSE);
    }
    public Warehouse(String s){
        super(s);
        setType(Type.WAREHOUSE);
    }

    @Override
    public void storageIsFull() {
        Main.printWarning("Too many containers in the Warehouse");
    }

    @Override
    public boolean load(Container c, List<Container> cc) {
        boolean loaded = false;
        Sender s = c.getSender();
        if(!s.getIsIrresponsible()) {
            loaded = super.load(c, cc);
            if (loaded && c.getArrivalDate() == null) {
                LocalDate d1 = Calendar.getDate();
                LocalDate d2;
                Container.Type type = c.getType();

                c.setArrivalDate(d1);
                d2 = switch (type) {
                    case EXPLOSIVE -> d1.plusDays(5);
                    case TOXIC_LIQUID -> d1.plusDays(10);
                    case TOXIC_POWDER -> d1.plusDays(14);
                    default -> null;
                };
                c.setExpireDate(d2);
            }
        }
        else{
            System.out.println("Container " + c+ Color.RED + " was not accepted and should be returned to " + Color.RESET + s);
        }
        return loaded;
    }

    public void checkTime() {
        List<Container> cc = getContainers();
        for (int i = 0; i < cc.size(); i++) {
            Container c = cc.get(i);
            try {
                c.checkTime();
            } catch (IrresponsibleSenderWithDangerousGoods e) {
                e.printStackTrace();
                cc.remove(i);
                i--;
            }
        }

    }

    @Override
    public String toString() {
        return Color.BLUE + "The Warehouse" + Color.RESET;
    }

    @Override
    public String saveAsString() {
        return "Max number of Containers: " + getMaxNumber() + "\n" +
                super.saveAsString();
    }
}
//class Arrival{
//    private Container container;
//    private LocalDate arrivalDate;
//    private LocalDate expireDate;
//
//    public Arrival(Container container, LocalDate arrivalDate){
//        this.container = container;
//        this.arrivalDate = arrivalDate;
//
//        expireDate = switch (container.getType()){
//            case EXPLOSIVE -> arrivalDate.plusDays(5);
//            case TOXIC_LIQUID -> arrivalDate.plusDays(10);
//            case TOXIC_POWDER -> arrivalDate.plusDays(14);
//            default -> null;
//        };
//    }
//}
