import java.util.List;

public class Train extends Storage{

    private boolean isBusy = false;
    public Train(){
        super(10);
        setType(Type.TRAIN);
    }

    @Override
    public boolean load(Container c, List<Container> cc) {
        if(!isBusy) {
            boolean loaded = super.load(c, cc);
            if (loaded && getContainers().size() == getMaxNumber())
                storageIsFull();
            return loaded;
        }
        else {
            Main.printWarning("Train is currently busy");
            return false;
        }
    }

    @Override
    public void storageIsFull() {
        Thread thread = new Thread(() -> {
            isBusy = true;
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Main.arriveNewTrain();
        });
        Main.printWarning("The train set off");
        thread.start();
    }
    @Override
    public String toString() {
        return Color.BLUE + "The Train" + Color.RESET;
    }

    //region Saving and Loading
    @Override
    public String saveAsString(){
        return super.saveAsString();
    }
}
