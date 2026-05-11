package gui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import model.Process;
import util.ValidationUtil;
import java.util.ArrayList;
import java.util.List;

public class MainViewController {

    @FXML private TextField txtQuantum, txtArrival, txtBurst;
    @FXML private TableView<Process> table;
    @FXML private TableColumn<Process, String> colId;
    @FXML private TableColumn<Process, Integer> colArrival, colBurst;
    @FXML private TableColumn<Process, Void> colAction;
    @FXML private ComboBox<String> comboScenarios;
    @FXML private HBox actionBox;

    private final ObservableList<Process> processList = FXCollections.observableArrayList();
    private int pCounter = 1;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("pid"));
        colArrival.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        colBurst.setCellValueFactory(new PropertyValueFactory<>("burstTime"));

        comboScenarios.setItems(FXCollections.observableArrayList(
                "Scenario A: Basic mixed workload",
                "Scenario B: Short-job-heavy case",
                "Scenario C: Fairness case",
                "Scenario D: Long-job sensitivity case"
        ));

        setupActionColumn();
        table.setItems(processList);
    }




    @FXML
    private void handleAddProcess() {
        String arrivalStr = txtArrival.getText().trim();
        String burstStr = txtBurst.getText().trim();


        if (arrivalStr.isEmpty() || burstStr.isEmpty()) {
            ValidationUtil.showWarning("Empty Input",
                    "Please fill in both Arrival Time and Burst Time fields.");
            return;
        }

        boolean isArrivalInvalid = !ValidationUtil.isValidArrival(arrivalStr);
        boolean isBurstInvalid = !ValidationUtil.isPositiveInt(burstStr);


        if (isArrivalInvalid && isBurstInvalid) {
            ValidationUtil.showWarning("Input Error",
                    "Both Arrival Time and Burst Time are invalid! " +
                            "Arrival must be (>=0) and Burst must be (>0).");
            return;
        }


        if (isArrivalInvalid) {
            ValidationUtil.showWarning("Input Error",
                    "Invalid Arrival Time! Please enter a non-negative integer (>= 0).");
            return;
        }


        if (isBurstInvalid) {
            ValidationUtil.showWarning("Input Error",
                    "Invalid Burst Time! Please enter a positive integer (> 0).");
            return;
        }


        processList.add(new Process(
                "P" + pCounter++,
                Integer.parseInt(arrivalStr),
                Integer.parseInt(burstStr)));

        showTableComponents();
        txtArrival.clear();
        txtBurst.clear();
    }





    @FXML
    private void handleScenarioSelection() {
        String selected = comboScenarios.getValue();
        if (selected == null) return;
        handleReset();

        switch (selected) {
            case "Scenario A: Basic mixed workload":
                txtQuantum.setText("4");
                processList.addAll(new Process("P1", 0, 8), new Process("P2", 1, 4), new Process("P3", 2, 9));
                break;
            case "Scenario B: Short-job-heavy case":
                txtQuantum.setText("2");
                processList.addAll(new Process("P1", 0, 20), new Process("P2", 2, 2), new Process("P3", 3, 1));
                break;
            case "Scenario C: Fairness case":
                txtQuantum.setText("3");
                processList.addAll(new Process("P1", 0, 10), new Process("P2", 0, 10), new Process("P3", 0, 10));
                break;
            case "Scenario D: Long-job sensitivity case":
                txtQuantum.setText("5");
                processList.addAll(new Process("P1", 0, 30), new Process("P2", 1, 2), new Process("P3", 2, 2));
                break;
        }
        showTableComponents();
    }

    @FXML
    private void handleReset() {
        processList.clear();
        hideTableComponents();
        txtQuantum.clear();
    }

    @FXML
    private void handleSimulate() {
        String quantumStr = txtQuantum.getText().trim();

        if (quantumStr.isEmpty() || !ValidationUtil.isPositiveInt(quantumStr) || processList.isEmpty()) {
            ValidationUtil.showWarning("Check Input", "Ensure Quantum is positive and table is not empty.");
            return;
        }

        int quantum = Integer.parseInt(quantumStr);

        SimulationManager manager = new SimulationManager();
        manager.startSimulation(processList, quantum);
    }

    private void setupActionColumn() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Remove");
            {
                btn.getStyleClass().add("remove-button");
                btn.setOnAction(event -> {
                    processList.remove(getTableView().getItems().get(getIndex()));
                    if (processList.isEmpty()) hideTableComponents();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void showTableComponents() { table.setVisible(true); table.setManaged(true); actionBox.setVisible(true); actionBox.setManaged(true); }
    private void hideTableComponents() { table.setVisible(false); table.setManaged(false); actionBox.setVisible(false); actionBox.setManaged(false); }
}
