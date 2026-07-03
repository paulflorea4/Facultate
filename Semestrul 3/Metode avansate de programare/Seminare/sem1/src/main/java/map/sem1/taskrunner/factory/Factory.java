package map.sem1.taskrunner.factory;

import map.sem1.taskrunner.domain.Container;
import map.sem1.taskrunner.domain.Strategy;

public interface Factory
{
    Container createContainer(Strategy strategy);
}
