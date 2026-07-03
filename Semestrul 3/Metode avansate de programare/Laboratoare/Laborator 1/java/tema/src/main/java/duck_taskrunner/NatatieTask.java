package duck_taskrunner;

import taskrunner.domain.Task;

public class NatatieTask extends Task {
    private Race race;

    public NatatieTask(String taskId,String description,Race race){
        super(taskId,description);
        this.race = race;
    }

    public void execute(){
        race.findMinimalRaceTime();
    }
}
