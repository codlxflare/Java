import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class Elevator {
    public int number;
    public int capacity;
    public int currentFloor;
    public ArrayDeque<Integer> targetFloors;
    public int status;
    public List<Integer> passengers;
    public int passengerStatus;

    Elevator(int number) {
        this.number = number;
        capacity = 12;
        currentFloor = 0;
        targetFloors = new ArrayDeque<>();
        status = 0;
        passengers = new ArrayList<>();
        passengerStatus = 0;
    }
}
