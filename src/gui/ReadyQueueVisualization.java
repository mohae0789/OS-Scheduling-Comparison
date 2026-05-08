package gui;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import metrics.SimulationResult;
import model.Process;

import java.util.ArrayList;
import java.util.List;


public class ReadyQueueVisualization {


    private static class QueueSnapshot {
        final int time;
        final String executing;
        final List<String> waiting;

        QueueSnapshot(int time, String executing, List<String> waiting) {
            this.time      = time;
            this.executing = executing;
            this.waiting   = new ArrayList<>(waiting);
        }
    }

    private final SimulationResult rrResult;
    private final List<Process>    originalProcesses;
    private final int              quantum;

    public ReadyQueueVisualization(SimulationResult rrResult, List<Process> originalProcesses, int quantum) {
        this.rrResult          = rrResult;
        this.originalProcesses = originalProcesses;
        this.quantum           = quantum;
    }


    public VBox build() {
        List<QueueSnapshot> snapshots = computeSnapshots();

        VBox card = new VBox(10);
        card.getStyleClass().add("result-card");

        Label title = new Label("Ready Queue Visualization");
        title.getStyleClass().add("result-section-title");

        VBox rows = new VBox(6);
        rows.setPadding(new Insets(4, 0, 4, 0));

        for (QueueSnapshot snap : snapshots) {
            rows.getChildren().add(buildRow(snap));
        }

        HBox legend = buildLegend();

        ScrollPane scroll = new ScrollPane(rows);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scroll.setPrefHeight(Math.min(snapshots.size() * 42 + 20, 320));

        card.getChildren().addAll(title, scroll, legend);
        return card;
    }


    private List<QueueSnapshot> computeSnapshots() {


        List<Process> sorted = sortedCopies(originalProcesses);
        int n = sorted.size();

        int[]     remaining  = new int[n];
        boolean[] arrived    = new boolean[n];

        for (int i = 0; i < n; i++) {
            remaining[i] = sorted.get(i).getBurstTime();
        }


        List<Integer> readyQueue = new ArrayList<>();
        List<QueueSnapshot> snapshots = new ArrayList<>();

        int time        = sorted.isEmpty() ? 0 : sorted.get(0).getArrivalTime();
        int nextArrival = 0;


        while (nextArrival < n && sorted.get(nextArrival).getArrivalTime() <= time) {
            readyQueue.add(nextArrival);
            arrived[nextArrival] = true;
            nextArrival++;
        }

        while (!readyQueue.isEmpty()) {

            int idx = readyQueue.remove(0);

            String execPid = sorted.get(idx).getPid();


            List<String> waitingPids = new ArrayList<>();
            for (int qi : readyQueue) {
                waitingPids.add(sorted.get(qi).getPid());
            }

            snapshots.add(new QueueSnapshot(time, execPid, waitingPids));

            int runTime = Math.min(quantum, remaining[idx]);
            time       += runTime;
            remaining[idx] -= runTime;


            for (int i = nextArrival; i < n; i++) {
                if (sorted.get(i).getArrivalTime() <= time) {
                    readyQueue.add(i);
                    arrived[i]   = true;
                    nextArrival  = i + 1;
                } else {
                    break;
                }
            }


            if (remaining[idx] > 0) {
                readyQueue.add(idx);
            }


            if (readyQueue.isEmpty() && nextArrival < n) {
                time = sorted.get(nextArrival).getArrivalTime();
                while (nextArrival < n && sorted.get(nextArrival).getArrivalTime() <= time) {
                    readyQueue.add(nextArrival);
                    arrived[nextArrival] = true;
                    nextArrival++;
                }
            }
        }

        return snapshots;
    }


    private HBox buildRow(QueueSnapshot snap) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(2, 6, 2, 6));


        Label timeLbl = new Label(String.format("t=%-3d", snap.time));
        timeLbl.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #34495e;" +
                        "-fx-min-width: 55px;"
        );


        Label execLbl = new Label(snap.executing);
        execLbl.setStyle(
                "-fx-background-color: #27ae60;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 12px;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 4 10 4 10;" +
                        "-fx-min-width: 46px;" +
                        "-fx-alignment: center;"
        );

        row.getChildren().addAll(timeLbl, execLbl);


        if (!snap.waiting.isEmpty()) {
            Label arrow = new Label("→");
            arrow.setStyle("-fx-font-size: 13px; -fx-text-fill: #7f8c8d;");
            row.getChildren().add(arrow);

            for (String pid : snap.waiting) {
                Label waitLbl = new Label(pid);
                waitLbl.setStyle(
                        "-fx-background-color: #3498db;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-size: 12px;" +
                                "-fx-background-radius: 6;" +
                                "-fx-padding: 4 10 4 10;" +
                                "-fx-min-width: 46px;" +
                                "-fx-alignment: center;"
                );
                row.getChildren().add(waitLbl);
            }
        } else {

            Label arrow = new Label("→");
            arrow.setStyle("-fx-font-size: 13px; -fx-text-fill: #7f8c8d;");
            Label emptyLbl = new Label("Empty");
            emptyLbl.setStyle(
                    "-fx-font-size: 12px;" +
                            "-fx-text-fill: #95a5a6;" +
                            "-fx-font-style: italic;"
            );
            row.getChildren().addAll(arrow, emptyLbl);
        }

        return row;
    }


    private HBox buildLegend() {
        HBox legend = new HBox(12);
        legend.setAlignment(Pos.CENTER_LEFT);
        legend.setPadding(new Insets(4, 6, 0, 6));

        Label greenBox = new Label("Green");
        greenBox.setStyle(
                "-fx-background-color: #27ae60;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 11px;" +
                        "-fx-background-radius: 4;" +
                        "-fx-padding: 2 8 2 8;"
        );
        Label greenDesc = new Label("= Currently Executing");
        greenDesc.setStyle("-fx-font-size: 11px; -fx-text-fill: #7f8c8d;");

        Label blueBox = new Label("Blue");
        blueBox.setStyle(
                "-fx-background-color: #3498db;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 11px;" +
                        "-fx-background-radius: 4;" +
                        "-fx-padding: 2 8 2 8;"
        );
        Label blueDesc = new Label("= In Ready Queue");
        blueDesc.setStyle("-fx-font-size: 11px; -fx-text-fill: #7f8c8d;");

        legend.getChildren().addAll(greenBox, greenDesc, blueBox, blueDesc);
        return legend;
    }


    private List<Process> sortedCopies(List<Process> original) {
        List<Process> copy = new ArrayList<>();
        for (Process p : original) {
            copy.add(new Process(p.getPid(), p.getArrivalTime(), p.getBurstTime()));
        }

        for (int i = 1; i < copy.size(); i++) {
            Process key = copy.get(i);
            int j = i - 1;
            while (j >= 0 && copy.get(j).getArrivalTime() > key.getArrivalTime()) {
                copy.set(j + 1, copy.get(j));
                j--;
            }
            copy.set(j + 1, key);
        }
        return copy;
    }
}
