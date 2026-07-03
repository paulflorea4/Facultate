import duck_taskrunner.*;
import taskrunner.domain.Strategy;
import taskrunner.domain.Task;
import taskrunner.runner.TaskRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        File file=new File("natatie.in");
        file.createNewFile();
        try {
            Scanner in = new Scanner(file);

            Duck[] ducks = null;
            Lane[] lanes = null;
            int N = 0, M = 0;

            for (int i = 0; i < 4; i++) {
                String line = in.nextLine().trim();
                String[] numStr = line.split("\\s+");
                int[] num = new int[numStr.length];
                for (int j = 0; j < numStr.length; j++) {
                    num[j] = Integer.parseInt(numStr[j]);
                }

                if (i == 0) {
                    N = num[0];
                    M = num[1];
                    ducks = new Duck[N];
                    lanes = new Lane[M];
                } else if (i == 1) {
                    for (int j = 0; j < N; j++) {
                        ducks[j] = new Duck(j+1,num[j], 0);
                    }
                } else if (i == 2) {
                    for (int j = 0; j < N; j++) {
                        ducks[j].setResistance(num[j]);
                    }
                } else {
                    for (int j = 0; j < M; j++) {
                        lanes[j] = new Lane(j+1,num[j]);
                    }
                }
            }

            in.close();

            TaskRunner runner = new NatatieTaskRunner(Strategy.FIFO);

            Race race1 = new Race(ducks, lanes, TimeStrategy.BINARY_SEARCH);
            Race race2 = new Race(ducks, lanes, TimeStrategy.BACKTRACKING);

            Task task1=new NatatieTask("1","natatie", race1);
            Task task2=new NatatieTask("2","natatie", race2);

            runner.addTask(task1);
            runner.addTask(task2);

            runner.executeAll();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
