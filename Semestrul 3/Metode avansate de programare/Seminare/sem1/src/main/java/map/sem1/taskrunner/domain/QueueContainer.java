package map.sem1.taskrunner.domain;

public class QueueContainer implements Container {

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
        return tasks[0];
    }

    @Override
    public void add(Task task) {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
