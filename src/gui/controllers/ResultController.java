package gui.controllers;

import gui.views.ResultWindowView;
import metrics.SimulationResult;
import model.Process;
import java.util.List;

public class ResultController {

    private final SimulationResult rrResult;
    private final SimulationResult sjfResult;
    private final List<Process> originalProcesses;

    public ResultController(SimulationResult rrResult, SimulationResult sjfResult, List<Process> originalProcesses) {
        this.rrResult = rrResult;
        this.sjfResult = sjfResult;
        this.originalProcesses = originalProcesses;
    }

    public void showResults() {

        ResultWindowView view = new ResultWindowView(rrResult, sjfResult, originalProcesses);


        ComparisonController compCtrl = new ComparisonController();


        view.show(compCtrl.generateSummaryCard(rrResult, sjfResult));
    }
}
