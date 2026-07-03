package taskrunner.domain;

import utils.Constants;

import java.time.LocalDateTime;

public class MessageTask extends Task{
    private String message;
    private String from;
    private String to;
    private LocalDateTime date;

    public MessageTask(String taskId, String description, String message, String to, String from, LocalDateTime date) {
        super(taskId, description);
        this.message = message;
        this.to = to;
        this.from = from;
        this.date = date;
    }

    public void execute(){
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "MessageTask{" +
                "mesaj='" + message + '\'' +
                ", de la ='" + from + '\'' +
                ", pentru ='" + to + '\'' +
                ", data =" + date.format(Constants.DATE_TIME_FORMATTER) +
                '}';
    }
}
