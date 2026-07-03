package org.example.ducksocialnetworkui.event;

import org.example.ducksocialnetworkui.domain.Duck;
import org.example.ducksocialnetworkui.domain.SwimmingDuck;

import java.util.ArrayList;
import java.util.List;

public class RaceEvent extends Event {

    private final List<Long> distances;
    private final List<SwimmingDuck> ducks;
    private double bestTime;
    private List<Integer> bestAssignment;

    public RaceEvent(Long id, String name, String status, List<Long> distances) {
        super(id, name, TipEvent.RACE, status);
        ducks = new ArrayList<>();
        this.distances = distances;
    }

    public RaceEvent(Long id, String name,String status ,List<Long> distances,List<SwimmingDuck> ducks) {
        super(id, name,TipEvent.RACE,status);
        this.distances = distances;
        this.ducks = ducks;
    }

    public List<Long> getDistances() {
        return distances;
    }

    public List<SwimmingDuck> getDucks() {
        return ducks;
    }

    public void startEvent() {
        bubbleSortDucks();
        binarySearch();
    }

    @Override
    public String toString() {
        return "RaceEvent{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", distances=" + distances + '\''+
                ", ducks=" + ducks +
                '}';
    }

    private void bubbleSortDucks() {
        for (int i = 0; i < ducks.size() - 1; ++i) {
            boolean swapped = false;

            for (int j = 0; j < ducks.size() - i - 1; ++j) {
                if (ducks.get(j).getRezistenta() > ducks.get(j + 1).getRezistenta()) {
                    SwimmingDuck temp = ducks.get(j);
                    ducks.set(j, ducks.get(j + 1));
                    ducks.set(j + 1, temp);
                    swapped = true;
                }
            }

            if (!swapped) break;
        }
    }

    private void binarySearch() {
        double low = 0.0;
        double high = 10.0;
        double eps = 0.001;
        double best = high;
        List<Integer> bestAssign = null;

        for (int i = 0; i < 60; ++i) {
            double mid = (low + high) / 2.0;
            List<Integer> assign = feasible(mid);

            if (assign != null) {
                best = mid;
                bestAssign = assign;
                high = mid;
            } else {
                low = mid;
            }

            if (high - low <= eps) break;
        }

        this.bestTime = best;
        this.bestAssignment = bestAssign;
    }

    private List<Integer> feasible(double time) {
        int nrDucks = ducks.size();
        int nrLanes = distances.size();
        boolean[] usedDucks = new boolean[nrDucks];
        List<Integer> assign = new ArrayList<>();

        for (int j = 0; j < nrLanes; ++j) {
            int pick = -1;
            double requiredSpeed = 2.0 * distances.get(j) / time;

            for (int i = 0; i < nrDucks; ++i) {
                if (ducks.get(i).getViteza() >= requiredSpeed && !usedDucks[i]) {
                    pick = i;
                    break;
                }
            }

            if (pick == -1) {
                return null;
            }

            usedDucks[pick] = true;
            assign.add(pick);
        }

        return assign;
    }

    public String getResults() {
        if (bestAssignment == null) {
            return "No results found";
        }

        StringBuilder result = new StringBuilder();
        result.append("Best time: ").append(bestTime).append("\n");

        for (int i = 0; i < distances.size(); ++i) {
            Duck duck = ducks.get(bestAssignment.get(i));
            double time = 2.0 * distances.get(i) / duck.getViteza();

            result.append(String.format(
                    "Duck %d on lane %d: t=%.3f secunde%n",
                    duck.getId(), i + 1, time
            ));
        }

        return result.toString();
    }

    public String showDuckResult(Long duckID) {
        if (bestAssignment == null)
            return "No results found";

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Best time: %.3f%n", bestTime));
        for (int i = 0; i < bestAssignment.size(); i++) {
            int duckIndex = bestAssignment.get(i);
            SwimmingDuck duck = ducks.get(duckIndex);

            if (duck.getId().equals(duckID)) {
                double time = (distances.get(i) * 2.0) / duck.getViteza();
                return sb.append(String.format("You ran on lane %d. Your Time: %.3f sec", (i + 1), time)).toString();
            }
        }
        return "You did not qualify for a lane in the optimal assignment.";
    }
}
