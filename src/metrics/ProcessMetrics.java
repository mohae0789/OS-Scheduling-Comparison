package metrics;

public class ProcessMetrics {

    private final String pid;
    private final int arrivalTime;
    private final int burstTime;
    private final int completionTime;
    private final int turnaroundTime;
    private final int waitingTime;
    private final int responseTime;

    public ProcessMetrics(String pid, int arrivalTime, int burstTime,
                          int completionTime, int responseTime) {
        this.pid            = pid;
        this.arrivalTime    = arrivalTime;
        this.burstTime      = burstTime;
        this.completionTime = completionTime;
        this.turnaroundTime = completionTime - arrivalTime;
        this.waitingTime    = turnaroundTime - burstTime;
        this.responseTime   = responseTime;
    }

    public String getPid()            { return pid; }
    public int getArrivalTime()       { return arrivalTime; }
    public int getBurstTime()         { return burstTime; }
    public int getCompletionTime()    { return completionTime; }
    public int getTurnaroundTime()    { return turnaroundTime; }
    public int getWaitingTime()       { return waitingTime; }
    public int getResponseTime()      { return responseTime; }
}
