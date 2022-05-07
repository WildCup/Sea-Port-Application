import java.util.LinkedList;
import java.util.List;

public abstract class Storage {
    enum Type{SHIP,TRAIN,WAREHOUSE}
    private int maxNumber;
    private List<Container> containers;
    private Type type;

    public Storage(int maxNumber){
        this.maxNumber = maxNumber;
        containers = new LinkedList<>();
    }
    public Storage(String s){
        try{
            maxNumber = Integer.parseInt(s);
        } catch (NumberFormatException e){
            Main.printWarning("Could not load some important data");
        }
        containers = new LinkedList<>();
    }
    //region getters and setters
    public List<Container> getContainers() {
        return containers;
    }
    public int getMaxNumber() { return maxNumber; }
    public void setType(Type type) {
        this.type = type;
    }
    public Type getType() {
        return type;
    }
    //endregion

    public boolean load(Container c, List<Container> cc){
        if(cc.contains(c)){
            if(containers.size() < maxNumber) {
                if(type == Type.SHIP && !containers.isEmpty()){
                    boolean added = false;
                    for (int i = 0; i < containers.size(); i++)
                        if(c.getGrossWeight() < containers.get(i).getGrossWeight()) {
                            containers.add(i, c);
                            added = true;
                            break;
                        }
                    if(!added) containers.add(c);
                }
                else containers.add(c);
                cc.remove(c);
                c.setWhereIsLoaded(this);
                return true;
            }
            else storageIsFull();
        }
        else Main.printWarning("Not such container available");
        return false;
    }
    public boolean load(Container c){
        if(c.getWhereIsLoaded() == null)
            return load(c,Container.getNotUsedContainers());
        else return load(c,c.getWhereIsLoaded().getContainers());
    }
    public abstract void storageIsFull();

//    public void loadAll(List<Container> containers){
//        for (Container c: containers)
//            addContainer(c);
//    }
    public String showContainers(){
        String returnMe = Color.BLUE + "Containers: " + Color.RESET + getContainers().size() + "/" + getMaxNumber() + "\n";
        for (Container c: containers)
            returnMe += "- " + c + "\n";

        return returnMe;
    }
    public String saveAsString(){
        String returnMe = "";
        for (Container c: containers)
            returnMe += "-" + c.saveAsString() + "\n";
        return returnMe;
    }
}
