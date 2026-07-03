package taskrunner.factory;

import taskrunner.domain.Container;
import taskrunner.domain.QueueContainer;
import taskrunner.domain.StackContainer;
import taskrunner.domain.Strategy;

public class TaskContainerFactory implements Factory{
    private TaskContainerFactory(){}

    private static TaskContainerFactory instance = null;
    public static TaskContainerFactory getInstance() {
        if (instance != null) {
            return instance;
        }else {
            instance = new TaskContainerFactory();
            return instance;
        }
    }

    @Override
    public Container createContainer(Strategy strategy) {
        if (strategy == Strategy.FIFO) {;
            return new QueueContainer();
        } else if (strategy == Strategy.LIFO) {
            return new StackContainer();
        }
        return null;
    }
}
