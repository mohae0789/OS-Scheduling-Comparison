package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import metrics.SimulationResult;
import model.Process;
import scheduler.RoundRobinScheduler;
import scheduler.SJFScheduler;
import util.ValidationUtil;

public class Controller {

    @FXML private TextField txtQuantum, txtArrival, txtBurst;
    @FXML private TableView<Process> table;
    @FXML private TableColumn<Process, String>  colId;
    @FXML private TableColumn<Process, Integer> colArrival, colBurst;
    @FXML private TableColumn<Process, Void>    colAction;
    @FXML private ComboBox<String>              comboScenarios;
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
        if (!ValidationUtil.isValidArrival(txtArrival.getText().trim()) ||
                !ValidationUtil.isPositiveInt(txtBurst.getText().trim())) {
            ValidationUtil.showWarning("Input Error",
                    "Please enter valid Arrival (>=0) and Burst (>0) times.");
            return;
        }
        processList.add(new Process(
                "P" + pCounter++,
                Integer.parseInt(txtArrival.getText().trim()),
                Integer.parseInt(txtBurst.getText().trim())));
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
                processList.addAll(
                        new Process("P1", 0, 8),
                        new Process("P2", 1, 4),
                        new Process("P3", 2, 9));
                break;
            case "Scenario B: Short-job-heavy case":
                txtQuantum.setText("2");
                processList.addAll(
                        new Process("P1", 0, 20),
                        new Process("P2", 2, 2),
                        new Process("P3", 3, 1));
                break;
            case "Scenario C: Fairness case":
                txtQuantum.setText("3");
                processList.addAll(
                        new Process("P1", 0, 10),
                        new Process("P2", 0, 10),
                        new Process("P3", 0, 10));
                break;
            case "Scenario D: Long-job sensitivity case":
                txtQuantum.setText("5");
                processList.addAll(
                        new Process("P1", 0, 30),
                        new Process("P2", 1, 2),
                        new Process("P3", 2, 2));
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

        if (!ValidationUtil.isPositiveInt(txtQuantum.getText().trim())) {
            ValidationUtil.showWarning("Missing Data",
                    "Please enter a positive Time Quantum.");
            return;
        }

        if (processList.isEmpty()) {
            ValidationUtil.showWarning("No Processes",
                    "Please add at least one process before simulating.");
            return;
        }

        int quantum = Integer.parseInt(txtQuantum.getText().trim());

        // Run Round Robin
        RoundRobinScheduler rrScheduler = new RoundRobinScheduler(quantum);
        SimulationResult rrResult = rrScheduler.simulate(processList);

        // Run SJF (Preemptive)
        SJFScheduler sjfScheduler = new SJFScheduler();
        SimulationResult sjfResult = sjfScheduler.simulate(processList);

        // Open result window with both results
        new ResultWindow(rrResult, sjfResult).show();
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

    private void showTableComponents() {
        table.setVisible(true);
        table.setManaged(true);
        actionBox.setVisible(true);
        actionBox.setManaged(true);
    }

    private void hideTableComponents() {
        table.setVisible(false);
        table.setManaged(false);
        actionBox.setVisible(false);
        actionBox.setManaged(false);}}