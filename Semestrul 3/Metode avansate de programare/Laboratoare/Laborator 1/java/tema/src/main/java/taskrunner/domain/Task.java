package taskrunner.domain;

import java.util.Objects;

public abstract class Task {
    private String taskId;
    private String description;

    public Task(String taskId, String description) {
        this.taskId = taskId;
        this.description = description;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public abstract void execute();

    @Override
    public String toString() {
        return "Task{" + "Id=" + taskId + ", descriere=" + description + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, description);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Task task = (Task)obj;
        return Objects.equals(taskId, task.taskId) && Objects.equals(description, task.description);
    }
}
