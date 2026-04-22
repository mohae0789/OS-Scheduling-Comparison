package scheduler;

import metrics.GanttEntry;
import metrics.ProcessMetrics;
import metrics.SimulationResult;
import model.Process;

import java.util.ArrayList;
import java.util.List;



public class RoundRobinScheduler {

    private final int quantum;

    public RoundRobinScheduler(int quantum) {
        this.quantum = quantum;
    }


    public SimulationResult simulate(List<Process> originalProcesses) {


        List<Process> sorted = buildSortedCopies(originalProcesses);


        List<GanttEntry> gantt       = new ArrayList<>();
        int[]  remaining  = new int[sorted.size()];
        int[]  firstStart = new int[sorted.size()];
        int[]  completion = new int[sorted.size()];
        boolean[] started = new boolean[sorted.size()];

        for (int i = 0; i < sorted.size(); i++) {
            remaining[i]  = sorted.get(i).getBurstTime();
            firstStart[i] = -1;
        }


        List<Integer> readyQueue = new ArrayList<>();
        int nextArrival = 0;


        int time = sorted.isEmpty() ? 0 : sorted.get(0).getArrivalTime();


        while (nextArrival < sorted.size() &&
                sorted.get(nextArrival).getArrivalTime() <= time) {
            readyQueue.add(nextArrival++);
        }


        while (!readyQueue.isEmpty()) {

            int idx = readyQueue.remove(0);   // dequeue front


            if (!started[idx]) {
                firstStart[idx] = time;
                started[idx]    = true;
            }


            int runTime = Math.min(quantum, remaining[idx]);


            gantt.add(new GanttEntry(sorted.get(idx).getPid(), time, time + runTime));

            time         += runTime;
            remaining[idx] -= runTime;


            while (nextArrival < sorted.size() &&
                    sorted.get(nextArrival).getArrivalTime() <= time) {
                readyQueue.add(nextArrival++);
            }

            if (remaining[idx] > 0) {
                readyQueue.add(idx);
            } else {
                completion[idx] = time;
            }


            if (readyQueue.isEmpty() && nextArrival < sorted.size()) {
                time = sorted.get(nextArrival).getArrivalTime();
                while (nextArrival < sorted.size() &&
                        sorted.get(nextArrival).getArrivalTime() <= time) {
                    readyQueue.add(nextArrival++);
                }
            }
        }


        List<ProcessMetrics> metricsList = new ArrayList<>();
        for (int i = 0; i < sorted.size(); i++) {
            Process p = sorted.get(i);
            int rt    = firstStart[i] - p.getArrivalTime();
            metricsList.add(new ProcessMetrics(
                    p.getPid(),
                    p.getArrivalTime(),
                    p.getBurstTime(),
                    completion[i],
                    rt
            ));
        }

        return new SimulationResult(quantum, gantt, metricsList);
    }




    private List<Process> buildSortedCopies(List<Process> original) {
        List<Process> copy = new ArrayList<>();
        for (Process p : original) {
            copy.add(new Process(p.getPid(), p.getArrivalTime(), p.getBurstTime()));
        }
        copy.sort((a, b) -> Integer.compare(a.getArrivalTime(), b.getArrivalTime()));
        return copy;
    }
}