package map.sem1.taskrunner.domain;

import map.sem1.duck_taskrunner.utils.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageTask extends Task {

    private String mesaj;
    private String from;
    private String to;
    private LocalDateTime date;


    public MessageTask(String descriere, String taskID,
                       String mesaj, String from,
                       String to, LocalDateTime date) {
        super(descriere, taskID);
        this.mesaj = mesaj;
        this.from = from;
        this.to = to;
        this.date = date;
    }

    public void execute(){
        System.out.println(this);
    }

    @Override
    public String toString() {
        return "MessageTask{" +
                "mesaj='" + mesaj + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", date=" + date.format(Constants.DATE_TIME_FORMATTER) +
                //", date=" + date.toString() +
                '}';
    }
}
