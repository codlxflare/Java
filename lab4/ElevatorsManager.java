import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ElevatorsManager {
    private static final List<Request> requestQueue = new ArrayList<>();
    private final Map<Integer, ConcurrentLinkedDeque<Integer>[]> floors;
    private final Elevator[] elevators;
    public final int floorsCount;
    public static volatile boolean requestsOver = false;

    @SuppressWarnings("unchecked")
    public ElevatorsManager(int floorsCount) {
        this.floorsCount = floorsCount;
        this.elevators = new Elevator[] {new Elevator(1), new Elevator(2)};
        this.floors = new HashMap<>(floorsCount + 1);

        for (int i = 0; i <= floorsCount; i++) {
            ConcurrentLinkedDeque<Integer>[] queues = new ConcurrentLinkedDeque[] {new ConcurrentLinkedDeque<>(), new ConcurrentLinkedDeque<>()};
            floors.put(i, queues);
        }
    }


    public synchronized void stepElevators() {
        for (Elevator elevator : elevators) {
            processCurrentFloor(elevator);
            updateElevatorStatus(elevator);
        }
    }

    public synchronized void addRequest(Request request) {
        System.out.println("Новый запрос на " + request.start + " этаже, направление " + (request.direction == 1 ? "вверх" : "вниз"));
        floors.get(request.start)[request.direction == 1 ? 0 : 1].add(request.end);
        assignElevatorToRequest(request);
    }

    private void assignElevatorToRequest(Request request) {
        Elevator bestElevator = null;
        int minDistance = Integer.MAX_VALUE;

        for (Elevator elevator : elevators) {
            int distance = Math.abs(elevator.currentFloor - request.start);
            if (distance < minDistance && (elevator.targetFloors.isEmpty() ||
                    elevator.passengerStatus == request.direction ||
                    elevator.passengerStatus == 0)) {
                bestElevator = elevator;
                minDistance = distance;
            }
        }

        if (bestElevator != null) {
            bestElevator.targetFloors.add(request.start);
            bestElevator.passengerStatus = request.direction;
            System.out.println("Лифт " + bestElevator.number + " взял запрос на этаже " + request.start + ", направление " +
                    (request.direction == 1 ? "вверх" : "вниз"));
        } else {
            requestQueue.add(request);
            System.out.println("Все лифты заняты, запрос помещен в очередь");
        }
    }

    private void processCurrentFloor(Elevator elevator) {
        unloadPassengers(elevator);
        if (elevator.passengerStatus != 0) {
            loadPassengers(elevator, elevator.passengerStatus);
        } else {
            int upQueueSize = floors.get(elevator.currentFloor)[0].size();
            int downQueueSize = floors.get(elevator.currentFloor)[1].size();
            if (upQueueSize >= downQueueSize) {
                loadPassengers(elevator, 1);
            } else {
                loadPassengers(elevator, -1);
            }
        }
    }

    private void unloadPassengers(Elevator elevator) {
        while (elevator.passengers.contains(elevator.currentFloor)) {
            int passengerCount = elevator.passengers.size();
            elevator.passengers.removeAll(Collections.singletonList(elevator.currentFloor));
            elevator.targetFloors.removeAll(Collections.singletonList(elevator.currentFloor));
            System.out.println((passengerCount - elevator.passengers.size()) + " Пассажир(ы) вышли из лифта " + elevator.number + " на этаже " + elevator.currentFloor);
            if (elevator.passengers.isEmpty()) {
                elevator.passengerStatus = 0;
            }
        }
    }

    private void loadPassengers(Elevator elevator, int direction) {
        ConcurrentLinkedDeque<Integer> floorQueue = floors.get(elevator.currentFloor)[direction == 1 ? 0 : 1];
        while (!floorQueue.isEmpty() && elevator.passengers.size() < elevator.capacity) {
            int currentPassenger = floorQueue.poll();
            elevator.targetFloors.add(currentPassenger);
            elevator.passengers.add(currentPassenger);
            System.out.println("Пассажир вошел в лифт " + elevator.number + " на этаже " + elevator.currentFloor);
        }
        elevator.status = direction;
    }

    private void updateElevatorStatus(Elevator elevator) {
        if (elevator.targetFloors.isEmpty()) {
            handleNoTargetFloors(elevator);
        } else {
            moveToNextTarget(elevator);
        }

        if (elevator.status == 0) {
            System.out.println("Лифт " + elevator.number + " ждет на этаже " + elevator.currentFloor);
        } else if (elevator.status == -1) {
            elevator.currentFloor = Math.max(0, elevator.currentFloor - 1);
            System.out.println("Лифт " + elevator.number + " идет ↓. Текущий этаж = " + elevator.currentFloor);
        } else if (elevator.status == 1){
            elevator.currentFloor = Math.min(floorsCount, elevator.currentFloor + 1);
            System.out.println("Лифт " + elevator.number + " идет ↑. Текущий этаж = " + elevator.currentFloor);
        }
    }

    private void handleNoTargetFloors(Elevator elevator) {
        if (requestQueue.isEmpty()) {
            elevator.status = 0;
            elevator.passengerStatus = 0;
        } else {
            Request request = requestQueue.remove(0);
            elevator.targetFloors.add(request.start);
            elevator.passengerStatus = request.direction;
            elevator.status = Integer.compare(request.start, elevator.currentFloor);
            System.out.println("Лифт " + elevator.number + " взял запрос на этаже " + request.start
                    + ", направление " + (request.direction == 1 ? "вверх" : "вниз"));
        }
    }

    private void moveToNextTarget(Elevator elevator) {
        Integer nextFloor = elevator.targetFloors.peek();
        if (nextFloor != null && nextFloor.equals(elevator.currentFloor)) {
            elevator.targetFloors.remove();
        } else {
            elevator.status = Integer.compare(nextFloor, elevator.currentFloor);
        }
    }

    public boolean hasPendingTasks() {
        return !requestQueue.isEmpty() || Arrays.stream(elevators).anyMatch(elevator -> !elevator.targetFloors.isEmpty());
    }

    public static void main(String[] args) {
        ElevatorsManager manager = new ElevatorsManager(10);
        manager.addRequest(new Request(1, 5, 1));
        manager.addRequest(new Request(3, 8, 1));
        manager.addRequest(new Request(9, 2, -1));

        Thread elevatorThread = new Thread(new ElevatorsThread(manager));
        elevatorThread.start();

        while (!requestsOver) {
            manager.stepElevators();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}





