package duck_taskrunner;

import taskrunner.domain.Container;
import taskrunner.domain.Strategy;
import taskrunner.domain.Task;
import taskrunner.factory.TaskContainerFactory;
import taskrunner.runner.TaskRunner;

public class NatatieTaskRunner implements TaskRunner {
    private Container container;

    public NatatieTaskRunner(Strategy strategy) {
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
