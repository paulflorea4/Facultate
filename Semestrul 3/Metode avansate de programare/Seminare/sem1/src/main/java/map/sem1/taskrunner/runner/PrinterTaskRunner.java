package map.sem1.taskrunner.runner;

public class PrinterTaskRunner extends AbstractTaskRunner{


   public PrinterTaskRunner(TaskRunner runner) {
        super(runner);
    }

    @Override
    public void executeOneTask() {
        super.executeOneTask();
        System.out.println("Task executat la ora: " + java.time.LocalDateTime.now());
    }


}
