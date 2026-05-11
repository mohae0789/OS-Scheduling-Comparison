package gui.views;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import metrics.GanttEntry;
import metrics.ProcessMetrics;
import metrics.SimulationResult;
import model.Process;

import scheduler.ReadyQueueService;
import scheduler.ReadyQueueService.QueueSnapshot;
import gui.views.ReadyQueueView;

import java.util.List;

public class ResultWindowView {

    private static final String[] COLORS = {"#3498db", "#e74c3c", "#2ecc71", "#e67e22", "#9b59b6", "#1abc9c", "#f39c12", "#e91e63", "#00bcd4", "#ff5722"};

    private final SimulationResult rrResult;
    private final SimulationResult sjfResult;
    private final List<Process> originalProcesses;

    public ResultWindowView(SimulationResult rr, SimulationResult sjf, List<Process> original) {
        this.rrResult = rr;
        this.sjfResult = sjf;
        this.originalProcesses = original;
    }

    public void show(VBox comparisonCard) {
        Stage stage = new Stage();
        stage.setTitle("Scheduling Comparison Results");

        VBox root = new VBox(30);
        root.getStyleClass().add("result-root");
        root.setPadding(new Insets(25));


        Label rrTitle = new Label("Round Robin (Quantum = " + rrResult.getQuantum() + ")");
        rrTitle.getStyleClass().add("result-main-title");


        ReadyQueueService rqService = new ReadyQueueService();
        List<QueueSnapshot> snapshots= rqService.computeSnapshots(originalProcesses,rrResult.getQuantum());




        ReadyQueueView rqView =new ReadyQueueView();
        VBox readyQueueCard = rqView.build(snapshots);



        root.getChildren().addAll(rrTitle, readyQueueCard,
                buildGanttSection("Round Robin Gantt Chart", rrResult),
                buildMetricsSection("Metrics for Round Robin", rrResult));

        root.getChildren().add(new Separator());


        Label sjfTitle = new Label("Shortest Job First (Preemptive)");
        sjfTitle.getStyleClass().add("result-main-title");

        root.getChildren().addAll(sjfTitle,
                buildGanttSection("SJF Gantt Chart", sjfResult),
                buildMetricsSection("Metrics for SJF", sjfResult));


        root.getChildren().add(comparisonCard);

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        Scene scene = new Scene(scroll, 950, 720);

        var css = getClass().getResource("/assets/Css/style.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        stage.setScene(scene);
        stage.show();
    }


    private VBox buildGanttSection(String heading, SimulationResult result) {
        VBox card = new VBox(10);
        card.getStyleClass().add("result-card");

        Label lbl = new Label(heading);
        lbl.getStyleClass().add("result-section-title");

        VBox ganttContent = new VBox(0, buildGanttBar(result), buildTimeLine(result));
        ScrollPane ganttScroll = new ScrollPane(ganttContent);
        ganttScroll.setFitToWidth(false);
        ganttScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        ganttScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        ganttScroll.setPrefHeight(95);
        ganttScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        card.getChildren().addAll(lbl, ganttScroll);
        return card;
    }

    private HBox buildGanttBar(SimulationResult result) {
        HBox bar = new HBox(0);
        bar.setAlignment(Pos.CENTER_LEFT);

        List<GanttEntry> entries = result.getGanttEntries();
        if (entries == null || entries.isEmpty()) return bar;

        for (GanttEntry entry : entries) {
            double widthPx = Math.max(entry.getDuration() * 50.0, 30);
            String color = colorForPid(entry.getPid(), result.getProcessMetrics());

            StackPane cell = new StackPane();
            cell.setPrefSize(widthPx, 50);

            Rectangle rect = new Rectangle(widthPx, 50);
            rect.setFill(Color.web(color));

            Label pid = new Label(entry.getPid());
            pid.getStyleClass().add("gantt-label");

            cell.getChildren().addAll(rect, pid);
            bar.getChildren().add(cell);
        }
        return bar;
    }

    private HBox buildTimeLine(SimulationResult result) {
        HBox line = new HBox(0);
        line.setAlignment(Pos.CENTER_LEFT);

        List<GanttEntry> entries = result.getGanttEntries();
        if (entries == null || entries.isEmpty()) return line;

        for (GanttEntry entry : entries) {
            double widthPx = Math.max(entry.getDuration() * 50.0, 30);
            Label tick = new Label(String.valueOf(entry.getStartTime()));
            tick.getStyleClass().add("gantt-tick");
            tick.setPrefWidth(widthPx);
            tick.setAlignment(Pos.CENTER_LEFT);
            line.getChildren().add(tick);
        }

        Label lastTick = new Label(
                String.valueOf(entries.get(entries.size() - 1).getEndTime()));
        lastTick.getStyleClass().add("gantt-tick");
        line.getChildren().add(lastTick);

        return line;
    }

    private VBox buildMetricsSection(String heading, SimulationResult result) {
        VBox card = new VBox(10);
        card.getStyleClass().add("result-card");

        Label lbl = new Label(heading);
        lbl.getStyleClass().add("result-section-title");

        card.getChildren().addAll(lbl, buildTable(result), buildFooter(result));
        return card;
    }

    @SuppressWarnings("unchecked")
    private TableView<ProcessMetrics> buildTable(SimulationResult result) {
        TableView<ProcessMetrics> tv = new TableView<>();
        tv.getStyleClass().add("result-table");
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tv.setFixedCellSize(45);

        tv.getColumns().addAll(
                makeCol("Process", "pid", 120),
                makeCol("AT", "arrivalTime", 80),
                makeCol("BT", "burstTime", 80),
                makeCol("CT", "completionTime", 80),
                makeCol("TAT", "turnaroundTime", 80),
                makeCol("WT", "waitingTime", 80),
                makeCol("RT", "responseTime", 80)
        );

        tv.setItems(FXCollections.observableArrayList(result.getProcessMetrics()));

        int rows = result.getProcessMetrics().size();
        tv.setPrefHeight(45 + rows * 45 + 2);

        return tv;
    }

    private VBox buildFooter(SimulationResult result) {
        VBox footer = new VBox(8);

        HBox avgRow = new HBox();
        avgRow.getStyleClass().add("averages-row");
        avgRow.setAlignment(Pos.CENTER_LEFT);

        Label avgLabel = new Label(String.format(
                "Averages          TAT: %.2f          WT: %.2f          RT: %.2f",
                result.avgTAT(), result.avgWT(), result.avgRT()));
        avgLabel.getStyleClass().add("averages-label");
        avgRow.getChildren().add(avgLabel);

        Label legend = new Label(
                "AT = Arrival Time,  BT = Burst Time,  CT = Completion Time,  " +
                        "TAT = Turnaround Time,  WT = Waiting Time,  RT = Response Time");
        legend.getStyleClass().add("legend-label");
        legend.setWrapText(true);

        footer.getChildren().addAll(avgRow, legend);
        return footer;
    }

    private <T> TableColumn<ProcessMetrics, T> makeCol(String title, String property, double width) {
        TableColumn<ProcessMetrics, T> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.setPrefWidth(width);
        col.setStyle("-fx-alignment: CENTER;");
        return col;
    }

    private String colorForPid(String pid, List<ProcessMetrics> metrics) {
        for (int i = 0; i < metrics.size(); i++) {
            if (metrics.get(i).getPid().equals(pid)) {
                return COLORS[i % COLORS.length];
            }
        }
        return "#95a5a6";
    }
}
