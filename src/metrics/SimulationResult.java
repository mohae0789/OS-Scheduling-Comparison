
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
        if (processMetrics.isEmpty()) return 0;
        double sum = 0;
        for (ProcessMetrics pm : processMetrics) {
            sum += pm.getTurnaroundTime();
        }
        return sum / processMetrics.size();
    }

    public double avgWT() {
        if (processMetrics.isEmpty()) return 0;

        double sum = 0;
        for (ProcessMetrics pm : processMetrics) {
            sum += pm.getWaitingTime();
        }
        return sum / processMetrics.size();
    }
    public double avgRT() {
        if (processMetrics.isEmpty()) return 0;

        double sum = 0;
        for (ProcessMetrics pm : processMetrics) {
            sum += pm.getResponseTime();
        }
        return sum / processMetrics.size();
    }
}

