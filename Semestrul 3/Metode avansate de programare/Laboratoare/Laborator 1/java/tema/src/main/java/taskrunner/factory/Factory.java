package taskrunner.factory;

import taskrunner.domain.Strategy;
import taskrunner.domain.Container;

public interface Factory {
    Container createContainer(Strategy strategy);
}
