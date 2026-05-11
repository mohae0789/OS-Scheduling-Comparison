package gui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import scheduler.ReadyQueueService.QueueSnapshot;
import java.util.List;

public class ReadyQueueView {

    public VBox build(List<QueueSnapshot> snapshots) {
        VBox card = new VBox(10);
        card.getStyleClass().add("result-card");

        Label title = new Label("Ready Queue Visualization (Step-by-Step)");
        title.getStyleClass().add("result-section-title");

        VBox rows = new VBox(6);
        rows.setPadding(new Insets(4, 0, 4, 0));

        for (QueueSnapshot snap : snapshots) {
            rows.getChildren().add(buildRow(snap));
        }

        ScrollPane scroll = new ScrollPane(rows);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scroll.setPrefHeight(Math.min(snapshots.size() * 45 + 20, 350));

        card.getChildren().addAll(title, scroll, buildLegend());
        return card;
    }

    private HBox buildRow(QueueSnapshot snap) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(3, 8, 3, 8));


        Label timeLbl = new Label(String.format("t = %-2d", snap.time));
        timeLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50; -fx-min-width: 50px;");


        Label execLbl = buildStyledLabel(snap.executing, "#27ae60");

        row.getChildren().addAll(timeLbl, execLbl);

        if (!snap.waiting.isEmpty()) {
            Label arrow = new Label("→");
            arrow.setStyle("-fx-text-fill: #95a5a6;");
            row.getChildren().add(arrow);

            for (String pid : snap.waiting) {
                row.getChildren().add(buildStyledLabel(pid, "#3498db"));
            }
        } else {
            Label arrow = new Label("→");
            Label empty = new Label("Empty Queue");
            empty.setStyle("-fx-font-style: italic; -fx-text-fill: #bdc3c7; -fx-font-size: 11px;");
            row.getChildren().addAll(arrow, empty);
        }

        return row;
    }

    private Label buildStyledLabel(String text, String color) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 3 8 3 8; -fx-background-radius: 5; " +
                "-fx-min-width: 40px; -fx-alignment: center;");
        return lbl;
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


}



