import java.time.LocalDate;

public class IrresponsibleSenderWithDangerousGoods extends Exception{
    private Container container;
    private LocalDate arrivalDate;
    private LocalDate expireDate;
    private int id;

    public IrresponsibleSenderWithDangerousGoods(Container container, String msg){
        super(msg);
        this.container=container;
        arrivalDate = container.getArrivalDate();
        expireDate = container.getExpireDate();
        id=container.getId();
        container.getSender().receiveWarning(this);
    }
}
