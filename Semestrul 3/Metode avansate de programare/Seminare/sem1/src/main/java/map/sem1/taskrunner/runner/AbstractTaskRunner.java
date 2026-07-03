package map.sem1.taskrunner.runner;

import map.sem1.taskrunner.domain.Task;

public abstract class AbstractTaskRunner implements TaskRunner {
    private TaskRunner runner;
    AbstractTaskRunner(TaskRunner runner) {
        this.runner = runner;
    }
    @Override
    public void executeOneTask() {
        runner.executeOneTask();
    }

    @Override
    public void executeAll() {
        while(hasTask()) {
            executeOneTask();
        }
    }

    @Override
    public void addTask(Task t) {
        runner.addTask(t);
    }

    @Override
    public boolean hasTask() {
        return runner.hasTask();
    }
}
