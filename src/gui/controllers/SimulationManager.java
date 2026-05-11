
package gui.controllers;

import metrics.SimulationResult;
import model.Process;
import scheduler.RoundRobinScheduler;
import scheduler.SJFScheduler;
import java.util.ArrayList;
import java.util.List;

public class SimulationManager {

    public void startSimulation(List<Process> processList, int quantum) {

        List<Process> snapshot = new ArrayList<>();
        for (Process p : processList) {
            snapshot.add(new Process(p.getPid(), p.getArrivalTime(), p.getBurstTime()));
        }


        RoundRobinScheduler rrScheduler = new RoundRobinScheduler(quantum);
        SimulationResult rrResult = rrScheduler.simulate(processList);

        SJFScheduler sjfScheduler = new SJFScheduler();
        SimulationResult sjfResult = sjfScheduler.simulate(processList);


        ResultController resultCtrl = new ResultController(rrResult, sjfResult, snapshot);
        resultCtrl.showResults();
    }
}

