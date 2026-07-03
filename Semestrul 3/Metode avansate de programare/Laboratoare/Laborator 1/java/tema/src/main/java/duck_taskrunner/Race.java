package duck_taskrunner;

public class Race {
    private Duck[] ducks;
    private Lane[] lanes;
    private TimeStrategy timeStrategy;

    private double bestTime;
    private int[] bestAssign;

    public Race(Duck[] ducks, Lane[] lanes, TimeStrategy timeStrategy) {
        this.ducks = ducks;
        this.lanes = lanes;
        this.timeStrategy = timeStrategy;
    }

    public void setStrategy(TimeStrategy timeStrategy) {
        this.timeStrategy = timeStrategy;
    }

    public void findMinimalRaceTime() {
        if (timeStrategy == TimeStrategy.BINARY_SEARCH) {
            double time1 = findMinimalTimeBinarySearch();
            System.out.printf("Timp minim (binary search): %.4f sec%n", time1);
        }
        else {
            double time2 = findMinimalTimeBackTracking();
            System.out.printf("Timp minim (backtracking): %.4f sec%n", time2);
            printBestAssignment();
        }
    }

    public double findMinimalTimeBinarySearch() {
        int N = ducks.length;
        int M = lanes.length;

        double maxD = 0;
        double minV = ducks[0].getSpeed();

        for (int i = 0; i < M; i++)
            if (lanes[i].getDistance() > maxD) maxD = lanes[i].getDistance();

        for (int i = 0; i < N; i++)
            if (ducks[i].getSpeed() < minV) minV = ducks[i].getSpeed();

        double low = 0, high = 2 * maxD / minV;

        while(low<=high){
            double mid = (low + high) / 2.0;
            if (feasible(mid))
                high = mid-0.0001;
            else
                low = mid+0.0001;
        }
        return high;
    }

    private boolean feasible(double T) {
        int N = ducks.length;
        int M = lanes.length;

        int[] idx = new int[N];
        for (int i = 0; i < N; i++)
            idx[i] = i;

        for (int i = 0; i < N - 1; i++) {
            for (int j = i + 1; j < N; j++) {
                if (ducks[idx[i]].getResistance() > ducks[idx[j]].getResistance()) {
                    int aux = idx[i];
                    idx[i] = idx[j];
                    idx[j] = aux;
                }
            }
        }

        boolean[] used = new boolean[N];

        for (int j = 0; j < M; j++) {
            double need = 2 * lanes[j].getDistance() / T;
            boolean found = false;

            for (int i = 0; i < N; i++) {
                int k = idx[i];
                if (!used[k] && ducks[k].getSpeed() >= need) {
                    used[k] = true;
                    found = true;
                    break;
                }
            }

            if (!found) return false;
        }

        return true;
    }


    public double findMinimalTimeBackTracking() {
        int N = ducks.length;
        int M = lanes.length;

        bestTime = Double.MAX_VALUE;
        bestAssign = new int[M];

        boolean[] used = new boolean[N];
        int[] selection = new int[M];

        backtrack(0, selection, used);

        return bestTime;
    }

    private void backtrack(int pos, int[] selection, boolean[] used) {
        int N = ducks.length;
        int M = lanes.length;

        if (pos == M) {
            for (int j = 1; j < M; j++) {
                if (ducks[selection[j]].getResistance() < ducks[selection[j - 1]].getResistance())
                    return;
            }

            double T = 0;
            for (int j = 0; j < M; j++) {
                double t = 2 * lanes[j].getDistance() / ducks[selection[j]].getSpeed();
                if (t > T) T = t;
            }

            if (T < bestTime) {
                bestTime = T;
                for (int j = 0; j < M; j++) bestAssign[j] = selection[j];
            }
            return;
        }

        for (int i = 0; i < N; i++) {
            if (!used[i]) {
                used[i] = true;
                selection[pos] = i;
                backtrack(pos + 1, selection, used);
                used[i] = false;
            }
        }
    }


    public void printBestAssignment() {
        if (bestAssign == null) {
            System.out.println("Nicio atribuire calculata inca.");
            return;
        }
        int M = lanes.length;
        for (int j = 0; j < M; j++) {
            Duck duck = ducks[bestAssign[j]];
            double time = 2 * lanes[j].getDistance() / duck.getSpeed();
            System.out.printf("Duck %d on lane %d: t=%.4f sec%n",
                    duck.getId(), j + 1, time);
        }
    }
}
