package gui.views;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import metrics.ComparisonSummary;

public class ComparisonSummaryView {


    public VBox buildCard(ComparisonSummary summary, int quantum) {
        VBox card = new VBox(15);
        card.getStyleClass().add("result-card");

        Label title = new Label("Comparison Summary");
        title.getStyleClass().add("result-section-title");

        HBox bodyRow = new HBox(20);
        bodyRow.getChildren().addAll(buildMetricsTable(summary), buildAnalysisPane(summary));

        VBox conclusion = buildConclusion(summary, quantum);
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

        addHeaderCell(table, "Metric", 0, 0);
        addHeaderCell(table, "Round Robin", 1, 0);
        addHeaderCell(table, "SJF", 2, 0);
        addHeaderCell(table, "Winner", 3, 0);


        addValueRow(table, 1, "Avg WT", String.format("%.2f", summary.getRrAvgWT()), String.format("%.2f", summary.getSjfAvgWT()), summary.waitingWinner());
        addValueRow(table, 2, "Avg TAT", String.format("%.2f", summary.getRrAvgTAT()), String.format("%.2f", summary.getSjfAvgTAT()), summary.turnaroundWinner());
        addValueRow(table, 3, "Avg RT", String.format("%.2f", summary.getRrAvgRT()), String.format("%.2f", summary.getSjfAvgRT()), summary.responseWinner());

        box.getChildren().addAll(heading, table);
        return box;
    }

    private VBox buildAnalysisPane(ComparisonSummary summary) {
        VBox box = new VBox(12);
        box.getStyleClass().add("comparison-box");

        Label heading = new Label("Analysis Questions");
        heading.getStyleClass().add("comparison-heading");

        box.getChildren().addAll(
                heading,
                createQuestionNode("Which algorithm gave lower average waiting time?", summary.waitingAnswer()),
                createQuestionNode("Which algorithm gave lower average response time?", summary.responseAnswer()),
                createQuestionNode("Which algorithm gave lower average turnaround time?", summary.turnaroundAnswer())
        );
        return box;
    }

    private VBox buildConclusion(ComparisonSummary summary, int quantum) {
        VBox box = new VBox(8);
        box.getStyleClass().add("comparison-conclusion");
        box.setPadding(new Insets(12, 14, 12, 14));

        Label title = new Label("Conclusion");
        title.getStyleClass().add("comparison-heading");

        box.getChildren().addAll(
                title,
                createConclusionLabel(summary.efficiencyLine()),
                createConclusionLabel(summary.fairnessLine()),
                createConclusionLabel(summary.quantumLine(quantum)),
                createConclusionLabel(summary.recommendationLine())
        );
        return box;
    }


    private VBox createQuestionNode(String qText, String aText) {
        VBox qBox = new VBox(3);
        Label q = new Label(qText); q.getStyleClass().add("comparison-question"); q.setWrapText(true);
        Label a = new Label(aText); a.getStyleClass().add("comparison-answer"); a.setWrapText(true);
        qBox.getChildren().addAll(q, a);
        return qBox;
    }

    private Label createConclusionLabel(String text) {
        Label l = new Label(text); l.getStyleClass().add("comparison-conclusion-line"); l.setWrapText(true);
        return l;
    }

    private void addHeaderCell(GridPane grid, String text, int col, int row) {
        Label l = new Label(text); l.getStyleClass().add("comparison-header-cell"); l.setMaxWidth(Double.MAX_VALUE);
        grid.add(l, col, row);
    }

    private void addValueRow(GridPane grid, int row, String metric, String rr, String sjf, String winner) {
        grid.add(createBodyCell(metric, false), 0, row);
        grid.add(createBodyCell(rr, false), 1, row);
        grid.add(createBodyCell(sjf, false), 2, row);
        grid.add(createBodyCell(winner, true), 3, row);
    }

    private Label createBodyCell(String text, boolean isWinner) {
        Label l = new Label(text);
        l.getStyleClass().add("comparison-body-cell");
        if (isWinner) l.getStyleClass().add("comparison-winner-cell");
        l.setMaxWidth(Double.MAX_VALUE);
        return l;
    }

}




