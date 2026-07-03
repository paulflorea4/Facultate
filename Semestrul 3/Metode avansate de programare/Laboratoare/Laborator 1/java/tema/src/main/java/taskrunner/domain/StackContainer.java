package taskrunner.domain;

public class StackContainer implements Container{
    private Task[] tasks;
    private int size;

    public StackContainer() {
        this.tasks = new Task[10];
        this.size = 0;
    }

    @Override
    public Task remove() {
        if (this.size != 0) {
            return tasks[--size];
        }
        return null;
    }

    @Override
    public void add(Task task) {
        if (size == tasks.length) {
            resize();
        }
        tasks[size++] = task;

    }

    public void resize(){
        Task[] newTasks = new Task[tasks.length * 2];
        for (int i = 0; i < tasks.length; i++) {
            newTasks[i] = tasks[i];
        }
        tasks = newTasks;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
