import java.time.LocalDate;

public class Calendar extends Thread{
    private static LocalDate date = LocalDate.now();
    private Warehouse warehouse;

    public Calendar(Warehouse warehouse){
        this.warehouse = warehouse;
    }

    public static LocalDate getDate() {
        return date;
    }
    public static void setDate(LocalDate date) {
        Calendar.date = date;
    }

    @Override
    public void run() {
        while(!Thread.interrupted()){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                return;
            }
            date = date.plusDays(1);
            warehouse.checkTime();
        }
    }
}
