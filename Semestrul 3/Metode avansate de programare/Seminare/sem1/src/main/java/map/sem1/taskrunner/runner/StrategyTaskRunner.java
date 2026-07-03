package map.sem1.taskrunner.runner;

import map.sem1.taskrunner.domain.Container;
import map.sem1.taskrunner.domain.Strategy;
import map.sem1.taskrunner.domain.Task;
import map.sem1.taskrunner.factory.TaskContainerFactory;

public class StrategyTaskRunner implements TaskRunner {

    private Container container;

    public StrategyTaskRunner(Strategy strategy) {
        this.container = TaskContainerFactory.getInstance().createContainer(strategy);
    }

    @Override
    public void executeOneTask() {
        if(!container.isEmpty()) {
            Task t = container.remove();
            t.execute();
        }
    }

    @Override
    public void executeAll() {
        while(!container.isEmpty()) {
            executeOneTask();
        }

    }

    @Override
    public void addTask(Task t) {
        container.add(t);

    }

    @Override
    public boolean hasTask() {
        return !container.isEmpty();
    }
}
