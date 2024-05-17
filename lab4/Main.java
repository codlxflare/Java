import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        elevatorDefaultTime();
        userInputs();
    }

    private static void elevatorDefaultTime() {
        System.out.println("Время лифта на один этаж по умолчанию 1000мс = 1с");
    }

    private static void userInputs() {
        Scanner scanner = new Scanner(System.in);

        // Настройки лифтов
        System.out.print("Введите количество этажей: ");
        var floorsCount = scanner.nextInt();

        System.out.print("Введите количество запросов: ");
        var requestsCount = scanner.nextInt();

        System.out.print("Введите интервал запросов (мс): ");
        var requestsInterval = scanner.nextInt();

        initializeManagerAndThreads(floorsCount, requestsCount, requestsInterval);
    }

    private static void initializeManagerAndThreads(int floorsCount, int requestsCount, int requestsInterval) {
        ElevatorsManager manager = new ElevatorsManager(floorsCount);

        Thread requests = new Thread(new RequestThread(manager, requestsCount, requestsInterval));
        Thread elevators = new Thread(new ElevatorsThread(manager));

        startAndJoinThreads(requests, elevators);
    }

    private static void startAndJoinThreads(Thread requests, Thread elevators) {
        requests.start();
        elevators.start();

        try {
            requests.join();
            elevators.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


}
