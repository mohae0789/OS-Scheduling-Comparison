package metrics;

public class ComparisonSummary {

    private final double rrAvgWT;
    private final double sjfAvgWT;
    private final double rrAvgTAT;
    private final double sjfAvgTAT;
    private final double rrAvgRT;
    private final double sjfAvgRT;

    public ComparisonSummary(SimulationResult rrResult, SimulationResult sjfResult) {
        this.rrAvgWT = rrResult.avgWT();
        this.sjfAvgWT = sjfResult.avgWT();
        this.rrAvgTAT = rrResult.avgTAT();
        this.sjfAvgTAT = sjfResult.avgTAT();
        this.rrAvgRT = rrResult.avgRT();
        this.sjfAvgRT = sjfResult.avgRT();
    }

    public double getRrAvgWT() {
        return rrAvgWT;
    }

    public double getSjfAvgWT() {
        return sjfAvgWT;
    }

    public double getRrAvgTAT() {
        return rrAvgTAT;
    }

    public double getSjfAvgTAT() {
        return sjfAvgTAT;
    }

    public double getRrAvgRT() {
        return rrAvgRT;
    }

    public double getSjfAvgRT() {
        return sjfAvgRT;
    }

    public String waitingWinner() {
        return winner(rrAvgWT, sjfAvgWT);
    }

    public String turnaroundWinner() {
        return winner(rrAvgTAT, sjfAvgTAT);
    }

    public String responseWinner() {
        return winner(rrAvgRT, sjfAvgRT);
    }

    public String waitingAnswer() {
        return metricAnswer(waitingWinner(), rrAvgWT, sjfAvgWT);
    }

    public String turnaroundAnswer() {
        return metricAnswer(turnaroundWinner(), rrAvgTAT, sjfAvgTAT);
    }

    public String responseAnswer() {
        return metricAnswer(responseWinner(), rrAvgRT, sjfAvgRT);
    }

    public String efficiencyLine() {
        return "Efficiency: SJF typically completes short jobs faster, minimizing average waiting time for workloads with varying burst times.";
    }

    public String fairnessLine() {
        return "Fairness: Round Robin distributes CPU time more evenly across all processes, reducing starvation risk for long-running jobs.";
    }

    public String quantumLine(int quantum) {
        return String.format("Quantum Effect: With quantum = %d, Round Robin switches context every %d time unit%s, balancing responsiveness and overhead.",
                quantum,
                quantum,
                quantum == 1 ? "" : "s");
    }

    public String recommendationLine() {
        return "Recommendation: Use SJF when average efficiency is the priority, and Round Robin when fairness and responsiveness are more important.";
    }

    private String winner(double rr, double sjf) {
        if (Math.abs(rr - sjf) < 0.0001) {
            return "Tie";
        }
        return rr < sjf ? "Round Robin" : "SJF";
    }

    private String metricAnswer(String winner, double rrValue, double sjfValue) {
        if ("Tie".equals(winner)) {
            return String.format("Tie with %.2f time units", rrValue);
        }
        double chosen = "Round Robin".equals(winner) ? rrValue : sjfValue;
        return String.format("%s with %.2f time units", winner, chosen);
    }
}
