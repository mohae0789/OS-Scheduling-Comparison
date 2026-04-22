package metrics;

import java.util.List;


public class SimulationResult {

    private final int quantum;
    private final List<GanttEntry>     ganttEntries;
    private final List<ProcessMetrics> processMetrics;

    public SimulationResult(int quantum,
                            List<GanttEntry>     ganttEntries,
                            List<ProcessMetrics> processMetrics) {
        this.quantum        = quantum;
        this.ganttEntries   = ganttEntries;
        this.processMetrics = processMetrics;
    }

    public int getQuantum()                        { return quantum; }
    public List<GanttEntry>     getGanttEntries()  { return ganttEntries; }
    public List<ProcessMetrics> getProcessMetrics(){ return processMetrics; }


    public double avgTAT() {
        return processMetrics.stream()
                .mapToInt(ProcessMetrics::getTurnaroundTime)
                .average().orElse(0);
    }


    public double avgWT() {
        return processMetrics.stream()
                .mapToInt(ProcessMetrics::getWaitingTime)
                .average().orElse(0);
    }


    public double avgRT() {
        return processMetrics.stream()
                .mapToInt(ProcessMetrics::getResponseTime)
                .average().orElse(0);
    }
}
