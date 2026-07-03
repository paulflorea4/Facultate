package map.sem1.taskrunner;


import map.sem1.taskrunner.domain.*;
import map.sem1.taskrunner.runner.PrinterTaskRunner;
import map.sem1.taskrunner.runner.StrategyTaskRunner;
import map.sem1.taskrunner.runner.TaskRunner;

import java.time.LocalDateTime;

public class Main {

    public static MessageTask[] createMessageTaskArray(){
        MessageTask t1=new MessageTask("1","Feedback lab1",
                "m1","Gigi", "Ana", LocalDateTime.now());
        MessageTask t2=new MessageTask("2","Feedback lab1",
                "m2","Gigi", "Ana", LocalDateTime.now());
        MessageTask t3=new MessageTask("3","Feedback lab3",
                "m3","Gigi", "Ana", LocalDateTime.now());
        return new MessageTask[]{t1,t2,t3};
    }


    public static void main(String[] args) {

        MessageTask[] l = Main.createMessageTaskArray();
        TaskRunner runner = new StrategyTaskRunner(Strategy.LIFO);

        runner.addTask(l[0]);
        runner.addTask(l[1]);
        runner.addTask(l[2]);
        //runner.executeAll();

        TaskRunner decorator = new PrinterTaskRunner(runner);
        decorator.executeAll();
    }

}