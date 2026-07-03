package taskrunner.domain;

public class QueueContainer implements Container{
    private Task[] tasks;
    private int size;

    public QueueContainer() {
        this.tasks = new Task[10];
        this.size = 0;
    }

    public void resize(){
        Task[] newTasks = new Task[tasks.length * 2];
        for (int i = 0; i < tasks.length; i++) {
            newTasks[i] = tasks[i];
        }
        tasks = newTasks;
    }


    @Override
    public Task remove() {
        Task removedTask = tasks[0];

        for (int i = 1; i < size; i++) {
            tasks[i - 1] = tasks[i];
        }

        tasks[--size] = null;
        return removedTask;
    }

    @Override
    public void add(Task task) {
        if(size == tasks.length){
            resize();
        }
        tasks[size++] = task;
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
