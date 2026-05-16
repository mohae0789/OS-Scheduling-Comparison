package gui.controllers;

import gui.views.ComparisonSummaryView;
import javafx.scene.layout.VBox;
import metrics.ComparisonSummary;
import metrics.SimulationResult;

public class ComparisonController {

    private final ComparisonSummaryView view = new ComparisonSummaryView();

    public VBox generateSummaryCard(SimulationResult rrResult, SimulationResult sjfResult) {


        ComparisonSummary summary = new ComparisonSummary(rrResult, sjfResult);

        return view.buildCard(summary, rrResult.getQuantum());

    }
}
