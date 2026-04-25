package scheduler;

import metrics.GanttEntry;
import metrics.ProcessMetrics;
import metrics.SimulationResult;
import model.Process;

import java.util.ArrayList;
import java.util.List;

public class SJFScheduler {

    public SimulationResult simulate(List<Process> originalProcesses) {

        List<Process> sorted = buildSortedCopies(originalProcesses);
        int n = sorted.size();

        List<GanttEntry> gantt    = new ArrayList<>();
        int[]  remaining  = new int[n];
        int[]  firstStart = new int[n];
        int[]  completion = new int[n];
        boolean[] started   = new boolean[n];
        boolean[] completed = new boolean[n];

        for (int i = 0; i < n; i++) {
            remaining[i]  = sorted.get(i).getBurstTime();
            firstStart[i] = -1;
        }

        int time           = sorted.isEmpty() ? 0 : sorted.get(0).getArrivalTime();
        int completedCount = 0;

        while (completedCount < n) {

            // Pick arrived process with shortest remaining time (SRTF)
            int shortest   = -1;
            int minRemTime = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (!completed[i] && sorted.get(i).getArrivalTime() <= time) {
                    if (remaining[i] < minRemTime) {
                        minRemTime = remaining[i];
                        shortest   = i;
                    }
                }
            }

            // No process arrived yet → jump clock to next arrival
            if (shortest == -1) {
                time++;
                continue;
            }

            // Record first start time (for Response Time)
            if (!started[shortest]) {
                firstStart[shortest] = time;
                started[shortest]    = true;
            }

            // Merge consecutive blocks of same process in Gantt chart
            if (!gantt.isEmpty() &&
                    gantt.get(gantt.size() - 1).getPid().equals(sorted.get(shortest).getPid())) {
                gantt.get(gantt.size() - 1).setEnd(time + 1);
            } else {
                gantt.add(new GanttEntry(sorted.get(shortest).getPid(), time, time + 1));
            }

            // Run 1 time unit
            remaining[shortest]--;
            time++;

            // Check completion
            if (remaining[shortest] == 0) {
                completion[shortest] = time;
                completed[shortest]  = true;
                completedCount++;
            }
        }

        // Build metrics — same constructor as RoundRobin
        List<ProcessMetrics> metricsList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
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

        return new SimulationResult(0, gantt, metricsList);
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