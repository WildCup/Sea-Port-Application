import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Sender {
    private String name;
    private String surname;
    private String pesel;
    private String address;
    private int id;
    private static int count = 0;

    private static List<Sender> senders = new LinkedList<>();
    private List<IrresponsibleSenderWithDangerousGoods> warnings;
    private boolean isIrresponsible = false;

    public Sender(String name, String surname, String pesel, String address){
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.address = address;
        id=count++;
        warnings = new LinkedList<>();
        senders.add(this);
    }
    //region Getters and Setters
    public LocalDate getDateFromPesel(){
        int code = Integer.parseInt(pesel.substring(0,6));
        int d = code % 100; //01 30 29 06492
        code /= 100;
        int m = code % 100;
        code /= 100;
        int y = code;
        if(m < 20){
            y += 1900;
        } else if(m < 80){
            y += 2000;
            m -= 20;
        }else{
            y += 1800;
            m -= 80;
        }
        return LocalDate.of(y,m,d);
    }
    public static List<Sender> getSenders() {
        return senders;
    }
    public boolean getIsIrresponsible(){ return isIrresponsible;}
    public String getName() {
        return name + " " + surname;
    }
    public int getId() {
        return id;
    }
    //endregion

    public void receiveWarning(IrresponsibleSenderWithDangerousGoods warning){
        warnings.add(warning);
        if(warnings.size() >= 2) isIrresponsible = true;
    }

    @Override
    public String toString() {
        return name + " " + surname + " id: "+  Color.BLUE + id + Color.RESET;
    }
    public String toStringDetails() {
        String returnMe = toString() + "\n" +
                "PESEL: "+  pesel + "\n" +
                "Birth date: " + getDateFromPesel() + "\n" +
                "Address: " + address;
        if(isIrresponsible) returnMe += Color.RED + "\nThis sender is irresponsible!" + Color.RESET;
        return returnMe;
    }
    public static String allSendersToString(){
        String returnMe = "";
        for(Sender s : senders) {
            returnMe += " - " + s;
            if(s.isIrresponsible) returnMe += Color.RED + " - irresponsible ";
            returnMe += Color.RESET + "\n";
        }
        return returnMe;
    }
}
