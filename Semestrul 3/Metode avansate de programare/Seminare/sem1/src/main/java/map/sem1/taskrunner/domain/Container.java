package map.sem1.taskrunner.domain;

public interface Container
{
    Task remove();
    void add(Task task);
    int size();
    boolean isEmpty();
}
