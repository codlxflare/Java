public class ElevatorsThread implements Runnable {
    private final ElevatorsManager manager;

    public ElevatorsThread(ElevatorsManager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        while (!ElevatorsManager.requestsOver || manager.hasPendingTasks()) {
            manager.stepElevators();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
