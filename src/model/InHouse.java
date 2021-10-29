package model;

/**
 * This class creates the InHouse Part objects. It extends the abstract Part class and adds machineID to the instance.
 * @author Jackson Congdon
 */
public class InHouse extends Part{
    private int machineId;
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * @return the machine ID
     */
    public int getMachineId() { return machineId; }

    /**
     * @param machineID the machine ID to set
     */
    public void setMachineId(int machineID) { this.machineId = machineId; }

}