package gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import metrics.ComparisonSummary;
import metrics.SimulationResult;

public class ComparisonSummaryCard {

    private final SimulationResult rrResult;
    private final SimulationResult sjfResult;

    public ComparisonSummaryCard(SimulationResult rrResult, SimulationResult sjfResult) {
        this.rrResult = rrResult;
        this.sjfResult = sjfResult;
    }

    public VBox build() {
        ComparisonSummary summary = new ComparisonSummary(rrResult, sjfResult);

        VBox card = new VBox(15);
        card.getStyleClass().add("result-card");

        Label title = new Label("Comparison Summary");
        title.getStyleClass().add("result-section-title");

        HBox bodyRow = new HBox(20);
        bodyRow.getChildren().addAll(buildMetricsTable(summary), buildAnalysisPane(summary));

        VBox conclusion = buildConclusion(summary);
        card.getChildren().addAll(title, bodyRow, conclusion);
        return card;
    }

    private VBox buildMetricsTable(ComparisonSummary summary) {
        VBox box = new VBox(8);
        box.getStyleClass().add("comparison-box");

        Label heading = new Label("Performance Metrics");
        heading.getStyleClass().add("comparison-heading");

        GridPane table = new GridPane();
        table.getStyleClass().add("comparison-table");
        table.setHgap(0);
        table.setVgap(0);

        addHeaderCell(table, "Metric", 0, 0);
        addHeaderCell(table, "Round Robin", 1, 0);
        addHeaderCell(table, "SJF", 2, 0);
        addHeaderCell(table, "Winner", 3, 0);

        addValueRow(table, 1, "Avg WT",
                String.format("%.2f", summary.getRrAvgWT()),
                String.format("%.2f", summary.getSjfAvgWT()),
                summary.waitingWinner());
        addValueRow(table, 2, "Avg TAT",
                String.format("%.2f", summary.getRrAvgTAT()),
                String.format("%.2f", summary.getSjfAvgTAT()),
                summary.turnaroundWinner());
        addValueRow(table, 3, "Avg RT",
                String.format("%.2f", summary.getRrAvgRT()),
                String.format("%.2f", summary.getSjfAvgRT()),
                summary.responseWinner());

        box.getChildren().addAll(heading, table);
        return box;
    }

    private VBox buildAnalysisPane(ComparisonSummary summary) {
        VBox box = new VBox(12);
        box.getStyleClass().add("comparison-box");

        Label heading = new Label("Analysis Questions");
        heading.getStyleClass().add("comparison-heading");

        VBox q1 = question("Which algorithm gave lower average waiting time?", summary.waitingAnswer());
        VBox q2 = question("Which algorithm gave lower average response time?", summary.responseAnswer());
        VBox q3 = question("Which algorithm gave lower average turnaround time?", summary.turnaroundAnswer());

        box.getChildren().addAll(heading, q1, q2, q3);
        return box;
    }

    private VBox buildConclusion(ComparisonSummary summary) {
        VBox box = new VBox(8);
        box.getStyleClass().add("comparison-conclusion");
        box.setPadding(new Insets(12, 14, 12, 14));

        Label title = new Label("Conclusion");
        title.getStyleClass().add("comparison-heading");

        Label efficiency = conclusionLine(summary.efficiencyLine());
        Label fairness = conclusionLine(summary.fairnessLine());
        Label quantum = conclusionLine(summary.quantumLine(rrResult.getQuantum()));
        Label recommendation = conclusionLine(summary.recommendationLine());

        box.getChildren().addAll(title, efficiency, fairness, quantum, recommendation);
        return box;
    }

    private VBox question(String questionText, String answerText) {
        VBox q = new VBox(3);
        Label question = new Label(questionText);
        question.getStyleClass().add("comparison-question");
        question.setWrapText(true);

        Label answer = new Label(answerText);
        answer.getStyleClass().add("comparison-answer");
        answer.setWrapText(true);
        q.getChildren().addAll(question, answer);
        return q;
    }

    private Label conclusionLine(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("comparison-conclusion-line");
        label.setWrapText(true);
        return label;
    }

    private void addHeaderCell(GridPane grid, String text, int col, int row) {
        Label label = new Label(text);
        label.getStyleClass().add("comparison-header-cell");
        label.setMaxWidth(Double.MAX_VALUE);
        grid.add(label, col, row);
    }

    private void addValueRow(GridPane grid, int row, String metric, String rr, String sjf, String winner) {
        grid.add(bodyCell(metric, false), 0, row);
        grid.add(bodyCell(rr, false), 1, row);
        grid.add(bodyCell(sjf, false), 2, row);
        grid.add(bodyCell(winner, true), 3, row);
    }

    private Label bodyCell(String text, boolean winner) {
        Label label = new Label(text);
        label.getStyleClass().add("comparison-body-cell");
        if (winner) {
            label.getStyleClass().add("comparison-winner-cell");
        }
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    }
}
